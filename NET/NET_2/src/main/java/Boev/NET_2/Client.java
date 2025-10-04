package Boev.NET_2;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client implements AutoCloseable{

    private FileInputStream reader;
    private File file;
    private Socket socket;
    private DataOutputStream out;
    private DataInputStream inputStreamServer;
    private byte[] buffer;
    private byte[] name;
    private long Port;


    public void Start(String[] args) throws ArrayIndexOutOfBoundsException, FileNotFoundException, UnknownHostException, IOException{
        file = new File(args[0]);
        reader = new FileInputStream(file);

        if(file.length() > UI.Constants.SizeFile){
            return;
        }
        System.out.println("Получил файл: " + file.length());
        Path pathFile = Paths.get(args[0]);

        String stringName;
        if(pathFile.isAbsolute()){
            stringName = String.valueOf(pathFile.getFileName());
        }
        else{
            stringName = args[0];
        }

        System.out.println("Получил имя: "  + stringName);
        name = stringName.getBytes(StandardCharsets.UTF_8);

        if(name.length > UI.Constants.NameLength){
            System.out.println("Слишком большое имя");
            return;
        }

        InetAddress inetAddress = InetAddress.getByName(args[1]);
        Port = Long.parseLong(args[2]);

        socket = new Socket(inetAddress, (int) Port);
        buffer = new byte[UI.Constants.FragmentSize];


        System.arraycopy(name, 0, buffer, 0, name.length);
        out = new DataOutputStream(socket.getOutputStream());

        try{
            writeNameSize(name.length);
            out.write(buffer);
            writeLengthData(file.length());

        } catch (IOException e){
            throw new Close();
        }

        int bytesReads;
        while((bytesReads = reader.read(buffer)) != -1){
            try {
                out.write(buffer, 0, bytesReads);
                System.out.println("Откравил " + bytesReads);
            } catch (IOException e){
                throw new Close();
            }

        }
        socket.shutdownOutput();
        inputStreamServer = new DataInputStream(socket.getInputStream());
        byte[] answer = new byte[1];

        try{
            inputStreamServer.readFully(answer);
        }catch (IOException e){
            throw new Close();
        }

        if(answer[0] == 1){
            System.out.println("Успешно");
        }
        else {
            System.out.println("Неуспешно");
        }

    }

    private void writeNameSize(int sizeName) throws IOException{
        byte[] sizeNameBuffer = new byte[4];
        ByteBuffer.wrap(sizeNameBuffer).putInt(sizeName);
        out.write(sizeNameBuffer);
    }

    private void writeLengthData(long size) throws IOException{
        byte[] sizeNameBuffer = new byte[8];
        ByteBuffer.wrap(sizeNameBuffer).putLong(size);
        out.write(sizeNameBuffer);
    }

    @Override
    public void close() throws Exception {
        if(socket != null) socket.close();
        if(reader != null) reader.close();
        if(out != null) out.close();
    }
}

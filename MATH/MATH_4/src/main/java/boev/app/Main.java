package boev.app;

import java.math.BigDecimal;

public class Main {
    static public double answer = 823.7226395026770;
    static public double N;

    static public double begin;
    static public double end;


    public static void main(String[] args) {

        try(Input input = new Input(args)){
            System.out.println("Введи N, начало и конец");

            System.out.println(-4 * func(7));
            String line;
            while ((line = input.reader.readLine()) != null){
                if(line.isEmpty()){
                    System.out.println("Введи что-нибудь, плохой человек");
                    continue;
                }
                String[] digit = line.split(" ");

                if(digit.length < 3){
                    System.out.println("Что-то не так");
                    continue;
                }

                N = Double.valueOf(digit[0]);
                begin = Double.valueOf(digit[1]);
                end = Double.valueOf(digit[2]);

                BigDecimal n = BigDecimal.valueOf(Double.valueOf(digit[0]));
                runge();


            }
        } catch (Exception e) {
            System.out.println("Что-то не так");
        }
        System.out.println("Hello world!");
    }

    static private double func(double x){
//        System.out.println(Math.exp(x) * Math.cos(x));
        return Math.exp(x) * Math.cos(x);
    }

    static private double countTrap(){

        double answer = 0;
        double h = (end - begin) / N;
        double x_now = begin;
        double x_next = begin + h;

        for(int i = 0; i < N; ++i){
            answer += h * (func(x_now) + func(x_next)) / 2;
            x_now = x_next;
            x_next = x_now + h;
        }
        return answer;
    }


    static private double countSimpson(){
        double answer = 0;

        double h = (end - begin) / 2 / N;

        double x_pred = begin;
        double x_now = begin + h;
        double x_next = x_now + h;

        for(int i = 0; i < N; ++i){
            answer += h * (func(x_pred) + 4 * func(x_now) + func(x_next)) / 3;
            x_pred = x_next;
            x_now = x_pred + h;
            x_next = x_now + h;
        }
        return answer;
    }

    static private double mycount(){
        double answer = 0;

        double h = (end - begin) / N / 3;

        double x_1 = begin;
        double x_2 = x_1 + 1 * h;
        double x_3 = x_1 + 2 * h;
        double x_4 = x_1 + 3 * h;

        for(int i = 0; i < N; ++i){
            answer += 3 * h * (func(x_1) + 3 * func(x_2) + 3 * func(x_3) + func(x_4)) / 8;
            x_1 = x_4;
            x_2 = x_1 + 1 * h;
            x_3 = x_1 + 2 * h;
            x_4 = x_1 + 3 * h;
        }
        return answer;

    }

    static private void runge(){
        double S_1_tr = countTrap();
        double S_1_Sim = countSimpson();
        double S_1_My = mycount();

        N = N * 2;

        double S_2_tr = countTrap();
        double S_2_Sim = countSimpson();
        double S_2_my = mycount();

        N = N * 2;


        double S_3_tr = countTrap();
        double S_3_Sim = countSimpson();
        double S_3_my = mycount();

        System.out.println("Trap answer: " + S_1_tr + " different: " + Math.abs(S_1_tr - answer));
        System.out.println("Simpsom answer: " + S_1_Sim + " different: " + Math.abs(S_1_Sim - answer));
        System.out.println("My answer: " + S_1_My + " different: " + Math.abs(S_1_My - answer));
        System.out.println("Good answer: " + answer);

        System.out.println("TRAP K: " + Math.log(Math.abs((S_1_tr - S_2_tr) / (S_2_tr - S_3_tr))) / Math.log(2));
        System.out.println("SIMPSON K: " + Math.log(Math.abs((S_1_Sim - S_2_Sim) / (S_2_Sim - S_3_Sim))) / Math.log(2));
        System.out.println("MY K: " + Math.log(Math.abs((S_1_My - S_2_my) / (S_2_my - S_3_my))) / Math.log(2));


    }
}
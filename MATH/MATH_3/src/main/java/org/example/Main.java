package org.example;

import java.lang.reflect.Array;

public class Main {
    public static void main(String[] args) {

        double a = 1;
        double b = 1;
        double c = 2;

        int f = 2;

        try(Input input = new Input(args)){
            String line;

            while((line = input.reader.readLine()) != null){
                if(line.isEmpty()){
                    System.out.println("Введи что-нибудь, плохой человек");
                    continue;
                }

                String[] digit = line.split(" ");

                if(digit.length != 1){
                    System.out.println("Что-то не так");
                    continue;
                }
                int mode = Integer.valueOf(digit[0]);
                if(mode != 1 && mode != 2 && mode != 3){
                    System.out.println("непонятный мод");
                    continue;
                }

                String[] digits = input.reader.readLine().split(" ");
                int N = Integer.valueOf(digits[0]);
                double Epsilion = Double.valueOf(digits[1]);
                double y = Double.valueOf(digits[2]);

                double[] alpha = new double[N];
                double[] beta = new double[N];
                double[] func = new double[N];
                double[] x = new double[N];
                double[] Si = new double[N];


                switch (mode){
                    case 1:

                        for(int i = 1; i <= N; ++i){
                            func[i-1] = f;
                        }

                        alpha[0] = b/c;
                        beta[0] = func[0] / c;

                        for(int i = 2; i <= N; ++i){
                            double del = c - a * alpha[i-2];
                            alpha[i-1] = b / del;
                            beta[i-1] = (func[i-1] + beta[i-2] * a) / del;
                        }

                        x[N-1] = beta[N-1];

                        for(int i = N-1; i > 0; i--){
                            x[i-1] = x[i] * alpha[i-1] + beta[i-1];
                        }

                        for(int i =0 ; i < N; ++i){
                            System.out.println(x[i]);
                        }

                        break;

                    case 2:
                        for(int i = 1; i <= N; ++i){
                            func[i-1] = f + Epsilion;
                        }

                        alpha[0] = b/c;
                        beta[0] = func[0] / c;

                        for(int i = 2; i <= N; ++i){
                            double del = c - a * alpha[i-2];
                            alpha[i-1] = b / del;
                            beta[i-1] = (func[i-1] + beta[i-2] * a) / del;
                        }

                        x[N-1] = beta[N-1];

                        for(int i = N-1; i > 0; i--){
                            x[i-1] = x[i] * alpha[i-1] + beta[i-1];
                        }

                        for(int i =0 ; i < N; ++i){
                            System.out.println(x[i]);
                        }

                        break;
                    case 3:
                        for(int i = 1; i <= N; ++i){
                            func[i-1] = 2 * (i + 1) + y;
                            Si[i-1] = 2 * i + y;
                        }

                        alpha[0] = b/Si[0];
                        beta[0] = func[0] / Si[0];

                        for(int i = 2; i <= N; ++i){
                            double del = Si[i-1] - a * alpha[i-2];
                            alpha[i-1] = b / del;
                            beta[i-1] = (func[i-1] + beta[i-2] * a) / del;
                        }

                        x[N-1] = beta[N-1];

                        for(int i = N-1; i > 0; i--){
                            x[i-1] = x[i] * alpha[i-1] + beta[i-1];
                        }

                        for(int i =0 ; i < N; ++i){
                            System.out.println(x[i]);
                        }

                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
package org.example;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;

public class UI {
    public static class Constants{
        public static double EPSILION = 1e-8;
        public static double SECOND_EPSILION = 1e-5;


        public static double ZERO;
        public static double FIRST;
        public static double SECOND;
        public static double THIRD = 1;
        public static int count = 0;
        public static double step = 1;

        public static double ZERO_PR;
        public static double FIRST_PR;
        public static double SECOND_PR;

        public static double DIS;

        public static double BEGIN_X = -3.6;
        public static double BEGIN_A = -4;
        public static double BEGIN_B = -3;



        public static long iter_1 = 0;
        public static  long iter_2 = 0;
        public static long iter_3 = 0;
        public static  long iter_4 = 0;

        public static double answer = 0;

        public static double x_n_1 = 0;
        public static double x_n_2 = 0;
        public static double x_n_3 = 0;

        public static double PR_x_n_1 = 0;
        public static double PR_x_n_2 = 0;
        public static double PR_x_n_3 = 0;
    }

    public static class HelpMethods{
        public static void SetConstants(double digit){
            switch (Constants.count){
                case 0:
                    Constants.SECOND = digit;
                    Constants.count++;
                    break;
                case 1:
                    Constants.FIRST = digit;
                    Constants.count++;
                    break;
                case 2:
                    Constants.ZERO = digit;
                    Constants.count++;
                    break;
                case 3:

                    Constants.EPSILION = digit;
                    Constants.count++;
                    break;
                case 4:

                    Constants.SECOND_EPSILION = digit;
                    Constants.count++;
                    break;
                case 5:
                    Constants.BEGIN_X = digit;
                    Constants.count++;
                    break;

                case 6:
                    Constants.BEGIN_A = digit;
                    Constants.count++;
                    break;
                case 7:
                    Constants.BEGIN_B = digit;
                    Constants.count++;
                    break;
                default:
                    System.out.println("я все записал");
            }
        }

        public static void N_GetAnswer(){
            double x_now = Constants.BEGIN_X;
            double x_pred = 0;

            double A_x;
            boolean pr_1;
            double A;
            boolean pr_2;

            boolean pr_3;
            double pr_3kv;

            do{
                x_pred = x_now;
                x_now = x_pred - (GetFunc(x_pred) / GetPrFunc(x_pred));
                Constants.iter_2++;

                A_x = Math.abs(x_now - x_pred);
                pr_1 = (A_x < Constants.SECOND_EPSILION);
                A = Math.abs(x_now - Constants.answer);
                pr_2 = (A < Constants.SECOND_EPSILION);

                pr_3kv = Math.abs(x_pred - Constants.answer);
                pr_3 = (A < Math.pow(pr_3kv, 2));

                System.out.println("N: " + Constants.iter_2 + " X: " + x_now + " F(x): " + GetFunc(x_now) + " A(x): " + A_x + " A(x) < e " + pr_1 + " Сигма: " + A + " Сигма меньше: " + pr_2 + " |F(x) < e| " + (Math.abs(GetFunc(x_now)) < Constants.SECOND_EPSILION) + " Порядок " + pr_3);
            }while(!pr_1);

            Constants.x_n_1 = x_now;
            Constants.PR_x_n_1 = x_pred;

        }

        public static void N_S_GetAnswer(){
            double x_now = Constants.BEGIN_X;
            double x_pred = 0;

            double del = GetPrFunc(x_now);

            do{
                x_pred = x_now;
                x_now = x_pred - (GetFunc(x_pred) / del);
                Constants.iter_3++;
            }while(Math.abs(x_now - x_pred) >= Constants.SECOND_EPSILION);

            Constants.x_n_2 = x_now;
            Constants.PR_x_n_2 = x_pred;

        }


        public static void SEC_N_GetAnswer(){


            double x_pred_1 = Constants.BEGIN_X;
            double x_pred = x_pred_1 - (GetFunc(x_pred_1) / GetPrFunc(x_pred_1));
            double x_now;
            Constants.iter_4++;

            double func_pred = GetFunc(x_pred_1);
            double func = GetFunc(x_pred);

            do{

                x_now = x_pred - (func * (x_pred - x_pred_1) / (func - func_pred));

                func_pred = func;
                func = GetFunc(x_now);
                x_pred_1 = x_pred;
                x_pred = x_now;
                Constants.iter_4++;

            }while(Math.abs(x_pred - x_pred_1) >= Constants.SECOND_EPSILION);

            Constants.x_n_3 = x_pred;
            Constants.PR_x_n_3 = x_pred_1;

        }
        public static double GetFunc(double digit){
            return Constants.ZERO + Constants.FIRST * digit + Constants.SECOND * Math.pow(digit, 2) + Constants.THIRD * Math.pow(digit, 3);
        }
        public static double GetPrFunc(double digit){
            return Constants.ZERO_PR + Constants.FIRST_PR * digit + Constants.SECOND_PR * Math.pow(digit, 2);
        }

        public static double sign(double digit){
            if (digit > 0){
                return 1;
            }
            if(digit < 0){
                return -1;
            }
            return 0;
        }

        public static double GetAnswer(double first, double second){

            if(first == NEGATIVE_INFINITY){
                double new_first = second - Constants.step;
                while(sign(GetFunc(second)) * sign(GetFunc(new_first)) > 0){
                    new_first = new_first - Constants.step;
                }

                return GetAnswer(new_first, second);
            }

            if(second == POSITIVE_INFINITY){
                double new_second = first + Constants.step;
                while(sign(GetFunc(first)) * sign(GetFunc(new_second)) > 0){
                    new_second = new_second + Constants.step;
                }

                return GetAnswer(first, new_second);
            }
            double new_digit = (first + second) / 2;
            double answer = GetFunc(new_digit);
            if(Math.abs(answer) < Constants.EPSILION){
                return new_digit;
            }

            if(sign(answer) *  sign(GetFunc(first)) <= 0){
                Constants.iter_1++;
                return GetAnswer(first, new_digit);
            }
            if(sign(answer) * sign(GetFunc(second)) <= 0){
                Constants.iter_1++;
                return GetAnswer(new_digit, second);
            }
            System.out.println("Где-то ошибка при нахождении корня");
            return 0;
        }

        public static void SetPR(){
            Constants.ZERO_PR = Constants.FIRST;
            Constants.FIRST_PR = Constants.SECOND * 2;
            Constants.SECOND_PR = Constants.THIRD * 3;

            Constants.DIS = Constants.FIRST_PR * Constants.FIRST_PR - 4 * Constants.SECOND_PR * Constants.ZERO_PR;

            return;
        }
        

    }
}

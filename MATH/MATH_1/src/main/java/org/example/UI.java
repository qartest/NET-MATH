package org.example;

import static java.lang.Double.*;

public class UI {
    public static class Constants{
        public static double EPSILION;
        public static double ZERO;
        public static double FIRST;
        public static double SECOND;
        public static double THIRD = 1;
        public static int count = 0;
        public static double step;

        public static double ZERO_PR;
        public static double FIRST_PR;
        public static double SECOND_PR;

        public static double DIS;


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
//                    if(digit <= 0){
//                        System.out.println("Эпсилион должен быть больше 0");
//                        break;
//                    }
                    Constants.EPSILION = digit;
                    Constants.count++;
                    break;
                case 4:
//                    if(digit <= 0){
//                        System.out.println("Шаг должен быть больше 0");
//                        break;
//                    }
                    Constants.step = digit;
                    Constants.count++;
                    break;
                default:
                    System.out.println("я все записал");
            }
        }
        public static double GetFunc(double digit){
            return Constants.ZERO + Constants.FIRST * digit + Constants.SECOND * Math.pow(digit, 2) + Constants.THIRD * Math.pow(digit, 3);
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
                return GetAnswer(first, new_digit);
            }
            if(sign(answer) * sign(GetFunc(second)) <= 0){
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

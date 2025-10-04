package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.Double.*;

public class Main {
    public static void main(String[] args){

        try(Input input = new Input(args)){
            Double digit;
            BufferedReader reader = input.reader;

            String line;

            while( (line = reader.readLine()) != null){
                UI.Constants.count = 0;
                String[] digits = line.split(" ");
                if(digits.length < 5){
                    System.out.println("Мало данных");
                    continue;
                }

                for(String stringDigit : digits){
                    try{
                        digit = Double.valueOf(stringDigit);
                        UI.HelpMethods.SetConstants(digit);
                    }catch (NumberFormatException e){
                        System.out.println("Неправильное число");
                        throw new RuntimeException();
                    }
                    if(UI.Constants.count > 4){
                        break;
                    }
                }

                UI.HelpMethods.SetPR();
                System.out.println();
                System.out.println("Дискриминант = " + UI.Constants.DIS);
                System.out.println();


                if(Math.abs(UI.Constants.DIS) < UI.Constants.EPSILION){
                    double begin = -1 * UI.Constants.FIRST_PR / 2 / UI.Constants.SECOND_PR;
                    double zero_func = UI.HelpMethods.GetFunc(begin);
                    double answer;
                    int kr;

                    if(Math.abs(zero_func) < UI.Constants.EPSILION){
                        answer = begin;
                        kr = 3;
                    }
                    else if (zero_func < -1 * UI.Constants.EPSILION){
                        answer = UI.HelpMethods.GetAnswer(begin, POSITIVE_INFINITY);
                        kr = 1;
                    }
                    else{
                        answer = UI.HelpMethods.GetAnswer(NEGATIVE_INFINITY, begin);
                        kr = 1;
                    }

                    System.out.println("1 Корень\n" + answer + " кратности " + kr);

                }
                else if(UI.Constants.DIS < -1 * UI.Constants.EPSILION){

                    double zero_func = UI.HelpMethods.GetFunc(0);
                    double answer;
                    if(Math.abs(zero_func) < UI.Constants.EPSILION){
                        answer = 0;
                    }
                    else if (zero_func < -1 * UI.Constants.EPSILION){
                        answer = UI.HelpMethods.GetAnswer(0, POSITIVE_INFINITY);
                    }
                    else{
                        answer = UI.HelpMethods.GetAnswer(NEGATIVE_INFINITY, 0);
                    }
                    System.out.println("1 Корень\n" + answer + " кратность 1");
                }
                else {
                    double first_x =  (-1 * UI.Constants.FIRST_PR - (Math.sqrt(UI.Constants.DIS))) / 2 / UI.Constants.SECOND_PR;
                    double second_x = (-1 * UI.Constants.FIRST_PR + (Math.sqrt(UI.Constants.DIS))) / 2 / UI.Constants.SECOND_PR ;

                    double func_first = UI.HelpMethods.GetFunc(first_x);
                    double func_second = UI.HelpMethods.GetFunc(second_x);


                    if((func_first > UI.Constants.EPSILION) && (func_second > UI.Constants.EPSILION)){
                        double answer = UI.HelpMethods.GetAnswer(NEGATIVE_INFINITY, first_x);
                        System.out.println("1 корень\n" + answer + " 1 кратности");
                    }
                    else if((func_first < -1 * UI.Constants.EPSILION) && (func_second < -1 * UI.Constants.EPSILION)){
                        double answer = UI.HelpMethods.GetAnswer(second_x, POSITIVE_INFINITY);
                        System.out.println("1 корень\n" + answer + " 1 кратности");
                    }
                    else if((func_first > UI.Constants.EPSILION) && (func_second < -1 * UI.Constants.EPSILION)){
                        double answer_1 = UI.HelpMethods.GetAnswer(NEGATIVE_INFINITY, first_x);
                        double answer_2 = UI.HelpMethods.GetAnswer(first_x, second_x);
                        double answer_3 = UI.HelpMethods.GetAnswer(second_x, POSITIVE_INFINITY);

                        System.out.println("3 корня\n" + answer_1 + " 1 кратности\n" +answer_2 + " 1 кратности\n" +answer_3 + " 1 кратности");

                    }else if((Math.abs(func_first) < UI.Constants.EPSILION) && (Math.abs(func_second) < UI.Constants.EPSILION)){
                        System.out.println("1 корень\n" + ((first_x + second_x) / 2) + " 3 кратности");
                    }
                    else if(Math.abs(func_first) < UI.Constants.EPSILION){
                        double answer = UI.HelpMethods.GetAnswer(second_x, POSITIVE_INFINITY);
                        System.out.println("2 корня\n" + first_x + " 2 кратности\n" + answer + " 1 кратности");
                    }
                    else if(Math.abs(func_second) < UI.Constants.EPSILION){
                        double answer = UI.HelpMethods.GetAnswer(NEGATIVE_INFINITY, first_x);
                        System.out.println("2 корня\n" + second_x + " 2 кратности\n" + answer + " 1 кратности");
                    }
                    else{
                        System.out.println("что-то где-то не так как нам нужно. Ошибка на выборе условий. Когда 2 корня у производной");
                    }

                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }



    }
}
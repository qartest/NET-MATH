package org.example;

import java.io.BufferedReader;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.POSITIVE_INFINITY;

public class Main {
    public static void main(String[] args){
        try(Input input = new Input(args)){
            Double digit;
            BufferedReader reader = input.reader;

            String line;

            while( (line = reader.readLine()) != null){
                UI.Constants.count = 0;
                String[] digits = line.split(" ");
                if(digits.length < 6){
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
                    if(UI.Constants.count > 8){
                        break;
                    }
                }

                UI.HelpMethods.SetPR();

                UI.Constants.answer = UI.HelpMethods.GetAnswer(UI.Constants.BEGIN_A, UI.Constants.BEGIN_B);
                System.out.println("ОТВЕТ: " + UI.Constants.answer + " Всего итераций: " + UI.Constants.iter_1);

                UI.HelpMethods.N_GetAnswer();

                double first = Math.abs(UI.Constants.x_n_1 - UI.Constants.PR_x_n_1);
                double second = Math.abs(UI.Constants.x_n_1 - UI.Constants.answer);
                double third = Math.abs(UI.Constants.PR_x_n_1 - UI.Constants.answer);

                boolean first_bool = (first < UI.Constants.SECOND_EPSILION);
                boolean second_bool = (second < UI.Constants.SECOND_EPSILION);
                boolean third_bool = (Math.abs(UI.HelpMethods.GetFunc(UI.Constants.x_n_1)) < UI.Constants.SECOND_EPSILION);
                boolean forth_bool = (second < Math.pow(third, 2));

//                System.out.println(UI.Constants.x_n_1 + " " + UI.HelpMethods.GetFunc(UI.Constants.x_n_1) + " " + first + " " + first_bool + " " + second + " " + second_bool + " " + third_bool + " " + forth_bool);

                UI.HelpMethods.N_S_GetAnswer();

                first = Math.abs(UI.Constants.x_n_2 - UI.Constants.PR_x_n_2);
                second = Math.abs(UI.Constants.x_n_2 - UI.Constants.answer);
                third = Math.abs(UI.Constants.PR_x_n_2 - UI.Constants.answer);

                first_bool = (first < UI.Constants.SECOND_EPSILION);
                second_bool = (second < UI.Constants.SECOND_EPSILION);
                third_bool = (Math.abs(UI.HelpMethods.GetFunc(UI.Constants.x_n_2)) < UI.Constants.SECOND_EPSILION);
                forth_bool = (second < Math.pow(third, 2));

                System.out.println(UI.Constants.x_n_2 + " " + UI.HelpMethods.GetFunc(UI.Constants.x_n_2) + " " + first + " " + first_bool + " " + second + " " + second_bool + " " + third_bool + " " + forth_bool);

                UI.HelpMethods.SEC_N_GetAnswer();

                first = Math.abs(UI.Constants.x_n_3 - UI.Constants.PR_x_n_3);
                second = Math.abs(UI.Constants.x_n_3 - UI.Constants.answer);
                third = Math.abs(UI.Constants.PR_x_n_3 - UI.Constants.answer);

                first_bool = (first < UI.Constants.SECOND_EPSILION);
                second_bool = (second < UI.Constants.SECOND_EPSILION);
                third_bool = (Math.abs(UI.HelpMethods.GetFunc(UI.Constants.x_n_3)) < UI.Constants.SECOND_EPSILION);
                forth_bool = (second < Math.pow(third, 2));

                System.out.println(UI.Constants.x_n_3 + " " + UI.HelpMethods.GetFunc(UI.Constants.x_n_3) + " " + first + " " + first_bool + " " + second + " " + second_bool + " " + third_bool + " " + forth_bool);
                System.out.println("Кол-во итерации в первом: " + UI.Constants.iter_2);
                System.out.println("Кол-во итерации во втором: " + UI.Constants.iter_3);
                System.out.println("Кол-во итерации во третьем: " + UI.Constants.iter_4);
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
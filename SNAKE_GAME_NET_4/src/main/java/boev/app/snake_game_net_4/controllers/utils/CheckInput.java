package boev.app.snake_game_net_4.controllers.utils;

import boev.app.snake_game_net_4.exception.InputException;

public class CheckInput {
    private final static int MIN_WIDTH = 10;
    private final static int MAX_WIDTH = 100;
    private final static int MIN_HEIGHT = 10;
    private final static int MAX_HEIGHT = 100;
    private final static int MIN_FOOD_STATIC = 0;
    private final static int MAX_FOOD_STATIC = 100;
    private final static int MIN_DELAY = 100;
    private final static int MAX_DELAY = 3000;

    public static void checkWidth(String width) throws InputException {
        if (width.isEmpty()) {
            throw new InputException("Введи ширину, олух");
        }
        try {
            int widthVal = Integer.parseInt(width);
            if (widthVal > MAX_WIDTH || widthVal < MIN_WIDTH) {
                throw new InputException("Неправильная ширина");
            }
        } catch (NumberFormatException nfe) {
            throw new InputException("ЭТО НЕ ЦИФРЫ, СЦУККО В ШИРИНЕ");
        }
    }

    public static void checkHeight(String width) throws InputException {
        if (width.isEmpty()) {
            throw new InputException("Введи высоту, олух");
        }
        try {
            int widthVal = Integer.parseInt(width);
            if (widthVal > MAX_WIDTH || widthVal < MIN_WIDTH) {
                throw new InputException("Неправильная высота");
            }
        } catch (NumberFormatException nfe) {
            throw new InputException("ЭТО НЕ ЦИФРЫ, СЦУККО В ВЫСОТЕ");
        }
    }


    public static void checkFoodStatic(String foodStatic) throws InputException {
        if (foodStatic.isEmpty()) {
            throw new InputException("ВВЕДИ ЕДУ");
        }
        try {
            int foodStaticVal = Integer.parseInt(foodStatic);
            if (foodStaticVal > MAX_FOOD_STATIC || foodStaticVal < MIN_FOOD_STATIC) {
                throw new InputException("ЕДЫ МАЛО ИЛИ МНОГО, ДА Я ВАЙБМЕН И ЧТО???");
            }
        } catch (NumberFormatException nfe) {
            throw new InputException("ЭТО НЕ ЦИФРЫ, СЦУККО В ЕДЕ");
        }
    }

    public static void checkDelay(String delay) throws InputException {
        if (delay.isEmpty()) {
            throw new InputException("ВВЕДИ ЕДУ");
        }
        try {
            int delayVal = Integer.parseInt(delay);
            if (delayVal > MAX_DELAY || delayVal < MIN_DELAY) {
                throw new InputException("Мало или много задержка");
            }
        } catch (NumberFormatException nfe) {
            throw new InputException("ЭТО НЕ ЦИФРЫ, СЦУККО В ЗАДЕРЖКЕ");
        }
    }

    public static void checkNickname(String nickname) throws InputException {
        if (nickname.isEmpty()) {
            throw new InputException("ТЫ БАРАН СУКА??? ВВЕДИ ИМЯ СВОЕ, ЛЮБИМОЕ МОЕ\n505 love");
        }
    }

    public static void checkGameName(String gameName) throws InputException {
        if (gameName.isEmpty()) {
            throw new InputException("ТЫ БАРАН СУКА??? ВВЕДИ ИМЯ ИГРЫ");
        }
    }
}

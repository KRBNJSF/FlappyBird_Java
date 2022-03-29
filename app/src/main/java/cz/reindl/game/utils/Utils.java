package cz.reindl.game.utils;

import java.util.Random;

public class Utils {

    public static int randomNumber(int value) {
        return new Random().nextInt(value);
    }

}

package classes.makerandom;

import java.util.ArrayList;
import java.util.Random;

public class MakeRandom {
    public static  <T> void shuffle(ArrayList<T> target) {
        for (int i = 0; i < target.size(); i++) {
            int r = (new Random()).nextInt(i + 1);
            swap(target, i, r);
        }
    }

    public static  <T> void swap(ArrayList<T> target, int i, int j) {
        T temp = target.get(i);
        target.set(i, target.get(j));
        target.set(j, temp);
    }

    public static int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }

    /**
     *
     * @param min min
     * @param max max
     * @param ignore require min < ignore < max
     * @return random number between min and max, ignore cannot be chosen
     */
    public static int random(int min, int max, int ignore) {
        int num1 = random(min, ignore - 1);
        int num2 = random(ignore + 1, max);
        return randomTwoNumber(num1, num2);
    }

    public static <T> T random (ArrayList<T> target) {
        int randomIndex = random(0, target.size() - 1);
        return target.get(randomIndex);
    }

    public static int randomTwoNumber(int num1, int num2) {
        int index = random(0, 1);
        if (index == 0) {
            return num1;
        }

        return num2;
    }

    /**
     *
     * @param min min
     * @param max max
     * @param numberRequire the number of random number to be get in [min, max]
     * @return an array contain different numberRequire number in [min, max]
     */
    public static ArrayList<Integer> randomManyNumbers(int min, int max, int numberRequire) {
        if (numberRequire > max - min + 1) {
            numberRequire = max - min;
        }

        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> remainValue = new ArrayList<>();

        for (int i = min; i <= max; i++) {
            remainValue.add(i);
        }

        for (int i = 0; i < numberRequire; i++) {
            int randomIndex = random(0, remainValue.size() - 1);
            result.add(remainValue.get(randomIndex));
            remainValue.remove(randomIndex);
        }

        return result;
    }

    public static <T> ArrayList<T> randomElements(T[] array, int numberRequire) {
        ArrayList<Integer> index = randomManyNumbers(0, array.length - 1, numberRequire);
        ArrayList<T> result = new ArrayList<>();
        for (int i : index) {
            result.add(array[i]);
        }

        return result;
    }
}

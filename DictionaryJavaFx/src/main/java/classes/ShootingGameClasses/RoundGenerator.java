package classes.ShootingGameClasses;

import classes.data.GameData;
import classes.data.WordWork;
import classes.makerandom.MakeRandom;
import controllers.dictionaryjavafx.ShootingGameController;

import java.util.ArrayList;
import java.util.HashMap;

public class RoundGenerator {
    private static ArrayList<Integer> remainIndex;
    private static final ArrayList<String> key = GameData.getShootingGameEnglishKeyWords();
    private static final HashMap<String, ArrayList<String>> words = GameData.getShootingGameWords();
    private static final ShootingGameController controller = ShootingGameController.getInstance();
    public static void generateEnBullet_ViTarget(int roundNumber) {
        controller.clear();
        switch (roundNumber) {
            /*
            round 1 have only 1 still target and 4 bullet
            the key bullet have the same color
             */
            case 1 -> {
                int randomWordIndex = randomAndRemoveIndex();
                String bulletWord = key.get(randomWordIndex);
                String targetWord = MakeRandom.random(words.get(bulletWord));
                GameObject.Color randomColor = getRandomColor();

                int randomBulletPos = MakeRandom.random(0, 3);
                for (int i = 0; i < 4; i++) {
                    if (i == randomBulletPos) {
                        controller.addBullet(new Bullet(randomColor, bulletWord, targetWord));
                    } else {
                        int randomWrongIndex = MakeRandom.random(remainIndex);
                        controller.addBullet(new Bullet(getRandomColor(), key.get(randomWrongIndex), ""));
                    }
                }

                controller.addTarget(new StillTarget(randomColor, targetWord, 1, new Vector(MakeRandom.random(400, 800), MakeRandom.random(200, 400))));
            }

            /*
            round 2 have 3 still target and 9 bullet
            each target have 2 true bullet
             */
            case 2 -> {
                ArrayList<Integer> randomIndexes = randomAndRemoveIndex(3);
                ArrayList<Bullet> bullets = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    GameObject.Color randomColor = getRandomColor();
                    String bulletWord = key.get(randomIndexes.get(i));
                    String targetWord = MakeRandom.random(words.get(bulletWord));

                    int range = i * 400;
                    controller.addTarget(new StillTarget(randomColor, targetWord, 1,
                            new Vector(MakeRandom.random(100 + range, 400 + range), MakeRandom.random(0, 600))));

                    bullets.add(new Bullet(getRandomColor(), bulletWord, targetWord));
                    bullets.add(new Bullet(getRandomColor(), bulletWord, targetWord));
                }

                for (int i = 0; i < 4; i++) {
                    int randomWrongIndex = MakeRandom.random(remainIndex);
                    bullets.add(new Bullet(getRandomColor(), key.get(randomWrongIndex), ""));
                }
                MakeRandom.shuffle(bullets);

                controller.addBullet(bullets);
            }

            case 3 -> {

            }

            case 4 -> {

            }

            case 5 -> {

            }

            case 6 -> {

            }

            case 7 -> {

            }

            case 8 -> {

            }

            case 9 -> {

            }

            case 10 -> {

            }

            case 11 -> {

            }

            case 12 -> {

            }

            case 13 -> {

            }

            case 14 -> {

            }

            case 15 -> {

            }

            case 16 -> {

            }

            case 17 -> {

            }

            case 18 -> {

            }

            case 19 -> {

            }

            case 20 -> {

            }

            default -> {

            }
        }
    }

    public static void refillIndex() {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < GameData.getShootingGameWords().size(); i++) {
            result.add(i);
        }
        remainIndex = result;
    }

    private static GameObject.Color getRandomColor() {
        int type = MakeRandom.random(1, 3);
        switch (type) {
            case 2 -> {
                return GameObject.Color.BLUE;
            }
            case 3 -> {
                return GameObject.Color.GREEN;
            }
            default -> {
                return GameObject.Color.RED;
            }
        }
    }

    private static int randomAndRemoveIndex() {
        if (remainIndex.isEmpty()) {
            refillIndex();
        }
        int pos = MakeRandom.random(0, remainIndex.size() - 1);
        int value = remainIndex.get(pos);
        remainIndex.remove(pos);
        return value;
    }

    private static ArrayList<Integer> randomAndRemoveIndex(int numberRandom) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < numberRandom; i++) {
            result.add(randomAndRemoveIndex());
        }
        return result;
    }
}

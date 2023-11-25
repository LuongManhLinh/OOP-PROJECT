package classes.ShootingGameClasses;

import classes.data.GameData;
import classes.data.WordWork;
import classes.makerandom.MakeRandom;
import controllers.dictionaryjavafx.ShootingGameController;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.HashMap;

public class RoundGenerator {
    private static ArrayList<Integer> remainIndex;
    private static final ArrayList<String> key = GameData.getShootingGameEnglishKeyWords();
    private static final HashMap<String, ArrayList<String>> words = GameData.getShootingGameWords();
    private static final ShootingGameController controller = ShootingGameController.getInstance();
    public static void generateEnBullet_ViTarget(int roundNumber) {
        controller.clear();
        System.out.println("generating...");
        switch (roundNumber) {
            /*
            round 1 has only 1 still target and 4 bullets
            the key bullet have the same color with the target
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

                controller.addTarget(new StillTarget(randomColor, targetWord, 1,
                        new Vector(MakeRandom.random(400, 1100), MakeRandom.random(200, 400))));
            }

            /*
            round 2 has 3 still targets and 10 bullet
            each target has 2 true bullets
             */
            case 2 -> {
                ArrayList<Integer> randomIndexes = randomAndRemoveIndex(3);
                ArrayList<Bullet> bullets = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    String bulletWord = key.get(randomIndexes.get(i));
                    String targetWord = MakeRandom.random(words.get(bulletWord));

                    int range = i * 400;
                    controller.addTarget(new StillTarget(getRandomColor(), targetWord, 1,
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

            /*
            round 3 has 1 straight moving target and 4 bullets
            2 in them are true
             */
            case 3 -> {
                int randomIndex = randomAndRemoveIndex();
                String bulletWord = key.get(randomIndex);
                String targetWord = MakeRandom.random(words.get(bulletWord));

                StraightMovingTarget target = new StraightMovingTarget(getRandomColor(), targetWord, 2);
                int x = MakeRandom.random(500, 1000);
                target.addDestination(new Vector(x, 0));
                target.addDestination(new Vector(x, 500));
                target.setVelocity(200);
                controller.addTarget(target);

                int randomWrongIndex = MakeRandom.random(remainIndex);
                String wrongBulletWord = key.get(randomWrongIndex);
                ArrayList<Bullet> bullets = new ArrayList<>();
                bullets.add(new Bullet(getRandomColor(), bulletWord, targetWord));
                bullets.add(new Bullet(getRandomColor(), bulletWord, targetWord));
                bullets.add(new Bullet(getRandomColor(), wrongBulletWord, ""));
                bullets.add(new Bullet(getRandomColor(), wrongBulletWord, ""));
                MakeRandom.shuffle(bullets);
                controller.addBullet(bullets);
            }

            /*
            round 4 has 3 straight moving target and 10 bullets
            each target has 2 true bullets
             */
            case 4 -> {
                ArrayList<Integer> randomIndexes = randomAndRemoveIndex(3);
                ArrayList<Bullet> bullets = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    String bulletWord = key.get(randomIndexes.get(i));
                    String targetWord = MakeRandom.random(words.get(bulletWord));

                    StraightMovingTarget target = new StraightMovingTarget(getRandomColor(), targetWord, 2);
                    if (i == 0) {
                        target.addDestination(new Vector(50, 0));
                        target.addDestination(new Vector(1200, 600));
                        target.setVelocity(MakeRandom.random(100, 200));
                    } else if (i == 1) {
                        target.addDestination(new Vector(1200, 0));
                        target.addDestination(new Vector(50, 600));
                        target.setVelocity(MakeRandom.random(100, 200));
                    } else {
                        target.addDestination(new Vector(100, 600));
                        target.addDestination(new Vector(300, 0));
                        target.addDestination(new Vector(500, 600));
                        target.addDestination(new Vector(700, 0));
                        target.addDestination(new Vector(1000, 600));
                        target.setVelocity(MakeRandom.random(200, 300));
                    }
                    controller.addTarget(target);

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

            /*
            round 5 has 2 round moving targets and 6 bullets
            each target has 2 true bullets
             */
            case 5 -> {
                ArrayList<Integer> randomIndexes = randomAndRemoveIndex(2);
                ArrayList<Bullet> bullets = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    String bulletWord = key.get(randomIndexes.get(i));
                    String targetWord = MakeRandom.random(words.get(bulletWord));

                    RoundMovingTarget target = new RoundMovingTarget(getRandomColor(), targetWord, 1.5,
                            new Vector(MakeRandom.random(300, 500), MakeRandom.random(300, 500)), MakeRandom.random(100, 200));
                    target.setRoundVelocity(90);
                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(), bulletWord, targetWord));
                    bullets.add(new Bullet(getRandomColor(), bulletWord, targetWord));
                }

                for (int i = 0; i < 2; i++) {
                    int randomWrongIndex = MakeRandom.random(remainIndex);
                    bullets.add(new Bullet(getRandomColor(), key.get(randomWrongIndex), ""));
                }
                MakeRandom.shuffle(bullets);
                controller.addBullet(bullets);
            }

            /*
            round 6 has 2 round moving targets with static centers and 2 with dynamic centers
            each has 2 true bullet
            we have 12 bullets
             */
            case 6 -> {
                ArrayList<Integer> randomIndexes = randomAndRemoveIndex(4);
                ArrayList<Bullet> bullets = new ArrayList<>();

                for (int i = 0; i < 4; i++) {
                    String bulletWord = key.get(randomIndexes.get(i));
                    String targetWord = MakeRandom.random(words.get(bulletWord));

                    RoundMovingTarget target = new RoundMovingTarget(getRandomColor(), targetWord, 1.5);
                    if (i == 2) {
                        target.setCenter(new Vector(200, 100));
                        target.setRadius(50);
                        target.setRoundVelocity(120);
                        target.setMoveDirection(new Vector(1, 0));
                        target.setMoveVelocity(100);
                    } else if (i == 3) {
                        target.setCenter(new Vector(200, 500));
                        target.setRadius(50);
                        target.setRoundVelocity(120);
                        target.setMoveDirection(new Vector(1, 0));
                        target.setMoveVelocity(100);
                    } else {
                        target.setCenter(new Vector(i * 400 + 400, 300));
                        target.setRadius(100);
                        target.setRoundVelocity(120);
                    }

                    controller.addTarget(target);

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

            /*
            Round 7 has 2 teleporting still targets, 2 still targets and 2 straight moving target and 1 round moving target
            since there are 7 target, we have 20 bullets, each target has 2 true bullet
             */
            case 7 -> {
                ArrayList<Bullet> bullets = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    int randomIndex = randomAndRemoveIndex();
                    String bulletText = key.get(randomIndex);
                    String targetText = MakeRandom.random(words.get(bulletText));

                    TeleportingStillTarget target = new TeleportingStillTarget(getRandomColor(), targetText, 2);
                    target.addPosition(new Vector(MakeRandom.random(100, 200), MakeRandom.random(0, 100)));
                    target.addPosition(new Vector(MakeRandom.random(1000, 1200), MakeRandom.random(500, 600)));
                    target.setTimeToJump(3);
                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                }

                for (int i = 0; i < 2; i++) {
                    int randomIndex = randomAndRemoveIndex();
                    String bulletText = key.get(randomIndex);
                    String targetText = MakeRandom.random(words.get(bulletText));

                    controller.addTarget(new StillTarget(getRandomColor(), targetText, 1,
                            new Vector(MakeRandom.random(300, 500) + i * 300, MakeRandom.random(300, 500))));

                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                }

                for (int i = 0; i < 2; i++) {
                    int randomIndex = randomAndRemoveIndex();
                    String bulletText = key.get(randomIndex);
                    String targetText = MakeRandom.random(words.get(bulletText));

                    StraightMovingTarget target = new StraightMovingTarget(getRandomColor(), targetText, 3);
                    if (i == 0) {
                        target.addDestination(new Vector(50, 0));
                        target.addDestination(new Vector(500, 0));
                        target.addDestination(new Vector(300, 0));
                        target.addDestination(new Vector(700, 0));
                        target.addDestination(new Vector(MakeRandom.random(1000, 1200), MakeRandom.random(100, 200)));
                        target.addDestination(new Vector(800, 100));
                    } else {
                        target.addDestination(new Vector(200, 250));
                        target.addDestination(new Vector(MakeRandom.random(300, 400), MakeRandom.random(150, 350)));
                        target.addDestination(new Vector(MakeRandom.random(350, 700), MakeRandom.random(150, 350)));
                        target.addDestination(new Vector(200, 250));
                    }
                    target.setVelocity(200);
                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                }

                int randomIndex = randomAndRemoveIndex();
                String bulletText = key.get(randomIndex);
                String targetText = MakeRandom.random(words.get(bulletText));

                RoundMovingTarget target = new RoundMovingTarget(getRandomColor(), targetText, 1.5, new Vector(550, 275), 100);
                controller.addTarget(target);
                bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                bullets.add(new Bullet(getRandomColor(), bulletText, targetText));

                for (int i = 0; i < 6; i++) {
                    int randomWrongIndex = MakeRandom.random(remainIndex);
                    bullets.add(new Bullet(getRandomColor(), key.get(randomWrongIndex), ""));
                }
                MakeRandom.shuffle(bullets);
                controller.addBullet(bullets);
            }

            /*
            Round 8 has 12 random targets divided by 2 phases
             */
            case 8 -> {

            }

            case 9 -> {

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

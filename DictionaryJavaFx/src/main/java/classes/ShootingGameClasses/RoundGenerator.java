package classes.ShootingGameClasses;

import classes.data.GameData;
import classes.makerandom.MakeRandom;
import controllers.dictionaryjavafx.ShootingGameController;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import kotlin.collections.MapsKt;

import java.util.ArrayList;
import java.util.HashMap;

public class RoundGenerator {
    private static ArrayList<Integer> remainIndex;
    private static final ArrayList<String> key = GameData.getShootingGameEnglishKeyWords();
    private static final HashMap<String, ArrayList<String>> words = GameData.getShootingGameWords();
    private static final ShootingGameController controller = ShootingGameController.getInstance();
    public static void generateEnBullet_ViTarget(int roundNumber) {
        switch (roundNumber) {
            /*
            round 1 has only 1 still target and 4 bullets
            the key bullet have the same color with the target
             */
            case 1 -> {
                int randomWordIndex = randomAndRemoveIndex();
                String bulletWord = key.get(randomWordIndex); //tu tieng anh
                String targetWord = MakeRandom.random(words.get(bulletWord));//nghia tieng viet
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

                controller.addTarget(new StillTarget(randomColor, targetWord, 10,
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
                    GameObject.Color targetColor = getRandomColor();
                    int range = i * 400;
                    StillTarget target = new StillTarget(targetColor, targetWord, 10,
                            new Vector(MakeRandom.random(100 + range, 400 + range), MakeRandom.random(0, 600)));
                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletWord, targetWord));
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

                StraightMovingTarget target = new StraightMovingTarget(getRandomColor(), targetWord, 20);
                int x = MakeRandom.random(500, 1000);
                target.addDestination(new Vector(x, 0));
                target.addDestination(new Vector(x, 500));
                target.setVelocity(200);
                controller.addTarget(target);

                int randomWrongIndex = MakeRandom.random(remainIndex);
                String wrongBulletWord = key.get(randomWrongIndex);
                ArrayList<Bullet> bullets = new ArrayList<>();
                bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletWord, targetWord));
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

                    StraightMovingTarget target = new StraightMovingTarget(getRandomColor(), targetWord, 20);
                    if (i == 0) {
                        target.addDestination(new Vector(50, 0));
                        target.addDestination(new Vector(1200, 600));
                        target.setVelocity(MakeRandom.random(150, 300));
                    } else if (i == 1) {
                        target.addDestination(new Vector(1200, 0));
                        target.addDestination(new Vector(50, 600));
                        target.setVelocity(MakeRandom.random(150, 300));
                    } else {
                        target.addDestination(new Vector(100, 600));
                        target.addDestination(new Vector(300, 0));
                        target.addDestination(new Vector(500, 600));
                        target.addDestination(new Vector(700, 0));
                        target.addDestination(new Vector(1000, 600));
                        target.setVelocity(MakeRandom.random(200, 300));
                    }
                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletWord, targetWord));
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

                    RoundMovingTarget target = new RoundMovingTarget(getRandomColor(), targetWord, 15,
                            new Vector(MakeRandom.random(300, 1000), MakeRandom.random(300, 500)), MakeRandom.random(100, 200));
                    target.setRoundVelocity(90);
                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletWord, targetWord));
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

                    RoundMovingTarget target = new RoundMovingTarget(getRandomColor(), targetWord, 15);
                    if (i == 2) {
                        target.setCenter(new Vector(200, 100));
                        target.setRadius(100);
                        target.setRoundVelocity(120);
                        target.setMoveDirection(new Vector(1, 0));
                        target.setMoveVelocity(100);
                    } else if (i == 3) {
                        target.setCenter(new Vector(200, 500));
                        target.setRadius(100);
                        target.setRoundVelocity(120);
                        target.setMoveDirection(new Vector(1, 0));
                        target.setMoveVelocity(100);
                    } else {
                        target.setCenter(new Vector(i * 400 + 400, 300));
                        target.setRadius(150);
                        target.setRoundVelocity(120);
                    }

                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletWord, targetWord));
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

                    TeleportingStillTarget target = new TeleportingStillTarget(getRandomColor(), targetText, 20);
                    target.addPosition(new Vector(MakeRandom.random(100, 1200), MakeRandom.random(0, 600)));
                    target.addPosition(new Vector(MakeRandom.random(100, 1200), MakeRandom.random(0, 600)));
                    target.addPosition(new Vector(MakeRandom.random(100, 1200), MakeRandom.random(0, 600)));
                    target.setTimeToJump(2000);
                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletText, targetText));
                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                }

                for (int i = 0; i < 2; i++) {
                    int randomIndex = randomAndRemoveIndex();
                    String bulletText = key.get(randomIndex);
                    String targetText = MakeRandom.random(words.get(bulletText));

                    StillTarget target = new StillTarget(getRandomColor(), targetText, 10,
                            new Vector(MakeRandom.random(300, 500) + i * 300, MakeRandom.random(300, 500)));
                    controller.addTarget(target);

                    bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletText, targetText));
                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                }

                for (int i = 0; i < 2; i++) {
                    int randomIndex = randomAndRemoveIndex();
                    String bulletText = key.get(randomIndex);
                    String targetText = MakeRandom.random(words.get(bulletText));

                    StraightMovingTarget target = new StraightMovingTarget(getRandomColor(), targetText, 30);
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

                    bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletText, targetText));
                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                }

                int randomIndex = randomAndRemoveIndex();
                String bulletText = key.get(randomIndex);
                String targetText = MakeRandom.random(words.get(bulletText));

                RoundMovingTarget target = new RoundMovingTarget(getRandomColor(), targetText, 15, new Vector(550, 275), 100);
                target.setRoundVelocity(150);
                controller.addTarget(target);
                bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletText, targetText));
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
            phase 1 has 50% chance still targets and 50% chance round moving targets
            phase 2 has 50% chance teleporting targets and 50% chance straight moving target
             */
            case 8 -> {
                ArrayList<Integer> randomIndexes = randomAndRemoveIndex(12);
                ArrayList<Bullet> bullets = new ArrayList<>();
                for (int i = 0; i < 6; i++) {
                    int randomTarget = MakeRandom.random(0, 1);
                    String bulletText = key.get(randomIndexes.get(i));
                    String targetText = MakeRandom.random(words.get(bulletText));

                    if (randomTarget == 0) {
                        StillTarget target = new StillTarget(getRandomColor(), targetText, 10,
                                new Vector(MakeRandom.random(50, 1200), MakeRandom.random(0, 600)));
                        controller.addTarget(target);
                        bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletText, targetText));
                        bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                    } else {
                        RoundMovingTarget target = new RoundMovingTarget(getRandomColor(), targetText, 15,
                                new Vector(MakeRandom.random(200, 1100), MakeRandom.random(100, 400)), MakeRandom.random(100, 200));
                        controller.addTarget(target);
                        bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletText, targetText));
                        bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                    }
                }

                for (int i = 6; i < 12; i++) {
                    int randomTarget = MakeRandom.random(0, 1);
                    String bulletText = key.get(randomIndexes.get(i));
                    String targetText = MakeRandom.random(words.get(bulletText));

                    if (randomTarget == 0) {
                        StraightMovingTarget target = new StraightMovingTarget(getRandomColor(), targetText, 30);
                        int randomNumberDestination = MakeRandom.random(2, 6);
                        for (int j = 0; j < randomNumberDestination; j++) {
                            target.addDestination(new Vector(MakeRandom.random(0, 1300), MakeRandom.random(0, 700)));
                        }
                        target.setVelocity(MakeRandom.random(50, 300));
                        controller.addWaitingTarget(target);
                        bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletText, targetText));
                        bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                    } else {
                        TeleportingStillTarget target = new TeleportingStillTarget(getRandomColor(), targetText, 30);
                        int randomNumberPos = MakeRandom.random(2, 4);
                        for (int j = 0; j < randomNumberPos; j++) {
                            target.addPosition(new Vector(MakeRandom.random(0, 1200), MakeRandom.random(0, 600)));
                        }
                        target.setTimeToJump(MakeRandom.random(2000, 4000));
                        controller.addWaitingTarget(target);
                        bullets.add(new Bullet(getRandomColor(target.getColorToBeCountered()), bulletText, targetText));
                        bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                    }
                }

                MakeRandom.shuffle(bullets);
                controller.addBullet(bullets);
            }

            /*
            Round 9 and next have 2 phases with 12 random targets:
            40% chance of still target and static round moving target
            60% chance of moving and teleporting target
             */
            default -> {
                ArrayList<Integer> randomIndexes = randomAndRemoveIndex(12);
                ArrayList<Bullet> bullets = new ArrayList<>();

                for (int i = 0; i < 12; i++) {
                    int randomValue = MakeRandom.random(1, 10);
                    String bulletText = key.get(randomIndexes.get(i));
                    String targetText = MakeRandom.random(words.get(bulletText));
                    GameObject.Color targetColor = getRandomColor();

                    Target checkColorTarget;

                    if (randomValue <= 4) {
                        int randomType = MakeRandom.random(0, 1);
                        if (randomType == 0) {
                            StillTarget target = new StillTarget(targetColor, targetText, 10,
                                    new Vector(MakeRandom.random(0, 1200), MakeRandom.random(0, 600)));
                            if (i < 6) {
                                controller.addTarget(target);
                            } else {
                                controller.addWaitingTarget(target);
                            }
                            checkColorTarget = target;
                        } else {
                            RoundMovingTarget target = new RoundMovingTarget(targetColor, targetText, 15,
                                    new Vector(MakeRandom.random(150, 1150), MakeRandom.random(150, 550)), MakeRandom.random(150, 300));
                            target.setRoundVelocity(MakeRandom.random(60, 180));

                            if (i < 6) {
                                controller.addTarget(target);
                            } else {
                                controller.addWaitingTarget(target);
                            }
                            checkColorTarget = target;
                        }
                    } else {
                        int randomType = MakeRandom.random(0, 2);
                        if (randomType == 0) {
                            RoundMovingTarget target = new RoundMovingTarget(targetColor, targetText, 25);
                            target.setCenter(new Vector(MakeRandom.random(0, 650), MakeRandom.random(0, 700)));
                            target.setRoundVelocity(MakeRandom.random(45, 180));

                            int moveDirY = MakeRandom.random(0, 10);
                            if (target.getCenter().getY() > 350) {
                               moveDirY = -moveDirY;
                            }
                            int moveDirX = MakeRandom.random(1, 10);
                            if (MakeRandom.random(0, 1) == 0) {
                                moveDirX = -moveDirX;
                            }
                            target.setMoveDirection(new Vector(moveDirX, moveDirY));
                            target.setMoveVelocity(MakeRandom.random(10, 200));

                            if (i < 6) {
                                controller.addTarget(target);
                            } else {
                                controller.addWaitingTarget(target);
                            }
                            checkColorTarget = target;
                        } else if (randomType == 1) {
                            StraightMovingTarget target = new StraightMovingTarget(targetColor, targetText, 30);
                            target.setVelocity(MakeRandom.random(50, 300));
                            int randomNumberDes = MakeRandom.random(2, 6);
                            for (int j = 0; j < randomNumberDes; j++) {
                                target.addDestination(new Vector(MakeRandom.random(0, 1300), MakeRandom.random(0, 700)));
                            }

                            if (i < 6) {
                                controller.addTarget(target);
                            } else {
                                controller.addWaitingTarget(target);
                            }
                            checkColorTarget = target;
                        } else {
                            TeleportingStillTarget target = new TeleportingStillTarget(targetColor, targetText, 30);
                            target.setTimeToJump(MakeRandom.random(1500, 4000));
                            int randomNumberPos = MakeRandom.random(2, 4);
                            for (int j = 0; j < randomNumberPos; j++) {
                                target.addPosition(new Vector(MakeRandom.random(0, 1250), MakeRandom.random(0, 650)));
                            }

                            if (i < 6) {
                                controller.addTarget(target);
                            } else {
                                controller.addWaitingTarget(target);
                            }
                            checkColorTarget = target;
                        }
                    }

                    bullets.add(new Bullet(getRandomColor(checkColorTarget.getColorToBeCountered()), bulletText, targetText));
                    bullets.add(new Bullet(getRandomColor(), bulletText, targetText));
                }

                MakeRandom.shuffle(bullets);
                controller.addBullet(bullets);
            }
        }
    }

    public static void refillIndex() {
        //tat ca cac tu
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

    private static GameObject.Color getRandomColor(GameObject.Color ignoreColor) {
        int type = MakeRandom.random(0, 1);
        if (ignoreColor == GameObject.Color.RED) {
            if (type == 0) {
                return GameObject.Color.BLUE;
            } else {
                return GameObject.Color.GREEN;
            }
        } else if (ignoreColor == GameObject.Color.BLUE) {
            if (type == 0) {
                return GameObject.Color.GREEN;
            } else {
                return GameObject.Color.RED;
            }
        } else {
            if (type == 0) {
                return GameObject.Color.RED;
            } else {
                return GameObject.Color.BLUE;
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

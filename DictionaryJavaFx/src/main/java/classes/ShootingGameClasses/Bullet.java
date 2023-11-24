package classes.ShootingGameClasses;

public class Bullet extends GameObject{
    private final String keyText;

    public Bullet(Color color, String showText, String keyText) {
        super(color, showText);
        this.keyText = keyText;
    }

    @Override
    public void loadView() {
        switch (color) {
            case RED -> objectView = new ObjectView(showText, ObjectImages.redBullet);
            case BLUE -> objectView = new ObjectView(showText, ObjectImages.blueBullet);
            case GREEN -> objectView = new ObjectView(showText, ObjectImages.greenBullet);
        }
    }

    public String getKeyText() {
        return keyText;
    }
}

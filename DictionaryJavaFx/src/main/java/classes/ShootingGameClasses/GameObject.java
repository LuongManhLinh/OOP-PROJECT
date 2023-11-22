package classes.ShootingGameClasses;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public abstract class GameObject {
    public enum Color {
        RED,
        GREEN,
        BLUE
    }

    protected Color color;
    protected ObjectView objectView;
    protected String showText;

    public GameObject(Color color, String showText) {
        this.color = color;
        this.showText = showText;
        loadView();
    }

    public Color getColor() {
        return color;
    }

    public Color getColorToCounter() {
        if(color == Color.RED) return Color.GREEN;
        else if(color == Color.GREEN) return Color.BLUE;
        else return Color.RED;
    }

    public Color getColorToBeCountered() {
        if(color == Color.RED) return Color.BLUE;
        else if(color == Color.GREEN) return Color.RED;
        else return Color.GREEN;
    }

    public abstract void loadView();

    public ObjectView getObjectView() {
        return objectView;
    }
}

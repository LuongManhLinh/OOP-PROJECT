package classes.ExperimentGameClasses;

import javafx.scene.control.Button;

public class Answer {
    private String name;
    private boolean isKey;
    private Button button;

    public Answer() {
        name = "";
        isKey = false;
        button = new Button();
    }

    public Answer(String name, boolean isKey) {
        this.name = name;
        this.isKey = isKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
        this.button.setText(name);
    }
}

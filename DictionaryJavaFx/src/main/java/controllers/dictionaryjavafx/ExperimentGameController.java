package controllers.dictionaryjavafx;

import classes.ExperimentGameClasses.Answer;
import classes.ExperimentGameClasses.BottleImages;
import classes.ExperimentGameClasses.Result;
import classes.FXMLFiles;
import classes.data.GameData;
import classes.makerandom.MakeRandom;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class ExperimentGameController implements Initializable {
    @FXML private AnchorPane startGamePane;
    @FXML private Button easyButton;
    @FXML private Button normalButton;
    @FXML private Button tenButton;
    @FXML private Button twentyButton;
    @FXML private Button unlimitedButton;

    @FXML private AnchorPane inGamePane;
    @FXML private Button bottle1, bottle2, bottle3, bottle4;
    @FXML private ImageView carrierBottle;
    @FXML private Label questionLabel;
    @FXML private Label numberQuestionAnsweredLabel;
    @FXML private Label choosingAnswerLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Button startPreparingButton;

    @FXML private AnchorPane endGamePane;
    @FXML private Label endGameLabel;
    @FXML private Label numberCorrectLabel;
    @FXML private TableView<Result> resultsTableView;
    @FXML private TableColumn<Result, String> questionColumn;
    @FXML private TableColumn<Result, String> yourAnswerColumn;
    @FXML private TableColumn<Result, String> trueAnswerColumn;

    @FXML private ImageView resultBottle;
    @FXML private ImageView successLight;

    private enum Mode {
        EASY,
        NORMAL,
        UNSELECTED
    }

    private enum Formula {
        TEN,
        TWENTY,
        UNLIMITED,
        UNSELECTED
    }

    private enum Status {
        START_GAME,
        IN_GAME,
        END_GAME
    }

    private enum EndGameStatus {
        FAIL,
        NO_REACTION,
        QUITE_SUCCESS,
        SUCCESS
    }

    private Mode mode = Mode.UNSELECTED;
    private Formula formula = Formula.UNSELECTED;

    private EndGameStatus endGameStatus = EndGameStatus.NO_REACTION;

    private final ArrayList<Button> modeButtons = new ArrayList<>();
    private final ArrayList<Button> formulaButtons = new ArrayList<>();

    private final int NUMBER_INGAME_BUTTON = 4;
    private final double DROP_POS_X = 380;
    private final double DROP_POS_Y = 170;
    private final double MAIN_BOTTLE_POS_X = 250;
    private final double MAIN_BOTTLE_POS_Y = 330;
    private final ArrayList<Button> inGameButtons = new ArrayList<>();


    private HashMap<String, String> Words;

    private ArrayList<String> EnglishKeyWords;

    private int numberQuestion;
    private int numberQuestionAnswered;
    private int numberCorrectAnswers;

    private ArrayList<Answer> answers;
    private String question;
    private String trueAnswer;
    private ArrayList<Integer> remainIndex;

    private int mouseIsOnWhichButton = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modeButtons.add(easyButton);
        modeButtons.add(normalButton);

        formulaButtons.add(tenButton);
        formulaButtons.add(twentyButton);
        formulaButtons.add(unlimitedButton);

        inGameButtons.add(bottle1);
        inGameButtons.add(bottle2);
        inGameButtons.add(bottle3);
        inGameButtons.add(bottle4);

        for (int i = 0; i < NUMBER_INGAME_BUTTON; i++) {
            int finalI = i;
            Button button = inGameButtons.get(i);
            button.setOnMouseEntered(event -> {
                choosingAnswerLabel.setVisible(true);
                choosingAnswerLabel.setText(button.getText());
                mouseIsOnWhichButton = finalI;
            });

            button.setOnMouseExited(event -> {
                choosingAnswerLabel.setVisible(false);
                mouseIsOnWhichButton = -1;
            });
        }

        questionLabel.setOnMouseEntered(event -> {
            choosingAnswerLabel.setVisible(true);
            choosingAnswerLabel.setText(questionLabel.getText());
        });

        questionLabel.setOnMouseExited(event -> {
            choosingAnswerLabel.setVisible(false);
        });

        installTable();
        changeScene(Status.START_GAME);
    }

    private void installTable() {
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        yourAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("yourAnswer"));
        trueAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("trueAnswer"));

        resultsTableView.setRowFactory(new Callback<>() {
            @Override
            public TableRow<Result> call(TableView<Result> resultTableView) {
                return new TableRow<>() {
                    @Override
                    protected void updateItem(Result item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            getStyleClass().removeAll("row-red", "row-green");
                        } else {
                            if (item.getTrueAnswer().equals(item.getYourAnswer())) {
                                getStyleClass().remove("row-red");
                                getStyleClass().add("row-green");
                            } else {
                                getStyleClass().remove("row-green");
                                getStyleClass().add("row-red");
                            }

                        }
                    }
                };
            }
        });
    }

    private void loadData() {
        if (mode != Mode.UNSELECTED && formula != Formula.UNSELECTED) {
            switch (mode) {
                case EASY -> {
                    Words = GameData.getEasyWords();
                    EnglishKeyWords = GameData.getEasyEnglishKeyWords();
                }
                case NORMAL -> {
                    Words = GameData.getNormalWords();
                    EnglishKeyWords = GameData.getNormalEnglishKeyWords();
                }
            }

            switch (formula) {
                case TEN -> {
                    numberQuestion = 10;
                    progressBar.setVisible(true);
                    startPreparingButton.setVisible(false);
                }
                case TWENTY -> {
                    numberQuestion = 20;
                    progressBar.setVisible(true);
                    startPreparingButton.setVisible(false);
                }
                case UNLIMITED -> {
                    numberQuestion = -1;
                    progressBar.setVisible(false);
                    startPreparingButton.setVisible(true);
                }
            }
            refillRemainIndexes();
            updateQuestionAndAnswer();

            fadeOut();
        }
    }

    public void onEasyModeClicked(ActionEvent event) {
        if (!mode.equals(Mode.EASY)) {
            mode = Mode.EASY;
            changeButtonColor((Button) event.getSource(), modeButtons);
            loadData();
        } else {
            mode = Mode.UNSELECTED;
            resetButtonColor((Button) event.getSource());
        }
    }

    public void onNormalModeClicked(ActionEvent event) {
        if (!mode.equals(Mode.NORMAL)) {
            mode = Mode.NORMAL;
            changeButtonColor((Button) event.getSource(), modeButtons);
            loadData();
        } else {
            mode = Mode.UNSELECTED;
            resetButtonColor((Button) event.getSource());
        }
    }

    public void on10Clicked(ActionEvent event) {
        if (!formula.equals(Formula.TEN)) {
            formula = Formula.TEN;
            changeButtonColor((Button) event.getSource(), formulaButtons);
            loadData();
        } else {
            formula = Formula.UNSELECTED;
            resetButtonColor((Button) event.getSource());
        }
    }

    public void on20Clicked(ActionEvent event) {
        if (!formula.equals(Formula.TWENTY)) {
            formula = Formula.TWENTY;
            changeButtonColor((Button) event.getSource(), formulaButtons);
            loadData();
        } else {
            formula = Formula.UNSELECTED;
            resetButtonColor((Button) event.getSource());
        }
    }

    public void onUnlimitedClicked(ActionEvent event) {
        if (!formula.equals(Formula.UNLIMITED)) {
            formula = Formula.UNLIMITED;
            changeButtonColor((Button) event.getSource(), formulaButtons);
            loadData();
        } else {
            formula = Formula.UNSELECTED;
            resetButtonColor((Button) event.getSource());
        }
    }

    public void onQuit() {
        SceneLoaderController.loadScene(FXMLFiles.MAIN_UI_SCENE);
    }

    public void onAnswerClicked(ActionEvent event) {
        Button button = (Button) event.getSource();

        for (Answer answer : answers) {
            if (answer.getButton().equals(button)) {
                if (answer.isKey()) {
                    numberCorrectAnswers++;
                }
                break;
            }
        }

        moveSelectedBottle(button);
        addNewItemToResultTable(question, button.getText(), trueAnswer);
    }

    public void onStartPreparing() {
        numberQuestion = numberQuestionAnswered - 1;
        if (numberCorrectAnswers < 0.5 * numberQuestion) {
            endGameStatus = EndGameStatus.FAIL;
        } else if (numberCorrectAnswers >= 0.5 * numberQuestion && numberCorrectAnswers <= 0.6 * numberQuestion) {
            endGameStatus = EndGameStatus.NO_REACTION;
        } else if (numberCorrectAnswers >= 0.7 * numberQuestion && numberCorrectAnswers <= 0.8 * numberQuestion) {
            endGameStatus = EndGameStatus.QUITE_SUCCESS;
        } else {
            endGameStatus = EndGameStatus.SUCCESS;
        }
        resultAnimation();
    }

    public void onReplayClicked() {
        reset();
        changeScene(Status.START_GAME);
    }

    private void fadeOut() {
        AnimationTimer fadeTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                startGamePane.setOpacity(startGamePane.getOpacity() - 0.005);
                if (startGamePane.getOpacity() <= 0) {
                    stop();
                    startGamePane.setOpacity(1);
                    changeScene(Status.IN_GAME);
                }
            }
        };
        fadeTimer.start();
    }

    private ArrayList<Answer> generateRandomQuestionAndAnswers(int numberAnswer, boolean inVietnamese) {
        ArrayList<Answer> result = new ArrayList<>();

        if (inVietnamese) {
            for (int i = 0; i < numberAnswer; i++) {
                int randomIndex = MakeRandom.random(0, remainIndex.size() - 1);
                int answerIndex = remainIndex.get(randomIndex);
                remainIndex.remove(randomIndex);
                if (remainIndex.isEmpty()) {
                    refillRemainIndexes();
                }

                String meanings = Words.get(EnglishKeyWords.get(answerIndex));
                Answer answer = new Answer(meanings, false);
                result.add(answer);

                // first answer is the true answer
                if (i == 0) {
                    question = EnglishKeyWords.get(answerIndex);
                    result.get(i).setKey(true);
                    trueAnswer = result.get(i).getName();
                }
            }
        } else {
            for (int i = 0; i < numberAnswer; i++) {
                int randomIndex = MakeRandom.random(0, remainIndex.size() - 1);
                int answerIndex = remainIndex.get(randomIndex);
                remainIndex.remove(randomIndex);
                if (remainIndex.isEmpty()) {
                    refillRemainIndexes();
                }

                String meanings = EnglishKeyWords.get(answerIndex);
                Answer answer = new Answer(meanings, false);
                result.add(answer);

                // first answer is the true answer
                if (i == 0) {
                    question = Words.get(EnglishKeyWords.get(answerIndex));
                    result.get(i).setKey(true);
                    trueAnswer = result.get(i).getName();
                }
            }
        }

        MakeRandom.shuffle(result);

        ArrayList<Image> chosenImages = MakeRandom.randomElements(BottleImages.allBottles, 4);
        for (int i = 0; i < NUMBER_INGAME_BUTTON; i++) {
            ((ImageView) inGameButtons.get(i).getGraphic()).setImage(chosenImages.get(i));
        }

        return result;
    }

    public void updateQuestionAndAnswer() {
        numberQuestionAnswered++;

        numberQuestionAnsweredLabel.setText(String.valueOf(numberQuestionAnswered));
        int randomNumber = MakeRandom.random(0, 1);
        if (randomNumber == 0) {
            answers = generateRandomQuestionAndAnswers(4, true);
        } else {
            answers = generateRandomQuestionAndAnswers(4, false);
        }

        questionLabel.setText(question);

        for (int i = 0; i < NUMBER_INGAME_BUTTON; i++) {
            answers.get(i).setButton(inGameButtons.get(i));
        }

        if (mouseIsOnWhichButton != -1) {
            choosingAnswerLabel.setText(inGameButtons.get(mouseIsOnWhichButton).getText());
        }

        if (!formula.equals(Formula.UNLIMITED)) {
            progressBar.setProgress((double) (numberQuestionAnswered - 1) / numberQuestion);
            if (numberQuestionAnswered > numberQuestion) {
                if (numberCorrectAnswers < 0.5 * numberQuestion) {
                    endGameStatus = EndGameStatus.FAIL;
                } else if (numberCorrectAnswers >= 0.5 * numberQuestion && numberCorrectAnswers <= 0.6 * numberQuestion) {
                    endGameStatus = EndGameStatus.NO_REACTION;
                } else if (numberCorrectAnswers >= 0.7 * numberQuestion && numberCorrectAnswers <= 0.8 * numberQuestion) {
                    endGameStatus = EndGameStatus.QUITE_SUCCESS;
                } else {
                    endGameStatus = EndGameStatus.SUCCESS;
                }
                resultAnimation();
            }
        }
    }

    private void moveSelectedBottle(Button button) {
        ImageView selectedBottle = (ImageView) button.getGraphic();
        selectedBottle.setVisible(false);
        carrierBottle.setVisible(true);
        carrierBottle.setImage(selectedBottle.getImage());

        for (int i = 0; i < NUMBER_INGAME_BUTTON; i++) {
            inGameButtons.get(i).setOnAction(null);
        }

        carrierBottle.setLayoutX(button.getLayoutX() + 35);
        carrierBottle.setLayoutY(button.getLayoutY() + 60);
        double moveDirX = DROP_POS_X - carrierBottle.getLayoutX();
        double moveDirY = DROP_POS_Y - carrierBottle.getLayoutY();

        AnimationTimer transition = new AnimationTimer() {
            @Override
            public void handle(long l) {
                carrierBottle.setLayoutX(carrierBottle.getLayoutX() + moveDirX * 0.02);
                carrierBottle.setLayoutY(carrierBottle.getLayoutY() + moveDirY * 0.02);

                if (Math.abs(carrierBottle.getLayoutX() - DROP_POS_X) < 0.05 && Math.abs(carrierBottle.getLayoutY() - DROP_POS_Y) < 0.05) {
                    dropCarrierBottle(selectedBottle);
                    stop();
                }
            }
        };

        transition.start();
    }

    private void dropCarrierBottle(ImageView selectedBottle) {
        ArrayList<Image> bottleImages = BottleImages.allBottles.stream().filter(images ->
                images.get(0) == carrierBottle.getImage()).findFirst().orElse(new ArrayList<>());
        Timeline t4 = new Timeline(
                new KeyFrame(Duration.millis(200), event -> {
                    carrierBottle.setRotate(0);
                    updateQuestionAndAnswer();
                    for (int i = 0; i < NUMBER_INGAME_BUTTON; i++) {
                        inGameButtons.get(i).setOnAction(this::onAnswerClicked);
                    }

                    selectedBottle.setVisible(true);
                    carrierBottle.setVisible(false);
                })
        );
        Timeline t3 = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    carrierBottle.setRotate(-95);
                    carrierBottle.setImage(bottleImages.get(3));
                    t4.play();
                })
        );
        Timeline t2 = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    carrierBottle.setRotate(-90);
                    carrierBottle.setImage(bottleImages.get(2));
                    t3.play();
                })
        );
        Timeline t1 = new Timeline(
                new KeyFrame(Duration.millis(100), event -> {
                    carrierBottle.setRotate(-45);
                    carrierBottle.setImage(bottleImages.get(1));
                    t2.play();
                })
        );
        t1.play();
    }

    private void resultAnimation() {
        inGamePane.setVisible(false);
        double midPointX = 500 - resultBottle.getFitWidth() / 2;
        double midPointY = 300 - resultBottle.getFitHeight() / 2;

        double moveDirX = midPointX - resultBottle.getLayoutX();
        double moveDirY = midPointY - resultBottle.getLayoutY();

        final boolean[] isIncreasingDegree = {false};
        final int[] numberRotate = {0};

        AnimationTimer rotateTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (isIncreasingDegree[0]) {
                    resultBottle.setRotate(resultBottle.getRotate() + (1 +  (double) numberRotate[0] / 5));
                    if (resultBottle.getRotate() >= 15) {
                        isIncreasingDegree[0] = false;
                        numberRotate[0]++;
                    }
                } else {
                    resultBottle.setRotate(resultBottle.getRotate() -  (1 + (double) numberRotate[0] / 5));
                    if (resultBottle.getRotate() <= -15) {
                        isIncreasingDegree[0] = true;
                        numberRotate[0]++;
                    }
                }

                if (numberRotate[0] == 30) {
                    stop();

                    if (endGameStatus == EndGameStatus.FAIL) {
                        resultBottle.setRotate(0);
                        resultBottle.setImage(BottleImages.failed);
                        moveResultBottleBack();
                    } else {
                        slowRotatingDown();
                    }
                }
            }
        };

        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                resultBottle.setLayoutX(resultBottle.getLayoutX() + moveDirX * 0.01);
                resultBottle.setLayoutY(resultBottle.getLayoutY() + moveDirY * 0.01);

                if (Math.abs(resultBottle.getLayoutX() - midPointX) < 0.05 && Math.abs(resultBottle.getLayoutY() - midPointY) < 0.05) {
                    stop();
                    rotateTimer.start();
                }
            }
        };
        moveTimer.start();
    }

    private void slowRotatingDown() {
        final boolean[] isIncreasingDegree = {false};
        final double[] rotateIncrease = {5};

        switch (endGameStatus) {
            case QUITE_SUCCESS -> {
                resultBottle.setImage(BottleImages.quiteSuccess);
            }
            case SUCCESS -> {
                successLight.setVisible(true);
                successLight.setLayoutX(500 - successLight.getFitWidth() / 2);
                successLight.setLayoutY(300 - successLight.getFitHeight() / 2);
                resultBottle.setImage(BottleImages.success);
            }
        }

        AnimationTimer rotateTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (isIncreasingDegree[0]) {
                    resultBottle.setRotate(resultBottle.getRotate() + rotateIncrease[0]);
                    if (resultBottle.getRotate() >= 15) {
                        isIncreasingDegree[0] = false;
                        rotateIncrease[0] -= 0.5;
                    }
                } else {
                    resultBottle.setRotate(resultBottle.getRotate() - rotateIncrease[0]);
                    if (resultBottle.getRotate() <= -15) {
                        isIncreasingDegree[0] = true;
                        rotateIncrease[0] -= 0.5;
                    }
                }
                if (rotateIncrease[0] < 1 && (resultBottle.getRotate() > -1 && resultBottle.getRotate() < 1)) {
                    stop();
                    resultBottle.setRotate(0);
                    moveResultBottleBack();
                }
            }
        };

        rotateTimer.start();
    }

    private void moveResultBottleBack() {
        double moveDirX = 30 - resultBottle.getLayoutX();
        double moveDirY = 265 - resultBottle.getLayoutY();

        AnimationTimer moveTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                resultBottle.setLayoutX(resultBottle.getLayoutX() + moveDirX * 0.02);
                resultBottle.setLayoutY(resultBottle.getLayoutY() + moveDirY * 0.02);
                if (endGameStatus == EndGameStatus.SUCCESS) {
                    successLight.setLayoutX(successLight.getLayoutX() + moveDirX * 0.02);
                    successLight.setLayoutY(successLight.getLayoutY() + moveDirY * 0.02);
                }

                if (Math.abs(resultBottle.getLayoutX() - 30) < 0.05 && Math.abs(resultBottle.getLayoutY() - 265) < 0.05) {
                    stop();
                    endGameMessaging();
                    changeScene(Status.END_GAME);
                }
            }
        };

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(1000), event -> {
                    moveTimer.start();
                })
        );
        timeline.play();
    }

    private void endGameMessaging() {
        String message = "";
        int randomNumber = MakeRandom.random(0, 1);
        switch (endGameStatus) {
            case FAIL -> {
                if (randomNumber == 0) {
                    message = "Failed!";
                } else {
                    message = "Exploded!";
                }
            }
            case NO_REACTION -> {
                if (randomNumber == 0) {
                    message = "Nothing happened!";
                } else {
                    message = "There's no reaction!";
                }
            }
            case QUITE_SUCCESS -> {
                if (randomNumber == 0) {
                    message = "That's pretty good!";
                } else {
                    message = "It's so closed!";
                }
            }
            case SUCCESS -> {
                if (randomNumber == 0) {
                    message = "Excellent! Successful experiment!";
                } else {
                    message = "It worked! Well done!";
                }
            }
        }
        endGameLabel.setText(message);
        numberCorrectLabel.setText(numberCorrectAnswers + "/" + numberQuestion + " correct!");
    }

    private void changeScene(Status status) {
        switch (status) {
            case START_GAME -> {
                startGamePane.setVisible(true);
                inGamePane.setVisible(false);
                endGamePane.setVisible(false);
                resultBottle.setVisible(false);
            }

            case IN_GAME -> {
                startGamePane.setVisible(false);
                inGamePane.setVisible(true);
                endGamePane.setVisible(false);
                resultBottle.setVisible(true);
            }

            case END_GAME -> {
                startGamePane.setVisible(false);
                inGamePane.setVisible(false);
                endGamePane.setVisible(true);
            }
        }
    }

    private void addNewItemToResultTable(String question, String yourAnswer, String trueAnswer) {
        Result result = new Result(question, yourAnswer, trueAnswer);
        ObservableList<Result> resultObservableList = resultsTableView.getItems();
        resultObservableList.add(result);
        resultsTableView.setItems(resultObservableList);
    }

    private void reset() {
        resultBottle.setImage(BottleImages.mainBottle);
        resultBottle.setLayoutX(MAIN_BOTTLE_POS_X);
        resultBottle.setLayoutY(MAIN_BOTTLE_POS_Y);
        successLight.setVisible(false);

        numberQuestionAnswered = 0;
        numberCorrectAnswers = 0;

        mode = Mode.UNSELECTED;
        formula = Formula.UNSELECTED;

        for (Button button : modeButtons) {
            resetButtonColor(button);
        }
        for (Button button : formulaButtons) {
            resetButtonColor(button);
        }

        clearResultTable();
    }

    private void refillRemainIndexes() {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < EnglishKeyWords.size(); i++) {
            indexes.add(i);
        }
        remainIndex = indexes;
    }

    private void clearResultTable() {
        resultsTableView.getItems().clear();
    }

    private void changeButtonColor(Button targetButton, ArrayList<Button> buttons) {
        for (Button button : buttons) {
            if (button.equals(targetButton)) {
                button.setStyle("-fx-background-color: red");
            } else {
                button.setStyle(null);
            }
        }
    }

    private void resetButtonColor(Button button) {
        button.setStyle(null);
    }
}

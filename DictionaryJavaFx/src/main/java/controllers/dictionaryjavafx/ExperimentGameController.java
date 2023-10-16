package controllers.dictionaryjavafx;

import classes.ExperimentGameClasses.Answer;
import classes.ExperimentGameClasses.BottleImages;
import classes.ExperimentGameClasses.Result;
import classes.GameData;
import classes.makerandom.MakeRandom;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;

public class ExperimentGameController implements Initializable {
    @FXML private AnchorPane startGamePane;

    @FXML private AnchorPane inGamePane;
    @FXML private Button bottle1, bottle2, bottle3, bottle4;
    @FXML private Label questionLabel;
    @FXML private Label numberQuestionAnsweredLabel;

    @FXML private AnchorPane endGamePane;
    @FXML private Label endGameLabel;
    @FXML private Label numberCorrectLabel;
    @FXML private TableView<Result> resultsTableView;
    @FXML private TableColumn<Result, String> questionColumn;
    @FXML private TableColumn<Result, String> yourAnswerColumn;
    @FXML private TableColumn<Result, String> trueAnswerColumn;


    private enum Status {
        START_GAME,
        IN_GAME,
        END_GAME
    }
    private HashMap<String, String> Words;

    private ArrayList<String> EnglishKeyWords;


    private int numberQuestionAnswered;
    private int numberCorrectAnswers;

    private ArrayList<Answer> answers;
    private String question;
    private String trueAnswer;
    private ArrayList<Integer> remainIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        yourAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("yourAnswer"));
        trueAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("trueAnswer"));

        changeScene(Status.START_GAME);
    }

    public void onEasyModeClicked(ActionEvent event) {
        Words = GameData.getEasyWords();
        EnglishKeyWords = GameData.getEasyEnglishKeyWords();
        changeScene(Status.IN_GAME);
        reset();
    }

    public void onNotEasyModeClicked(ActionEvent event) {
        Words = GameData.getNotEasyWords();
        EnglishKeyWords = GameData.getNotEasyEnglishKeyWords();
        changeScene(Status.IN_GAME);
        reset();
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

        addNewItemToResultTable(question, button.getText(), trueAnswer);
        updateQuestionAndAnswer();
    }

    public void onReplayClicked(ActionEvent event) {
        changeScene(Status.START_GAME);
        reset();
    }

    private ArrayList<Answer> generateRandomQuestionAndAnswers(int numberAnswer, boolean inVietnamese) {
        ArrayList<Answer> result = new ArrayList<>();

        if (inVietnamese) {
            for (int i = 0; i < numberAnswer; i++) {
                int randomIndex = MakeRandom.random(0, remainIndex.size() - 1);
                int answerIndex = remainIndex.get(randomIndex);
                remainIndex.remove(randomIndex);

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
        }

        MakeRandom.shuffle(result);
        ArrayList<Image> chosenImages = MakeRandom.randomElements(BottleImages.containingBottles, 4);
        ((ImageView) bottle1.getGraphic()).setImage(chosenImages.get(0));
        ((ImageView) bottle2.getGraphic()).setImage(chosenImages.get(1));
        ((ImageView) bottle3.getGraphic()).setImage(chosenImages.get(2));
        ((ImageView) bottle4.getGraphic()).setImage(chosenImages.get(3));
        return result;
    }



    public void updateQuestionAndAnswer() {
        numberQuestionAnswered++;
        numberQuestionAnsweredLabel.setText(String.valueOf(numberQuestionAnswered));

        if (numberQuestionAnswered < 10) {
            answers = generateRandomQuestionAndAnswers(4, true);

            questionLabel.setText(question);

            answers.get(0).setButton(bottle1);
            answers.get(1).setButton(bottle2);
            answers.get(2).setButton(bottle3);
            answers.get(3).setButton(bottle4);
        } else {
            endGameMessaging();
            changeScene(Status.END_GAME);
        }
    }

    private void endGameMessaging() {
        String message;
        if (numberCorrectAnswers < 5) {
            message = "Boommm! You lose";
        } else if (numberCorrectAnswers == 5 || numberCorrectAnswers == 6) {
            message = "Nothing happened!";
        } else if (numberCorrectAnswers == 7 || numberCorrectAnswers == 8) {
            message = "That's pretty good";
        } else {
            message = "Excellent! Successful Experiment";
        }
        endGameLabel.setText(message);
        numberCorrectLabel.setText(numberCorrectAnswers + "/10 correct!");
    }

    private void changeScene(Status status) {
        switch (status) {
            case START_GAME -> {
                startGamePane.setVisible(true);
                inGamePane.setVisible(false);
                endGamePane.setVisible(false);
            }

            case IN_GAME -> {
                startGamePane.setVisible(false);
                inGamePane.setVisible(true);
                endGamePane.setVisible(false);
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
        numberQuestionAnswered = -1;
        numberCorrectAnswers = 0;

        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < EnglishKeyWords.size(); i++) {
            indexes.add(i);
        }
        remainIndex = indexes;
        clearResultTable();
        updateQuestionAndAnswer();
    }

    private void clearResultTable() {
        resultsTableView.getItems().clear();
    }
}

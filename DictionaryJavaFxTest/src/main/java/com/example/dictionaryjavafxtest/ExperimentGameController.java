package com.example.dictionaryjavafxtest;

import com.example.dictionaryjavafxtest.ExperimentGameClasses.Answer;
import com.example.dictionaryjavafxtest.ExperimentGameClasses.Result;
import com.example.dictionaryjavafxtest.makerandom.MakeRandom;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;

public class ExperimentGameController implements Initializable {
    @FXML private Button bottle1, bottle2, bottle3, bottle4;
    @FXML private Label questionLabel;
    @FXML private Label numberQuestionAnsweredLabel;

    @FXML private Label endGameLabel;
    @FXML private TableView<Result> resultsTableView;
    @FXML private TableColumn<Result, String> questionColumn;
    @FXML private TableColumn<Result, String> yourAnswerColumn;
    @FXML private TableColumn<Result, String> trueAnswerColumn;


    private HashMap<String, String> Words;

    private ArrayList<String> EnglishKeyWords;


    private int numberQuestionAnswered;
    private int numberCorrectAnswers;

    private ArrayList<Answer> answers;
    private String question;
    private String trueAnswer;

    ObservableList<Result> observableList = FXCollections.observableArrayList(
            new Result("1", "2", "3"),
            new Result("1", "2", "3"),
            new Result("1", "2", "3")

    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("question"));
        yourAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("yourAnswer"));
        trueAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("trueAnswer"));

        resultsTableView.setItems(observableList);

        Words = Dictionary.getWords();
        EnglishKeyWords = Dictionary.getEnglishKeyWord();
        numberQuestionAnswered = -1;
        numberCorrectAnswers = 0;
        updateQuestionAndAnswer();
    }

    private ArrayList<Answer> generateRandomQuestionAndAnswers(int numberAnswer, boolean inVietnamese) {
//        ArrayList<Answer> result = new ArrayList<>();
//        int n = EnglishKeyWords.size() - 1;
//        ArrayList<Integer> remainIndex = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            remainIndex.add(i);
//        }
//
//        if (inVietnamese) {
//            for (int i = 0; i < numberAnswer; i++) {
//                int answerIndex = MakeRandom.random(remainIndex);
//                String meanings = Words.get(EnglishKeyWords.get(answerIndex));
//                Answer answer = new Answer(meanings.get(MakeRandom.random(0, meanings.size() - 1)), false);
//                result.add(answer);
//
//                if (i == 0) {
//                    question = EnglishKeyWords.get(answerIndex);
//                    result.get(i).setKey(true);
//                    trueAnswer = result.get(i).getName();
//                }
//            }
//        }
//
//        MakeRandom.shuffle(result);
        return null;
    }

    public void onAnswerClicked(ActionEvent event) {
        Button button = (Button) event.getSource();

        for (Answer answer : answers) {
            if (answer.getButton().equals(button)) {
                if (answer.isKey()) {
                    System.out.println("True");
                    numberCorrectAnswers++;
                } else {
                    System.out.println("False");
                }
                break;
            }
        }

        addNewItemToResultTable(question, button.getText(), trueAnswer);
        updateQuestionAndAnswer();
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
            changeVisibleAnswersAndQuestion(false);
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
        endGameLabel.setVisible(true);
        endGameLabel.setText(message);
    }

    private void changeVisibleAnswersAndQuestion(boolean isVisible) {
        questionLabel.setVisible(isVisible);
        bottle1.setVisible(isVisible);
        bottle2.setVisible(isVisible);
        bottle3.setVisible(isVisible);
        bottle4.setVisible(isVisible);

        resultsTableView.setVisible(isVisible);
    }

    private void addNewItemToResultTable(String question, String yourAnswer, String trueAnswer) {
        Result result = new Result(question, yourAnswer, trueAnswer);
        ObservableList<Result> resultObservableList = resultsTableView.getItems();
        resultObservableList.add(result);
        resultsTableView.setItems(resultObservableList);
    }

    private void addNewItemToResultTable() {
        Result result = new Result("question", "answer", "answer");
        ObservableList<Result> resultObservableList= resultsTableView.getItems();
        resultObservableList.add(result);
        resultsTableView.setItems(resultObservableList);
    }

}

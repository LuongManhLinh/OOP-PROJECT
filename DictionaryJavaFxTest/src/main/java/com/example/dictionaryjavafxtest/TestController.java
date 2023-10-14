package com.example.dictionaryjavafxtest;

import com.example.dictionaryjavafxtest.ExperimentGameClasses.Result;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {
    @FXML private TableView<Result> resultsTableView;
    @FXML private TableColumn<Result, String> questionColumn = new TableColumn<>("1");
    @FXML private TableColumn<Result, String> yourAnswerColumn = new TableColumn<>("2");
    @FXML private TableColumn<Result, String> trueAnswerColumn = new TableColumn<>("3");

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
        resultsTableView.getColumns().addAll(questionColumn, yourAnswerColumn, trueAnswerColumn);
        resultsTableView.setItems(observableList);
    }
}

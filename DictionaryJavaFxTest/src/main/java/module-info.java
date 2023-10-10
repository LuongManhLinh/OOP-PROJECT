module com.example.dictionaryjavafxtest {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.dictionaryjavafxtest to javafx.fxml;
    exports com.example.dictionaryjavafxtest;
    exports com.example.dictionaryjavafxtest.dictionarycommandline;
    opens com.example.dictionaryjavafxtest.dictionarycommandline to javafx.fxml;
}
module controllers.dictionaryjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;
    requires jlayer;
    requires javafx.web;

    opens controllers.dictionaryjavafx to javafx.fxml;
    exports controllers.dictionaryjavafx;
    exports classes;
    opens classes to javafx.fxml;
    exports classes.ExperimentGameClasses;
    opens classes.ExperimentGameClasses to javafx.fxml;
}
module controllers.dictionaryjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.almasb.fxgl.all;
    requires jlayer;

    opens controllers.dictionaryjavafx to javafx.fxml;
    exports controllers.dictionaryjavafx;
    exports classes;
    opens classes to javafx.fxml;
    exports classes.ExperimentGameClasses;
    opens classes.ExperimentGameClasses to javafx.fxml;
    exports classes.data;
    opens classes.data to javafx.fxml;
    exports classes.ShootingGameClasses;
    opens classes.ShootingGameClasses to javafx.fxml;
}
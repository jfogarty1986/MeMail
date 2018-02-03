package memail;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String APPLICATION_TITLE = "MeMail";
    private static final String LOGIN_SCREEN_PATH = "login/login.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource(LOGIN_SCREEN_PATH));
        primaryStage.setTitle(APPLICATION_TITLE);
        Scene scene = new Scene(root, 600, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

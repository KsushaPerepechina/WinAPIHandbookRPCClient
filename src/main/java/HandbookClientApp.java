import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static java.lang.System.exit;

public class HandbookClientApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(HandbookClientApp.class.getResource("form.fxml"));
            primaryStage.setTitle("WinAPI Handbook");
            primaryStage.setScene(new Scene(root, 800, 800));
            primaryStage.show();
        }
        catch (Exception exception) {
            exit(1);
        }
    }
}
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(final Stage stage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass()
                .getResource("/HemmingNetUi.fxml")
        );

        Scene scene = new Scene(root, 700, 400);

        scene.getStylesheets().add("game.css");
        stage.setScene(scene);
        stage.setTitle("Нейронная сеть Хемминга");
        stage.show();
    }

    public static void main(final String[] arguments) {
        Application.launch(arguments);
    }
}

package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import LogIn.*; // Asegúrate de importar el controlador si lo necesitas

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Asegúrate de que la ruta sea correcta
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/LogIn/LogIn.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Login Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

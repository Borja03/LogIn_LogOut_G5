package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LogInController;

/**
 * Clase principal de la aplicación que extiende javafx.application.Application.
 * Esta clase es responsable de iniciar la aplicación y cargar la interfaz de
 * usuario.
 */
public class Application extends javafx.application.Application {

    /**
     * Método que se ejecuta al iniciar la aplicación. Carga el archivo FXML de
     * la vista de inicio de sesión y configura la escena.
     *
     * @param primaryStage El escenario principal donde se mostrará la interfaz
     * de usuario.
     * @throws Exception Si ocurre un error al cargar el archivo FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));

        Parent root = loader.load();
        LogInController controller = loader.getController();
        Stage newStage = new Stage();
        controller.setStage(newStage);
        controller.initialize(root);
    }

    /**
     * Método principal que inicia la aplicación.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch(args); // Llama al método launch para iniciar la aplicación
    }
}

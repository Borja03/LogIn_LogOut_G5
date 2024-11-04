package Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.LogInController;

/**
 * Clase principal de la aplicación que extiende {@link javafx.application.Application}.
 * <p>
 * Esta clase es responsable de inicializar y ejecutar la aplicación JavaFX, cargando
 * la interfaz gráfica y configurando la escena inicial para la vista de inicio de sesión.
 * Utiliza un archivo FXML para definir la estructura de la interfaz gráfica.
 * </p>
 * <p>
 * Al ejecutarse, esta clase crea un {@link javafx.stage.Stage} primario que representa
 * la ventana principal de la aplicación y un {@link Scene} inicial cargado desde el archivo
 * FXML ubicado en el paquete <i>view</i>.
 * </p>
 */
public class Application extends javafx.application.Application {

    /**
     * Método de inicio de la aplicación, ejecutado automáticamente por JavaFX al lanzar
     * la aplicación mediante el método {@link #main(String[])}. 
     * <p>
     * Este método carga el archivo FXML correspondiente a la interfaz de inicio de sesión,
     * asigna el controlador {@link LogInController}, configura el escenario y establece la
     * escena inicial en la interfaz gráfica.
     * </p>
     *
     * @param primaryStage El escenario principal donde se mostrará la interfaz de usuario.
     * @throws Exception Si ocurre un error al cargar el archivo FXML o al configurar
     *                   la interfaz de usuario.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LogIn.fxml"));
        
        Parent root = loader.load();
        LogInController controller = loader.getController();
        
        // Configuración de un nuevo Stage para la ventana de inicio de sesión
        Stage newStage = new Stage();
        controller.setStage(newStage);
        controller.initialize(root);
    }

    /**
     * Método principal que inicia la ejecución de la aplicación.
     * <p>
     * Este método se encarga de llamar a {@link javafx.application.Application#launch(String...)}
     * para iniciar la aplicación JavaFX, lo cual a su vez ejecutará el método {@link #start(Stage)}
     * para configurar y mostrar la interfaz.
     * </p>
     *
     * @param args Argumentos de línea de comandos. Estos argumentos son recibidos por JavaFX 
     *             pero no suelen utilizarse directamente en aplicaciones JavaFX comunes.
     */
    public static void main(String[] args) {
        launch(args); // Llama al método launch para iniciar la aplicación
    }
}

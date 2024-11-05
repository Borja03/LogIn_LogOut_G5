package view;

import Application.Application;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.isVisible;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogInControllerIT extends org.testfx.framework.junit.ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Application().start(stage);
    }

    @Test
    public void a_ServerError() {
        clickOn("#emailTextField");
        write("aaa@aaa.aaa");
        clickOn("#logInButton");
        verifyThat("Problemas de conexión con el servidor", isVisible());
    }

    @Test
    public void b_ConecctionError() {
        clickOn("#emailTextField");
        write("aaa@aaa.aaa");
        clickOn("#logInButton");
        verifyThat("Problemas de conexión a la base de datos", isVisible());
    }
    
    @Test
    public void c_MaxThread() {
        clickOn("#emailTextField");
        write("aaa@aaa.aaa");
        clickOn("#logInButton");
        verifyThat("No se pudo iniciar sesión. Máximo número de usuarios alcanzado. Espere unos minutos", isVisible());
    }
}
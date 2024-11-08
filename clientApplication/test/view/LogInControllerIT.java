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
import org.testfx.api.FxAssert;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.matcher.base.NodeMatchers;
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
        write("borjaahedo@gmail.com");
        clickOn("#passwordField");
        write("Borja44_");
        clickOn("#logInButton");
        verifyThat("Problemas de conexión con el servidor.", NodeMatchers.isVisible());
    }

    @Test
    public void b_ConecctionError() {
       clickOn("#emailTextField");
        write("borjaahedo@gmail.com");
        clickOn("#passwordField");
        write("Borja44_");
        clickOn("#logInButton");
        verifyThat("Problemas de conexión a la base de datos.", NodeMatchers.isVisible());
    }
    
    @Test
    public void c_MaxThread() {
      clickOn("#emailTextField");
        write("borjaahedo@gmail.com");
        clickOn("#passwordField");
        write("Borja44_");
        clickOn("#logInButton");
        verifyThat("Ocurrió un error inesperado.", isVisible());
    }
}
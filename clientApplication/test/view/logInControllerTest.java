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
public class logInControllerTest extends org.testfx.framework.junit.ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new Application().start(stage);
    }

    @Test
    public void a_correctSignIn() {
        clickOn("#emailTextField");
        write("aa@aa.aa");
        clickOn("#passwordField");
        write("@Aa12345");
        clickOn("#logInButton");
        verifyThat("#mainPane", isVisible());
    }

    @Test
    public void b_invalidEmailFormat() {
        clickOn("#emailTextField");
        write("invalidEmailFormat");
        clickOn("#logInButton");
        verifyThat("El texto tiene que estar en formato email 'example@example.extension'", isVisible());
    }

    @Test
    public void c_incorrectSignInWrongPassword() {
        clickOn("#emailTextField");
        write("aa@aa.aa");
        clickOn("#logInButton");
        verifyThat("No se pudo iniciar sesión. Verifique sus credenciales.", isVisible());
    }
    
    @Test
    public void d_incorrectSignInWrongEmail() {
        clickOn("#emailTextField");
        write("aaa@aaa.aaa");
        clickOn("#passwordField");
        write("@Aa12345");
        clickOn("#logInButton");
        verifyThat("No se pudo iniciar sesión. Verifique sus credenciales.", isVisible());
    }

    @Test
    public void e_createUserLinkFunctionality() {
        clickOn("#createUserLink");
        verifyThat("#vbx_card", isVisible());
    }

    @Test
    public void f_togglePasswordVisibility() {
        clickOn("#passwordField");
        write("mySecretPassword");
        clickOn("#passwordImage");
        verifyThat("#visiblePasswordField", isVisible());
        clickOn("#passwordImage");
        verifyThat("#passwordField", isVisible());
    }
}
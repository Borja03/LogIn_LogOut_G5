
package view;

import Application.Application;
import Application.ApplicationSignUp;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerSignUpTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationSignUp().start(stage);
    }

   

    @Test
    public void b_serverNotAvailable() {
        clickOn("#tf_email");
        write("borja@outlook.es");
        clickOn("#pf_password");
        write("Borja@B1");
        clickOn("#imgShowPassword");
        clickOn("#tf_password_confirm");
        write("Borja@B1");
        clickOn("#tf_name");
        write("Borja");
        clickOn("#tf_street");
        write("Tartanga");
        clickOn("#tf_city");
        write("Ermua");
        clickOn("#tf_zip");
        write("48260");
        clickOn("#chb_active");
        clickOn("#btn_signup");

        FxAssert.verifyThat("Server is not available at the moment. Please try again later.", NodeMatchers.isVisible());
    }

}

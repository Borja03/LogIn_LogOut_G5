/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Adrian y Omar
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServerEmailSignUpTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationSignUp().start(stage);
    }

    @Test
    public void a_EmailAlreadyExists() {
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

        FxAssert.verifyThat("Email already exists.", NodeMatchers.isVisible());
    }

}

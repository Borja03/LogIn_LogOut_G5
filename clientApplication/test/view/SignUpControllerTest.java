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
 * @author 2dam
 */
    
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignUpControllerTest extends ApplicationTest {

    @Override
    public void start(Stage stage) throws Exception {
        new ApplicationSignUp().start(stage);
    }

    @Test
    public void test1WriteSignUp() {
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

        FxAssert.verifyThat("Your account has been created successfully!", NodeMatchers.isVisible());
    }

    /*
    @Test
    public void test1_EmptyFields() {
        clickOn("#btn_signup"); // Intentar registrar sin completar campos
        FxAssert.verifyThat("All fields are required.", NodeMatchers.isVisible());
    }
     */
    @Test
    public void test2_EmailCannotBeEmpty() {
        clickOn("#btn_signup");
        FxAssert.verifyThat("Email cannot be empty.", NodeMatchers.isVisible());
    }

    @Test
    public void test3_PasswordCannotBeEmpty() {
        clickOn("#tf_email");
        write("borja@outlook.es");
        clickOn("#btn_signup");
        FxAssert.verifyThat("Email cannot be empty.", NodeMatchers.isVisible());
    }

    @Test
    public void test4_NameCannotBeEmpty() {
        clickOn("#tf_email");
        write("borja@outlook.es");
        clickOn("#pf_password");
        write("Borja@B1");
        clickOn("#imgShowPassword");
        clickOn("#tf_password_confirm");
        write("Borja@B1");
        clickOn("#btn_signup");
        FxAssert.verifyThat("Name cannot be empty.", NodeMatchers.isVisible());
    }

    @Test
    public void test5_StreetCannotBeEmpty() {
        clickOn("#tf_email");
        write("borja@outlook.es");
        clickOn("#pf_password");
        write("Borja@B1");
        clickOn("#imgShowPassword");
        clickOn("#tf_password_confirm");
        write("Borja@B1");
        clickOn("#tf_name");
        write("Borja");
        clickOn("#btn_signup");
        FxAssert.verifyThat("Street cannot be empty.", NodeMatchers.isVisible());
    }

    @Test
    public void test6_CityCannotBeEmpty() {
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
        clickOn("#btn_signup");
        FxAssert.verifyThat("City cannot be empty.", NodeMatchers.isVisible());
    }

    @Test
    public void test7_ZipCannotBeEmpty() {
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
        clickOn("#btn_signup");
        FxAssert.verifyThat("City cannot be empty.", NodeMatchers.isVisible());
    }

    @Test
    public void test8_PasswordMismatch() {
        clickOn("#tf_email");
        write("borja@outlook.es");
        clickOn("#pf_password");
        write("Borja@B1");
        clickOn("#tf_password_confirm");
        write("Borja@B12"); // Contraseña diferente
        clickOn("#tf_name");
        write("Borja");
        clickOn("#tf_street");
        write("Tartanga");
        clickOn("#tf_city");
        write("Ermua");
        clickOn("#tf_zip");
        write("48260");
        clickOn("#btn_signup");
        FxAssert.verifyThat("Passwords do not match.", NodeMatchers.isVisible());
    }

    @Test
    public void test9_WeakPassword() {
        clickOn("#tf_email");
        write("borja@outlook.es");
        clickOn("#pf_password");
        write("123");
        clickOn("#tf_password_confirm");
        write("123");
        clickOn("#tf_name");
        write("Borja");
        clickOn("#tf_street");
        write("Tartanga");
        clickOn("#tf_city");
        write("Ermua");
        clickOn("#tf_zip");
        write("48260");
        clickOn("#btn_signup");
        FxAssert.verifyThat("Password must be at least 6 characters, with lowercase, uppercase, numbers, and special characters.", NodeMatchers.isVisible());
    }

    /*
    @Test
    public void test10_EmailAlreadyExists() {
        // Primero, registrar un email
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

        // Intentar registrar el mismo email nuevamente
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
     */
    @Test
    public void test11_InvalidEmailFormat() {
        clickOn("#tf_email");
        write("siiiiHombre");
        clickOn("#pf_password");
        write("Borja@B1");
        clickOn("#imgShowPassword");
        write("Borja@B1");
        clickOn("#tf_password_confirm");
        write("Borja@B1");
        clickOn("#tf_name");
        write("Borja");
        clickOn("#tf_street");
        write("Tartanga");
        clickOn("#tf_city");
        write("Ermua");
        clickOn("#tf_zip");
        write("1234");
        clickOn("#chb_active");
        clickOn("#btn_signup");

        FxAssert.verifyThat("Email must be in a valid format (e.g., example@domain.com).", NodeMatchers.isVisible());
    }

    @Test
    public void test12_InvalidCityFormat() {
        clickOn("#tf_email");
        write("borja@outlook.es");
        clickOn("#pf_password");
        write("Borja@B1");
        clickOn("#imgShowPassword");
        write("Borja@B1");
        clickOn("#tf_password_confirm");
        write("Borja@B1");
        clickOn("#tf_name");
        write("Borja");
        clickOn("#tf_street");
        write("Tartanga");
        clickOn("#tf_city");
        write("Ermua33");
        clickOn("#tf_zip");
        write("1234");
        clickOn("#chb_active");
        clickOn("#btn_signup");

        FxAssert.verifyThat("City must only contain letters.", NodeMatchers.isVisible());
    }

    @Test
    public void test13_InvalidCityFormatNumber() {
        clickOn("#tf_email");
        write("borja@outlook.es");
        clickOn("#pf_password");
        write("Borja@B1");
        clickOn("#imgShowPassword");
        write("Borja@B1");
        clickOn("#tf_password_confirm");
        write("Borja@B1");
        clickOn("#tf_name");
        write("Borja");
        clickOn("#tf_street");
        write("Tartanga");
        clickOn("#tf_city");
        write("Ermua33");
        clickOn("#tf_zip");
        write("123456");
        clickOn("#chb_active");
        clickOn("#btn_signup");

        FxAssert.verifyThat("City must only contain letters.", NodeMatchers.isVisible());
    }

}

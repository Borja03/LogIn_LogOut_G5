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

/**
 *
 * @author 2dam
 */

@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class SignUpControllerTest extends ApplicationTest {
    
    @Override public void start(Stage stage) throws Exception{
      new  ApplicationSignUp().start(stage);    
    }
    
    @Test
    public void testWriteSignUp(){
        clickOn("#tf_email");
        write("AlderSubnormal");
        clickOn("#pf_password");
        write("PutaBarça");
        clickOn("#imgShowPassword");
    }
    
    public SignUpControllerTest() {
    }

    @Test
    public void testSomeMethod() {
    }
    
}

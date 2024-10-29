/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import Application.Application;
import javafx.stage.Stage;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.framework.junit.ApplicationTest;
import static org.testfx.matcher.base.NodeMatchers.isEnabled;



/**
 *
 * @author 2dam
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class logInControllerTest extends ApplicationTest {
    
    @Override 
    public void start (Stage stage) throws Exception {
        new Application().start(stage);
    }
    
    @Test 
    public void A_hacerLogIn (){
        clickOn ("#emailTextField");
        write ("borjaahedo@gmail.com");
        clickOn ("#passwordField");
        write ("Borja44_");
        clickOn ("#logInButton");
        verifyThat ("#mainPane", isEnabled());
    }
    
      @Test 
    public void B_goToCreateUserView (){
        clickOn ("create user");
       
    }

}

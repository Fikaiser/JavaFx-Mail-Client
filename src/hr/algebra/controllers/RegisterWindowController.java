/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.MailClientFx;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.threads.NumberGeneratorThread;
import hr.algebra.utils.DialogUtils;
import hr.algebra.utils.RegexUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author RnaBo
 */
public class RegisterWindowController implements Initializable {

    List<TextField> validationFields;

   

    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfNickname;
    @FXML
    private Button btnRegister;
    @FXML
    private Button btnLogin;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private PasswordField pfConfirmPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initValidation();
    }

    @FXML
    private void registerAccount(ActionEvent event) {

        if (formValid()) {

            try {
                if (!RepositoryFactory.getRepository().checkAccountExists(tfEmail.getText().trim())) {
                    RepositoryFactory.getRepository().registerLogin(tfEmail.getText().trim(), pfPassword.getText().trim(), tfNickname.getText().trim());
                    DialogUtils.showInfoMessage("Register", "Registration successful", "Account registered");
                    redirectToLogin();
                } else {
                    DialogUtils.showErrorMessage("Register", "SQL Error", "This adresse is already taken");
                }

            } catch (Exception ex) {

                DialogUtils.showErrorMessage("Register", "SQL Error", "Error registering account");
            }

        } else {

            DialogUtils.showErrorMessage("Register", "Validation error", "Please fill all fields and match passwords");
        }

    }

    private void initValidation() {

        validationFields = new ArrayList<>();
        validationFields.add(tfEmail);
        validationFields.add(tfNickname);
        validationFields.add(pfPassword);
        validationFields.add(pfConfirmPassword);

    }

    private boolean formValid() {

        boolean valid = true;

        for (TextField tf : validationFields) {
            valid = tf.getText().trim().isEmpty()? false : valid ;

            if (tf.getId().equals(tfEmail.getId())) {
                
                valid = RegexUtils.validateEmail(tfEmail.getText().trim()) ? valid : false;
            }

        }

       
        valid = pfPassword.getText().trim().equals(pfConfirmPassword.getText().trim()) ? valid : false;

        return valid;
    }

    private void redirectToLogin() throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/hr/algebra/views/LoginWindow.fxml"));

        Scene scene = new Scene(root);

        MailClientFx.getMainStage().setTitle("Login");
        MailClientFx.getMainStage().setScene(scene);

    }

    @FXML
    private void btnLoginClick(ActionEvent event) throws IOException {

        redirectToLogin();
    }

    @FXML
    private void randomizeNickname(ActionEvent event) {
        
        ExecutorService service = Executors.newCachedThreadPool();
        StringBuffer sb = new StringBuffer("User");
        for (int i = 0; i < 10; i++) {
            service.execute(new NumberGeneratorThread(i, sb));
        }
        
        tfNickname.setText(sb.toString());
        
    }

}

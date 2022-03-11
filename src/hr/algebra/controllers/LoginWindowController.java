/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.MailClientFx;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Account;
import hr.algebra.utils.DialogUtils;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class LoginWindowController implements Initializable {

    @FXML
    private TextField tfEmail;

    @FXML
    private Button btnLogin;
    @FXML
    private Button btnRegister;
    @FXML
    private PasswordField pfPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    // TODO
    }

    @FXML
    private void redirectToRegister(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/hr/algebra/views/RegisterWindow.fxml"));

        Scene scene = new Scene(root);

        MailClientFx.getMainStage().setTitle("Register");
        MailClientFx.getMainStage().setScene(scene);

    }

    @FXML
    private void tryLogIn(ActionEvent event) {

        try {
            Optional<Account> login = RepositoryFactory.getRepository().getLogin(tfEmail.getText().trim(), pfPassword.getText().trim());

            if (login.isPresent()) {
                MailClientFx.setActiveAccount(login.get());
                redirectToMainWindow();
            } else {

                DialogUtils.showErrorMessage("Log in", "Error", "No such credentials");

            }
        } catch (Exception ex) {
            Logger.getLogger(LoginWindowController.class.getName()).log(Level.SEVERE, null, ex);
            DialogUtils.showErrorMessage("Log in", "SQL Error", "Error while logging in");
        }
    }

    private void redirectToMainWindow() throws IOException {

        Parent root = FXMLLoader.load(
                getClass().getResource("/hr/algebra/views/InboxWindow.fxml"));

        Scene scene = new Scene(root);

        MailClientFx.getMainStage().setTitle("Inbox");
        MailClientFx.getMainStage().setScene(scene);

    }

}

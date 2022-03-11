/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.MailClientFx;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Email;
import hr.algebra.threads.DataReaderThread;
import hr.algebra.utils.DialogUtils;
import hr.algebra.utils.ReflectionUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author RnaBo
 */
public class InboxWindowController implements Initializable {

    //Very important git comment
    //Very very important git comment
    

    @FXML
    private TextField tfFrom;
    @FXML
    private TextField tfTo;
    @FXML
    private TextField tfSubject;
    @FXML
    private TextArea taContent;
    @FXML
    private ListView<Email> lvEmails;
    @FXML
    private MenuItem btnInbox;
    @FXML
    private MenuItem btnOutbox;
    @FXML
    private MenuItem btnComposeNew;
    @FXML
    private MenuItem btnExit;
    @FXML
    private MenuItem btnMailingListOverview;
    @FXML
    private MenuItem btnDocs;
    @FXML
    private MenuItem btnOpenChatWindow;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setInboxActive(new ActionEvent());
        
    }

    @FXML
    private void setInboxActive(ActionEvent event) {
        MailClientFx.getMainStage().setTitle("Inbox");
        clearForm();

        List<Email> emails = new ArrayList<>();

        CompletableFuture<Void> future = CompletableFuture.runAsync(new DataReaderThread(emails, Boolean.TRUE, MailClientFx.getActiveAccount().getEmail()));

        while (!future.isDone()) {
            Logger.getLogger(InboxWindowController.class.getName()).info("Waiting!");
        }

        fillUI(emails);

    }

    private void fillUI(List<Email> emails) {

        ObservableList<Email> e = FXCollections.observableArrayList(emails);

        lvEmails.setItems(e);

        lvEmails.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Email> observable, Email oldValue, Email newValue) -> {
            if (newValue != null) {
                tfFrom.setText(newValue.getSender());
                tfTo.setText(MailClientFx.getActiveAccount().getEmail());
                tfSubject.setText(newValue.getTitle());
                taContent.setText(newValue.getContent());
            }
        });

    }

    @FXML
    private void setOutboxActive(ActionEvent event) {
        MailClientFx.getMainStage().setTitle("Outbox");
       clearForm();

        List<Email> emails = new ArrayList<>();

        CompletableFuture<Void> future = CompletableFuture.runAsync(new DataReaderThread(emails, Boolean.FALSE, MailClientFx.getActiveAccount().getEmail()));

        while (!future.isDone()) {
            Logger.getLogger(InboxWindowController.class.getName()).info("Waiting!");
        }

        fillUI(emails);

    }

    @FXML
    private void openNewEmailWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/hr/algebra/views/EmailEditorWindow.fxml"));
            /* 
         * if "fx:controller" is not set in fxml
         * fxmlLoader.setController(NewWindowController);
             */
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Composing mail");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Failed to create new Window.", e);
        }
    }

    private void clearForm() {
        tfFrom.setText("");
        tfTo.setText("");
        tfSubject.setText("");
        taContent.setText("");
        lvEmails.getSelectionModel().clearSelection();
    }

    @FXML
    private void openMailingListWindow(ActionEvent event) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/hr/algebra/views/MailingListWindow.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Mailing Lists");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(InboxWindowController.class.getName()).log(Level.SEVERE, null, ex);
            DialogUtils.showErrorMessage("Mailing Lists", "Error", "Error opening mailing lists");
        }

    }

    @FXML
    private void ExitApp(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void WriteDocumentation(ActionEvent event) {

        ReflectionUtils.writeProjectDocumentation();

    }

    @FXML
    private void openChatWindow(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/hr/algebra/views/ChatWindow.fxml"));

            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Chatroom");

            stage.setScene(scene);

            stage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    ((ChatWindowController) fxmlLoader.getController()).stopChatThread();
                    System.out.println("Test");
                }
            });

            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(InboxWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.dal.EmailListHolderSingleton;
import hr.algebra.dal.MailingListHolderSingleton;
import hr.algebra.model.MailingList;
import hr.algebra.utils.DialogUtils;
import hr.algebra.utils.RegexUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RnaBo
 */
public class MailingListEditorWindowController implements Initializable {

    private Boolean IsEditing;
    
    @FXML
    private TextField tfName;
    @FXML
    private ListView<String> lvEmails;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;
    @FXML
    private TextField tfEmail;
    @FXML
    private Button btnSave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        checkIfEditingList();
    }
    
    

    @FXML
    private void AddEmail(ActionEvent event) {
        if (RegexUtils.validateEmail(tfEmail.getText().trim())) {
            lvEmails.getItems().add(tfEmail.getText().trim());
        } else {
            DialogUtils.showErrorMessage("List editor", "Validation", "Please enter a valid email");
        }
    }

    @FXML
    private void RemoveEmail(ActionEvent event) {
        if (lvEmails.getSelectionModel().getSelectedItem() != null) {
            lvEmails.getItems().remove(lvEmails.getSelectionModel().getSelectedIndex());
        }

    }

    @FXML
    private void SaveList(ActionEvent event) {
        if (!tfName.getText().trim().isEmpty()) {
           
            if (IsEditing) {
                MailingList list = EmailListHolderSingleton.getInstance().getList();
                list.setEmails(lvEmails.getItems());
                list.setName(tfName.getText().trim());
            }
            else{
             MailingListHolderSingleton.getInstance().getList().add(new MailingList(tfName.getText().trim(), lvEmails.getItems()));
            
            }
            
            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
           
        } else {
            DialogUtils.showErrorMessage("List editor", "Validation", "Please enter a list name");
        }
    }

    private void checkIfEditingList() {
        
        IsEditing = EmailListHolderSingleton.getInstance().getList() != null;
        if (IsEditing) {
            MailingList list = EmailListHolderSingleton.getInstance().getList();
            tfName.setText( list.getName());
            lvEmails.setItems(FXCollections.observableArrayList(list.getEmails()));
        }
        
    }

   

}

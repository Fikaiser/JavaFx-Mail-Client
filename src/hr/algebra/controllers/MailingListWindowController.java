/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.dal.EmailListHolderSingleton;
import hr.algebra.dal.MailingListHolderSingleton;
import hr.algebra.model.MailingList;
import hr.algebra.utils.DOMUtils;
import hr.algebra.utils.DialogUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RnaBo
 */
public class MailingListWindowController implements Initializable {

    
    
    @FXML
    private ListView<MailingList> lvLists;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSave;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
        lvLists.setItems(MailingListHolderSingleton.getInstance().getList());
    }    

    @FXML
    private void OpenListEditorAdd(ActionEvent event) {
        try {
            EmailListHolderSingleton.getInstance().setList(null);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/hr/algebra/views/MailingListEditorWindow.fxml"));
           
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
           
            stage.setTitle("Composing list");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MailingListWindowController.class.getName()).log(Level.SEVERE, null, ex);
             DialogUtils.showErrorMessage("Mailing Lists", "Error", "Error opening mailing list editor");
        }
    }

    @FXML
    private void OpenListEditorEdit(ActionEvent event) {
        try {
            EmailListHolderSingleton.getInstance().setList(lvLists.getSelectionModel().getSelectedItem());
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/hr/algebra/views/MailingListEditorWindow.fxml"));
           
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
           
            
            stage.setTitle("Editing list");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MailingListWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void DeleteList(ActionEvent event) {
        if (lvLists.getSelectionModel().getSelectedItem() != null) {
            lvLists.getItems().remove(lvLists.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    private void SerializeMailingLists(ActionEvent event) {
        
        
        try {
            MailingListHolderSingleton.getInstance().serializeList();
            DialogUtils.showInfoMessage("Mailing Lists", "Success", "Mailing lists saved successfully!");
        } catch (IOException ex) {
            Logger.getLogger(MailingListWindowController.class.getName()).log(Level.SEVERE, null, ex);
            DialogUtils.showErrorMessage("Mailing Lists", "Error", "Error serializing lists");
        }
        
        
    }

    @FXML
    private void exportMailingLists(ActionEvent event) {
        DOMUtils.saveMailingLists(MailingListHolderSingleton.getInstance().getList());
    }

    @FXML
    private void importMailingLists(ActionEvent event) {
        MailingListHolderSingleton.getInstance().setList(DOMUtils.loadMailingLists());
        lvLists.setItems(MailingListHolderSingleton.getInstance().getList());
    }
    
}

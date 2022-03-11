/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.MailClientFx;
import hr.algebra.dal.MailingListHolderSingleton;
import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Email;
import hr.algebra.model.MailingList;
import hr.algebra.utils.DialogUtils;
import hr.algebra.utils.JndiUtils;
import hr.algebra.utils.RegexUtils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author RnaBo
 */
public class EmailEditorWindowController implements Initializable {

    List<TextInputControl> validationFields;
    private static final String FILE_LOCATION = "d:\\";
    private static String SERVER;
    private static int PORT;

    @FXML
    private Button btnSend;
    @FXML
    private TextField tfFrom;
    @FXML
    private TextField tfTo;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextArea taContent;
    @FXML
    private ComboBox<MailingList> cbMailingLists;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initValidation();
        initComboBox();
        initListener();
        initServerInfo();
        tfFrom.setText(MailClientFx.getActiveAccount().getEmail());
        tfFrom.setEditable(false);
    }

    @FXML
    private void sendEmail(ActionEvent event) {

        if (formValid()) {

            
                List<String> addresses = Arrays.asList(tfTo.getText().split(";"));
                
                addresses.forEach(addresse -> {
                
                    if (RegexUtils.validateEmail(addresse.trim())) {
                        try {
                            sendEmailRequest(new Email(tfTitle.getText().trim(), tfFrom.getText().trim(), taContent.getText().trim(), addresse.trim()));
                            //RepositoryFactory.getRepository().sendEmail(tfFrom.getText().trim(), addresse.trim(), tfTitle.getText().trim(), taContent.getText().trim());
                        } catch (Exception ex) {
                            Logger.getLogger(EmailEditorWindowController.class.getName()).log(Level.SEVERE, null, ex);
                            DialogUtils.showErrorMessage("Email editor", "SQL Error", "Unable to send email");
                        }
                    }
                
                
                });
                
                DialogUtils.showInfoMessage("Email editor", "Success", "Email sent");
                Stage stage = (Stage)tfFrom.getScene().getWindow();
                stage.close();
             
        }
        else{
        
        DialogUtils.showErrorMessage("Email editor", "Validation error", "Please fill all fields with valid data");
        }

    }

    private boolean formValid() {

        boolean ok = true;

        for (TextInputControl vf : validationFields) {

            ok = vf.getText().isEmpty() ? false : ok;

        }
        
        //ok = RegexUtils.validateEmail(tfTo.getText().trim()) ? ok : false;

        return ok;
    }

    private void initValidation() {

        validationFields = new ArrayList<>();
        validationFields.add(tfTo);
        validationFields.add(tfTitle);
        validationFields.add(taContent);

    }

    private void initListener() {
       
        cbMailingLists.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                StringBuilder sb = new StringBuilder();
                newValue.getEmails().forEach(email -> {sb.append(email).append(";");});
                tfTo.setText(sb.toString());
            }
        }) );
                
        
    }

    private void initComboBox() {
        
        cbMailingLists.setItems(MailingListHolderSingleton.getInstance().getList());
    }

    private void sendEmailRequest(Email email) {
        
         try (Socket clientSocket = new Socket(SERVER, PORT)) {
            System.err.println("Client is connecting to: " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
 
            
        ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
        oos.writeObject(email);
       
            
        } catch (IOException ex) {
            Logger.getLogger(EmailEditorWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void initServerInfo() {
        
         Properties appProps = JndiUtils.readConfigFile(FILE_LOCATION);
         SERVER = appProps.getProperty("server");
         PORT = Integer.parseInt(appProps.getProperty("tcp.port"));
        
    }


}

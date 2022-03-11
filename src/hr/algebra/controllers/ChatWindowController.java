/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controllers;

import hr.algebra.MailClientFx;
import hr.algebra.threads.ChatRefreshThread;
import hr.contract.model.ChatMessage;
import hr.contract.service.MessengerService;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author RnaBo
 */
public class ChatWindowController implements Initializable {

    @FXML
    private TextArea taChatHistory;
    @FXML
    private TextField tfMessage;
    @FXML
    private Button btnSend;
    
    private Registry registry;
    private MessengerService server;
    ChatRefreshThread thread;
    Boolean isRunning = true;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        try {
            registry = LocateRegistry.getRegistry();
            
            System.out.println("Dohvatio sam RMI registry!");

             server = (MessengerService) registry
                    .lookup("MessengerService");
            
            System.out.println("Dohvatio sam servis!");

            startChatThread();
            server.getChatHistory().forEach(System.out::println);
            
        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(ChatWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void sendMessage(ActionEvent event) {
        if (!tfMessage.getText().isEmpty()) {
            try {
                server.sendMessage(new ChatMessage(MailClientFx.getActiveAccount().getNickname(), tfMessage.getText().trim(), LocalDateTime.now()));
                tfMessage.setText("");
            } catch (RemoteException ex) {
                Logger.getLogger(ChatWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
    private void startChatThread() {

        thread = new ChatRefreshThread(server,taChatHistory,isRunning);
        
        new Thread(thread).start();
        
    }
    
    public void stopChatThread(){
    
        isRunning = false;
    
    }
    
}

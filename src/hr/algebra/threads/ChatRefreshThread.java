/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threads;

import hr.contract.service.MessengerService;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

/**
 *
 * @author RnaBo
 */
public class ChatRefreshThread implements Runnable{
    
    private final MessengerService server;
    private final TextArea chatArea;
    private Boolean isRunning;

    public ChatRefreshThread(MessengerService server, TextArea textarea, Boolean isRunning) {
        this.server = server;
        this.chatArea = textarea;
        this.isRunning = isRunning;
    }

    @Override
    public void run() {

        while (isRunning) {            
             Platform.runLater(() ->{
                 try {
                     StringBuffer sb = new StringBuffer();
                     server.getChatHistory().forEach(message -> {
                         
                         sb.append(message.toString()).append(System.lineSeparator());
                         
                     });
                     chatArea.setText(sb.toString());
                 } catch (RemoteException ex) {
                     Logger.getLogger(ChatRefreshThread.class.getName()).log(Level.SEVERE, null, ex);
                 }
             });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatRefreshThread.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

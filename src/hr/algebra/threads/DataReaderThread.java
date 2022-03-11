/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threads;

import hr.algebra.dal.RepositoryFactory;
import hr.algebra.model.Email;
import hr.algebra.utils.DialogUtils;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RnaBo
 */
public class DataReaderThread implements Runnable{
    
    private List<Email> emails;
    private final Boolean readInbox;
    private final String emailAddress;
   
    

   

    public DataReaderThread(List<Email> emails, Boolean readInbox, String emailAddress) {
        this.emails = emails;
        this.readInbox = readInbox;
        this.emailAddress = emailAddress;
        
    }
    
    
    

    @Override
    public void run() {
        
        
        
        try {
            //emails = readInbox ? RepositoryFactory.getRepository().getInbox(emailAddress) : RepositoryFactory.getRepository().getOutbox(emailAddress);
            if (readInbox) {
                RepositoryFactory.getRepository().getInbox(emailAddress).forEach(e -> emails.add(e));
            }
            else{
                RepositoryFactory.getRepository().getOutbox(emailAddress).forEach(e -> emails.add(e));
            }
           
        } catch (Exception ex) {
            Logger.getLogger(DataReaderThread.class.getName()).log(Level.SEVERE, null, ex);
            DialogUtils.showErrorMessage("Mail client", "Error", "Error fetching email list");
        }
        
        
    }
    
}

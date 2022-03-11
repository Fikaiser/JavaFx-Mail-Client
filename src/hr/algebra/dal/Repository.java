/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.Account;
import hr.algebra.model.Email;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author RnaBo
 */
public interface Repository {

    public Optional<Account> getLogin(String email, String pass) throws Exception;

    public void registerLogin(String email, String pass, String nickname) throws Exception;

    public boolean checkAccountExists(String email) throws Exception;

    public List<Email> getInbox(String emailAdresse) throws Exception;

    public List<Email> getOutbox(String emailAdresse) throws Exception;

    public String getRecipients(int idEmail) throws Exception;

    public void sendEmail(String sender, String recipient, String title, String content) throws Exception;
    
    

}

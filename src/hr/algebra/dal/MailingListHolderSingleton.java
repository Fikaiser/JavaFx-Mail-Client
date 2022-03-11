/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.MailingList;
import hr.algebra.utils.SerializationUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author RnaBo
 */
public class MailingListHolderSingleton {
    
    
    
    private ObservableList<MailingList> list;
    private final String FILENAME = "mailinglists.ser";
    private final static MailingListHolderSingleton INSTANCE = new MailingListHolderSingleton();

    private MailingListHolderSingleton() {
        try {
            
           list = FXCollections.observableArrayList(readSerializedList());
           
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(MailingListHolderSingleton.class.getName()).log(Level.SEVERE, null, ex);
             list = FXCollections.observableArrayList();
        }
    }

    public static MailingListHolderSingleton getInstance() {
        return INSTANCE;
    }

    public ObservableList<MailingList> getList() {
        return list;
    }

    public void setList(ObservableList<MailingList> list) {
        this.list = list;
    }
  
    public void serializeList() throws IOException{
    
        List<MailingList> mailingLists = new ArrayList<>(list);
    
        SerializationUtils.write(mailingLists, FILENAME);
    }
    
    private List<MailingList> readSerializedList() throws IOException, ClassNotFoundException{
    
        return (List<MailingList>) SerializationUtils.read(FILENAME);
    
    }
    
  
    
}

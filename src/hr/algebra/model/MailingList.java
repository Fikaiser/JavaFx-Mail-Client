/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RnaBo
 */
public class MailingList implements Externalizable {
    
    private String name;
    private List<String> emails;

    public MailingList() {
    }

    
    
    public MailingList(String name, List<String> emails) {
        this.name = name;
        this.emails = emails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
        out.writeObject(new ArrayList<>(emails));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        
         name = in.readUTF();
         emails = (ArrayList<String>) in.readObject();
        
    }
    
    
    
}

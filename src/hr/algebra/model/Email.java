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

/**
 *
 * @author RnaBo
 */
public class Email implements Externalizable{

    private  int idEmail;
    private  String title;
    private  String sender;
    private  String content;
    private  String recipient;

    public Email() {
    }

    
    
    public Email(int idEmail, String title, String sender, String content) {
        this.idEmail = idEmail;
        this.title = title;
        this.sender = sender;
        this.content = content;
    }
    
    public Email( String title, String sender, String content, String recipient) {
        this.title = title;
        this.sender = sender;
        this.content = content;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return sender + " - " + title;
    }

    public int getIdEmail() {
        return idEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getRecipient() {
        return recipient;
    }
    

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(title);
        out.writeUTF(sender);
        out.writeUTF(recipient);
        out.writeUTF(content);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        title = in.readUTF();
        sender = in.readUTF();
        recipient = in.readUTF();
        content = in.readUTF();
        
    }
    
    
    
}



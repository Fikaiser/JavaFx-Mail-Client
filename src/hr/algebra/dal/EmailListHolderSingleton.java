/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dal;

import hr.algebra.model.MailingList;

/**
 *
 * @author RnaBo
 */
public class EmailListHolderSingleton {
    
     private MailingList list;
  private final static EmailListHolderSingleton INSTANCE = new EmailListHolderSingleton();

    private EmailListHolderSingleton() {
    }

    public MailingList getList() {
        return list;
    }

    public void setList(MailingList list) {
        this.list = list;
    }

    public static EmailListHolderSingleton getInstance() {
        return INSTANCE;
    }
  
    
    
}

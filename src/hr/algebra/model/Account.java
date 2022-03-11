/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

/**
 *
 * @author RnaBo
 */
public class Account {
    
    private int accountId;
    private String nickname;
    private String email;

    public Account(int accountId, String nickname, String email) {
        this.accountId = accountId;
        this.nickname = nickname;
        this.email = email;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
    
    
    
}

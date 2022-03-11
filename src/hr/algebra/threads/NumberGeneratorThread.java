/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threads;

/**
 *
 * @author RnaBo
 */
public class NumberGeneratorThread implements Runnable{
    
    private final int number;
    private final StringBuffer stringBuffer;

    public NumberGeneratorThread(int number, StringBuffer stringBuffer) {
        this.number = number;
        this.stringBuffer = stringBuffer;
    }

    @Override
    public void run() {
        
        stringBuffer.append(number);
        
    }
    
    
    
}

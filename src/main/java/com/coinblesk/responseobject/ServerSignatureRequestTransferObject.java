package com.coinblesk.responseobject;

import java.util.ArrayList;
import java.util.List;

public class ServerSignatureRequestTransferObject extends TransferObject {
    
    
    private String partialTx; // Base64 encoded partial transaction
    private List<Byte> accountNumbers; // the account numbers of the keys used
    private List<Integer> childNumbers; // the child numbers of the keys used
    
    
    public String getPartialTx() {
        return partialTx;
    }

    public void setPartialTx(String partialTx) {
        this.partialTx = partialTx;
    }
    
    public List<Byte> getAccountNumbers() {
        return accountNumbers;
    }

    public void setAccountNumbers(List<Byte> accountNumbers) {
        this.accountNumbers = accountNumbers;
    }
    
    public void addAccountNumber(byte accountNumber) {
        if(accountNumbers == null) {
            accountNumbers = new ArrayList<>();
        }
        this.accountNumbers.add(accountNumber);
    }

    public List<Integer> getChildNumbers() {
        return childNumbers;
    }

    public void setChildNumbers(List<Integer> childNumbers) {
        this.childNumbers = childNumbers;
    }
    
    public void addChildNumber(int childNumber) {
        if(childNumbers == null) {
            childNumbers = new ArrayList<>();
        }
        this.childNumbers.add(childNumber);
    }

    /**
     * Clears the data stored in this transfer object
     */
    public void clear() {
        this.partialTx = null;
        this.childNumbers = null;
    }

}

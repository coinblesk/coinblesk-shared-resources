package ch.uzh.csg.coinblesk.responseobject;

public class SignedTxTransferObject extends TransferObject {
    
    private String signedTx;
    
    public SignedTxTransferObject() {}
    
    public SignedTxTransferObject(String signedTx) {
        this.signedTx = signedTx;
    }

    public String getSignedTx() {
        return signedTx;
    }

    public void setSignedTx(String signedTx) {
        this.signedTx = signedTx;
    }

}

package ch.uzh.csg.coinblesk.responseobject;


public class RefundTxTransferObject extends TransferObject {
    
    private String refundTx; // Base64 encoded refund transaction
    
    public RefundTxTransferObject() {
    }
    
    public RefundTxTransferObject(String refundTx) {
        this.refundTx = refundTx;
    }

    public String getRefundTx() {
        return refundTx;
    }

    public void setRefundTx(String partialTx) {
        this.refundTx = partialTx;
    }

}

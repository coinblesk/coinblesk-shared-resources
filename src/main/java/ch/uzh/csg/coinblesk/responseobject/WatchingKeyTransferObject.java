package ch.uzh.csg.coinblesk.responseobject;

import ch.uzh.csg.coinblesk.bitcoin.BitcoinNet;

public class WatchingKeyTransferObject extends TransferObject {
    
    private String watchingKey;
    private BitcoinNet bitcoinNet;
    
    public WatchingKeyTransferObject(String watchingKey, BitcoinNet bitcoinNet) {
        this.bitcoinNet = bitcoinNet;
        this.watchingKey = watchingKey;
    }
    
    public WatchingKeyTransferObject() {
    }
    
    public BitcoinNet getBitcoinNet() {
        return bitcoinNet;
    }

    public void setBitcoinNet(BitcoinNet bitcoinNet) {
        this.bitcoinNet = bitcoinNet;
    }

    public String getWatchingKey() {
        return watchingKey;
    }

    public void setWatchingKey(String watchingKey) {
        this.watchingKey = watchingKey;
    }

}

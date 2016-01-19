package com.coinblesk.responseobject;

import com.coinblesk.bitcoin.BitcoinNet;

public class SetupRequestObject extends TransferObject {
	
	private String serverWatchingKey;
	private String bitcoinNet;

	public String getServerWatchingKey() {
        return serverWatchingKey;
    }

    public void setServerWatchingKey(String serverWatchingKey) {
        this.serverWatchingKey = serverWatchingKey;
    }

    public BitcoinNet getBitcoinNet() {
        return BitcoinNet.of(bitcoinNet);
    }

    public void setBitcoinNet(BitcoinNet bitcoinNet) {
        this.bitcoinNet = bitcoinNet.toString();
    }

}

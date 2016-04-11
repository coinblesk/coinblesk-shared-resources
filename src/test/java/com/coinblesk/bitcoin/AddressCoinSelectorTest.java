package com.coinblesk.bitcoin;

import static org.junit.Assert.*;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.testing.FakeTxBuilder;
import org.bitcoinj.wallet.CoinSelection;
import org.junit.Test;

public class AddressCoinSelectorTest {
	private static final NetworkParameters params = MainNetParams.get();
	
	@Test
	public void testCoinSelectorByAddress() {
		Address a1 = new ECKey().toAddress(params);
		Address a2 = new ECKey().toAddress(params);
		
		Transaction tx;
		CoinSelection selection;
		
		tx = FakeTxBuilder.createFakeTx(params, Coin.COIN.div(2), a1);
		selection = new AddressCoinSelector(a1)
											.select(Coin.ZERO, tx.getOutputs());
		assertTrue( selection.valueGathered.equals(Coin.COIN.div(2)) );
		assertTrue( selection.gathered.size() == 1 );
		
		tx = FakeTxBuilder.createFakeTx(params, Coin.COIN.div(2), a2);
		selection = new AddressCoinSelector(a1)
											.select(Coin.ZERO, tx.getOutputs());
		assertTrue( selection.valueGathered.equals(Coin.ZERO) );
		assertTrue( selection.gathered.size() == 0 );
	}
	
}

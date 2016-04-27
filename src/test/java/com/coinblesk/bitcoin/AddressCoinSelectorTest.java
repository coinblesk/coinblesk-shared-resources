package com.coinblesk.bitcoin;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.CoinSelection;
import org.junit.Test;

import com.coinblesk.util.FakeTxBuilder;


public class AddressCoinSelectorTest {
	private static final NetworkParameters params = MainNetParams.get();
	
	@Test
	public void testCoinSelectorByAddress() {
		Address a1 = new ECKey().toAddress(params);
		Address a2 = new ECKey().toAddress(params);
		
		Transaction tx;
		CoinSelection selection;
		
		tx = FakeTxBuilder.createFakeTx(params, Coin.COIN.div(2), a1);
		selection = new AddressCoinSelector(a1, params)
											.select(Coin.ZERO, tx.getOutputs());
		assertTrue( selection.valueGathered.equals(Coin.COIN.div(2)) );
		assertTrue( selection.gathered.size() == 1 );
		
		tx = FakeTxBuilder.createFakeTx(params, Coin.COIN.div(2), a2);
		selection = new AddressCoinSelector(a1, params)
											.select(Coin.ZERO, tx.getOutputs());
		assertTrue( selection.valueGathered.equals(Coin.ZERO) );
		assertTrue( selection.gathered.size() == 0 );
	}
	
	@Test
	public void testCoinSelectorMultiple() {
		Address a1 = new ECKey().toAddress(params);
		Address a2 = new ECKey().toAddress(params);
		Coin to_a1 = Coin.COIN.div(2);
		Coin to_a2 = Coin.COIN.div(4);
		
		Transaction tx_a1 = FakeTxBuilder.createFakeTx(params, to_a1, a1);
		Transaction tx_a2 = FakeTxBuilder.createFakeTx(params, to_a2, a2);
		List<TransactionOutput> allOutputs = new ArrayList<>();
		allOutputs.addAll(tx_a1.getOutputs());
		allOutputs.addAll(tx_a2.getOutputs());
		
		AddressCoinSelector selector = new AddressCoinSelector(null, params);
		selector.select(Coin.ZERO, allOutputs);
		
		Map<Address, Coin> balances = selector.getAddressBalances();
		assertTrue(balances.containsKey(a1));
		assertEquals(balances.get(a1), to_a1);
		assertTrue(balances.containsKey(a2));
		assertEquals(balances.get(a2), to_a2);
	}
	
}

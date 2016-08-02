package com.coinblesk.bitcoin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.wallet.CoinSelection;
import org.bitcoinj.wallet.CoinSelector;

/**
 * This coin selector selects transaction outputs based on the target address. 
 * It can be used to calculate the wallet balance for a given address.
 * 
 * @author Andreas Albrecht
 *
 */
public class AddressCoinSelector implements CoinSelector {
	
	private final Address address;
	private final Map<Address, Coin> balanceByAddress;
	private final NetworkParameters params;
	
	public AddressCoinSelector(Address address, NetworkParameters params) {
		this.address = address;
		this.params = params;
		this.balanceByAddress = new HashMap<>();
	}
		
	@Override
	public CoinSelection select(Coin target, List<TransactionOutput> candidates) {
		Coin value = Coin.ZERO;
		
		Set<TransactionOutput> selectedOutputs = new HashSet<TransactionOutput>();
		for (TransactionOutput output : candidates) {
			if (output.isAvailableForSpending()) {
				Address paidTo = output.getScriptPubKey().getToAddress(params);
				Coin currentBalance;
				if (balanceByAddress.containsKey(paidTo)) {
					currentBalance = balanceByAddress.get(paidTo);
				} else {
					currentBalance = Coin.ZERO;					
				}
				Coin newBalance = currentBalance.add(output.getValue());
				balanceByAddress.put(paidTo, newBalance);
				
				if (address == null || paidTo.equals(address)) {
					selectedOutputs.add(output);
					value = value.add(output.getValue());
				}
				
			}
		}
		
		CoinSelection selection = new CoinSelection(value, selectedOutputs);
		return selection;
	}
	
	public Map<Address, Coin> getAddressBalances() {
		return balanceByAddress;
	}
}

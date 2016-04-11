package com.coinblesk.bitcoin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.script.ScriptBuilder;
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
	
	private final byte[] outputScript;
	
	public AddressCoinSelector(Address address) {
		outputScript = ScriptBuilder
				.createOutputScript(address)
				.getProgram();
	}
	
	public AddressCoinSelector(byte[] outputScript) {
		this.outputScript = outputScript;
	}
	
	@Override
	public CoinSelection select(Coin target, List<TransactionOutput> candidates) {
		if (outputScript == null || outputScript.length <= 0) {
			throw new IllegalStateException("Cannot select coins if no script provided.");
		}
		
		Coin value = Coin.ZERO;
		Set<TransactionOutput> selectedOutputs = new HashSet<TransactionOutput>();
		for (TransactionOutput output : candidates) {
			if (output.isAvailableForSpending() && 
						Arrays.equals(outputScript, output.getScriptBytes())) {
				
				selectedOutputs.add(output);
				value = value.add(output.getValue());
			}
		}
		
		CoinSelection selection = new CoinSelection(value, selectedOutputs);
		return selection;
	}
}

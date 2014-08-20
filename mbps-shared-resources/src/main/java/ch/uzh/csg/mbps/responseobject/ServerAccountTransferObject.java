package ch.uzh.csg.mbps.responseobject;

import java.util.ArrayList;

import ch.uzh.csg.mbps.model.ServerAccount;

public class ServerAccountTransferObject {
	private ArrayList<ServerAccount> serverAccountList;
	private long numberofSA;
	
	public ServerAccountTransferObject() {
		serverAccountList = new ArrayList<ServerAccount>();
	}

	public ServerAccountTransferObject(ArrayList<ServerAccount> list, long nofSA) {
		serverAccountList = new ArrayList<ServerAccount>();
		numberofSA = nofSA;
	}
	
	public ArrayList<ServerAccount> getServerAccountList() {
		return serverAccountList;
	}

	public void setServerAccountList(ArrayList<ServerAccount> list) {
		this.serverAccountList = list;
	}
	
	public long getNumberOfSA(){
		return numberofSA;
	}
	
	public void setNumberOfSA(long nofSA){
		this.numberofSA = nofSA;
	}
	
	public void decode(String payOutRulesTO) {
	    // TODO Auto-generated method stub
	    
    }
}

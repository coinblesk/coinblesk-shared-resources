package ch.uzh.csg.mbps.responseobject;

import java.util.ArrayList;

import ch.uzh.csg.mbps.model.ServerAccount;

public class ServerAccountTransferObject {
	private ArrayList<ServerAccount> serverAccountList;

	public ServerAccountTransferObject() {
		serverAccountList = new ArrayList<ServerAccount>();
	}

	public ArrayList<ServerAccount> getServerAccountList() {
		return serverAccountList;
	}

	public void setServerAccountList(ArrayList<ServerAccount> list) {
		this.serverAccountList = list;
	}
	
	public void decode(String payOutRulesTO) {
	    // TODO Auto-generated method stub
	    
    }
}

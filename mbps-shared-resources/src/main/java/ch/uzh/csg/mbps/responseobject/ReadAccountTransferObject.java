package ch.uzh.csg.mbps.responseobject;

import ch.uzh.csg.mbps.model.UserAccount;

public class ReadAccountTransferObject {
	private UserAccount userAccount;

	public ReadAccountTransferObject() {
	}

	public ReadAccountTransferObject(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void decode(String readAccountTO) {
	    // TODO Auto-generated method stub
	    
    }

}

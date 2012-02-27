package models;

import java.io.Serializable;

public class UserSessionInfo implements Serializable{
	public String accessToken;
	public String apiEndpoint;
	public String flowState;
	Integer step;
	public UserSessionInfo(){
		super();
		this.step=0;
	}
	public UserSessionInfo(String accessToken, String apiEndpoint) {
		this();
		this.accessToken = accessToken;
		this.apiEndpoint = apiEndpoint;
	}
	public void incrementStep(){
		step++;
	}
	@Override
	public String toString() {
		return "UserSessionInfo [accessToken=" + accessToken + ", apiEndpoint="
				+ apiEndpoint + ", flowState=" + flowState + "]";
	}
	
}
package com.arzen.iFoxLib.contacts;

import java.io.Serializable;

public class Contact implements Serializable {

//	public String systemId;
	public String name;
	public String phone;
//	public String getSystemId() {
//		return systemId;
//	}
//
//	public void setSystemId(String systemId) {
//		this.systemId = systemId;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}

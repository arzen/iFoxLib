package com.arzen.iFoxLib.bean;

import java.io.Serializable;

public class Auth extends BaseBean implements Serializable {

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
	
	public static class Data implements Serializable
	{
		public String uid;
		public String code;
		public String getUid() {
			return uid;
		}
		public void setUid(String uid) {
			this.uid = uid;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		
		
	}
}

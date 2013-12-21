package com.arzen.iFoxLib.bean;

import java.io.Serializable;

public class Token extends BaseBean implements Serializable {

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data implements Serializable {
		public String uid;
		public String token;
		public String expires_in;

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getExpires_in() {
			return expires_in;
		}

		public void setExpires_in(String expires_in) {
			this.expires_in = expires_in;
		}

	}
}

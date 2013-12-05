package com.arzen.iFoxLib.bean;

import java.io.Serializable;

import com.arzen.iFoxLib.bean.Order.Data;

public class User extends BaseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String uid;
		private String token;

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

	}
}

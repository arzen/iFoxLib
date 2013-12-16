package com.arzen.iFoxLib.bean;

import java.io.Serializable;

import com.arzen.iFoxLib.bean.Order.Data;

public class PrepaidCard extends BaseBean {
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
		private String msg;

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

	}
}

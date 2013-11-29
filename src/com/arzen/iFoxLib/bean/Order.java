package com.arzen.iFoxLib.bean;

import java.io.Serializable;
/**
 * 订单对象
 * @author Encore.liang
 *
 */
public class Order implements Serializable {

	private int code;

	private Data data;

	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static class Data implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String orderid;

		public String getOrderid() {
			return orderid;
		}

		public void setOrderid(String orderid) {
			this.orderid = orderid;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

	}
}

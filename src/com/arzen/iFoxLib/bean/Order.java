package com.arzen.iFoxLib.bean;

import java.io.Serializable;

/**
 * 订单对象
 * 
 * @author Encore.liang
 * 
 */
public class Order extends BaseBean implements Serializable {

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

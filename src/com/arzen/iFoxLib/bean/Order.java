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
		private String tn;

		public String getOrderid() {
			return orderid;
		}

		public void setOrderid(String orderid) {
			this.orderid = orderid;
		}

		public String getTn() {
			return tn;
		}

		public void setTn(String tn) {
			this.tn = tn;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

	}
}

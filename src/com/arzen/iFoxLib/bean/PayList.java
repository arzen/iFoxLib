package com.arzen.iFoxLib.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

public class PayList extends BaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	// 初始化成功返回的内容
	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public static class Data implements Serializable {
		public PayListData list;

		public PayListData getList() {
			return list;
		}

		public void setList(PayListData list) {
			this.list = list;
		}
	}

	public static class PayListData implements Serializable {

		// 微派支付
		private String wiipay;
		// 支付宝
		private String alipay;
		// 银联
		private String unionpay;
		
		private String prepaidCard;

		public String getWiipay() {
			return wiipay;
		}

		@JsonProperty("1")
		public void setWiipay(String wiipay) {
			this.wiipay = wiipay;
		}

		public String getAlipay() {
			return alipay;
		}

		@JsonProperty("2")
		public void setAlipay(String alipay) {
			this.alipay = alipay;
		}

		public String getUnionpay() {
			return unionpay;
		}

		@JsonProperty("3")
		public void setUnionpay(String unionpay) {
			this.unionpay = unionpay;
		}

		public String getPrepaidCard() {
			return prepaidCard;
		}
		@JsonProperty("4")
		public void setPrepaidCard(String prepaidCard) {
			this.prepaidCard = prepaidCard;
		}
		
		
	}

}

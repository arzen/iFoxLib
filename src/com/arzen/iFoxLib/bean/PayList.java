package com.arzen.iFoxLib.bean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

public class PayList implements Serializable {

	private static final long serialVersionUID = 1L;
	// 返回码
	private int code;
	// 返回信息
	private String msg;
	// 初始化成功返回的内容
	private Data data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

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
	}

}

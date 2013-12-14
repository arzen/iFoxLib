package com.arzen.iFoxLib.bean;

import java.io.Serializable;
import java.util.List;

public class Top extends BaseBean {

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
		public List<TopList> list;

		public List<TopList> getList() {
			return list;
		}

		public void setList(List<TopList> list) {
			this.list = list;
		}

	}

	public static class TopList implements Serializable {
		public String uid;
		public String uname;
		public String avatar;
		public int score;
		public int rank;
		public int invited; // 1不允许邀请，0允许邀请
		public String play_name; // 正在玩的游戏
		public String dl; // 下载地址

		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public String getUname() {
			return uname;
		}

		public void setUname(String uname) {
			this.uname = uname;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		public int getInvited() {
			return invited;
		}

		public void setInvited(int invited) {
			this.invited = invited;
		}

		public String getPlay_name() {
			return play_name;
		}

		public void setPlay_name(String play_name) {
			this.play_name = play_name;
		}

		public String getDl() {
			return dl;
		}

		public void setDl(String dl) {
			this.dl = dl;
		}

	}
}

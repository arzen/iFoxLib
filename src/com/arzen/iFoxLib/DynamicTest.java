package com.arzen.iFoxLib;

import android.app.Activity;

public class DynamicTest implements IDynamic {
	
	private Activity act;

	@Override
	public String helloWorld() {
		// TODO Auto-generated method stub
		return "Hello World!";
	}
	
	public void init(Activity act) {
		this.act = act;
		MsgUtil.msg("test", act);
//		MsgUtil.msg(DeviceUtil.getUUID(act), act);
	}

}

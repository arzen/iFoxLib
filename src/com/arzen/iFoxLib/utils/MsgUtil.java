package com.arzen.iFoxLib.utils;

import android.content.Context;
import android.widget.Toast;

public class MsgUtil {
	public static void msg(String message, Context con) {
		Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
	}

}

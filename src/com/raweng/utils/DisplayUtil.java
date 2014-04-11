package com.raweng.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.WindowManager;

public class DisplayUtil {
	public int displayWidth;
	public int displayHeight;
	public int climbover;
	public int menuWidth;

	float menuWidhtPercent;
	float menuClimbPercent;

	public  void calculateScreenBounds(Context context) {
		displayHeight = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
		displayWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
	}

	public  int getScreenOrientation(final Activity activity) {
		final Configuration c2a = activity.getResources().getConfiguration();
		return c2a.orientation;
	}


}

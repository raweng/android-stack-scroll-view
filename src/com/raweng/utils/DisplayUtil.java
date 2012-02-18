package com.raweng.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtil {
	public static int displayWidth;
	public static int displayHeight;
	public int climbover;
	public int menuWidth;
	
	float menuWidhtPercent;
	float menuClimbPercent;

	public static void calculateScreenBounds(Context context) {
		
		
		displayHeight = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
		displayWidth = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
		
		//Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		//displayHeight = display.getHeight();
		//displayWidth = display.getWidth();
		/*int statusBarHeight = (int) Math.ceil(25 * context.getResources().getDisplayMetrics().density);				
		displayHeight = displayHeight - statusBarHeight;*/
	}
	
	public static int getScreenOrientation(final Activity activity) {
		/*final Display getOrient = activity.getWindowManager().getDefaultDisplay();

        int orientation = getOrient.getOrientation();

        // Sometimes you may get undefined orientation Value is 0
        // simple logic solves the problem compare the screen
        // X,Y Co-ordinates and determine the Orientation in such cases
        if (orientation == Configuration.ORIENTATION_UNDEFINED) {

            final Configuration config = activity.getResources().getConfiguration();
            orientation = config.orientation;

            if (orientation == Configuration.ORIENTATION_UNDEFINED) {
                //if height and width of screen are equal then
                // it is square orientation
                if (getOrient.getWidth() == getOrient.getHeight()) {
                    orientation = Configuration.ORIENTATION_SQUARE;
                } else { //if widht is less than height than it is portrait
                    if (getOrient.getWidth() < getOrient.getHeight()) {
                        orientation = Configuration.ORIENTATION_PORTRAIT;
                    } else { // if it is not any of the above it will defineitly be landscape
                        orientation = Configuration.ORIENTATION_LANDSCAPE;
                    }
                }
            }
        }*/

		final Configuration c2a = activity.getResources().getConfiguration();

		return c2a.orientation;
	}

	
}

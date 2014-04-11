package com.example.androidstackscrollview;


/*import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.FrameLayout.LayoutParams;*/

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.raweng.stackscrollview.StackScrollView;
import com.raweng.test.MenuViewFragment;
import com.raweng.utils.DisplayUtil;

public class RootViewController extends FragmentActivity {

	private int ITEMS_LIST_ID = 0x101;
	private int ITEM_DETAILS_ID = 0x102;
	private StackScrollView stackScrollView;

	com.raweng.utils.DisplayUtil displayUtil;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.init();
		View view = this.setupViews();
		setContentView(view);
		view = null;
	}

		
	private void init() {
		// Retrieve display width and height
		//DisplayUtil.calculateScreenBounds(getApplicationContext());
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		System.out.println("----------------DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD-----------  "+newConfig.orientation);
		super.onConfigurationChanged(newConfig);

		displayUtil.calculateScreenBounds(this);
		
		if(stackScrollView != null){
			stackScrollView.onConfigurationChanged(newConfig);
		}


	}

	private View setupViews() {
		//LinearLayout rootView = new LinearLayout(this);
		//rootView.setOrientation(LinearLayout.HORIZONTAL);
		//rootView.setLayoutParams(new LinearLayout.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.FILL_PARENT));
		displayUtil = new DisplayUtil();
		displayUtil.calculateScreenBounds(this);
		FrameLayout rootView = new FrameLayout(getApplicationContext());
		rootView.setLayoutParams(new FrameLayout.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.FILL_PARENT));
		rootView.setBackgroundResource(R.drawable.backgroundimagerepeat);


		FrameLayout leftMenuView = new FrameLayout(getApplicationContext());
		int leftMenuViewWidth ;
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){	
				leftMenuViewWidth = (int) (displayUtil.displayWidth * 0.35);
		}else{
				leftMenuViewWidth = (int) (displayUtil.displayHeight*0.30);
		}
		
		
		leftMenuView.setLayoutParams(new FrameLayout.LayoutParams(leftMenuViewWidth, ViewGroup.LayoutParams.FILL_PARENT));
		leftMenuView.setId(ITEMS_LIST_ID);
		
		MenuViewFragment menuViewController = new MenuViewFragment(ITEM_DETAILS_ID);
		{
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.add(ITEMS_LIST_ID, menuViewController);
			fragmentTransaction.commit();
		}
	
		FrameLayout rightSlideView = new FrameLayout(getApplicationContext());
		rightSlideView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		
		
		/* FrameLayout.LayoutParams rightSlideViewParams =   new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT); 
        rightSlideViewParams.leftMargin = rightSlideViewWidth;
        rightSlideView.setLayoutParams(rightSlideViewParams);*/

		rightSlideView.setId(ITEM_DETAILS_ID);
		stackScrollView = new StackScrollView(getApplicationContext(),leftMenuViewWidth);
		stackScrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		stackScrollView.setStartX(leftMenuViewWidth);
		
		rightSlideView.addView(stackScrollView);
		
		rootView.addView(leftMenuView);
		rootView.addView(rightSlideView);
		
		return rootView;
	}




	public StackScrollView getStackScrollView() {
		return stackScrollView;
	}

	public void setStackScrollView(StackScrollView stackScrollView) {
		this.stackScrollView = stackScrollView;
	}
}
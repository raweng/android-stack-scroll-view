package com.raweng.stackscrollview;

import com.example.androidstackscrollview.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;


public class ShadowViewGroup extends FrameLayout {

	public final static int SHADOW_WIDTH = 20;
	public final static int LEFTMARGIN = 4;
	public final static int LEFTMARGIN1 = 2;
	public  View leftMargin;
	public  View leftMargin1;
	public  View leftMargin2;
	
	public ShadowViewGroup(Context context, int id, int width) 
	{
		super(context);

		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width + SHADOW_WIDTH, ViewGroup.LayoutParams.FILL_PARENT);
		lp.gravity = Gravity.LEFT ;
		this.setLayoutParams(lp);	


		FrameLayout shadowView = new FrameLayout(context);
		FrameLayout.LayoutParams shadowViewLayoutParams = new FrameLayout.LayoutParams(SHADOW_WIDTH, ViewGroup.LayoutParams.FILL_PARENT);
		shadowViewLayoutParams.gravity = Gravity.LEFT ;
		shadowView.setLayoutParams(shadowViewLayoutParams);	
		shadowView.setBackgroundResource(R.drawable.drop_shadow);

		FrameLayout slideView = new FrameLayout(context);
		slideView.setId(id);
		FrameLayout.LayoutParams slideViewLayoutParams = new FrameLayout.LayoutParams ( width + SHADOW_WIDTH , ViewGroup.LayoutParams.FILL_PARENT);
		slideViewLayoutParams.gravity = Gravity.LEFT ;
		slideViewLayoutParams.leftMargin = SHADOW_WIDTH;
		slideView.setLayoutParams(slideViewLayoutParams);	

		leftMargin  = new View(context);
		LayoutParams leftMarginParams = new LayoutParams (2, ViewGroup.LayoutParams.FILL_PARENT);
		leftMarginParams.gravity = Gravity.LEFT ;
		leftMarginParams.leftMargin = SHADOW_WIDTH;
		leftMargin.setBackgroundColor(Color.GRAY);
		leftMargin.setVisibility(View.GONE);
		leftMargin.setLayoutParams(leftMarginParams);	
		
		leftMargin1  = new View(context);
		LayoutParams leftMarginParams1 = new LayoutParams (1, ViewGroup.LayoutParams.FILL_PARENT);
		leftMarginParams1.gravity = Gravity.LEFT ;
		leftMarginParams1.leftMargin = SHADOW_WIDTH+3;
		leftMargin1.setBackgroundColor(Color.GRAY);
		leftMargin1.setVisibility(View.GONE);
		leftMargin1.setLayoutParams(leftMarginParams1);	
		
		leftMargin2  = new View(context);
		LayoutParams leftMarginParams2 = new LayoutParams (1, ViewGroup.LayoutParams.FILL_PARENT);
		leftMarginParams2.gravity = Gravity.LEFT ;
		leftMarginParams2.leftMargin = SHADOW_WIDTH+5;
		leftMargin2.setBackgroundColor(Color.GRAY);
		leftMargin2.setVisibility(View.GONE);
		leftMargin2.setLayoutParams(leftMarginParams2);	

		
	
		addView(slideView);
		addView(shadowView);
		addView(leftMargin);
		addView(leftMargin1);
		addView(leftMargin2);

		this.showShadow(false);
	}


	public void showShadow(boolean showShadow) {
		if (showShadow) {
			getChildAt(1).setVisibility(View.VISIBLE);
			FrameLayout.LayoutParams lp = (LayoutParams) this.getLayoutParams();
			lp.gravity = Gravity.LEFT ;//| Gravity.TOP;
			lp.width = getChildAt(0).getLayoutParams().width;
			this.setLayoutParams(lp);

			FrameLayout.LayoutParams slideViewLayoutParams = new FrameLayout.LayoutParams(getChildAt(0).getLayoutParams().width , ViewGroup.LayoutParams.FILL_PARENT);
			slideViewLayoutParams.gravity = Gravity.LEFT ;
			slideViewLayoutParams.leftMargin = SHADOW_WIDTH;
			getChildAt(0).setLayoutParams(slideViewLayoutParams);

		}else{
			getChildAt(1).setVisibility(View.GONE);
			FrameLayout.LayoutParams lp = (LayoutParams) this.getLayoutParams();
			lp.gravity = Gravity.LEFT ;
			lp.width = getChildAt(0).getLayoutParams().width;
			this.setLayoutParams(lp);
			FrameLayout.LayoutParams slideViewLayoutParams = new FrameLayout.LayoutParams(getChildAt(0).getLayoutParams().width, ViewGroup.LayoutParams.FILL_PARENT);
			slideViewLayoutParams.gravity = Gravity.LEFT;
			getChildAt(0).setLayoutParams(slideViewLayoutParams);
		}
	}


	public int getShadowViewWidth() {
		return SHADOW_WIDTH;
	}


	public int getSlideViewWidth() {
		FrameLayout.LayoutParams slideViewParam = (LayoutParams) getChildAt(0).getLayoutParams();
		return ((slideViewParam.width ));
	}

	public int getSlideViewId() {
		return  getChildAt(0).getId();
	}

	public int getSlideViewLeft() {

		return (super.getLeft() ) ;
	}

	public View getLeftMargin(int visibility,int visibility1){
		leftMargin.setVisibility(View.GONE);
		leftMargin1.setVisibility(View.GONE);
		leftMargin2.setVisibility(View.GONE);
		return leftMargin;

	}

}

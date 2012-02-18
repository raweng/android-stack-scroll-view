package com.raweng.stackscrollview;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.raweng.main.R;

public class ShadowViewGroup extends FrameLayout {

	public final static int SHADOW_WIDTH = 20;
	public final static int LEFTMARGIN = 4;
	public final static int LEFTMARGIN1 = 2;
	
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
	
		addView(slideView);
		addView(shadowView);

		this.showShadow(false);
	}


	public void showShadow(boolean showShadow) {
		if (showShadow) {
			getChildAt(1).setVisibility(View.VISIBLE);
			FrameLayout.LayoutParams lp = (LayoutParams) this.getLayoutParams();
			lp.gravity = Gravity.LEFT ;
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
			slideViewLayoutParams.gravity = Gravity.LEFT ;//
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
}

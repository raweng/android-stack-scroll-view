package com.raweng.stackscrollview;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.raweng.test.DataViewFragment;




public class StackScrollView extends FrameLayout {	
	private static ArrayList<Fragment> viewControllersStack = new ArrayList<Fragment>();
	private static ShadowViewGroup viewAtLeft;
	private static ShadowViewGroup viewAtRight;
	private static ShadowViewGroup viewAtLeft2;
	private static ShadowViewGroup viewAtRight2;
	private static LayoutParams viewAtLeftParams;
	private static LayoutParams viewAtRightParams; 
	private static LayoutParams viewAtLeft2Params;
	private static LayoutParams viewAtRight2Params;
	private static GestureDetector gestureDetector;
	private static String dragDirection = "";
	private static final String DIRECTION_RIGHT = "RIGHT";
	private static final String DIRECTION_LEFT = "LEFT";
	private static int DURATION = 225;


	private static boolean isScrolling;
	private  int startX ;
	private static int SLIDE_VIEWS_START_X_POS ;
	private static int SLIDE_VIEWS_MINUS_X_POSITION = 0;

	private static int displacementPosition = 0; 
	private static int slideViewWidth = 0;

	private Display display;
	private static int displayWidth;
	private Paint myPaint;
	private Paint rectPaint;
	private static int deletePoint;
	private RectF rectangle;
	RectF rectangle1;

	float rectLeft;
	float rectTop;
	float rectWidth;
	float rectHeight;


	public StackScrollView(Context context,int startX) {
		super(context);		
		gestureDetector = new GestureDetector(new MyGestureDetector());
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		SLIDE_VIEWS_MINUS_X_POSITION =  (int) (startX * 0.3);
		if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
			displayWidth = display.getHeight()+ 48;	// where 48 is sum of above and below action bar
		}else{
			displayWidth = display.getWidth();
		}
		this.startX = startX;
		slideViewWidth = ((displayWidth-SLIDE_VIEWS_MINUS_X_POSITION) ) / 2;
		myPaint = new Paint();
		rectPaint = new Paint();
		setSmallRectangleProperties();
		rectangle1 = new RectF();
		this.setWillNotDraw(false);
		deletePoint = (int) (display.getWidth() - (display.getWidth()*0.3));	
	}

	public StackScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public StackScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	public void setSmallRectangleProperties(){
		int height = 100;
		rectLeft = getStartX()+10;
		rectTop  = display.getHeight()/2 - height/2;
		rectWidth = (rectLeft+60);
		rectHeight = ((rectTop)+height);
		rectangle = new RectF();
		rectangle.left = rectLeft;
		rectangle.top = rectTop;
		rectangle.right = rectWidth;
		rectangle.bottom = rectHeight;
	}

	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		myPaint.setColor(Color.GRAY);
		myPaint.setStrokeWidth(1f);
		if(viewAtLeft!= null){
			if(viewAtLeft.getAnimation() == null){
				if(getChildCount() > 3 ) {
					if(getIndexOf(viewAtLeft2) == 1 ){
						if(viewAtLeft.getLeft() == -20){
							canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
							canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);
							canvas.drawLine(4, 0, 4,this.getHeight(), myPaint);		
						}else{
							System.out.println("********************************************************  777  ");

							canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
							canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);
						}
					}else if(getIndexOf(viewAtLeft2) == 0 && viewAtLeft.getLeft() == -20) {
						System.out.println("********************************************************  888  ");

						canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
						canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);
					}else if(getIndexOf(viewAtLeft2) > 1) {
						canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
						canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);
						canvas.drawLine(4, 0, 4,this.getHeight(), myPaint);
					}

				}else if(getChildCount() == 3 ) { 
					if(getIndexOf(viewAtLeft2) == 0 && viewAtLeft.getLeft() == -20) {
						System.out.println("********************************************************  9999 ");

						canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
						canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);
					}

				}
			}else if(viewAtLeft.getAnimation() != null){
				if(getChildCount() == 4 ) {
					if(getIndexOf(viewAtLeft2) == 0 && viewAtLeft.getLeft() == (viewAtLeft.getSlideViewWidth()-40)){

					}else if(getIndexOf(viewAtLeft2) == 0 && viewAtLeft.getLeft() == -20){

					}else{
						if(viewAtLeft2 != null){
							canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
							canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);
						}
					}
				}else if(getChildCount() > 4 ){
					if(getIndexOf(viewAtLeft2) == 1 ){
						System.out.println("******************************************************** 222   ");
						canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
						canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);

					}else if(getIndexOf(viewAtLeft2) == 0 && getChildCount() == -20) {
						System.out.println("******************************************************** 3333   ");
						canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
						canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);
					}else if(getIndexOf(viewAtLeft2) >= 2) {
						canvas.drawLine(0, 0, 0,this.getHeight(), myPaint);
						canvas.drawLine(2, 0, 2,this.getHeight(), myPaint);
						canvas.drawLine(4, 0, 4,this.getHeight(), myPaint);		

					}

				}
			}
		}
	}


	@Override
	public void draw(Canvas canvas) {
		if(this.getLeft()!= 0){
			// more than one card on screen
			canvas.drawLine(getStartX()-this.getLeft(), 0,(getStartX()-this.getLeft()),this.getHeight(), myPaint);
		}else{
			// incase of only one card , on screen
			canvas.drawLine(getStartX(), 0, getStartX(),this.getHeight(), myPaint);
		}
		super.draw(canvas);
		if(getChildCount() > 1){

			rectPaint.setStrokeWidth(2);
			rectPaint.setColor(Color.WHITE);
			rectPaint.setStyle(Style.STROKE);
			canvas.drawRoundRect(rectangle, 5, 5, rectPaint);

			if ((getChildAt(0).getLeft()) >= deletePoint) {
				rectPaint.setColor(Color.GRAY);
				rectPaint.setStyle(Style.STROKE);
				Matrix matrix = canvas.getMatrix();
				matrix.preRotate(30, (rectWidth-70), (rectangle.centerY() + rectWidth) );             // for toshiba tab 3.0       // need to find a common solution to be device independent
				//matrix.preRotate(30, ((rectWidth-70/2)), (rectangle.centerY() + rectWidth/2) );      //  for kindle and mobiles
				canvas.setMatrix(matrix);
				canvas.drawRoundRect(rectangle1, 5, 5, rectPaint);
				this.invalidate();
			}else{
				rectangle1.left = rectLeft+20;
				rectangle1.top = rectTop + 15;
				rectangle1.right = rectWidth+20;
				rectangle1.bottom = rectHeight+15;
				canvas.drawRoundRect(rectangle1, 5, 5, rectPaint);
				super.draw(canvas);
			}


		}



	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_MOVE) {
			return 	gestureDetector.onTouchEvent(event);
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if(isScrolling) {
				isScrolling = false;
				if(event.getX() > (int) (startX * 0.3)) {
					return true;	
				} else {
					return 	gestureDetector.onTouchEvent(event);
				}
			} else {
				if(event.getX() > startX) {
					return true;					
				}else {
					return 	gestureDetector.onTouchEvent(event);
				}
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if(isScrolling) {
				this.onUp(event);
				isScrolling = false;
				return 	gestureDetector.onTouchEvent(event);		
			}else{
				return true;
			}
		}
		return false;



	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if(isScrolling) {
				isScrolling = false;
				//this.onUp(event);
				//	return true; //commented because this create problem in click after scrolling the stack 
			}
		}else if(event.getAction() == MotionEvent.ACTION_MOVE) {

		}
		return gestureDetector.onTouchEvent(event);	
	}





	private  Animation viewAnimation(final ViewGroup view, int moveFromX, int moveToX, int velocity) {
		TranslateAnimation anim = new TranslateAnimation(moveFromX, moveToX, 0, 0);
		anim.setDuration(velocity);
		view.setAnimationCacheEnabled(false);

		return anim;
	}

	private  Animation viewBounceAnimation(final View view,int moveFromL, int moveFromR, final int moveTo) {
		TranslateAnimation viewBounceAnimation = new TranslateAnimation(0, moveTo, 0, 0);
		viewBounceAnimation.setDuration(200);
		viewBounceAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				if(getChildAt(getIndexOf(viewAtLeft2) - 1) !=null){
					getChildAt(getIndexOf(viewAtLeft2) - 1).setVisibility(View.VISIBLE);
				}
			}
			public void onAnimationRepeat(Animation animation){
			}

			public void onAnimationEnd(Animation animation) {
				view.setAnimation(viewBounceBackAnimation(view,moveTo,0));
			}
		});
		return viewBounceAnimation;
	}

	private Animation viewBounceBackAnimation(final View view,int moveFrom, int moveTo) {
		TranslateAnimation viewBounceAnimation = new TranslateAnimation(moveFrom, moveTo, 0, 0);
		viewBounceAnimation.setDuration(200);
		viewBounceAnimation.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub	
			}
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (getIndexOf(viewAtLeft2) > 1) {
					getChildAt(getIndexOf(viewAtLeft2) - 1).setVisibility(View.GONE);
				}
				System.gc();
			}
		});
		return viewBounceAnimation;
	}

	public void onUp(MotionEvent e) {
		Animation animViewLeft ;
		Animation animViewRight;
		FrameLayout.LayoutParams stackViewParams;
		if (dragDirection.equalsIgnoreCase(DIRECTION_LEFT)) {
			if (viewAtRight != null) {
				if (getIndexOf(viewAtLeft) == 0 && !(viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION || viewAtLeft.getLeft() == SLIDE_VIEWS_START_X_POS)) {
					//Drop Card View Animation
					//getChildAt(0).getLeft() + 75 ) states the postion where card is almost ready to be discarded
					if ((getChildAt(0).getLeft() + 75 ) >= deletePoint) {
						int viewControllerCount = viewControllersStack.size();
						int j = viewControllerCount;
						if (j > 1) {
							for (int i = 1; i < viewControllerCount; i++) {
								j--;
								Fragment f = viewControllersStack.get(j);
								FragmentTransaction ft = f.getFragmentManager().beginTransaction();
								ft.remove(f);
								ft.commit();
								this.removeViewAt(j);
								viewControllersStack.remove(j);
							}
						}
						viewAtLeft2 = null;
						viewAtRight = null;
						viewAtRight2 = null;

						stackViewParams = (LayoutParams)this.getLayoutParams();
						stackViewParams.leftMargin = 0;
						stackViewParams.width = LayoutParams.FILL_PARENT;
						stackViewParams.gravity = Gravity.LEFT;
						this.setLayoutParams(stackViewParams);
						SLIDE_VIEWS_START_X_POS = (int) (getStartX());
						viewAtLeft.showShadow(false);
						viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS  ;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);

						if (viewAtRight != null) {
							viewAtRightParams.leftMargin = SLIDE_VIEWS_START_X_POS + viewAtLeft.getSlideViewWidth() ;
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
						}

					}else{
						System.out.println("---------------<<<<<<<< 1else------------ ");
						FrameLayout.LayoutParams stackViewParams1 = (LayoutParams)this.getLayoutParams();
						//here (getMENU_WIDTH() - stackViewParams1.leftMargin) gives the |0----a----1| distance between a to 1 tats 40 where distanc of 0 to 1 is menuwidth 
						//from that we calculate the margin for stackView 

						System.out.println("---------------------s----------------   "+(getStartX() - stackViewParams1.leftMargin));
						if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin) || viewAtLeft.getLeft() < (getStartX() - stackViewParams1.leftMargin)){
							if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin)){
								System.out.println("-123--");
								stackViewParams = (LayoutParams)this.getLayoutParams();
								SLIDE_VIEWS_MINUS_X_POSITION = getStartX() -((getStartX() - stackViewParams1.leftMargin));
								stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
								stackViewParams.gravity = Gravity.LEFT;
								this.setLayoutParams(stackViewParams);
								if(stackViewParams.leftMargin != 0){
									viewAtLeftParams.leftMargin = (getStartX() - stackViewParams.leftMargin-(ShadowViewGroup.SHADOW_WIDTH)); // more than one card on screen
								}else{
									viewAtLeftParams.leftMargin = getStartX(); // incase of only one card there on screen
								}
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
								if (viewAtRight != null) {
									viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
									viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)) + (getStartX() - stackViewParams1.leftMargin);
									viewAtRightParams.gravity = Gravity.LEFT;
									viewAtRight.setLayoutParams(viewAtRightParams);	
								}


							}else if(viewAtLeft.getLeft() < (getStartX() - stackViewParams1.leftMargin)){
								//in middle of 200 and 240
								System.out.println("-134--");
								stackViewParams = (LayoutParams)this.getLayoutParams();
								SLIDE_VIEWS_MINUS_X_POSITION =  getStartX()-((getStartX() - stackViewParams1.leftMargin));
								stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
								stackViewParams.gravity = Gravity.LEFT;
								this.setLayoutParams(stackViewParams);
								viewAtLeftParams.leftMargin = -(ShadowViewGroup.SHADOW_WIDTH) ;
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
								if (viewAtRight != null) {
									viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
									viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2));
									viewAtRightParams.gravity = Gravity.LEFT;
									viewAtRight.setLayoutParams(viewAtRightParams);	
									viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0,viewAtRight.getLeft() , -10));

								}

								if (viewAtRight2 != null) {
									viewAtRight2Params = (FrameLayout.LayoutParams) viewAtRight2.getLayoutParams();		
									viewAtRight2Params.leftMargin = this.getWidth()-(ShadowViewGroup.SHADOW_WIDTH);
									viewAtRight2Params.gravity = Gravity.LEFT;
									viewAtRight2.setLayoutParams(viewAtRight2Params);
									viewAtRight2.setVisibility(View.VISIBLE);
									viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -15));
									viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -10));
								}

							}

						}
					}

				} else if (viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION && (viewAtRight.getLeft() + viewAtRight.getSlideViewWidth()) > this.getWidth()) {
					System.out.println("///////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\     ");
					viewAtRightParams.leftMargin = this.getWidth() - viewAtRight.getSlideViewWidth();
					viewAtRightParams.gravity = Gravity.LEFT;
					viewAtRight.setLayoutParams(viewAtRightParams);

					animViewRight = viewAnimation(viewAtRight, -(this.getWidth()-viewAtRight.getRight()), 0, DURATION);
					viewAtRight.clearAnimation();
					viewAtRight.setAnimation(animViewRight);
					animViewRight.setAnimationListener(new AnimationListener() {

						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
						}

						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
						}

						public void onAnimationEnd(Animation animation) {
							viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 10));
						}
					});


				} else if (viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION && (viewAtRight.getLeft() + viewAtRight.getSlideViewWidth()) < this.getWidth()) {
					System.out.println("---------- RIGHT-WITH-RIGHT ----------");
					viewAtRightParams.leftMargin = this.getWidth() - viewAtRight.getSlideViewWidth();
					viewAtRightParams.gravity = Gravity.LEFT;
					viewAtRight.setLayoutParams(viewAtRightParams);

					animViewRight = viewAnimation(viewAtRight, -(this.getWidth()-viewAtRight.getRight()), 0, DURATION);
					viewAtRight.clearAnimation();
					animViewRight.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 10));
							viewAtLeft.setAnimation(viewBounceAnimation(viewAtLeft,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 10));
						}
					});
					viewAtRight.setAnimation(animViewRight);
					animViewRight = null;

				} else if (viewAtLeft.getLeft() > SLIDE_VIEWS_MINUS_X_POSITION) {

					if ((viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth() > this.getWidth()) && viewAtLeft.getLeft() > ((this.getWidth()-viewAtLeft.getSlideViewWidth()))) {
						System.out.println("-----<<- LEFT-WITH-LEFT ----d------ ");
						viewAtLeftParams.leftMargin = this.getWidth() - (viewAtLeft.getSlideViewWidth());
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);
						//Show bounce effect
						if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
							viewAtRightParams.leftMargin = viewAtRight.getSlideViewWidth()+(ShadowViewGroup.SHADOW_WIDTH);
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);	
						}else{
							viewAtRightParams.leftMargin = this.getWidth()-(ShadowViewGroup.SHADOW_WIDTH);
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
						}
						animViewLeft = viewAnimation(viewAtLeft,(viewAtLeft.getLeft() - 100),0, DURATION);
						viewAtLeft.clearAnimation();
						viewAtRight.clearAnimation();
						viewAtLeft.setAnimation(animViewLeft);

						animViewLeft.setAnimationListener(new AnimationListener() {
							public void onAnimationStart(Animation animation) {
							}

							public void onAnimationRepeat(Animation animation) {
							}

							public void onAnimationEnd(Animation animation) {
								viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0, -(viewAtLeft.getRight()), -20));
								viewAtLeft.setAnimation(viewBounceAnimation(viewAtLeft,0, -(viewAtRight.getLeft()), -10));
							}
						});

						animViewLeft = null;
						animViewRight = null;


					} else {
						System.out.println("------<<---- LEFT-WITH-RIGHT ----------");
						viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);
						if (viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth()- (ShadowViewGroup.SHADOW_WIDTH *2) <= this.getWidth()) {

							viewAtRightParams.leftMargin = this.getWidth() - (viewAtRight.getSlideViewWidth());
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
							animViewLeft = viewAnimation(viewAtLeft, (viewAtLeft.getLeft()),0, DURATION);
							if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
								animViewRight = viewAnimation(viewAtRight, (viewAtRight.getLeft()+(ShadowViewGroup.SHADOW_WIDTH)-viewAtLeft.getSlideViewWidth()), (0), DURATION); 
							}else{
								animViewRight = viewAnimation(viewAtRight, (viewAtRight.getLeft()-(ShadowViewGroup.SHADOW_WIDTH *2))-100, (0), DURATION);	
							}

							animViewRight.setAnimationListener(new AnimationListener() {
								public void onAnimationStart(Animation animation) {
								}

								public void onAnimationRepeat(Animation animation) {
								}

								public void onAnimationEnd(Animation animation) {
									if(viewAtRight2 != null){
										viewAtRight2.setVisibility(View.VISIBLE);
										viewAtRightParams.leftMargin = StackScrollView.this.getWidth()- viewAtRight.getSlideViewWidth();
										viewAtRightParams.gravity = Gravity.LEFT;
										viewAtRight.setLayoutParams(viewAtRightParams);

										viewAtRight2Params.leftMargin = (viewAtRight.getRight()-(ShadowViewGroup.SHADOW_WIDTH));
										viewAtRight2Params.gravity = Gravity.LEFT;
										viewAtRight2.setLayoutParams(viewAtRight2Params);
										viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0,viewAtRight.getLeft() , -10));
										viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -15));
									}else{
										viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0,viewAtRight.getLeft() , -10));
									}
								}
							});

							viewAtLeft.clearAnimation();
							viewAtRight.clearAnimation();

							viewAtLeft.setAnimation(animViewLeft);
							viewAtRight.setAnimation(animViewRight);

							animViewLeft = null;
							animViewRight = null;

						} else {
							viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft.getSlideViewWidth();
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);


						}
						//Show bounce effect
						//TODO: need to set animation listener for bounce back
						if (viewAtRight2 != null) {
							viewAtRight2Params.leftMargin = this.getWidth() ;
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight2.setLayoutParams(viewAtRight2Params);

						}
					}
				}

			} else {
				viewAtLeft.showShadow(false);
				viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS ;
				viewAtLeftParams.gravity = Gravity.LEFT;
				viewAtLeft.setLayoutParams(viewAtLeftParams);
				Animation anim = viewAnimation(viewAtLeft, -(SLIDE_VIEWS_START_X_POS-viewAtLeft.getLeft()),0, DURATION);
				viewAtLeft.clearAnimation();
				viewAtLeft.setAnimation(anim);
				anim = null;

			}

		} else if (dragDirection.equalsIgnoreCase(DIRECTION_RIGHT)) {
			if (viewAtLeft != null) {
				if (getIndexOf(viewAtLeft) == 0 && !(viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION || viewAtLeft.getLeft() == SLIDE_VIEWS_START_X_POS)) {
					System.out.println( "------->>>>--- ss ----sssss----ss--");
					if (viewAtLeft.getLeft() > SLIDE_VIEWS_MINUS_X_POSITION || viewAtRight == null) {
						//Drop Card View Animation
						if ((getChildAt(0).getLeft()) >= deletePoint) {
							int viewControllerCount = viewControllersStack.size();
							int j = viewControllerCount;
							if (j > 1) {
								for (int i = 1; i < viewControllerCount; i++) {
									j--;
									Fragment f = viewControllersStack.get(j);
									FragmentTransaction ft = f.getFragmentManager().beginTransaction();
									ft.remove(f);
									ft.commit();
									this.removeViewAt(j);
									viewControllersStack.remove(j);

								}
							}
							viewAtLeft2 = null;
							viewAtRight = null;
							viewAtRight2 = null;


							stackViewParams = (LayoutParams)this.getLayoutParams();
							stackViewParams.leftMargin = 0;
							stackViewParams.width = LayoutParams.FILL_PARENT;
							stackViewParams.gravity = Gravity.LEFT;
							this.setLayoutParams(stackViewParams);
							SLIDE_VIEWS_START_X_POS = (int) (getStartX());
							viewAtLeft.showShadow(false);
							viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS  ;
							viewAtLeftParams.gravity = Gravity.LEFT;
							viewAtLeft.setLayoutParams(viewAtLeftParams);



							if (viewAtRight != null) {
								viewAtRightParams.leftMargin = SLIDE_VIEWS_START_X_POS + viewAtLeft.getSlideViewWidth();
								viewAtRightParams.gravity = Gravity.LEFT;
								viewAtRight.setLayoutParams(viewAtRightParams);
							}
							Animation anim = viewAnimation(viewAtLeft, -(SLIDE_VIEWS_START_X_POS-viewAtLeft.getLeft()),0, DURATION);
							viewAtLeft.clearAnimation();
							viewAtLeft.setAnimation(anim);							


						}else{
							System.out.println("------------>>>>>>-------1else--  ");
							//here (getMENU_WIDTH() - stackViewParams1.leftMargin) gives the |0----a----1| distance between a to 1 tats 40 where distanc of 0 to 1 is menuwidth 
							//from that we calculate the margin for stackView 
							FrameLayout.LayoutParams stackViewParams1 = (LayoutParams)this.getLayoutParams();
							if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin) || viewAtLeft.getLeft() < (getStartX() - stackViewParams1.leftMargin)){
								System.out.println("-2@@--"+SLIDE_VIEWS_MINUS_X_POSITION);
								if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin)){
									System.out.println("-232--");
									stackViewParams = (LayoutParams)this.getLayoutParams();
									SLIDE_VIEWS_MINUS_X_POSITION = getStartX()-((getStartX() - stackViewParams1.leftMargin));
									stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
									stackViewParams.width = LayoutParams.FILL_PARENT;
									stackViewParams.gravity = Gravity.LEFT;
									this.setLayoutParams(stackViewParams);
									System.out.println("------------>>>>>>------- +++++  "+stackViewParams.leftMargin);
									viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
									if(stackViewParams.leftMargin != 0){
										viewAtLeftParams.leftMargin = (getStartX() - stackViewParams.leftMargin-(ShadowViewGroup.SHADOW_WIDTH)); // more than one card on screen
									}else{
										viewAtLeftParams.leftMargin = getStartX(); // incase of only one card there on screen
									}
									viewAtLeftParams.gravity = Gravity.LEFT;
									viewAtLeft.setLayoutParams(viewAtLeftParams);

									if (viewAtRight != null) {
										viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
										viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)) + (getStartX() - stackViewParams1.leftMargin); 
										viewAtRightParams.gravity = Gravity.LEFT;
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

									if(viewAtRight == null){
										Animation anim = viewAnimation(viewAtLeft, -(SLIDE_VIEWS_START_X_POS-viewAtLeft.getLeft()),0, DURATION);
										viewAtLeft.clearAnimation();
										viewAtLeft.setAnimation(anim);								
									}else{
										Animation anim = viewAnimation(viewAtLeft, -(getStartX() - stackViewParams.leftMargin-(ShadowViewGroup.SHADOW_WIDTH)-viewAtLeft.getLeft()),0, DURATION);

										viewAtLeft.clearAnimation();
										viewAtLeft.setAnimation(anim);		

										viewAtRight.clearAnimation();
										viewAtRight.setAnimation(anim);
									}




								}else if(viewAtLeft.getLeft() < (getStartX() - stackViewParams1.leftMargin)){
									//in middle of 200 and 240
									System.out.println("-234--");
									stackViewParams = (LayoutParams)this.getLayoutParams();
									SLIDE_VIEWS_MINUS_X_POSITION = getStartX()-((getStartX() - stackViewParams1.leftMargin));
									stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
									stackViewParams.width = LayoutParams.FILL_PARENT;
									stackViewParams.gravity = Gravity.LEFT;
									this.setLayoutParams(stackViewParams);

									viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams(); 
									viewAtLeftParams.leftMargin = (getStartX() - stackViewParams1.leftMargin-(ShadowViewGroup.SHADOW_WIDTH));
									viewAtLeftParams.gravity = Gravity.LEFT;
									viewAtLeft.setLayoutParams(viewAtLeftParams);

									if (viewAtRight != null) {
										viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
										viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth() -(ShadowViewGroup.SHADOW_WIDTH *2)) + (getStartX() - stackViewParams1.leftMargin); 
										viewAtRightParams.gravity = Gravity.LEFT;
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

								}
							}
						}

					} else {
						viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);
						viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft.getSlideViewWidth();
						viewAtRightParams.gravity = Gravity.LEFT;
						viewAtRight.setLayoutParams(viewAtRightParams);		
					}

				} else if (viewAtRight != null && viewAtRight.getLeft() < this.getWidth()) {					
					System.out.println( "------->>>>--- ss ----------     "+viewAtLeft.getLeft());
					System.out.println("+++============   up---  "+viewAtLeft.getSlideViewWidth());

					if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
						System.out.println("----------------->>>>----RIGHT WITH RIGHT---------------");
						if(viewAtRight.getLeft()>viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)){
							viewAtLeftParams.leftMargin = viewAtLeft2.getRight()-(ShadowViewGroup.SHADOW_WIDTH); ;
							viewAtLeftParams.gravity = Gravity.LEFT;
							viewAtLeft.setLayoutParams(viewAtLeftParams);
							animViewLeft = viewAnimation(viewAtLeft, -((viewAtLeft2.getRight()-(ShadowViewGroup.SHADOW_WIDTH)) - (viewAtLeft.getLeft()+(ShadowViewGroup.SHADOW_WIDTH))), (0), DURATION);
							viewAtLeft.setAnimation(animViewLeft);
							animViewLeft.setAnimationListener(new AnimationListener() {
								public void onAnimationStart(Animation animation) {
								}
								public void onAnimationRepeat(Animation animation) {
								}
								public void onAnimationEnd(Animation animation) {
									viewAtLeft.setAnimation(viewBounceAnimation(viewAtLeft,0, -(viewAtLeft2.getRight() - viewAtLeft.getLeft()), 10));
									if(viewAtLeft2 != null)
										viewAtLeft2.setAnimation(viewBounceAnimation(viewAtLeft2,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 20));
								}
							});
							viewAtRightParams.leftMargin = this.getWidth();
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);		
							animViewRight = viewAnimation(viewAtRight, ((viewAtRight.getLeft()-this.getWidth())), (0), DURATION);
						}else{
							viewAtRightParams.leftMargin = viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH); ;//viewAtLeft.getSlideViewWidth()-40;
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);		
							animViewRight = viewAnimation(viewAtRight, -((viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH)) - (viewAtRight.getLeft()+(ShadowViewGroup.SHADOW_WIDTH))), (0), DURATION);
						}

						//TODO: need to set animation listener for bounce back



						animViewRight.setAnimationListener(new AnimationListener() {
							public void onAnimationStart(Animation animation) {
							}
							public void onAnimationRepeat(Animation animation) {
							}
							public void onAnimationEnd(Animation animation) {
								viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 10));
								viewAtLeft.setAnimation(viewBounceAnimation(viewAtLeft,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 10));
							}
						});
						viewAtRight.setAnimation(animViewRight);
						animViewRight = null;

					} else {
						System.out.println( "------->>>>--- RIGHT-WITH-LEFT ----------");

						if(getIndexOf(viewAtLeft) > 0) {
							if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
								System.out.println( "-----if up 11  ");
								//potrait
								viewAtLeftParams.leftMargin  = viewAtLeft2.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);// this.getWidth() - viewAtLeft.getSlideViewWidth();
								viewAtLeftParams.gravity = Gravity.LEFT;
							} else {			
								System.out.println( "--- else up11  ");
								// landscape
								viewAtLeftParams.leftMargin = viewAtLeft2.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
							}

							viewAtRightParams.leftMargin = (this.getWidth()-(ShadowViewGroup.SHADOW_WIDTH));
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);

							if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
								System.out.println( "--- if up33  ");
								animViewLeft = viewAnimation(viewAtLeft, -((viewAtRight.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)) - viewAtLeft.getLeft()),0, DURATION);
								animViewRight = viewAnimation(viewAtRight, -((viewAtLeft2.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)) - viewAtLeft.getLeft()),0,DURATION);
							}else{
								System.out.println( "--- else up33  "  );
								animViewLeft = viewAnimation(viewAtLeft, -(100  ),0, DURATION);
								animViewRight = viewAnimation(viewAtRight, 0,0 ,DURATION );
							}

							animViewLeft.setAnimationListener(new AnimationListener() {
								public void onAnimationStart(Animation animation) {
								}

								public void onAnimationRepeat(Animation animation) {
								}

								public void onAnimationEnd(Animation animation) {
									viewAtLeft.setAnimation(viewBounceAnimation(viewAtLeft,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 10));
									if(viewAtLeft2 != null){
										viewAtLeft2.setAnimation(viewBounceAnimation(viewAtLeft2,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 10));
									}
								}
							});

							viewAtLeft.clearAnimation();
							viewAtRight.clearAnimation();

							viewAtLeft.setAnimation(animViewLeft);
							viewAtRight.setAnimation(animViewRight);


						} else {
							viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
							viewAtLeftParams.gravity = Gravity.LEFT;
							viewAtLeft.setLayoutParams(viewAtLeftParams);
							viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);// w/o shadow
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);		
						}
						//TODO: need to set animation listener for bounce back						
					}

				}
			}
		}
	}













	class MyGestureDetector extends SimpleOnGestureListener {
		// It is necessary to return true from onDown for the onFling event to register
		@Override
		public boolean onDown(MotionEvent e) {
			if(viewAtLeft!= null){
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();			
				SLIDE_VIEWS_MINUS_X_POSITION = -viewAtLeft.getShadowViewWidth(); // since the margin of stack container have moved by SLIDE_VIEWS_MINUS_X_POSITION , if not set to zero shall again move viewAtLeft +SLIDE_VIEWS_MINUS_X_POSITION
				if (viewAtRight != null) {
					viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();
				}
			}
			return false;
		}



		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			/*
			 * calculate velocity
			 * 
			 * 
			 * System.out.println("++++++e1.getEventTime()+++++    "+e1.getEventTime());
			System.out.println("++++e1.getDownTime()+++++++    "+e1.getDownTime());
			System.out.println("+++++++e2.getEventTime()++++    "+e2.getEventTime());

			System.out.println("+++++++____----------------------------------------------++++    "+(e2.getEventTime()-e1.getEventTime()));
			if(((int) (e2.getEventTime()-e1.getEventTime()) < DURATION) && ((int) (e2.getEventTime()-e1.getEventTime()) > 200)){
				DURATION = (int) (e2.getEventTime()-e1.getEventTime());
			}else{
				DURATION = 250;
			}
			 */
			if(viewAtLeft.getAnimation() != null){
				viewAtLeft.clearAnimation();	

			}
			if (viewAtRight != null ){
				viewAtRight.clearAnimation();
			}

			if(viewAtRight2 != null && viewAtRight2.getAnimation() != null){
				viewAtRight2.clearAnimation();

			}

			if(viewAtLeft2 != null && viewAtLeft2.getAnimation() != null){
				viewAtLeft2.clearAnimation();
			}

			isScrolling = true;
			if(distanceY < 20 && distanceY > -20){
				if(e2.getAction() == MotionEvent.ACTION_MOVE){


					if (distanceX > 0) { // right to left swipe

						dragDirection = DIRECTION_LEFT;
						displacementPosition = (int) (distanceX * -1);
						System.out.println("------------------------<<<---------AAAAAAAAAAAA-------------L---------------------------------------   "+viewAtLeft.getLeft());
						if(viewAtRight != null){
							System.out.println("--------------------<<<-------------VVVVVVVVVVV-----------R-----------------------------------------   "+viewAtRight.getLeft());
							if (viewAtRight.getRight() <= getWidth()){	
								System.out.println("-----<<------1 ");
								if (getIndexOf(viewAtRight) < (getChildCount()-1)) {
									System.out.println("-----<<------1 if ");
									viewAtLeft2 = viewAtLeft;
									viewAtLeft = viewAtRight;
									viewAtRight = viewAtRight2;
									viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
									viewAtRightParams = (LayoutParams) viewAtRight.getLayoutParams();

									if(getChildAt(getIndexOf(viewAtLeft2) - 2) != null){
										getChildAt(getIndexOf(viewAtLeft2) - 2).setVisibility(View.GONE);
									}

									if (getIndexOf(viewAtRight) < (getChildCount()-1)) {
										System.out.println("-----<<------1 a if ");
										viewAtRight2 = (ShadowViewGroup) getChildAt(getIndexOf(viewAtRight) + 1);
										viewAtRight2.setVisibility(View.GONE);
										viewAtRight2Params = (LayoutParams) viewAtRight2.getLayoutParams();

									} else {
										viewAtRight2 = null;
									}


									if(viewAtLeft2 != null){
										System.out.println("-----<<------1 B if ");
										viewAtLeft2.setVisibility(View.VISIBLE);
										viewAtLeft2Params = (LayoutParams) viewAtLeft2.getLayoutParams();
										viewAtLeft2Params.leftMargin = -(ShadowViewGroup.SHADOW_WIDTH);
										viewAtLeft2Params.gravity = Gravity.LEFT;
										viewAtLeft2.setLayoutParams(viewAtLeft2Params);
									}

									if(viewAtRight != null){
										System.out.println("-----<<------1 C if ");
										viewAtRight.setVisibility(View.VISIBLE);
										viewAtRightParams = (LayoutParams) viewAtRight.getLayoutParams();
										viewAtRightParams.leftMargin = viewAtLeft.getLeft() ;
										viewAtRightParams.leftMargin = ((StackScrollView.this.getWidth())-(viewAtRight.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)));

										viewAtRightParams.gravity = Gravity.LEFT;  // included to run on 2.X versions
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

									if(viewAtRight2 != null){
										System.out.println("-----<<------1 D if ");
										viewAtRight2Params.leftMargin = viewAtRight.getLeft() + (viewAtRight.getSlideViewWidth()) ;
										viewAtRight2Params.gravity = Gravity.LEFT;
										viewAtRight2.setLayoutParams(viewAtRight2Params);
									}

									if (getIndexOf(viewAtLeft2) > 1) {
										getChildAt(getIndexOf(viewAtLeft2) - 2).setVisibility(View.GONE);
									}
								}
							}



							if ((viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION) && ((viewAtRight.getLeft() + viewAtRight.getSlideViewWidth()) > StackScrollView.this.getWidth())) {
								System.out.println("-------<<----2 ");
								if ((viewAtLeft.getLeft() + displacementPosition) + viewAtRight.getSlideViewWidth() <= getWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)) {
									System.out.println("----<<-------2 if");
									//at potrait
									viewAtRightParams.leftMargin = (int) (viewAtRight.getLeft() + displacementPosition);
									viewAtRight.setLayoutParams(viewAtRightParams);
								} else {
									System.out.println("----<<-------2 else");
									viewAtRightParams.leftMargin = (viewAtRight.getLeft() + displacementPosition);
									viewAtRight.setLayoutParams(viewAtRightParams);
								}

							} 
							else if ((getIndexOf(viewAtRight) == getChildCount()-1) && (viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION)) {		
								System.out.println("-----<<------3 ");
								if ((viewAtRight.getLeft() + displacementPosition) <= SLIDE_VIEWS_MINUS_X_POSITION) {
									if (viewAtRight.getLeft() > 0) {
										System.out.println("-----<<------3 if ");
										viewAtRightParams.leftMargin = (int) (viewAtRight.getLeft() + displacementPosition);
										viewAtRight.setLayoutParams(viewAtRightParams);
									}
									viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
									viewAtRight.setLayoutParams(viewAtRightParams);

								} else {
									System.out.println("-----<<------3 else");
									viewAtRightParams.leftMargin = viewAtRight.getLeft() + displacementPosition;
									viewAtRight.setLayoutParams(viewAtRightParams);
								}

							} else {
								System.out.println("-----<<------4 ");
								if ((viewAtLeft.getLeft() + displacementPosition) < SLIDE_VIEWS_MINUS_X_POSITION) {
									System.out.println("-----<<------4 if ");
									viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
									viewAtLeft.setLayoutParams(viewAtLeftParams);

									if (viewAtRight != null) {
										System.out.println("-----<<------4 if i iff ");
										viewAtRight.setVisibility(View.VISIBLE);
										viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH);//viewAtRight.getLeft() + displacementPosition;
										viewAtRight.setLayoutParams(viewAtRightParams);


									}

								} else {
									System.out.println("-------<<----4 else ");
									viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
									viewAtLeftParams.leftMargin = viewAtLeft.getLeft() + displacementPosition;
									viewAtLeft.setLayoutParams(viewAtLeftParams);
									if (viewAtRight != null) {
										if((viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH) ) <= StackScrollView.this.getWidth() ){
											System.out.println("-------<<----5 if ");
											viewAtRightParams.leftMargin = (viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH) )+ displacementPosition;
											viewAtRight.setLayoutParams(viewAtRightParams);
										}else{
											System.out.println("-------<<----5 else ");
											viewAtRightParams.leftMargin = viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH *2);
											viewAtRight.setLayoutParams(viewAtRightParams);
										}
									}
								}
							}

						} else {
							System.out.println("--------<<---6 if");
							viewAtLeftParams.leftMargin = viewAtLeft.getLeft() + displacementPosition;
							viewAtLeft.setLayoutParams(viewAtLeftParams);
						}

					} else if (distanceX < 0) { // left to right swipe
						dragDirection = DIRECTION_RIGHT;
						displacementPosition = (int) (distanceX);
						if (viewAtLeft != null) {
							System.out.println("--------------------->>>>>>-------AAAAAAAAAAAA-------------L--------   "+viewAtLeft.getLeft());
							System.out.println("--------------------->>>>>>-------AAAAAAAAAAAA-------------viewAtLeft.getRight()--------   "+viewAtLeft.getRight());
							if(viewAtRight != null){
								System.out.println("----------------->>>>>>-------VVVVVVVVVVV-----------R-----------   "+(viewAtRight.getLeft()));


								if (viewAtRight.getLeft() >= getWidth()) {
									System.out.println("-----_____________________________--------------------------------____SWAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAPPPPPPPPPPPPPPPPPPP          >> 1 " );
									if (getIndexOf(viewAtLeft) > 0) {
										viewAtRight2 = viewAtRight;
										viewAtRight = viewAtLeft;
										viewAtLeft = viewAtLeft2;		
										viewAtRight2.setVisibility(View.GONE);
										viewAtRight2Params = new LayoutParams(viewAtRight2.getSlideViewWidth(), LayoutParams.FILL_PARENT);
										viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
										viewAtRightParams = (LayoutParams) viewAtRight.getLayoutParams();


										if (getIndexOf(viewAtLeft) > 0) {
											viewAtLeft2 = (ShadowViewGroup) getChildAt(getIndexOf(viewAtLeft) - 1);
											viewAtLeft2.setVisibility(View.VISIBLE);
											viewAtLeft2Params = new LayoutParams(viewAtLeft2.getSlideViewWidth(), LayoutParams.FILL_PARENT); 
										} else {
											viewAtLeft2 = null;
										}

										if(getChildAt(getIndexOf(viewAtLeft2) - 2) != null){
											getChildAt(getIndexOf(viewAtLeft2) - 2).setVisibility(View.GONE);
										}

										if(viewAtRight2 != null){
											viewAtRight2Params.leftMargin = viewAtRight.getRight() ;
											viewAtRight2.setLayoutParams(viewAtRight2Params);
										}

										if(viewAtLeft2 != null){
											viewAtLeft2Params.leftMargin = -(ShadowViewGroup.SHADOW_WIDTH);//SLIDE_VIEWS_MINUS_X_POSITION;
											viewAtLeft2Params.gravity = Gravity.LEFT;
											viewAtLeft2.setLayoutParams(viewAtLeft2Params);
											viewAtLeft2.setVisibility(View.VISIBLE);
										}

										if(viewAtLeft != null){
											viewAtLeftParams.leftMargin = -(ShadowViewGroup.SHADOW_WIDTH);//SLIDE_VIEWS_MINUS_X_POSITION;
											viewAtLeftParams.gravity = Gravity.LEFT;
											viewAtLeft.setLayoutParams(viewAtLeftParams);
										}

									}
								}


								if((viewAtRight.getSlideViewLeft()   < (viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2))) && viewAtLeft.getSlideViewLeft() == SLIDE_VIEWS_MINUS_X_POSITION) {
									if ( (viewAtRight.getSlideViewLeft() - displacementPosition) >= (viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth() - (ShadowViewGroup.SHADOW_WIDTH *2))) {
										System.out.println("------>>------- 1 if");
										viewAtRightParams = (LayoutParams) viewAtRight.getLayoutParams();
										viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
										viewAtRight.setLayoutParams(viewAtRightParams);
									}else {
										System.out.println("------>>------- 1 else");
										viewAtRightParams.leftMargin = viewAtRight.getSlideViewLeft() - displacementPosition;
										viewAtRight.setLayoutParams(viewAtRightParams);

									}

								} else if (getIndexOf(viewAtLeft) == 0) {
									System.out.println("-------->>----- 2");
									if (viewAtRight == null) {
										System.out.println("------->>------ 2 if 1");
										viewAtLeftParams.leftMargin = viewAtLeft.getLeft() - displacementPosition;
										viewAtLeft.setLayoutParams(viewAtLeftParams);
									} else {
										System.out.println("------->>------ 2 else 1");
										viewAtRightParams.leftMargin = (viewAtRight.getLeft()) - displacementPosition;
										viewAtRight.setLayoutParams(viewAtRightParams);

										if (viewAtRight.getLeft() < (viewAtLeft.getRight() - (ShadowViewGroup.SHADOW_WIDTH *2))) {
											System.out.println("------>>--*************************************************----- 2 if 2 "+viewAtLeft.getRight());
											viewAtLeft.setLayoutParams(viewAtLeftParams);
										} else {
											System.out.println("----->>-------- 2 else 2 ");
											viewAtLeftParams.leftMargin = (viewAtLeft.getLeft()) - displacementPosition;
											viewAtLeft.setLayoutParams(viewAtLeftParams);
										}
									}

								} else {

									if ((viewAtRight.getLeft() - displacementPosition) >= getWidth()) {
										System.out.println("-------->>----if----- 3");
										viewAtRightParams.leftMargin = viewAtLeft.getRight();
										viewAtRight.setLayoutParams(viewAtRightParams);
									} else {
										System.out.println("------->>--else-  3");
										viewAtRightParams.leftMargin = viewAtRight.getLeft()  - displacementPosition;
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

									if ((viewAtLeft.getRight() - displacementPosition) >= getWidth()) {
										System.out.println("------->>-if--  4");
										viewAtLeftParams.leftMargin = getWidth() - (viewAtLeft.getSlideViewWidth());
										viewAtLeft.setLayoutParams(viewAtLeftParams);
									} else {
										System.out.println("------->>-else--  4"+viewAtLeft.getSlideViewWidth());
										viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
										viewAtLeftParams.leftMargin = viewAtRight.getLeft() - (viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH)) - displacementPosition;
										viewAtLeft.setLayoutParams(viewAtLeftParams);
									}
								}	

							} else {
								System.out.println("------->>-else--  5");
								viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
								viewAtLeftParams.leftMargin = viewAtLeft.getLeft() - displacementPosition;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
							}
						}
					}

				}
				return true;	
			}else{
				return false;
			}


		}
	}

	public int getStackScrollViewWidth(){
		return (getWidth() - viewAtLeft.getShadowViewWidth());
	}

	private int getIndexOf(View view) {
		int index = -1;
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			if (view == getChildAt(i)) {
				index = i;
				break;
			}
		}
		return index;
	}






	public synchronized void addViewInSlider(Fragment f, boolean isStackStartView) {
		int childCount;
		if (isStackStartView) {
			DataViewFragment.numberOfCardOnDeck = 0;
			/** Remove all views from the stack scroll view. */
			FragmentTransaction ft = f.getFragmentManager().beginTransaction();
			childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				FrameLayout frameLayout = (FrameLayout) getChildAt(i);				
				DataViewFragment detailsFragment = (DataViewFragment) f.getFragmentManager().findFragmentById(frameLayout.getId());
				if(detailsFragment != null)
					ft.remove(detailsFragment);
			}

			ft.commit();
			removeAllViews();
			viewControllersStack.clear();


		} else {
			if (DataViewFragment.numberOfCardOnDeck == (DataViewFragment.color.length-1)) {
				DataViewFragment.numberOfCardOnDeck = 0;
			} else {
				DataViewFragment.numberOfCardOnDeck++;
			}




			childCount = getChildCount();
			if (childCount > 0) {
				for (int i = 0; i < getChildCount(); i++) {
					FrameLayout frameLayout = (FrameLayout) getChildAt(i);
					ShadowViewGroup sh = (ShadowViewGroup) frameLayout;
					if (sh.getSlideViewId() == f.getId()) {
						removeViewAt(i);
						viewControllersStack.remove(i);
						if (childCount > i) {                                                                
							int itemsToRemove = (childCount - i) - 1;        
							int j = getChildCount()-1;
							for (int k = 0; k < itemsToRemove; k++) {
								removeViewAt(j);
								viewControllersStack.remove(j);                                                                
								j--;
							}
						}
						break;
					}
				}
			}
		}






		viewControllersStack.add(f);

		/*
		 * Inorder to check for different size left and right cards
		 * 
		 */
		/*if(getChildCount()  == 0 ){
		slideViewWidth = 300;
	}else{
		if(getChildCount() % 2 == 0){
			slideViewWidth = (this.getWidth()+40)-viewAtRight.getSlideViewWidth();
		}else{
			slideViewWidth = 1024-360;	//1280 for toshiba and 1024 for kindle

		}

	}*/


		ShadowViewGroup slideView = new ShadowViewGroup(getContext(),f.getId(),slideViewWidth);
		slideView.setTag(viewControllersStack.size() - 1);
		this.addView(slideView);
		slideView = null;
		childCount = getChildCount();
		System.out.println("-------childCount: " + childCount);
		if (childCount > 0) {
			if (childCount == 1) {
				viewAtLeft2 = null;
				viewAtLeft = (ShadowViewGroup) getChildAt(0);				
				viewAtRight = null;
				viewAtRight2 = null;

				FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				stackViewParams.leftMargin = 0;
				stackViewParams.gravity = Gravity.LEFT;
				stackViewParams.width = LayoutParams.FILL_PARENT;
				this.setLayoutParams(stackViewParams);
				SLIDE_VIEWS_START_X_POS = getStartX();
				viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS;
				viewAtLeft.setLayoutParams(viewAtLeftParams);
				viewAtLeft.getSlideViewWidth();

				/*Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setDuration(1000);
				this.setLayoutAnimation(new LayoutAnimationController(fadeIn));*/

			} else if (childCount == 2) {
				viewAtLeft2 = null;
				viewAtLeft = (ShadowViewGroup) getChildAt(0);
				viewAtRight = (ShadowViewGroup) getChildAt(1);
				viewAtRight2 = null;

				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();				
				SLIDE_VIEWS_START_X_POS = 0;

				if (this.getLeft() == 0) {
					FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
					SLIDE_VIEWS_MINUS_X_POSITION = (int) (startX * 0.3);
					stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION ;
					stackViewParams.gravity = Gravity.LEFT;
					stackViewParams.width = this.getWidth() - SLIDE_VIEWS_MINUS_X_POSITION;
					this.setLayoutParams(stackViewParams);
				}

				viewAtLeft.showShadow(true);


				viewAtLeftParams.leftMargin = -viewAtLeft.getShadowViewWidth();
				viewAtLeft.setLayoutParams(viewAtLeftParams);

				System.out.println("_____---------------------------_________________------------------___this.getWidth()_____________   "+this.getWidth());

				final TranslateAnimation animrightinIL = new TranslateAnimation(viewAtLeft.getLeft()-SLIDE_VIEWS_MINUS_X_POSITION,0,0,0);

				viewAtLeft.setAnimation(animrightinIL);

				TranslateAnimation animrightinIR = null ;

				viewAtRight.showShadow(true);
				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
				}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					viewAtRightParams.leftMargin = (this.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH))));	
					animrightinIR =  new TranslateAnimation(this.getWidth() ,0,0,0);
					animrightinIL.setDuration(300);
					animrightinIR.setDuration(300);		
				}


				viewAtRight.setLayoutParams(viewAtRightParams);	
				viewAtRight.setAnimation(animrightinIR);

				/*Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setDuration(1000);
				viewAtRight.startAnimation(fadeIn);*/

			} else {

				System.out.println("------------------++---------------------");
				viewAtLeft2 = (ShadowViewGroup) getChildAt(childCount - 3);				
				viewAtLeft = (ShadowViewGroup) getChildAt(childCount - 2);
				viewAtRight = (ShadowViewGroup) getChildAt(childCount - 1);

				viewAtRight2 = null;
				viewAtLeft2.setVisibility(View.VISIBLE);
				viewAtLeft2Params = (FrameLayout.LayoutParams) viewAtLeft2.getLayoutParams();
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();

				viewAtLeftParams.leftMargin = -viewAtLeft.getShadowViewWidth();
				viewAtLeft.setLayoutParams(viewAtLeftParams);
				//inorder to ensure tat the right card is fully diplayed when new right one is clicked

				viewAtRight.showShadow(true);
				final TranslateAnimation animrightinIL = new TranslateAnimation(viewAtLeft.getLeft()+(ShadowViewGroup.SHADOW_WIDTH),0,0,0);
				animrightinIL.setDuration(500);

				TranslateAnimation animrightinIR = null ;


				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					viewAtRightParams.leftMargin = (this.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH))));	
					animrightinIR =  new TranslateAnimation(this.getWidth() ,0,0,0);
					animrightinIR.setDuration(500);
				}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					viewAtRightParams.leftMargin  = viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2) ;
					animrightinIR =  new TranslateAnimation(StackScrollView.this.getWidth() - (viewAtLeft.getSlideViewWidth()) ,0,0,0);
					animrightinIR.setDuration(500);
				}
				viewAtRight.setLayoutParams(viewAtRightParams);

				if(viewAtLeft.getLeft() > 0){
					//there left2 sets to its position before the animation starts there for listener added  and left2 is set after completing animation
					{
						final TranslateAnimation animrightinLeft2 = new TranslateAnimation(viewAtLeft2.getLeft()+(ShadowViewGroup.SHADOW_WIDTH),0,0,0);
						animrightinLeft2.setDuration(500);
						viewAtLeft2Params.leftMargin = -(ShadowViewGroup.SHADOW_WIDTH) ;
						viewAtLeft2.showShadow(true);
						viewAtLeft2.setLayoutParams(viewAtLeft2Params);
						viewAtLeft2.setAnimation(animrightinLeft2);
					}
					viewAtRight.setAnimation(animrightinIR);
					viewAtLeft.setAnimation(animrightinIL);
				}else{
					Animation fadeIn = new AlphaAnimation(0f, 1);
					fadeIn.setDuration(900);
					viewAtRight.setAnimation(fadeIn);	
				}
				if (childCount > 3) {
					getChildAt(childCount - 4).setVisibility(View.GONE);
				}
			}
		}
	}



	public void setStartX(int startX) {

		this.startX = startX;
	}

	public  int getStartX() {
		return startX;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
			displayWidth = display.getWidth();
		}else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			displayWidth = display.getWidth();
		}
		setSmallRectangleProperties();
		deletePoint = (int) (display.getWidth() - (display.getWidth()*0.3));	

		int childCount = this.getChildCount();
		if (childCount > 0) {
			if (childCount == 1) {
				viewAtLeft2 = null;
				viewAtLeft = (ShadowViewGroup) getChildAt(0);				
				viewAtRight = null;
				viewAtRight2 = null;

				FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
				stackViewParams.leftMargin = 0;
				stackViewParams.width = displayWidth;
				stackViewParams.gravity = Gravity.LEFT;
				this.setLayoutParams(stackViewParams);
				SLIDE_VIEWS_START_X_POS = getStartX();
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS;
				viewAtLeftParams.gravity = Gravity.LEFT;
				viewAtLeft.setLayoutParams(viewAtLeftParams);

				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setDuration(1000);
				this.setLayoutAnimation(new LayoutAnimationController(fadeIn));

			} else if (childCount == 2) {
				viewAtLeft2 = null;
				viewAtLeft = (ShadowViewGroup) getChildAt(0);
				viewAtRight = (ShadowViewGroup) getChildAt(1);
				viewAtRight2 = null;
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();				
				SLIDE_VIEWS_START_X_POS = 0;
				FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
				SLIDE_VIEWS_MINUS_X_POSITION = (int) (startX * 0.3);
				stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
				stackViewParams.width = displayWidth - SLIDE_VIEWS_MINUS_X_POSITION;
				stackViewParams.gravity = Gravity.LEFT;
				this.setLayoutParams(stackViewParams);


				viewAtLeftParams.leftMargin = -(ShadowViewGroup.SHADOW_WIDTH);
				viewAtRightParams.gravity = Gravity.LEFT;
				viewAtLeft.setLayoutParams(viewAtLeftParams);

				viewAtRightParams.leftMargin = viewAtRight.getSlideViewWidth() - (viewAtLeft.getShadowViewWidth() + viewAtRight.getShadowViewWidth());
				viewAtRight.setLayoutParams(viewAtRightParams);	


				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setDuration(100);

				viewAtRight.startAnimation(fadeIn);

			} else {
				FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
				SLIDE_VIEWS_MINUS_X_POSITION = (int) (startX * 0.3);
				stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
				stackViewParams.width = displayWidth - SLIDE_VIEWS_MINUS_X_POSITION;
				stackViewParams.gravity = Gravity.LEFT;
				this.setLayoutParams(stackViewParams);

				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();

				if(viewAtLeft.getLeft() > startX){
					System.out.println("-=++++++++++++++@@@@@++++++");
					viewAtLeftParams.leftMargin = viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
					viewAtLeftParams.gravity = Gravity.LEFT;
					viewAtLeft.setLayoutParams(viewAtLeftParams);
					viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth())+(viewAtRight.getSlideViewWidth());
					viewAtRight.setLayoutParams(viewAtRightParams);	
					if(viewAtRight2 != null){
						viewAtRight2Params.leftMargin =(viewAtLeft.getSlideViewWidth())+(viewAtRight.getSlideViewWidth());;
						viewAtRight2.setLayoutParams(viewAtRight2Params);
					}

				}else if(viewAtLeft2 != null){
					System.out.println("-=++++++***********");
					if(viewAtLeft.getLeft() == viewAtLeft2.getRight() - (ShadowViewGroup.SHADOW_WIDTH)){
						System.out.println("-=++++++++++++++!!!!!!!!!!!++++++");
						viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()*2;
						viewAtRight.setLayoutParams(viewAtRightParams);	
					}else{
						viewAtLeftParams.leftMargin = -(ShadowViewGroup.SHADOW_WIDTH);
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);

						viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
						viewAtRight.setLayoutParams(viewAtRightParams);	

						if(viewAtRight2 != null){
							viewAtRight2Params.leftMargin = (viewAtLeft.getSlideViewWidth())+(viewAtRight.getSlideViewWidth());;
							viewAtRight2.setLayoutParams(viewAtRight2Params);
						}

					}
				} else {
					System.out.println("-=++++++++++++++33333333333333++++++");
					viewAtLeftParams.leftMargin = -(ShadowViewGroup.SHADOW_WIDTH);
					viewAtLeftParams.gravity = Gravity.LEFT;
					viewAtLeft.setLayoutParams(viewAtLeftParams);
					viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
					viewAtRight.setLayoutParams(viewAtRightParams);	

					if(viewAtRight2 != null){
						viewAtRight2Params.leftMargin = slideViewWidth*2;
						viewAtRight2.setLayoutParams(viewAtRight2Params);
					}

					if(viewAtRight2 != null){
						for (int i = getIndexOf(viewAtRight2); i < getChildCount(); i++) {
							LayoutParams params = (LayoutParams) getChildAt(i).getLayoutParams(); 		
							params.leftMargin = slideViewWidth*2;
							getChildAt(i).setLayoutParams(params);
						}
					}

				}

			}
		}
	}
}


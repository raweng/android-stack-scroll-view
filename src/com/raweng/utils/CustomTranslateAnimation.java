/*package com.raweng.stackscrollview;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.raweng.test.DataViewFragment;
import com.raweng.utils.CustomTranslateAnimation;



import java.util.ArrayList;

import org.apache.http.util.LangUtils;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LayoutAnimationController;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.raweng.test.DataViewFragment;
 



public class StackScrollView extends FrameLayout implements OrientationListener {	

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
	private static final int DURATION = 180;

	private static boolean isScrolling;
	private  int startX ;//= 240;
	private static int SLIDE_VIEWS_START_X_POS ;
	private static int SLIDE_VIEWS_MINUS_X_POSITION = 0;

	private static int displacementPosition = 0; 
	private static int slideViewWidth = 0;

	private Display display;
	private static int displayWidth;

	private int currentOrientation ;

	private static CustomTranslateAnimation anim ;


	public StackScrollView(Context context,int startX) {
		super(context);		
		gestureDetector = new GestureDetector(new MyGestureDetector());
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		SLIDE_VIEWS_MINUS_X_POSITION =  (int) (startX * 0.3);
		currentOrientation = context.getResources().getConfiguration().orientation;
		if(currentOrientation == Configuration.ORIENTATION_PORTRAIT){
			displayWidth = display.getHeight();// + 48;	
		}else{
			displayWidth = display.getWidth();
		}

		slideViewWidth = ((displayWidth-SLIDE_VIEWS_MINUS_X_POSITION)/ 2);

	}

	public StackScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public StackScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

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
				//	return true; //commented by rahul because this create problem in click after scrolling the stack 
			}


		}else if(event.getAction() == MotionEvent.ACTION_MOVE) {
		}

		return gestureDetector.onTouchEvent(event);	
	}





	private static Animation viewAnimation(int moveFromX, int moveToX, int velocity) {
			System.runFinalization();
		Runtime.getRuntime().gc();
		System.gc();
		//anim = new CustomTranslateAnimation(moveFromX, moveToX, 0, 0);
		
		anim = CustomTranslateAnimation.getInstance(moveFromX, moveToX, 0, 0);
		anim.setDuration(DURATION);
		anim.setInterpolator(new AccelerateInterpolator());
		return anim;
	}

	private static Animation viewBounceAnimation(final View view,int moveFromL, int moveFromR, final int moveTo) {
		TranslateAnimation viewBounceAnimation = new TranslateAnimation(0, moveTo, 0, 0);
		viewBounceAnimation.setInterpolator(new DecelerateInterpolator());
		viewBounceAnimation.setDuration(300);
		viewBounceAnimation.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				view.setAnimation(viewBounceBackAnimation(moveTo,0));


			}
		});
		return viewBounceAnimation;
	}

	private static Animation viewBounceBackAnimation(int moveFrom, int moveTo) {
		TranslateAnimation viewBounceAnimation = new TranslateAnimation(moveFrom, moveTo, 0, 0);
		viewBounceAnimation.setInterpolator(new DecelerateInterpolator());
		viewBounceAnimation.setDuration(300);
		return viewBounceAnimation;
	}


	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0,viewAtRight.getLeft() , -10));
				viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -10));
				viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0,viewAtRight.getLeft() , -10));
				viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -10));
			}

			super.handleMessage(msg);
		}


	};


	public void onUp(MotionEvent e) {
		Animation animViewLeft ;
		Animation animViewRight;
		FrameLayout.LayoutParams stackViewParams;




		if (dragDirection.equalsIgnoreCase(DIRECTION_LEFT)) {
			if (viewAtRight != null) {
				if (getIndexOf(viewAtLeft) == 0 && !(viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION || viewAtLeft.getLeft() == SLIDE_VIEWS_START_X_POS)) {
					//Drop Card View Animation
					//if ((getChildAt(0).getLeft() + 200) >= this.getLeft() + getChildAt(0).getLayoutParams().width) {
					if ((getChildAt(0).getLeft() + 75 ) >= SLIDE_VIEWS_START_X_POS + getChildAt(0).getLayoutParams().width) {
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
						SLIDE_VIEWS_START_X_POS = getStartX();
						viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);
						if (viewAtRight != null) {
							viewAtRightParams.leftMargin = SLIDE_VIEWS_START_X_POS + viewAtLeft.getSlideViewWidth() ;
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
						}


						animViewLeft = viewAnimation(( viewAtLeft.getLeft()),0, DURATION);
						viewAtLeft.clearAnimation();
						viewAtLeft.setAnimation(animViewLeft);								
						if (viewAtRight != null) {
							animViewRight = viewAnimation(-(SLIDE_VIEWS_START_X_POS - viewAtLeft.getLeft()), 0,DURATION );
							viewAtRight.clearAnimation();
							viewAtRight.setAnimation(animViewRight);
						}


					}else{

						System.out.println("---------------<<<<<<<< 1else------------ ");

						FrameLayout.LayoutParams stackViewParams1 = (LayoutParams)this.getLayoutParams();
						//here (getMENU_WIDTH() - stackViewParams1.leftMargin) gives the |0----a----1| distance between a to 1 tats 40 where distanc of 0 to 1 is menuwidth 
						//from that we calculate the margin for stackView 

						System.out.println("---------------------s----------------   "+(getStartX() - stackViewParams1.leftMargin));
						if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin) || viewAtLeft.getLeft() < (getStartX() - stackViewParams1.leftMargin)){
							System.out.println("-!@@--"+SLIDE_VIEWS_MINUS_X_POSITION);

							if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin)){
								System.out.println("-123--");

								stackViewParams = (LayoutParams)this.getLayoutParams();
								SLIDE_VIEWS_MINUS_X_POSITION = getStartX() -((getStartX() - stackViewParams1.leftMargin));
								stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
								stackViewParams.gravity = Gravity.LEFT;
								this.setLayoutParams(stackViewParams);
								viewAtLeftParams.leftMargin = getStartX() - stackViewParams1.leftMargin ;
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
								if (viewAtRight != null) {
									viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
									viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth()-40) + (getStartX() - stackViewParams1.leftMargin);
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
								viewAtLeftParams.leftMargin = -20 ;
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
								if (viewAtRight != null) {
									viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
									//viewAtRightParams.leftMargin = viewAtLeft.getWidth();
									viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth()-40);
									viewAtRightParams.gravity = Gravity.LEFT;
									viewAtRight.setLayoutParams(viewAtRightParams);	
									viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0,viewAtRight.getLeft() , -10));

								}

								if (viewAtRight2 != null) {
									viewAtRight2Params = (FrameLayout.LayoutParams) viewAtRight2.getLayoutParams();		
									viewAtRight2Params.leftMargin = this.getWidth()-20;
									viewAtRight2Params.gravity = Gravity.LEFT;
									viewAtRight2.setLayoutParams(viewAtRight2Params);
									viewAtRight2.setVisibility(View.VISIBLE);
									//	viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -10));
								}

							}

						}
					}

				} else if (viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION && (viewAtRight.getLeft() + viewAtRight.getSlideViewWidth()) > this.getWidth()) {
					System.out.println("///////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\     ");
					viewAtRightParams.leftMargin = this.getWidth() - viewAtRight.getSlideViewWidth();
					viewAtRightParams.gravity = Gravity.LEFT;
					viewAtRight.setLayoutParams(viewAtRightParams);
					animViewRight = viewAnimation(-(this.getWidth()-viewAtRight.getRight()), 0, DURATION);
					viewAtRight.clearAnimation();
					viewAtRight.setAnimation(animViewRight);
					//animViewRight = null;

				} else if (viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION && (viewAtRight.getLeft() + viewAtRight.getSlideViewWidth()) < this.getWidth()) {
					System.out.println("---------- RIGHT-WITH-RIGHT ----------");
					viewAtRightParams.leftMargin = this.getWidth() - viewAtRight.getSlideViewWidth();
					viewAtRightParams.gravity = Gravity.LEFT;
					viewAtRight.setLayoutParams(viewAtRightParams);
					//TODO: need to set animation listener for bounce back
					//Animation animViewRight = viewAnimation(viewAtRight, -(viewAtLeft.getRight() - viewAtRight.getLeft()), (SLIDE_VIEWS_MINUS_X_POSITION), DURATION);
					//Animation animViewRight = viewAnimation(viewAtRight, -(viewAtLeft.getLeft()), (SLIDE_VIEWS_MINUS_X_POSITION), DURATION);

					animViewRight = viewAnimation( -(this.getWidth()-viewAtRight.getRight()), 0, DURATION);
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
					//animViewRight = null;

				} else if (viewAtLeft.getLeft() > SLIDE_VIEWS_MINUS_X_POSITION) {

					//if ((viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth() > this.getWidth()) && viewAtLeft.getLeft() < (this.getWidth() - (viewAtLeft.getSlideViewWidth())/2)) {
					if ((viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth() > this.getWidth()) && viewAtLeft.getLeft() > ((this.getWidth()-viewAtLeft.getSlideViewWidth()))) {
						System.out.println("-----<<- LEFT-WITH-LEFT ---------- ");
						viewAtLeftParams.leftMargin = this.getWidth() - (viewAtLeft.getSlideViewWidth());
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);
						//Show bounce effect
						//TODO: need to set animation listener for bounce back
						viewAtRightParams.leftMargin = viewAtRight.getSlideViewWidth()+20;
						viewAtRightParams.gravity = Gravity.LEFT;
						viewAtRight.setLayoutParams(viewAtRightParams);	
						//left -10;
						//right -20;

						animViewLeft = viewAnimation((viewAtLeft.getLeft()-20)+displacementPosition,-20, 500);
						animViewRight = viewAnimation(viewAtRight.getLeft(), -20, 500);

						viewAtLeft.clearAnimation();
						viewAtRight.clearAnimation();

						viewAtLeft.setAnimation(animViewLeft);
						animViewRight.setAnimationListener(new AnimationListener() {
							public void onAnimationStart(Animation animation) {
							}

							public void onAnimationRepeat(Animation animation) {
							}

							public void onAnimationEnd(Animation animation) {
								viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), -20));
								viewAtLeft.setAnimation(viewBounceAnimation(viewAtLeft,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), -10));
							}
						});
						viewAtRight.setAnimation(animViewRight);

						//animViewLeft = null;
						//animViewRight = null;


					} else {
						System.out.println("------<<---- LEFT-WITH-RIGHT ----------");
						viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);
						if (viewAtLeft.getLeft() + (viewAtLeft.getSlideViewWidth()- 40) <= this.getWidth()) {
							viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth() - 40;//this.getWidth() - viewAtRight.getSlideViewWidth();
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);

							animViewLeft = viewAnimation( (viewAtLeft.getLeft()),0, DURATION);
							animViewRight = viewAnimation( ((viewAtRight.getLeft()-20)-viewAtRightParams.leftMargin), (0), DURATION);

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

										viewAtRight2Params.leftMargin = (viewAtRight.getRight()-20);
										viewAtRight2Params.gravity = Gravity.LEFT;
										viewAtRight2.setLayoutParams(viewAtRight2Params);

											Message msg = new Message();
										msg.what = 0;
										handler.sendMessage(msg);
										viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0,viewAtRight.getLeft() , -10));
										viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -10));
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
							viewAtRight2Params.leftMargin = this.getWidth() ;//- viewAtRight2Params.width;
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight2.setLayoutParams(viewAtRight2Params);

						}
					}
				}

			} else {
				viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS;
				viewAtLeftParams.gravity = Gravity.LEFT;
				viewAtLeft.setLayoutParams(viewAtLeftParams);

				animViewLeft = viewAnimation( -(SLIDE_VIEWS_START_X_POS-viewAtLeft.getLeft()),0, DURATION);
				viewAtLeft.clearAnimation();
				viewAtLeft.setAnimation(animViewLeft);
			//	animViewLeft = null;

			}

		} else if (dragDirection.equalsIgnoreCase(DIRECTION_RIGHT)) {
			if (viewAtLeft != null) {
				if (getIndexOf(viewAtLeft) == 0 && !(viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION || viewAtLeft.getLeft() == SLIDE_VIEWS_START_X_POS)) {
					System.out.println( "------->>>>--- ss ----sssss----ss--");
					if (viewAtLeft.getLeft() > SLIDE_VIEWS_MINUS_X_POSITION || viewAtRight == null) {
						//Drop Card View Animation
						//if ((getChildAt(0).getLeft() + 200) >= this.getLeft() + getChildAt(0).getLayoutParams().width) {
						if ((getChildAt(0).getLeft() + 75 ) >= SLIDE_VIEWS_START_X_POS + getChildAt(0).getLayoutParams().width) {
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
							System.out.println("==========viewControllersStack.size(): " + viewControllersStack.size());

							viewAtLeft2 = null;
							viewAtRight = null;
							viewAtRight2 = null;

							stackViewParams = (LayoutParams)this.getLayoutParams();
							stackViewParams.leftMargin = 0;
							stackViewParams.width = LayoutParams.FILL_PARENT;
							stackViewParams.gravity = Gravity.LEFT;
							this.setLayoutParams(stackViewParams);
							SLIDE_VIEWS_START_X_POS = getStartX();



							viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS;
							viewAtLeftParams.gravity = Gravity.LEFT;
							viewAtLeft.setLayoutParams(viewAtLeftParams);

							if (viewAtRight != null) {
								viewAtRightParams.leftMargin = SLIDE_VIEWS_START_X_POS + viewAtLeft.getSlideViewWidth();
								viewAtRightParams.gravity = Gravity.LEFT;
								viewAtRight.setLayoutParams(viewAtRightParams);
							}
							animViewLeft = viewAnimation( -(SLIDE_VIEWS_START_X_POS-viewAtLeft.getLeft()),0, DURATION);
							viewAtLeft.clearAnimation();
							viewAtLeft.setAnimation(animViewLeft);								

						//	animViewLeft = null;


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
									viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
									viewAtLeftParams.leftMargin = (getStartX() - stackViewParams1.leftMargin);
									viewAtLeftParams.gravity = Gravity.LEFT;
									viewAtLeft.setLayoutParams(viewAtLeftParams);
									if (viewAtRight != null) {
										viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
										//viewAtRightParams.leftMargin = viewAtLeft.getWidth() + (getStartX() - stackViewParams1.leftMargin); w/o shadow
										viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth()-40) + (getStartX() - stackViewParams1.leftMargin); 
										viewAtRightParams.gravity = Gravity.LEFT;
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

									animViewLeft = viewAnimation( -(SLIDE_VIEWS_START_X_POS - viewAtLeft.getLeft()),0, DURATION);
									viewAtLeft.clearAnimation();
									viewAtLeft.setAnimation(animViewLeft);								
									//animViewLeft = null;

									if (viewAtRight != null) {
										animViewRight = viewAnimation( -(SLIDE_VIEWS_START_X_POS - viewAtLeft.getLeft()), 0,DURATION );
										viewAtRight.clearAnimation();
										viewAtRight.setAnimation(animViewRight);
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
									viewAtLeftParams.leftMargin = (getStartX() - stackViewParams1.leftMargin);
									viewAtLeftParams.gravity = Gravity.LEFT;
									viewAtLeft.setLayoutParams(viewAtLeftParams);

									if (viewAtRight != null) {
										viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
										//		viewAtRightParams.leftMargin = viewAtLeft.getWidth() + (getStartX() - stackViewParams1.leftMargin); w/o shadow
										viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth() -40) + (getStartX() - stackViewParams1.leftMargin); 
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
					if((viewAtRight.getLeft() < ((viewAtLeft.getLeft()) + viewAtLeft.getSlideViewWidth()-40)) && ((viewAtRight.getLeft() < (this.getWidth() - ((viewAtRight.getSlideViewWidth()-40)/2))))){
						System.out.println("----------------->>>>----RIGHT WITH RIGHT---------------");
						viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()-40;
						viewAtRightParams.gravity = Gravity.LEFT;
						viewAtRight.setLayoutParams(viewAtRightParams);		
						//TODO: need to set animation listener for bounce back

						//Animation animViewRight = viewAnimation(viewAtRight, -((viewAtLeft.getRight() - viewAtRight.getLeft())-40), (0), DURATION);
						animViewRight = viewAnimation( -((viewAtLeft.getRight()-20) - (viewAtRight.getLeft()+20)), (0), DURATION);
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
					//	animViewRight = null;

					} else {
						System.out.println( "------->>>>--- RIGHT-WITH-LEFT ----------");
						if(getIndexOf(viewAtLeft) > 0) {
							//if (viewAtRight.getLeft()  + viewAtRightParams.width <= this.getWidth()) {  
							//if (viewAtRight.getLeft()  + viewAtRight.getSlideViewWidth()-40   <= (viewAtRight.getSlideViewWidth()-40)*2) {
							if (currentOrientation== Configuration.ORIENTATION_PORTRAIT){
								System.out.println( "-----if up 11  ");
								//potrait
								//	viewAtLeftParams.leftMargin = this.getWidth() - viewAtLeft.getSlideViewWidth();
								viewAtLeftParams.leftMargin  = viewAtLeft2.getSlideViewWidth()-40;// this.getWidth() - viewAtLeft.getSlideViewWidth();
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
							} else {			
								System.out.println( "--- else up11  ");
								// landscape
								//viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft2Params.width;
								viewAtLeftParams.leftMargin = viewAtLeft2.getSlideViewWidth()-40;
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);

							}

							viewAtRightParams.leftMargin = (this.getWidth()-20);
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
							//Animation animViewLeft = viewAnimation(viewAtLeft, -((viewAtLeft.getSlideViewWidth()) - viewAtLeft.getLeft()),0, 5000);

							if(currentOrientation == Configuration.ORIENTATION_LANDSCAPE){
								System.out.println( "--- if up33  ");
								animViewLeft = viewAnimation( -((viewAtLeft.getSlideViewWidth()-40) - viewAtLeft.getLeft()),-20, DURATION);
								animViewRight = viewAnimation( -((viewAtLeft2.getSlideViewWidth()-40) - viewAtLeft.getLeft()+20),-20,DURATION);
							}else
							{
								System.out.println( "--- else up33  "  );
								//animViewRight = viewAnimation(viewAtRight, -(this.getWidth()- viewAtRight.getLeft()),viewAtLeft.getSlideViewWidth()-40 ,DURATION );
								animViewLeft = viewAnimation( -(100  ),0, DURATION);
								animViewRight = viewAnimation( 0,0 ,DURATION );
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

							//animViewLeft = null;
							//animViewRight = null;


						} else {
							System.out.println( "--- ***************************************  "  );
							viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
							viewAtLeftParams.gravity = Gravity.LEFT;
							viewAtLeft.setLayoutParams(viewAtLeftParams);

							viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft.getSlideViewWidth()-40;// w/o shadow
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
			viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();			
			SLIDE_VIEWS_MINUS_X_POSITION = -viewAtLeft.getShadowViewWidth(); // since the margin of stack container have moved by SLIDE_VIEWS_MINUS_X_POSITION , if not set to zero shall again move viewAtLeft +SLIDE_VIEWS_MINUS_X_POSITION
			if (viewAtRight != null) {
				viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();
			}
			//viewAtLeft2Params = new FrameLayout.LayoutParams(slideViewWidth, ViewGroup.LayoutParams.FILL_PARENT);

			return false;
		}



		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

			isScrolling = true;
			if(distanceY < 10 && distanceY > -10){
				if(e2.getAction() == MotionEvent.ACTION_MOVE){
					if (distanceX > 0) { // right to left swipe
						dragDirection = DIRECTION_LEFT;
						displacementPosition = (int) (distanceX * -1);
						System.out.println("------------------------<<<---------AAAAAAAAAAAA-------------L---------------------------------------   "+viewAtLeft.getLeft());
						if(viewAtRight != null){
							System.out.println("--------------------<<<-------------VVVVVVVVVVV-----------R-----------------------------------------   "+viewAtRight.getLeft());
							if (viewAtLeft.getLeft() <= SLIDE_VIEWS_MINUS_X_POSITION) {
								System.out.println("-----<<------1 ");
								if (getIndexOf(viewAtRight) < (getChildCount()-1)) {
									System.out.println("-----<<------1 if ");
									viewAtLeft2 = viewAtLeft;
									viewAtLeft = viewAtRight;
									viewAtRight = viewAtRight2;
									if (getIndexOf(viewAtRight) < (getChildCount()-1)) {
										System.out.println("-----<<------1 a if ");
										viewAtRight2 = (ShadowViewGroup) getChildAt(getIndexOf(viewAtRight) + 1);
										viewAtRight2.setVisibility(View.GONE);
										//viewAtRight2Params = (LayoutParams) viewAtRight2.getLayoutParams();
										viewAtRight2Params = new LayoutParams(viewAtLeft.getSlideViewWidth(), LayoutParams.FILL_PARENT);
									} else {
										viewAtRight2 = null;
									}


									if(viewAtLeft2 != null){
										System.out.println("-----<<------1 B if ");
										viewAtLeft2Params.leftMargin = -20;//SLIDE_VIEWS_MINUS_X_POSITION;
										viewAtLeft2Params.gravity = Gravity.LEFT;
										viewAtLeft2.setLayoutParams(viewAtLeft2Params);
									}

									if(viewAtRight != null){
										System.out.println("-----<<------1 C if ");
										viewAtRight.setVisibility(View.VISIBLE);
										viewAtRightParams = (LayoutParams) viewAtRight.getLayoutParams();
										//viewAtRightParams.leftMargin = viewAtLeft.getRight() ;
										viewAtRightParams.leftMargin = viewAtLeft.getLeft() ;
										viewAtRightParams.leftMargin = ((StackScrollView.this.getWidth())-(viewAtRight.getSlideViewWidth()-40));

										viewAtRightParams.gravity = Gravity.LEFT;  // included to run on 2.X versions
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

									if(viewAtRight2 != null){
										System.out.println("-----<<------1 D if ");
										//	viewAtRight2.setVisibility(View.VISIBLE);
										viewAtRight2Params.leftMargin = viewAtRight.getLeft() + (viewAtRight.getSlideViewWidth()) ;
										viewAtRight2Params.gravity = Gravity.LEFT;
										viewAtRight2.setLayoutParams(viewAtRight2Params);
										//viewAtRight2.setAnimation(viewBounceAnimation(0,viewAtRight2.getLeft() , -20));
									}

									if (getIndexOf(viewAtLeft2) > 1) {
										//getChildAt(getIndexOf(viewAtLeft2) - 2).setVisibility(View.GONE);
									}
								}
							}



							if ((viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION) && ((viewAtRight.getLeft() + viewAtRight.getSlideViewWidth()) > StackScrollView.this.getWidth())) {
								System.out.println("-------<<----2 ");
								if ((viewAtLeft.getLeft() + displacementPosition) + viewAtRight.getSlideViewWidth() <= getWidth()-40) {
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
							else if ((getIndexOf(viewAtRight) == getChildCount()-1) && viewAtRight.getLeft() <= (StackScrollView.this.getWidth()-40) - viewAtRight.getSlideViewWidth()) {
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
								FrameLayout.LayoutParams stackViewParams = (LayoutParams)StackScrollView.this.getLayoutParams();
								System.out.println("---------------------stackViewParams.leftMargin------------------------------ "+stackViewParams.leftMargin);
								if(stackViewParams.leftMargin == 0){
								stackViewParams.leftMargin = 200;
								StackScrollView.this.setLayoutParams(stackViewParams);
								}

								if ((viewAtLeft.getLeft() + displacementPosition) <= SLIDE_VIEWS_MINUS_X_POSITION) {
									System.out.println("-----<<------4 if ");

									viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
									viewAtLeft.setLayoutParams(viewAtLeftParams);

									if (viewAtRight != null) {
										System.out.println("-----<<------4 if i iff ");
										viewAtRight.setVisibility(View.VISIBLE);
										//viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth(); 
										viewAtRightParams.leftMargin = viewAtRight.getLeft() + displacementPosition;
										viewAtRight.setLayoutParams(viewAtRightParams);




									}

								} else {
									System.out.println("-------<<----4 else ");
									viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
									viewAtLeftParams.leftMargin = viewAtLeft.getLeft() + displacementPosition;
									viewAtLeft.setLayoutParams(viewAtLeftParams);
									if (viewAtRight != null) {
										if((viewAtLeft.getRight() ) <= StackScrollView.this.getWidth() ){
											System.out.println("-------<<----5 if ");
											//viewAtRightParams.leftMargin = (viewAtLeft.getLeft()+(viewAtLeft.getSlideViewWidth()-40))  + displacementPosition;
											viewAtRightParams.leftMargin = (viewAtLeft.getLeft()-20)+(viewAtLeft.getSlideViewWidth())  + displacementPosition;
											viewAtRight.setLayoutParams(viewAtRightParams);
										}else{
											System.out.println("-------<<----5 else ");
											viewAtRightParams.leftMargin = viewAtLeft.getRight()-40;//StackScrollView.this.getWidth();
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


								if (viewAtLeft.getRight() >= getWidth()) {
									System.out.println("-----***************************************************************                >> 1 " );
									if (getIndexOf(viewAtLeft) > 0) {
										viewAtRight2 = viewAtRight;
										viewAtRight = viewAtLeft;
										viewAtLeft = viewAtLeft2;		
										viewAtRight2.setVisibility(View.GONE);
										//	viewAtRight2Params = (LayoutParams) viewAtRight2.getLayoutParams();
										viewAtRight2Params = new LayoutParams(viewAtLeft.getSlideViewWidth(), LayoutParams.FILL_PARENT);
										if (getIndexOf(viewAtLeft) > 0) {
											viewAtLeft2 = (ShadowViewGroup) getChildAt(getIndexOf(viewAtLeft) - 1);
											viewAtLeft2.setVisibility(View.VISIBLE);
										} else {
											viewAtLeft2 = null;
										}

										if(viewAtRight2 != null){
											//	viewAtRight2.setVisibility(View.VISIBLE);
											//viewAtRight2Params.leftMargin = viewAtRight.getLeft() + viewAtRight.getSlideViewWidth() ;
											//viewAtRight2Params.leftMargin = viewAtRight.getLeft() + viewAtRight.getSlideViewWidth() ;
											viewAtRight2Params.leftMargin = viewAtRight.getRight() ;
											viewAtRight2.setLayoutParams(viewAtRight2Params);
											//viewAtRight2.setAnimation(viewBounceAnimation(0,viewAtRight2.getLeft() , -20));
										}
									}
								}


								//if((viewAtRight.getLeft() < (viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth())) && viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION) {
								if((viewAtRight.getSlideViewLeft()   < (viewAtLeft.getSlideViewWidth()-40)) && viewAtLeft.getSlideViewLeft() == SLIDE_VIEWS_MINUS_X_POSITION) {

									System.out.println("-----***************************************************************                >> 1 " );
									if ( (viewAtRight.getSlideViewLeft() - displacementPosition) >= (viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth() - 40)) {
										System.out.println("------>>------- 1 if");
										viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()-40;
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
										viewAtRightParams.leftMargin = viewAtRight.getLeft() - displacementPosition;
										viewAtRight.setLayoutParams(viewAtRightParams);

										//if (viewAtRight.getLeft() - viewAtLeft.getSlideViewWidth() < SLIDE_VIEWS_MINUS_X_POSITION) { w/o shadow to ensure tat the left card is placed in
										//its right place
										if (viewAtRight.getLeft() < (viewAtLeft.getRight() - 40)) {

											System.out.println("------>>--*************************************************----- 2 if 2");
											//	viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
											viewAtLeft.setLayoutParams(viewAtLeftParams);
										} else {
											System.out.println("----->>-------- 2 else 2 ");
											viewAtLeftParams.leftMargin = viewAtLeft.getLeft() - displacementPosition;
											viewAtLeft.setLayoutParams(viewAtLeftParams);
										}
									}

								} else {
									if ((viewAtRight.getLeft() - displacementPosition) >= getWidth()) {
										System.out.println("-------->>----if----- 3");
										viewAtRightParams.leftMargin = viewAtLeft.getRight();//getWidth();
										viewAtRight.setLayoutParams(viewAtRightParams);
									} else {
										System.out.println("------->>--else-  3");
										viewAtRightParams.leftMargin = viewAtRight.getLeft() - displacementPosition;
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

									if ((viewAtLeft.getRight() - displacementPosition) >= getWidth()) {
										System.out.println("------->>-if--  4");
										viewAtLeftParams.leftMargin = getWidth() - (viewAtLeft.getSlideViewWidth());
										viewAtLeft.setLayoutParams(viewAtLeftParams);
									} else {
										System.out.println("------->>-else--  4");
										viewAtLeftParams.leftMargin = viewAtRight.getLeft() - (viewAtLeft.getSlideViewWidth()-20) - displacementPosition;
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


			*//** Remove all views from the stack scroll view. *//*
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
				for (int i = 0; i < childCount; i++) {
					FrameLayout frameLayout = (FrameLayout) getChildAt(i);
					if (frameLayout.getId() == f.getId()) {
						System.out.println();
						removeViewAt(i);
						viewControllersStack.remove(i);
						break;
					}
				}
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

		ShadowViewGroup slideView = new ShadowViewGroup(getContext(),f.getId(),slideViewWidth);
		//FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(slideViewWidth, ViewGroup.LayoutParams.FILL_PARENT);
		//lp.gravity = Gravity.LEFT ;//| Gravity.TOP;
		//slideView.setLayoutParams(lp);
		//slideView.setLayoutParams(new FrameLayout.LayoutParams(slideViewWidth, ViewGroup.LayoutParams.FILL_PARENT));
		//slideView.setId(f.getId());
		slideView.setTag(viewControllersStack.size() - 1);

		this.addView(slideView);
		slideView = null;

		System.out.println("______________________________________________________  THIS WIDTH ");

		childCount = getChildCount();
		System.out.println("-------childCount: " + childCount);
		if (childCount > 0) {
			if (childCount == 1) {
				viewAtLeft2 = null;
				viewAtLeft = (ShadowViewGroup) getChildAt(0);				
				viewAtRight = null;
				viewAtRight2 = null;

				FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
				stackViewParams.leftMargin = 0;
				stackViewParams.gravity = Gravity.LEFT;
				stackViewParams.width = LayoutParams.FILL_PARENT;
				this.setLayoutParams(stackViewParams);
				SLIDE_VIEWS_START_X_POS = getStartX();
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS;
				viewAtLeft.setLayoutParams(viewAtLeftParams);
				viewAtLeft.getSlideViewWidth();

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

				if (this.getLeft() == 0) {
					FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
					SLIDE_VIEWS_MINUS_X_POSITION = (int) (startX * 0.3);// display.getWidth() -(slideViewWidth*2) ; //(getWidth() - viewAtRight.getSlideViewWidth() - viewAtLeft.getSlideViewWidth());
					stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION ;
					stackViewParams.gravity = Gravity.LEFT;
					stackViewParams.width = this.getWidth() - SLIDE_VIEWS_MINUS_X_POSITION;
					this.setLayoutParams(stackViewParams);
				}

				viewAtLeft.showShadow(true);

				viewAtLeftParams.leftMargin = -viewAtLeft.getShadowViewWidth();
				viewAtLeft.setLayoutParams(viewAtLeftParams);

				//viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth();
				viewAtRight.showShadow(true);
				viewAtRightParams.leftMargin = viewAtRight.getSlideViewWidth() - (viewAtLeft.getShadowViewWidth() + viewAtRight.getShadowViewWidth());
				viewAtRight.setLayoutParams(viewAtRightParams);	



				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setDuration(1000);
				viewAtRight.startAnimation(fadeIn);
				 
			} else {
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
				//viewAtRightParams.leftMargin = this.getWidth() - viewAtLeft.getSlideViewWidth() - viewAtRight.getShadowViewWidth();
				//viewAtRightParams.leftMargin = viewAtRight.getSlideViewWidth() - (viewAtLeft.getShadowViewWidth() + viewAtRight.getShadowViewWidth());
				viewAtRightParams.leftMargin =  ((this.getWidth())-viewAtRight.getSlideViewWidth());

				viewAtRight.setLayoutParams(viewAtRightParams);


				final TranslateAnimation animrightinIL = new TranslateAnimation(viewAtLeft.getLeft()+20,0,0,0);
				animrightinIL.setDuration(DURATION);


				if(viewAtLeft.getLeft() > 0){
					TranslateAnimation animrightinIR = null ;

					if(viewAtLeft.getRight() == ((viewAtLeft.getLeft() + viewAtRight.getSlideViewWidth()))){
						System.out.println("+++++++++++++++++++++++++++++++++++++  1111111");
						animrightinIR = new TranslateAnimation(viewAtLeft.getSlideViewWidth()-20,0,0,0);
					}else{
						System.out.println("+++++++++++++++++++++++++++++++++++++  2222222");

						//animrightinIR = new TranslateAnimation(viewAtLeft.getSlideViewWidth() - ((viewAtLeft.getLeft() + viewAtRight.getSlideViewWidth()) - viewAtLeft.getRight()),0,0,0);
						animrightinIR = new TranslateAnimation(StackScrollView.this.getWidth() - (viewAtLeft.getSlideViewWidth()) ,0,0,0);
					}
					animrightinIR.setDuration(DURATION);
					//ther left2 sets to its posiotion before the animation starts there for listenr added  and left2 is set after compliting animation
					animrightinIR.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
						}

						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
						}

						public void onAnimationEnd(Animation animation) {
							System.out.println("-------------------------------------   "+SLIDE_VIEWS_MINUS_X_POSITION);
							viewAtLeft2Params.leftMargin = -20 ;
							viewAtLeft2.showShadow(true);
							viewAtLeft2.setLayoutParams(viewAtLeft2Params);
						}
					});
					viewAtRight.setAnimation(animrightinIR);
					viewAtLeft.setAnimation(animrightinIL);
				}else{
					Animation fadeIn = new AlphaAnimation(0f, 1);
					fadeIn.setDuration(900);
					viewAtRight.setAnimation(fadeIn);	
				}
				if (childCount > 3) {
					//getChildAt(childCount - 4).setVisibility(View.GONE);
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


				viewAtLeftParams.leftMargin = -20;
				viewAtRightParams.gravity = Gravity.LEFT;
				viewAtLeft.setLayoutParams(viewAtLeftParams);

				viewAtRightParams.leftMargin = viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth();
				viewAtRightParams.gravity = Gravity.LEFT;
				viewAtRight.setLayoutParams(viewAtRightParams);	

				viewAtRightParams.leftMargin = viewAtRight.getSlideViewWidth() - (viewAtLeft.getShadowViewWidth() + viewAtRight.getShadowViewWidth());
				viewAtRight.setLayoutParams(viewAtRightParams);	


				Animation fadeIn = new AlphaAnimation(0, 1);
				fadeIn.setDuration(100);
				viewAtRight.startAnimation(fadeIn);

			} else {
				System.out.println("-=++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
				FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
				SLIDE_VIEWS_MINUS_X_POSITION = (int) (startX * 0.3);

				stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
				stackViewParams.width = displayWidth - SLIDE_VIEWS_MINUS_X_POSITION;
				stackViewParams.gravity = Gravity.LEFT;
				this.setLayoutParams(stackViewParams);
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();

				viewAtLeftParams.leftMargin = -20;
				viewAtLeftParams.gravity = Gravity.LEFT;
				viewAtLeft.setLayoutParams(viewAtLeftParams);
				viewAtRightParams.leftMargin = viewAtLeft.getRight()-20;
				viewAtRight.setLayoutParams(viewAtRightParams);
				//viewAtRightParams.leftMargin = this.getWidth() - viewAtLeft.getSlideViewWidth();


				//viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth();

				// this makes the rightCard to position itsself at rightSide of leftCard
				//viewAtRightParams.leftMargin = viewAtLeft.getLeft()+viewAtLeft.getSlideViewWidth();

				viewAtRightParams.leftMargin = ((this.getWidth())-viewAtRight.getSlideViewWidth());
				viewAtRightParams.gravity = Gravity.LEFT;
				viewAtRight.setLayoutParams(viewAtRightParams);


				if(viewAtLeft2 != null){
					viewAtLeft2Params = (LayoutParams) viewAtLeft2.getLayoutParams();
					viewAtLeft2Params.leftMargin = -20;
					viewAtLeft2Params.gravity = Gravity.LEFT;
					viewAtLeft2.setLayoutParams(viewAtLeft2Params);

				}
				if (viewAtRight2 != null) {
					viewAtRight2Params = new LayoutParams(viewAtLeft.getSlideViewWidth(), LayoutParams.FILL_PARENT);
					viewAtRight2Params.leftMargin = 0; 
					viewAtRight2.setLayoutParams(viewAtRightParams);
					viewAtRight2.setVisibility(View.GONE);

				}

			}
		}
	}
}
*/
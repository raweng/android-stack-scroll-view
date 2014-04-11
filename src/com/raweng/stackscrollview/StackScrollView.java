package com.raweng.stackscrollview;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Build;
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
import com.raweng.utils.AppConstants;





public class StackScrollView extends FrameLayout {

	
	public  ArrayList<Fragment> viewControllersStack = new ArrayList<Fragment>();
	private  ShadowViewGroup viewAtLeft;
	private  ShadowViewGroup viewAtRight;
	private  ShadowViewGroup viewAtLeft2;
	private  ShadowViewGroup viewAtRight2;
	private  LayoutParams viewAtLeftParams;
	private  LayoutParams viewAtRightParams; 
	private  LayoutParams viewAtLeft2Params;
	private  LayoutParams viewAtRight2Params;
	private  GestureDetector gestureDetector;
	private  String dragDirection = "";
	private  final String DIRECTION_RIGHT = "RIGHT";
	private  final String DIRECTION_LEFT = "LEFT";

	
	private static int DURATION = 225;
	private int actionBarHeight  = 48;

	private static boolean isScrolling;
	private  int startX ;
	private static int SLIDE_VIEWS_START_X_POS ;
	private static int SLIDE_VIEWS_MINUS_X_POSITION = 0;

	private static int displacementPosition = 0; 
	private  int slideViewWidth = 0;

	private Display display;
	private static int displayWidth;
	private static int deletePoint;

	Context _context;
	int stackWidht;

	boolean touchedDown = false;

	public StackScrollView(Context context,int startX) {
		super(context);		
		gestureDetector = new GestureDetector(new MyGestureDetector());
		display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		SLIDE_VIEWS_MINUS_X_POSITION =  (int) (startX * 0.3);
		if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
			displayWidth = display.getHeight()+ actionBarHeight;	// where 48 is sum of above and below action bar
		}else{
			displayWidth = display.getWidth();
		}
		this.startX = startX;
		slideViewWidth = ((displayWidth - SLIDE_VIEWS_MINUS_X_POSITION) ) / 2;
		this.setWillNotDraw(false);
		deletePoint = (int) (display.getWidth() - (display.getWidth()*0.3));	
	}

	public StackScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public StackScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
	}


	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
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
			}
		}else if(event.getAction() == MotionEvent.ACTION_MOVE) {

		}

		return gestureDetector.onTouchEvent(event);	
	}





	private  Animation viewAnimation(final ViewGroup view, int moveFromX, int moveToX, int velocity) {
		TranslateAnimation anim = new TranslateAnimation(moveFromX, moveToX, 0, 0);
		anim.setDuration(velocity);
		view.setAnimationCacheEnabled(false);
		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {

			}
		});
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

				if(viewAtLeft != null && viewAtLeft == view){
					viewAtLeft.getLeftMargin(View.VISIBLE,View.GONE);
				}

				if(viewAtLeft2 != null && viewAtLeft2.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION && viewAtLeft != getChildAt(0) && viewAtLeft2 == view){
					viewAtLeft2.getLeftMargin(View.GONE,View.GONE);
				}

				if(viewAtLeft2 != null && viewAtLeft2 == view && getChildAt(getIndexOf(viewAtLeft2) - 1)!= null){
					((ShadowViewGroup) getChildAt(getIndexOf(viewAtLeft2) - 1)).getLeftMargin(View.VISIBLE,View.VISIBLE);
				}
			}

			public void onAnimationRepeat(Animation animation) {
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

				if(viewAtLeft != null && viewAtLeft == view){
					viewAtLeft.getLeftMargin(View.GONE,View.GONE);
				}
				if(viewAtLeft2 != null && viewAtLeft2 != view && getChildAt(getIndexOf(viewAtLeft2) - 1)!= null){

					((ShadowViewGroup) getChildAt(getIndexOf(viewAtLeft2) - 1)).getLeftMargin(View.VISIBLE,View.VISIBLE);
				}
			}
			public void onAnimationRepeat(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				if (getIndexOf(viewAtLeft2) > 1) {
					getChildAt(getIndexOf(viewAtLeft2) - 1).setVisibility(View.GONE);
				}
				if(viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION && viewAtLeft != getChildAt(0)){
					viewAtLeft.getLeftMargin(View.VISIBLE,View.VISIBLE);
				}
				if(viewAtLeft2 != null && viewAtLeft2 != getChildAt(0)){
					viewAtLeft2.getLeftMargin(View.VISIBLE,View.VISIBLE);
				}
				System.gc();
			}
		});
		return viewBounceAnimation;
	}

	public void onUp(MotionEvent e) {
		Animation animViewLeft = null;
		Animation animViewRight = null;
		FrameLayout.LayoutParams stackViewParams;
		if (dragDirection.equalsIgnoreCase(DIRECTION_LEFT)) {
			if (viewAtRight != null) {
				if (getIndexOf(viewAtLeft) == 0 && !(viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION || viewAtLeft.getLeft() == SLIDE_VIEWS_START_X_POS)) {
					//Drop Card View Animation
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

							Fragment f = viewControllersStack.get(0);
							DataViewFragment detailsFragment = (DataViewFragment) f.getFragmentManager().findFragmentById(f.getId());
//							if(detailsFragment.productDataSource != null){
//								detailsFragment.productDataSource.selectededPosition = -1;	
//							}
//							if(detailsFragment.categoryDataSource != null){
//								detailsFragment.categoryDataSource.selectededPosition = -1;	
//							}
							detailsFragment.getListView().invalidateViews();

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
						viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS -(ShadowViewGroup.SHADOW_WIDTH) ;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);

						if (viewAtRight != null) {
							viewAtRightParams.leftMargin = SLIDE_VIEWS_START_X_POS + viewAtLeft.getSlideViewWidth() ;
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
						}
					}else{
						FrameLayout.LayoutParams stackViewParams1 = (LayoutParams)this.getLayoutParams();
						if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin) || viewAtLeft.getLeft() < (getStartX() - stackViewParams1.leftMargin)){

							if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin)){

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
								stackViewParams = (LayoutParams)this.getLayoutParams();

								SLIDE_VIEWS_MINUS_X_POSITION = (int) (startX * 0.3);
								stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION ;
								stackViewParams.gravity = Gravity.LEFT;
								stackViewParams.width = display.getWidth() - SLIDE_VIEWS_MINUS_X_POSITION;
								this.setLayoutParams(stackViewParams);


								viewAtLeftParams.leftMargin = 0; 
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);

								if (viewAtRight != null) {
									viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		

									viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2));
									viewAtRightParams.gravity = Gravity.LEFT;

									viewAtRight.setLayoutParams(viewAtRightParams);	

									Animation leftanim = viewAnimation(viewAtLeft, (viewAtLeft.getLeft()),0, DURATION);
									Animation rightanim = viewAnimation(viewAtRight, (viewAtLeft.getLeft()),0, DURATION);

									rightanim.setAnimationListener(new AnimationListener() {

										@Override
										public void onAnimationStart(Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationRepeat(Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationEnd(Animation animation) {
											// TODO Auto-generated method stub
											if (viewAtRight2 != null) {
												viewAtRight2Params = (FrameLayout.LayoutParams) viewAtRight2.getLayoutParams();		
												viewAtRight2Params.leftMargin = getWidth()-(ShadowViewGroup.SHADOW_WIDTH);
												viewAtRight2Params.gravity = Gravity.LEFT;
												viewAtRight2.setLayoutParams(viewAtRight2Params);
												viewAtRight2.setVisibility(View.VISIBLE);
												if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
													viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -5));	
												}

											}

										}
									});

									viewAtLeft.clearAnimation();
									viewAtRight.clearAnimation();

									viewAtLeft.setAnimation(leftanim);		
									viewAtRight.setAnimation(rightanim);


								}





							}

						}
					}

				} else if (viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION && (viewAtRight.getLeft() + viewAtRight.getSlideViewWidth()) > this.getWidth()) {

					viewAtRightParams.leftMargin = this.getWidth() - viewAtRight.getSlideViewWidth();
					viewAtRightParams.gravity = Gravity.LEFT;
					viewAtRight.setLayoutParams(viewAtRightParams);
					animViewRight = viewAnimation(viewAtRight, -(this.getWidth()-viewAtRight.getRight()), 0, DURATION);
					viewAtRight.clearAnimation();
					viewAtRight.setAnimation(animViewRight);

					animViewRight.setAnimationListener(new AnimationListener() {

						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {
							if (viewAtRight2 != null) {
								viewAtRight2Params = (FrameLayout.LayoutParams) viewAtRight2.getLayoutParams();		
								viewAtRight2Params.leftMargin = getWidth()-(ShadowViewGroup.SHADOW_WIDTH);
								viewAtRight2Params.gravity = Gravity.LEFT;
								viewAtRight2.setLayoutParams(viewAtRight2Params);
								viewAtRight2.setVisibility(View.VISIBLE);
								viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -5));	

							}
						}
					});


				} else if (viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION && (viewAtRight.getLeft() + viewAtRight.getSlideViewWidth()) < this.getWidth()) {

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

						if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

							viewAtLeftParams.leftMargin = 0;
							viewAtLeftParams.gravity = Gravity.LEFT;
							viewAtLeft.setLayoutParams(viewAtLeftParams);
							viewAtRightParams.leftMargin = this.getWidth() - (viewAtRight.getSlideViewWidth());
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
							animViewRight = viewAnimation(viewAtRight, (viewAtRight.getLeft()+(0)-viewAtLeft.getSlideViewWidth()), (0), DURATION); 
							viewAtRight.clearAnimation();
							viewAtRight.setAnimation(animViewRight);
							animViewLeft = viewAnimation(viewAtLeft, (viewAtLeft.getLeft()),0, DURATION);

						}else{
							viewAtLeftParams.leftMargin = this.getWidth() - (viewAtLeft.getSlideViewWidth());
							viewAtLeftParams.gravity = Gravity.LEFT;
							viewAtLeft.setLayoutParams(viewAtLeftParams);
							viewAtRightParams.leftMargin = this.getWidth()-(ShadowViewGroup.SHADOW_WIDTH);
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
							animViewLeft = viewAnimation(viewAtLeft,(viewAtLeft.getLeft() - 100),0, DURATION);

						}
						viewAtLeft.clearAnimation();
						viewAtLeft.setAnimation(animViewLeft);

						animViewLeft.setAnimationListener(new AnimationListener() {
							public void onAnimationStart(Animation animation) {
							}

							public void onAnimationRepeat(Animation animation) {
							}

							public void onAnimationEnd(Animation animation) {
								viewAtRight.setAnimation(viewBounceAnimation(viewAtRight,0, -(viewAtLeft.getRight()), -(ShadowViewGroup.SHADOW_WIDTH)));
								viewAtLeft.setAnimation(viewBounceAnimation(viewAtLeft,0, -(viewAtRight.getLeft()), -10));
							}
						});

						animViewLeft = null;
						animViewRight = null;


					} else {
						if(viewAtLeft != null ){
							viewAtLeft.getLeftMargin(View.VISIBLE,View.VISIBLE);
						}
						viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);
						if (viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth()- (ShadowViewGroup.SHADOW_WIDTH *2) <= this.getWidth()) {
							viewAtRightParams.leftMargin = this.getWidth() - (viewAtRight.getSlideViewWidth());
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);
							animViewLeft = viewAnimation(viewAtLeft, (viewAtLeft.getLeft()),0, DURATION);

							if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
								animViewRight = viewAnimation(viewAtRight, (viewAtRight.getLeft()+(0)-viewAtLeft.getSlideViewWidth()), (0), DURATION); 
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


										viewAtRight2.setAnimation(viewBounceAnimation(viewAtRight2,0,viewAtRight2.getLeft() , -5));
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
				if(viewAtLeft.getRight() < this.getWidth() || (viewAtLeft.getSlideViewWidth()+SLIDE_VIEWS_START_X_POS) < this.getWidth()){

					viewAtLeft.showShadow(true);
					viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS -(ShadowViewGroup.SHADOW_WIDTH) ;
					viewAtLeftParams.gravity = Gravity.LEFT;

					viewAtLeft.setLayoutParams(viewAtLeftParams);

					Animation anim = viewAnimation(viewAtLeft, -(SLIDE_VIEWS_START_X_POS - viewAtLeft.getLeft()),0, DURATION);

					viewAtLeft.clearAnimation();
					viewAtLeft.setAnimation(anim);
					anim = null;
				}else{
					viewAtLeft.showShadow(true);
					viewAtLeftParams.leftMargin = this.getWidth() - viewAtLeft.getSlideViewWidth() ;

					viewAtLeftParams.gravity = Gravity.LEFT;

					viewAtLeft.setLayoutParams(viewAtLeftParams);
				}

			}

		} else if (dragDirection.equalsIgnoreCase(DIRECTION_RIGHT)) {

			if (viewAtLeft != null) {
				if (getIndexOf(viewAtLeft) == 0 && !(viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION || viewAtLeft.getLeft() == SLIDE_VIEWS_START_X_POS)) {

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
//									if(AppConstants.listviewDataFragment != null){
//										DataViewFragment.productDataSource.selectededPosition = -1;
//										AppConstants.listviewDataFragment.invalidateViews();
//									}

								}
								Fragment f = viewControllersStack.get(0);
								DataViewFragment detailsFragment = (DataViewFragment) f.getFragmentManager().findFragmentById(f.getId());

//								if(detailsFragment.productDataSource != null){
//									detailsFragment.productDataSource.selectededPosition = -1;	
//								}
//								if(detailsFragment.categoryDataSource != null){
//									detailsFragment.categoryDataSource.selectededPosition = -1;	
//								}

								detailsFragment.getListView().invalidateViews();


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
							viewAtLeft.showShadow(true);
							viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS - (ShadowViewGroup.SHADOW_WIDTH) ;
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
							anim = null;

						}else{
							FrameLayout.LayoutParams stackViewParams1 = (LayoutParams)this.getLayoutParams();
							if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin) || viewAtLeft.getLeft() < (getStartX() - stackViewParams1.leftMargin)){
								if(viewAtLeft.getLeft() > (getStartX() - stackViewParams1.leftMargin)){
									stackViewParams = (LayoutParams)this.getLayoutParams();
									SLIDE_VIEWS_MINUS_X_POSITION = getStartX()-((getStartX() - stackViewParams1.leftMargin));
									stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
									stackViewParams.width = LayoutParams.FILL_PARENT;
									stackViewParams.gravity = Gravity.LEFT;
									this.setLayoutParams(stackViewParams);
									viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
									if(stackViewParams.leftMargin != 0){
										viewAtLeftParams.leftMargin = (getStartX() - stackViewParams.leftMargin-(ShadowViewGroup.SHADOW_WIDTH)); // more than one card on screen
									}else{
										viewAtLeftParams.leftMargin = getStartX()-(ShadowViewGroup.SHADOW_WIDTH); // incase of only one card there on screen
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

									Animation leftanim = viewAnimation(viewAtLeft, -(getStartX() - stackViewParams.leftMargin-(ShadowViewGroup.SHADOW_WIDTH)-viewAtLeft.getLeft()),0, DURATION);
									Animation rightanim = viewAnimation(viewAtLeft, -(getStartX() - stackViewParams.leftMargin-(ShadowViewGroup.SHADOW_WIDTH)-viewAtLeft.getLeft()),0, DURATION);

									viewAtLeft.clearAnimation();
									viewAtLeft.setAnimation(leftanim);

									if (viewAtRight != null) {
										viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();		
										viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth() -(ShadowViewGroup.SHADOW_WIDTH *2)) + (getStartX() - stackViewParams1.leftMargin); 
										viewAtRightParams.gravity = Gravity.LEFT;
										viewAtRight.setLayoutParams(viewAtRightParams);
										viewAtRight.clearAnimation();
										viewAtRight.setAnimation(rightanim);
									}

								}
							}
						}

					} else {

						viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION ;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);
						viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft.getSlideViewWidth();
						viewAtRightParams.gravity = Gravity.LEFT;
						viewAtRight.setLayoutParams(viewAtRightParams);		
					}

				} else if (viewAtRight != null && viewAtRight.getLeft() < this.getWidth()) {	

					if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

						if(viewAtRight.getLeft() > viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)  && viewAtLeft2 != null){

							viewAtLeftParams.leftMargin = viewAtLeft2.getRight()-(ShadowViewGroup.SHADOW_WIDTH); ;//viewAtLeft.getSlideViewWidth()-40;
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
									viewAtLeft2.setAnimation(viewBounceAnimation(viewAtLeft2,0, -(viewAtLeft.getRight() - viewAtRight.getLeft()), 20));
								}
							});
							viewAtRightParams.leftMargin = this.getWidth();
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);		
							animViewRight = viewAnimation(viewAtRight, ((viewAtRight.getLeft()-this.getWidth())), (0), DURATION);
						}else{
							viewAtRightParams.leftMargin = viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH); 
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);		
							animViewRight = viewAnimation(viewAtRight, -((viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH)) - (viewAtRight.getLeft()+(ShadowViewGroup.SHADOW_WIDTH))), (0), DURATION);
						}

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
						if(getIndexOf(viewAtLeft) > 0 && viewAtLeft.getLeft() > SLIDE_VIEWS_MINUS_X_POSITION) {
							if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
								//potrait
								viewAtLeftParams.leftMargin  = viewAtLeft2.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
								viewAtLeftParams.gravity = Gravity.LEFT;
							} else {			
								// landscape
								viewAtLeftParams.leftMargin = viewAtLeft2.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH);
								viewAtLeftParams.gravity = Gravity.LEFT;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
							}

							viewAtRightParams.leftMargin = (this.getWidth()-(ShadowViewGroup.SHADOW_WIDTH));
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);

							if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
								animViewLeft = viewAnimation(viewAtLeft, -((viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)) - viewAtLeft.getLeft()),0, DURATION);
								animViewRight = viewAnimation(viewAtRight, -((viewAtLeft2.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)) - viewAtLeft.getLeft()),0,DURATION);
							}else{
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
							viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH);// w/o shadow
							viewAtRightParams.gravity = Gravity.LEFT;
							viewAtRight.setLayoutParams(viewAtRightParams);		
						}
					}

				}
			}
		}
	}













	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			touchedDown = true;

			if(viewAtLeft!= null){
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();			
				SLIDE_VIEWS_MINUS_X_POSITION = 0; 
				if (viewAtRight != null) {
					viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();
				}
			}
			return false;
		}



		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			if(viewAtLeft != null && viewAtLeft.getAnimation() != null){
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

			int traversedDistanceX = (int) (e2.getX() - e1.getX());
			int traversedDistanceY = (int) (e2.getY() - e1.getY());

			if(touchedDown){
				if(Math.abs(traversedDistanceX) > Math.abs(traversedDistanceY)){
					isScrolling = true; // v
				}else{
					isScrolling = false;
				}
				touchedDown = false;
			}

			if(isScrolling){
				if(getChildCount() > 0){				
					if(e2.getAction() == MotionEvent.ACTION_MOVE){
						if (distanceX > 0) { // right to left swipe
							if(viewAtLeft.getLeft()+(ShadowViewGroup.SHADOW_WIDTH-ShadowViewGroup.LEFTMARGIN) > 0){
								viewAtLeft.getLeftMargin(View.GONE,View.GONE);
							}else{
								viewAtLeft.getLeftMargin(View.VISIBLE,View.VISIBLE);
							}
							dragDirection = DIRECTION_LEFT;
							displacementPosition = (int) (distanceX * -1);
							if(viewAtRight != null){
								if (viewAtRight.getRight() <= getWidth()){	
									if (getIndexOf(viewAtRight) < (getChildCount()-1)) {
										viewAtLeft2 = viewAtLeft;
										viewAtLeft = viewAtRight;
										viewAtRight = viewAtRight2;
										viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
										viewAtRightParams = (LayoutParams) viewAtRight.getLayoutParams();

										if(getChildAt(getIndexOf(viewAtLeft2) - 2) != null){
											getChildAt(getIndexOf(viewAtLeft2) - 2).setVisibility(View.GONE);
										}

										if (getIndexOf(viewAtRight) < (getChildCount()-1)) {
											viewAtRight2 = (ShadowViewGroup) getChildAt(getIndexOf(viewAtRight) + 1);
											viewAtRight2.setVisibility(View.GONE);
											viewAtRight2Params = (LayoutParams) viewAtRight2.getLayoutParams();

										} else {
											viewAtRight2 = null;
										}
										if(viewAtLeft2 != null){
											viewAtLeft2.setVisibility(View.VISIBLE);
											viewAtLeft2Params = (LayoutParams) viewAtLeft2.getLayoutParams();
											viewAtLeft2Params.leftMargin = 0;
											viewAtLeft2Params.gravity = Gravity.LEFT;
											viewAtLeft2.setLayoutParams(viewAtLeft2Params);
										}

										if(viewAtRight != null){
											viewAtRight.setVisibility(View.VISIBLE);
											viewAtRightParams = (LayoutParams) viewAtRight.getLayoutParams();
											viewAtRightParams.leftMargin = viewAtLeft.getLeft() ;
											viewAtRightParams.leftMargin = ((StackScrollView.this.getWidth())-(viewAtRight.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)));

											viewAtRightParams.gravity = Gravity.LEFT;  // included to run on 2.X versions
											viewAtRight.setLayoutParams(viewAtRightParams);
										}

										if(viewAtRight2 != null){
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
									if ((viewAtLeft.getLeft() + displacementPosition) + viewAtRight.getSlideViewWidth() <= getWidth()-(ShadowViewGroup.SHADOW_WIDTH *2)) {
										//at potrait
										viewAtRightParams.leftMargin = (int) (viewAtRight.getLeft() + displacementPosition);
										viewAtRight.setLayoutParams(viewAtRightParams);
									} else {
										viewAtRightParams.leftMargin = (viewAtRight.getLeft() + displacementPosition);
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

								} 
								else if ((getIndexOf(viewAtRight) == getChildCount()-1) && (viewAtLeft.getLeft() == SLIDE_VIEWS_MINUS_X_POSITION)) {		
									if ((viewAtRight.getLeft() + displacementPosition) <= 0) {
										if (viewAtRight.getLeft() > 0) {
											viewAtRightParams.leftMargin = (int) (viewAtRight.getLeft() + displacementPosition);
											viewAtRight.setLayoutParams(viewAtRightParams);
										}else{
											viewAtRightParams.leftMargin = 0;
											viewAtRight.setLayoutParams(viewAtRightParams);
										}


									} else {
										viewAtRightParams.leftMargin = viewAtRight.getLeft() + displacementPosition;
										viewAtRight.setLayoutParams(viewAtRightParams);
									}

								} else {
									if ((viewAtLeft.getLeft() + displacementPosition) < SLIDE_VIEWS_MINUS_X_POSITION) {
										viewAtLeftParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION;
										viewAtLeft.setLayoutParams(viewAtLeftParams);

										if (viewAtRight != null) {
											viewAtRight.setVisibility(View.VISIBLE);
											viewAtRightParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION + viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH);//viewAtRight.getLeft() + displacementPosition;
											viewAtRight.setLayoutParams(viewAtRightParams);


										}

									} else {
										viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
										viewAtLeftParams.leftMargin = viewAtLeft.getLeft() + displacementPosition;
										viewAtLeft.setLayoutParams(viewAtLeftParams);
										if (viewAtRight != null) {
											if((viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH) ) <= StackScrollView.this.getWidth() ){
												viewAtRightParams.leftMargin = (viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH) )+ displacementPosition;
												viewAtRight.setLayoutParams(viewAtRightParams);
											}else{
												viewAtRightParams.leftMargin = viewAtLeft.getRight()-(ShadowViewGroup.SHADOW_WIDTH *2);//StackScrollView.this.getWidth();
												viewAtRight.setLayoutParams(viewAtRightParams);
											}
										}
									}
								}

							} else {
								viewAtLeftParams.leftMargin = viewAtLeft.getLeft() + displacementPosition;
								viewAtLeft.setLayoutParams(viewAtLeftParams);
							}

						} else if (distanceX < 0) { // left to right swipe
							if(viewAtLeft.getLeft()+(ShadowViewGroup.SHADOW_WIDTH-ShadowViewGroup.LEFTMARGIN) > 0){
								viewAtLeft.getLeftMargin(View.GONE,View.GONE);
							}else{
								viewAtLeft.getLeftMargin(View.VISIBLE,View.GONE);
							}

							if(viewAtLeft2 != null && viewAtLeft2 == getChildAt(0)){
								viewAtLeft2.getLeftMargin(View.GONE,View.GONE);
							}
							dragDirection = DIRECTION_RIGHT;
							displacementPosition = (int) (distanceX);
							if (viewAtLeft != null) {
								if(viewAtRight != null){
									if (viewAtRight.getLeft() >= getWidth()) {
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
												viewAtLeft2Params.leftMargin = 0;
												viewAtLeft2Params.gravity = Gravity.LEFT;
												viewAtLeft2.setLayoutParams(viewAtLeft2Params);
												viewAtLeft2.setVisibility(View.VISIBLE);
											}

											if(viewAtLeft != null){
												viewAtLeftParams.leftMargin = 0;
												viewAtLeftParams.gravity = Gravity.LEFT;
												viewAtLeft.setLayoutParams(viewAtLeftParams);
											}

										}
									}


									if((viewAtRight.getSlideViewLeft()   < (viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2))) && viewAtLeft.getSlideViewLeft() == SLIDE_VIEWS_MINUS_X_POSITION) {
										if ( (viewAtRight.getSlideViewLeft() - displacementPosition) >= (viewAtLeft.getLeft() + viewAtLeft.getSlideViewWidth() - (ShadowViewGroup.SHADOW_WIDTH *2))) {
											viewAtRightParams = (LayoutParams) viewAtRight.getLayoutParams();
											viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
											viewAtRight.setLayoutParams(viewAtRightParams);
										}else {
											viewAtRightParams.leftMargin = viewAtRight.getSlideViewLeft() - displacementPosition;
											viewAtRight.setLayoutParams(viewAtRightParams);

										}

									} else if (getIndexOf(viewAtLeft) == 0) {
										if (viewAtRight == null) {
											viewAtLeftParams.leftMargin = viewAtLeft.getLeft() - displacementPosition;
											viewAtLeft.setLayoutParams(viewAtLeftParams);
										} else {
											viewAtRightParams.leftMargin = (viewAtRight.getLeft()) - displacementPosition;
											viewAtRight.setLayoutParams(viewAtRightParams);

											if (viewAtRight.getLeft() < (viewAtLeft.getRight() - (ShadowViewGroup.SHADOW_WIDTH *2))) {
												viewAtLeft.setLayoutParams(viewAtLeftParams);
											} else {
												viewAtLeftParams.leftMargin = (viewAtLeft.getLeft()) - displacementPosition;
												viewAtLeft.setLayoutParams(viewAtLeftParams);
											}
										}

									} else {

										if ((viewAtRight.getLeft() - displacementPosition) >= getWidth()) {
											viewAtRightParams.leftMargin = viewAtLeft.getRight();//getWidth();
											viewAtRight.setLayoutParams(viewAtRightParams);
										} else {
											viewAtRightParams.leftMargin = viewAtRight.getLeft()  - displacementPosition;// (viewAtRight.getLeft()) - displacementPosition;
											viewAtRight.setLayoutParams(viewAtRightParams);
										}

										if ((viewAtLeft.getRight() - displacementPosition) >= getWidth()) {
											viewAtLeftParams.leftMargin = getWidth() - (viewAtLeft.getSlideViewWidth());
											viewAtLeft.setLayoutParams(viewAtLeftParams);
										} else {
											viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
											viewAtLeftParams.leftMargin = viewAtRight.getLeft() - (viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH)) - displacementPosition;
											viewAtLeft.setLayoutParams(viewAtLeftParams);
										}
									}	

								} else {
									viewAtLeftParams = (LayoutParams) viewAtLeft.getLayoutParams();
									viewAtLeftParams.leftMargin = viewAtLeft.getLeft() - displacementPosition;
									viewAtLeft.setLayoutParams(viewAtLeftParams);
								}
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

	public void removeStackAt (int i){

		if (getChildCount() > 0) {

			removeViewAt(i);
			viewControllersStack.remove(i);  
			
//			if(AppConstants.listviewDataFragment != null){
//				DataViewFragment.productDataSource.selectededPosition = -1;
//				AppConstants.listviewDataFragment.invalidateViews();
//			}

			if (getChildCount() > 0) {
				if (getChildCount() == 1) {
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

					viewAtLeft.showShadow(true);
					SLIDE_VIEWS_START_X_POS = getStartX();
					if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
						viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS - (ShadowViewGroup.SHADOW_WIDTH);
					}else{
						viewAtLeftParams.leftMargin = display.getWidth()-viewAtLeft.getSlideViewWidth();	
					}

					viewAtLeft.setLayoutParams(viewAtLeftParams);

				}else if (getChildCount() == 2) {
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
					viewAtLeftParams.leftMargin = 0;
					viewAtLeft.setLayoutParams(viewAtLeftParams);
					final TranslateAnimation animrightinIL = new TranslateAnimation(viewAtLeft.getLeft()-SLIDE_VIEWS_MINUS_X_POSITION,0,0,0);

					viewAtLeft.setAnimation(animrightinIL);

					TranslateAnimation animrightinIR = null ;

					viewAtRight.showShadow(true);
					if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
						viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH *2);
						viewAtRight.setLayoutParams(viewAtRightParams);	
						viewAtRight.setAnimation(animrightinIR);
					}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
						viewAtRightParams.leftMargin = (this.getWidth() - (viewAtRight.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH)));	
						viewAtRight.setLayoutParams(viewAtRightParams);	
						viewAtRight.setAnimation(animrightinIR);
					}
				} else {
					viewAtLeft2 = (ShadowViewGroup) getChildAt(getChildCount()-3);				
					viewAtLeft = (ShadowViewGroup) getChildAt(getChildCount()-2);
					viewAtRight = (ShadowViewGroup) getChildAt(getChildCount()-1);

					viewAtRight2 = null;
					viewAtLeft2.showShadow(true);
					viewAtLeft2.setVisibility(View.VISIBLE);
					viewAtLeft2Params = (FrameLayout.LayoutParams) viewAtLeft2.getLayoutParams();
					viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
					viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();


					viewAtLeftParams.leftMargin = 0;
					viewAtLeft.setLayoutParams(viewAtLeftParams);
					//inorder to ensure tat the right card is fully diplayed when new right one is clicked

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
						//ther left2 sets to its posiotion before the animation starts there for listenr added  and left2 is set after compliting animation
						{
							final TranslateAnimation animrightinLeft2 = new TranslateAnimation(viewAtLeft2.getLeft()+(ShadowViewGroup.SHADOW_WIDTH),0,0,0);
							animrightinLeft2.setDuration(500);
							viewAtLeft2Params.leftMargin = 0;
							viewAtLeft2.showShadow(true);
							viewAtLeft2.setLayoutParams(viewAtLeft2Params);
							viewAtLeft2.setAnimation(animrightinLeft2);
						}
						animrightinIR.setAnimationListener(new AnimationListener() {
							public void onAnimationStart(Animation animation) {
							}

							public void onAnimationRepeat(Animation animation) {
							}

							public void onAnimationEnd(Animation animation) {
								viewAtLeft.getLeftMargin(View.VISIBLE,View.VISIBLE);
							}
						});
						viewAtRight.setAnimation(animrightinIR);
						viewAtLeft.setAnimation(animrightinIL);
					}
				}



			}else{
				//remove's the focus of menufragment
//				if(AppConstants.listviewMenuFragment != null){
//					MenuViewFragment.productDataSource.selectededPosition = -1;
//					AppConstants.listviewMenuFragment.invalidateViews();
//				}
			}
		}
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
		slideView.setTag(viewControllersStack.size() - 1);
		this.addView(slideView);
		slideView = null;
		childCount = getChildCount();

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

				viewAtLeft.showShadow(true);
				SLIDE_VIEWS_START_X_POS = getStartX();
				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS - (ShadowViewGroup.SHADOW_WIDTH); 
				}else{
					viewAtLeftParams.leftMargin = display.getWidth()-viewAtLeft.getSlideViewWidth();	
				}

				viewAtLeft.setLayoutParams(viewAtLeftParams);
			} else if (childCount == 2) {

				viewAtLeft2 = null;
				viewAtLeft = (ShadowViewGroup) getChildAt(0);
				viewAtRight = (ShadowViewGroup) getChildAt(1);
				viewAtRight2 = null;

				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();				
				SLIDE_VIEWS_START_X_POS = 0;
				FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();

				if (this.getLeft() == 0) {
					SLIDE_VIEWS_MINUS_X_POSITION = (int) (startX * 0.3);
					stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION ;
					stackViewParams.gravity = Gravity.LEFT;
					stackViewParams.width = this.getWidth() - SLIDE_VIEWS_MINUS_X_POSITION;
					this.setLayoutParams(stackViewParams);
				}

				viewAtLeft.showShadow(true);
				viewAtLeftParams.leftMargin = 0;
				viewAtLeft.setLayoutParams(viewAtLeftParams);

				final TranslateAnimation animrightinIL = new TranslateAnimation(viewAtLeft.getLeft()-SLIDE_VIEWS_MINUS_X_POSITION,0,0,0);
				TranslateAnimation animrightinIR = null ;

				viewAtRight.showShadow(true);
				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					viewAtRightParams.leftMargin = (this.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getSlideViewWidth())));
					animrightinIR =  new TranslateAnimation((viewAtLeft.getRight()) ,0,0,0);
					animrightinIL.setDuration(500);
					animrightinIR.setDuration(450);
				}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					viewAtRightParams.leftMargin = (this.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getSlideViewWidth())));	
					animrightinIR =  new TranslateAnimation(viewAtLeft.getRight() ,0,0,0);
					animrightinIL.setDuration(300);
					animrightinIR.setDuration(300);		
				}
				viewAtRight.setLayoutParams(viewAtRightParams);	
				viewAtLeft.setAnimation(animrightinIL);
				viewAtRight.setAnimation(animrightinIR);

			} else {
				viewAtLeft2 = (ShadowViewGroup) getChildAt(childCount - 3);				
				viewAtLeft = (ShadowViewGroup) getChildAt(childCount - 2);
				viewAtRight = (ShadowViewGroup) getChildAt(childCount - 1);

				viewAtRight2 = null;
				viewAtLeft2.setVisibility(View.VISIBLE);
				viewAtLeft2Params = (FrameLayout.LayoutParams) viewAtLeft2.getLayoutParams();
				viewAtLeftParams = (FrameLayout.LayoutParams) viewAtLeft.getLayoutParams();
				viewAtRightParams = (FrameLayout.LayoutParams) viewAtRight.getLayoutParams();

				viewAtLeftParams.leftMargin = 0;
				viewAtLeft.setLayoutParams(viewAtLeftParams);
				viewAtLeft.showShadow(true);
				//inorder to ensure tat the right card is fully diplayed when new right one is clicked
				viewAtRight.showShadow(true);

				final TranslateAnimation animrightinIL = new TranslateAnimation(viewAtLeft.getLeft(),0,0,0);
				animrightinIL.setDuration(500);

				TranslateAnimation animrightinIR = null ;


				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					viewAtRightParams.leftMargin = (this.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getSlideViewWidth())));	
					animrightinIR =  new TranslateAnimation(this.getWidth() ,0,0,0);
					animrightinIR.setDuration(500);
				}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					viewAtRightParams.leftMargin = (this.getWidth() - viewAtRight.getSlideViewWidth());	
					animrightinIR =  new TranslateAnimation(StackScrollView.this.getWidth() - (viewAtLeft.getSlideViewWidth()) ,0,0,0);
					animrightinIR.setDuration(500);
				}
				viewAtRight.setLayoutParams(viewAtRightParams);

				final TranslateAnimation animrightinLeft2 = new TranslateAnimation(viewAtLeft2.getLeft(),0,0,0);
				animrightinLeft2.setDuration(500);
				viewAtLeft2Params.leftMargin = 0;
				viewAtLeft2.showShadow(true);
				viewAtLeft2.setLayoutParams(viewAtLeft2Params);
				viewAtLeft2.setAnimation(animrightinLeft2);

				animrightinIR.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}

					public void onAnimationRepeat(Animation animation) {
					}

					public void onAnimationEnd(Animation animation) {
						viewAtLeft.getLeftMargin(View.VISIBLE,View.VISIBLE);
					}
				});
				viewAtRight.setAnimation(animrightinIR);
				viewAtLeft.setAnimation(animrightinIL);
				if(viewAtRight2!=null){
					viewAtRight2Params.leftMargin = this.getWidth();
					viewAtRight2.setLayoutParams(viewAtRight2Params);
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

	public void setCardWidht(String fromMenu,String cardWidth) {
		if(fromMenu.equalsIgnoreCase("menu")){
			if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
				stackWidht = (int) (display.getWidth()-(startX * 0.3));
				AppConstants.CATEGORY_CARD_WIDTH =  (int) (stackWidht*0.50);
				AppConstants.PRODUCT_CARD_WIDTH = (int)( (stackWidht*0.55)-(ShadowViewGroup.SHADOW_WIDTH));
				AppConstants.PRODUCT_LIST_CARD_WIDTH = (int) (stackWidht - AppConstants.CATEGORY_CARD_WIDTH);
			}else{
				if(Build.VERSION.SDK_INT > 11){
					stackWidht = (int) (display.getHeight() + actionBarHeight   -(startX * 0.3));
				}else{
					stackWidht = (int) (display.getHeight()   -(startX * 0.3));
				}

				AppConstants.CATEGORY_CARD_WIDTH =  (int) (stackWidht * 0.50);
				AppConstants.PRODUCT_CARD_WIDTH = (int) (stackWidht *0.55) - (ShadowViewGroup.SHADOW_WIDTH);
				AppConstants.PRODUCT_LIST_CARD_WIDTH = (int) (stackWidht - AppConstants.CATEGORY_CARD_WIDTH);
			}
		}

		if(cardWidth.equalsIgnoreCase(AppConstants.PRODUCT)){
			this.slideViewWidth = AppConstants.PRODUCT_CARD_WIDTH;
		}else if(cardWidth.equalsIgnoreCase(AppConstants.PRODUCT_LIST)){
			this.slideViewWidth = AppConstants.PRODUCT_LIST_CARD_WIDTH;
		}else {
			this.slideViewWidth = AppConstants.CATEGORY_CARD_WIDTH;
		}
	}

	public int getCardWidht() {

		return this.slideViewWidth;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);

		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
			displayWidth = display.getWidth();
		}else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			displayWidth = display.getWidth();
		}
		deletePoint = (int) (display.getWidth() - (display.getWidth()*0.3));	

		int childCount = this.getChildCount();
		
		System.out.println("=========================== OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO   "+childCount);
		
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

				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					viewAtLeftParams.leftMargin = SLIDE_VIEWS_START_X_POS - (ShadowViewGroup.SHADOW_WIDTH);
				}else{
					viewAtLeftParams.leftMargin = display.getWidth() - viewAtLeft.getSlideViewWidth();	
				}

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

				FrameLayout.LayoutParams stackViewParams = (LayoutParams)this.getLayoutParams();
				SLIDE_VIEWS_MINUS_X_POSITION = (int) (startX * 0.3);
				stackViewParams.leftMargin = SLIDE_VIEWS_MINUS_X_POSITION ;
				stackViewParams.gravity = Gravity.LEFT;
				stackViewParams.width = display.getWidth() - SLIDE_VIEWS_MINUS_X_POSITION;
				this.setLayoutParams(stackViewParams);


				viewAtLeft.showShadow(true);
				viewAtLeftParams.leftMargin = 0;
				viewAtLeft.setLayoutParams(viewAtLeftParams);


				if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
					viewAtRightParams.leftMargin = (display.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getWidth())));
				}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
					viewAtRightParams.leftMargin = (display.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getSlideViewWidth())));
				}
				viewAtRight.setLayoutParams(viewAtRightParams);	


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

					viewAtRightParams.leftMargin = (viewAtLeft.getSlideViewWidth())+(viewAtRight.getSlideViewWidth());
					viewAtRight.setLayoutParams(viewAtRightParams);

					if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
						viewAtLeftParams.leftMargin = (display.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtLeft.getWidth())));
					}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
						viewAtLeftParams.leftMargin = (display.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtLeft.getSlideViewWidth())));
					}

					viewAtLeft.setLayoutParams(viewAtLeftParams);

					if(viewAtRight2 != null){
						viewAtRight2Params.leftMargin = 0;
						viewAtRight2.setLayoutParams(viewAtRight2Params);
					}

				}else if(viewAtLeft2 != null){

					if(viewAtLeft.getLeft() != 0){
						viewAtLeftParams.leftMargin = viewAtLeft2.getSlideViewWidth()-(ShadowViewGroup.SHADOW_WIDTH);
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);

						viewAtRightParams.leftMargin = viewAtLeft.getSlideViewWidth()*2;
						viewAtRight.setLayoutParams(viewAtRightParams);	
					}else{

						viewAtLeftParams.leftMargin = 0;
						viewAtLeftParams.gravity = Gravity.LEFT;
						viewAtLeft.setLayoutParams(viewAtLeftParams);

						if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
							viewAtRightParams.leftMargin = (display.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getWidth())));
						}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
							viewAtRightParams.leftMargin = (display.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getSlideViewWidth())));
						}

						viewAtRight.setLayoutParams(viewAtRightParams);

						if(viewAtRight2 != null){
							viewAtRight2Params.leftMargin =  (viewAtLeft.getSlideViewWidth())+(viewAtRight.getSlideViewWidth());;
							viewAtRight2.setLayoutParams(viewAtRight2Params);
						}

					}
				} else {
					viewAtLeftParams.leftMargin = 0;
					viewAtLeftParams.gravity = Gravity.LEFT;
					viewAtLeft.setLayoutParams(viewAtLeftParams);

					if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
						viewAtRightParams.leftMargin = (display.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getWidth())));
					}else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
						viewAtRightParams.leftMargin = (display.getWidth() - (SLIDE_VIEWS_MINUS_X_POSITION + (viewAtRight.getSlideViewWidth())));
					}

					viewAtRight.setLayoutParams(viewAtRightParams);
					viewAtRight2Params.leftMargin = (viewAtLeft.getSlideViewWidth())+(viewAtRight.getSlideViewWidth());;
					viewAtRight2.setLayoutParams(viewAtRight2Params);	


				}

				if(viewAtRight2 != null){
					for (int i = getIndexOf(viewAtRight2); i < getChildCount(); i++) {

						LayoutParams params = (LayoutParams) getChildAt(i).getLayoutParams(); 		
						params.leftMargin =slideViewWidth*2;
						params.gravity = Gravity.LEFT;
						getChildAt(i).setVisibility(View.VISIBLE);
						getChildAt(i).setLayoutParams(params);
					}
				}
			}
		}
	}

}


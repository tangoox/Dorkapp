package com.septinary.xbwapp.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

@SuppressLint("NewApi")
public class ResideLayout extends ViewGroup {
	private static final String TAG = "ResideLayout";

	private static final int DEFAULT_OVERHANG_SIZE = 80; // dp;

	private int mSliderFadeColor = 0x99000000;

	private static final int MIN_FLING_VELOCITY = 400; // dips per second

	private int mCoveredFadeColor = 0xcc000000;

	private final int mOverhangSize;

	private boolean mCanSlide;

	private View mSlideableView;

	private float mSlideOffset;

	private float mParallaxOffset;

	private int mSlideRange;

	private boolean mIsUnableToDrag;

	private int mParallaxBy;

	private float mInitialMotionX;
	private float mInitialMotionY;

	private PanelSlideListener mPanelSlideListener;

	private final ViewDragHelper mDragHelper;

	private boolean mPreservedOpenState;
	private boolean mFirstLayout = true;

	private final Rect mTmpRect = new Rect();
	
	private final ArrayList<DisableLayerRunnable> mPostedRunnables = new ArrayList<DisableLayerRunnable>();

	static final SlidingPanelLayoutImpl IMPL;

	static {
		final int deviceVersion = Build.VERSION.SDK_INT;
		if (deviceVersion >= 17) {
			IMPL = new SlidingPanelLayoutImplJBMR1();
		} else if (deviceVersion >= 16) {
			IMPL = new SlidingPanelLayoutImplJB();
		} else {
			IMPL = new SlidingPanelLayoutImplBase();
		}
	}

	public interface PanelSlideListener {

		public void onPanelSlide(View panel, float slideOffset);

		public void onPanelOpened(View panel);

		public void onPanelClosed(View panel);
	}

	public ResideLayout(Context context) {
		this(context, null);
	}

	public ResideLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ResideLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		final float density = context.getResources().getDisplayMetrics().density;
		mOverhangSize = (int) (DEFAULT_OVERHANG_SIZE * density + 0.5f);

		setWillNotDraw(false);

		mDragHelper = ViewDragHelper.create(this, 0.5f,
				new DragHelperCallback());
		mDragHelper.setMinVelocity(MIN_FLING_VELOCITY * density);
	}

	public void setParallaxDistance(int parallaxBy) {
		mParallaxBy = parallaxBy;
		requestLayout();
	}

	public int getParallaxDistance() {
		return mParallaxBy;
	}

	public void setPanelSlideListener(PanelSlideListener listener) {
		mPanelSlideListener = listener;
	}

	void dispatchOnPanelSlide(View panel) {
		if (mPanelSlideListener != null) {
			mPanelSlideListener.onPanelSlide(panel, mSlideOffset);
		}
	}

	void dispatchOnPanelOpened(View panel) {
		if (mPanelSlideListener != null) {
			mPanelSlideListener.onPanelOpened(panel);
		}
	}

	void dispatchOnPanelClosed(View panel) {
		if (mPanelSlideListener != null) {
			mPanelSlideListener.onPanelClosed(panel);
		}
	}

	void updateObscuredViewsVisibility(View panel) {
		final int startBound = getPaddingLeft();
		final int endBound = getWidth() - getPaddingRight();
		final int topBound = getPaddingTop();
		final int bottomBound = getHeight() - getPaddingBottom();
		final int left;
		final int right;
		final int top;
		final int bottom;
		if (panel != null && viewIsOpaque(panel)) {
			left = panel.getLeft();
			right = panel.getRight();
			top = panel.getTop();
			bottom = panel.getBottom();
		} else {
			left = right = top = bottom = 0;
		}

		for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
			final View child = getChildAt(i);

			if (child == panel) {
				break;
			}

			final int clampedChildLeft = Math.max(startBound, child.getLeft());
			final int clampedChildTop = Math.max(topBound, child.getTop());
			final int clampedChildRight = Math.min(endBound, child.getRight());
			final int clampedChildBottom = Math.min(bottomBound,
					child.getBottom());
			final int vis;
			if (clampedChildLeft >= left && clampedChildTop >= top
					&& clampedChildRight <= right
					&& clampedChildBottom <= bottom) {
				vis = INVISIBLE;
			} else {
				vis = VISIBLE;
			}
			child.setVisibility(vis);
		}
	}

	void setAllChildrenVisible() {
		for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() == INVISIBLE) {
				child.setVisibility(VISIBLE);
			}
		}
	}

	private static boolean viewIsOpaque(View v) {
		if (ViewCompat.isOpaque(v))
			return true;

		if (Build.VERSION.SDK_INT >= 18)
			return false;

		final Drawable bg = v.getBackground();
		return bg != null && bg.getOpacity() == PixelFormat.OPAQUE;
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mFirstLayout = true;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mFirstLayout = true;

		for (final DisableLayerRunnable dlr : mPostedRunnables) {
			dlr.run();
		}
		mPostedRunnables.clear();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode != MeasureSpec.EXACTLY) {
			if (isInEditMode()) {
				if (widthMode == MeasureSpec.UNSPECIFIED) {
					widthSize = 300;
				}
			} else {
				throw new IllegalStateException(
						"Width must have an exact value or MATCH_PARENT");
			}
		} else if (heightMode == MeasureSpec.UNSPECIFIED) {
			if (isInEditMode()) {
				if (heightMode == MeasureSpec.UNSPECIFIED) {
					heightMode = MeasureSpec.AT_MOST;
					heightSize = 300;
				}
			} else {
				throw new IllegalStateException(
						"Height must not be UNSPECIFIED");
			}
		}

		int layoutHeight = 0;
		int maxLayoutHeight = -1;
		switch (heightMode) {
		case MeasureSpec.EXACTLY:
			layoutHeight = maxLayoutHeight = heightSize - getPaddingTop()
					- getPaddingBottom();
			break;
		case MeasureSpec.AT_MOST:
			maxLayoutHeight = heightSize - getPaddingTop() - getPaddingBottom();
			break;
		}

		float weightSum = 0;
		boolean canSlide = false;
		final int widthAvailable = widthSize - getPaddingLeft()
				- getPaddingRight();
		int widthRemaining = widthAvailable;
		final int childCount = getChildCount();

		if (childCount > 2) {
			Log.e(TAG,
					"onMeasure: More than two child views are not supported.");
		}

		mSlideableView = null;

		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);
			final LayoutParams lp = (LayoutParams) child.getLayoutParams();

			if (child.getVisibility() == GONE) {
				lp.dimWhenOffset = false;
				continue;
			}

			if (lp.weight > 0) {
				weightSum += lp.weight;

				if (lp.width == 0)
					continue;
			}

			int childWidthSpec;
			final int horizontalMargin = lp.leftMargin + lp.rightMargin;
			if (lp.width == LayoutParams.WRAP_CONTENT) {
				childWidthSpec = MeasureSpec.makeMeasureSpec(widthAvailable
						- horizontalMargin, MeasureSpec.AT_MOST);
			} else if (lp.width == LayoutParams.MATCH_PARENT) {
				childWidthSpec = MeasureSpec.makeMeasureSpec(widthAvailable
						- horizontalMargin, MeasureSpec.EXACTLY);
			} else {
				childWidthSpec = MeasureSpec.makeMeasureSpec(lp.width,
						MeasureSpec.EXACTLY);
			}

			int childHeightSpec;
			if (lp.height == LayoutParams.WRAP_CONTENT) {
				childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight,
						MeasureSpec.AT_MOST);
			} else if (lp.height == LayoutParams.MATCH_PARENT) {
				childHeightSpec = MeasureSpec.makeMeasureSpec(maxLayoutHeight,
						MeasureSpec.EXACTLY);
			} else {
				childHeightSpec = MeasureSpec.makeMeasureSpec(lp.height,
						MeasureSpec.EXACTLY);
			}

			child.measure(childWidthSpec, childHeightSpec);
			final int childWidth = child.getMeasuredWidth();
			final int childHeight = child.getMeasuredHeight();

			if (heightMode == MeasureSpec.AT_MOST && childHeight > layoutHeight) {
				layoutHeight = Math.min(childHeight, maxLayoutHeight);
			}

			widthRemaining -= childWidth;
			canSlide |= lp.slideable = widthRemaining < 0;
			if (lp.slideable) {
				mSlideableView = child;
			}
		}

		if (canSlide || weightSum > 0) {
			final int fixedPanelWidthLimit = widthAvailable;
			
			for (int i = 0; i < childCount; i++) {
				final View child = getChildAt(i);

				if (child.getVisibility() == GONE) {
					continue;
				}

				final LayoutParams lp = (LayoutParams) child.getLayoutParams();

				if (child.getVisibility() == GONE) {
					continue;
				}

				final boolean skippedFirstPass = lp.width == 0 && lp.weight > 0;
				final int measuredWidth = skippedFirstPass ? 0 : child
						.getMeasuredWidth();
				if (canSlide && child != mSlideableView) {
					if (lp.width < 0
							&& (measuredWidth > fixedPanelWidthLimit || lp.weight > 0)) {
						final int childHeightSpec;
						if (skippedFirstPass) {
							if (lp.height == LayoutParams.WRAP_CONTENT) {
								childHeightSpec = MeasureSpec.makeMeasureSpec(
										maxLayoutHeight, MeasureSpec.AT_MOST);
							} else if (lp.height == LayoutParams.MATCH_PARENT) {
								childHeightSpec = MeasureSpec.makeMeasureSpec(
										maxLayoutHeight, MeasureSpec.EXACTLY);
							} else {
								childHeightSpec = MeasureSpec.makeMeasureSpec(
										lp.height, MeasureSpec.EXACTLY);
							}
						} else {
							childHeightSpec = MeasureSpec.makeMeasureSpec(
									child.getMeasuredHeight(),
									MeasureSpec.EXACTLY);
						}
						final int childWidthSpec = MeasureSpec.makeMeasureSpec(
								fixedPanelWidthLimit, MeasureSpec.EXACTLY);
						child.measure(childWidthSpec, childHeightSpec);
					}
				} else if (lp.weight > 0) {
					int childHeightSpec;
					if (lp.width == 0) {
						if (lp.height == LayoutParams.WRAP_CONTENT) {
							childHeightSpec = MeasureSpec.makeMeasureSpec(
									maxLayoutHeight, MeasureSpec.AT_MOST);
						} else if (lp.height == LayoutParams.MATCH_PARENT) {
							childHeightSpec = MeasureSpec.makeMeasureSpec(
									maxLayoutHeight, MeasureSpec.EXACTLY);
						} else {
							childHeightSpec = MeasureSpec.makeMeasureSpec(
									lp.height, MeasureSpec.EXACTLY);
						}
					} else {
						childHeightSpec = MeasureSpec.makeMeasureSpec(
								child.getMeasuredHeight(), MeasureSpec.EXACTLY);
					}

					if (canSlide) {
						final int horizontalMargin = lp.leftMargin
								+ lp.rightMargin;
						final int newWidth = widthAvailable - horizontalMargin;
						final int childWidthSpec = MeasureSpec.makeMeasureSpec(
								newWidth, MeasureSpec.EXACTLY);
						if (measuredWidth != newWidth) {
							child.measure(childWidthSpec, childHeightSpec);
						}
					} else {
						final int widthToDistribute = Math.max(0,
								widthRemaining);
						final int addedWidth = (int) (lp.weight
								* widthToDistribute / weightSum);
						final int childWidthSpec = MeasureSpec
								.makeMeasureSpec(measuredWidth + addedWidth,
										MeasureSpec.EXACTLY);
						child.measure(childWidthSpec, childHeightSpec);
					}
				}
			}
		}

		final int measuredWidth = widthSize;
		final int measuredHeight = layoutHeight + getPaddingTop()
				+ getPaddingBottom();

		setMeasuredDimension(measuredWidth, measuredHeight);
		mCanSlide = canSlide;

		if (mDragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE
				&& !canSlide) {
			mDragHelper.abort();
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
		final int width = r - l;
		final int paddingStart = getPaddingLeft();
		final int paddingEnd = getPaddingRight();
		final int paddingTop = getPaddingTop();

		final int childCount = getChildCount();
		int xStart = paddingStart;
		int nextXStart = xStart;

		if (mFirstLayout) {
			mSlideOffset = mCanSlide && mPreservedOpenState ? 1.f : 0.f;
		}

		for (int i = 0; i < childCount; i++) {
			final View child = getChildAt(i);

			if (child.getVisibility() == GONE) {
				continue;
			}

			final LayoutParams lp = (LayoutParams) child.getLayoutParams();

			final int childWidth = child.getMeasuredWidth();
			int offset = 0;

			if (lp.slideable) {
				final int margin = lp.leftMargin + lp.rightMargin;
				final int range = Math.min(nextXStart, width - paddingEnd
						- mOverhangSize)
						- xStart - margin;
				mSlideRange = range;
				final int lpMargin = lp.leftMargin;
				lp.dimWhenOffset = xStart + lpMargin + range + childWidth / 2 > width
						- paddingEnd;
				final int pos = (int) (range * mSlideOffset);
				xStart += pos + lpMargin;
				mSlideOffset = (float) pos / mSlideRange;
			} else if (mCanSlide && mParallaxBy != 0) {
				offset = (int) ((1 - mSlideOffset) * mParallaxBy);
				xStart = nextXStart;
				lp.dimWhenOffset = true;
			} else {
				xStart = nextXStart;
			}

			final int childRight;
			final int childLeft;
			childLeft = xStart - offset;
			childRight = childLeft + childWidth;

			final int childBottom = paddingTop + child.getMeasuredHeight();
			child.layout(childLeft, paddingTop, childRight, childBottom);

			nextXStart += child.getWidth();
		}

		if (mFirstLayout) {
			if (mCanSlide) {
				if (mParallaxBy != 0) {
					parallaxOtherViews(mSlideOffset);
				}
				if (((LayoutParams) mSlideableView.getLayoutParams()).dimWhenOffset) {
					dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
				}
			} else {
				for (int i = 0; i < childCount; i++) {
					dimChildView(getChildAt(i), 0, mSliderFadeColor);
				}
			}
			updateObscuredViewsVisibility(mSlideableView);
		}

		mFirstLayout = false;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (w != oldw) {
			mFirstLayout = true;
		}
	}

	@Override
	public void requestChildFocus(View child, View focused) {
		super.requestChildFocus(child, focused);
		if (!isInTouchMode() && !mCanSlide) {
			mPreservedOpenState = child == mSlideableView;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = MotionEventCompat.getActionMasked(ev);

		if (!mCanSlide && action == MotionEvent.ACTION_DOWN
				&& getChildCount() > 1) {
			final View secondChild = getChildAt(1);
			if (secondChild != null) {
				mPreservedOpenState = !mDragHelper.isViewUnder(secondChild,
						(int) ev.getX(), (int) ev.getY());
			}
		}

		if (!mCanSlide
				|| (mIsUnableToDrag && action != MotionEvent.ACTION_DOWN)) {
			mDragHelper.cancel();
			return super.onInterceptTouchEvent(ev);
		}

		if (action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			mDragHelper.cancel();
			return false;
		}

		boolean interceptTap = false;

		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			mIsUnableToDrag = false;
			final float x = ev.getX();
			final float y = ev.getY();
			mInitialMotionX = x;
			mInitialMotionY = y;

			if (mDragHelper.isViewUnder(mSlideableView, (int) x, (int) y)
					&& isDimmed(mSlideableView)) {
				interceptTap = true;
			}
			break;
		}

		case MotionEvent.ACTION_MOVE: {
			final float x = ev.getX();
			final float y = ev.getY();
			final float adx = Math.abs(x - mInitialMotionX);
			final float ady = Math.abs(y - mInitialMotionY);
			final int slop = mDragHelper.getTouchSlop();

			View view = findViewAtPosition(this, (int) x, (int) y);

			if (adx > slop && ady > adx || view != null) {
				if (view != null) {
					Log.d(TAG, "touch on unscrollable view");
				}
				mDragHelper.cancel();
				mIsUnableToDrag = true;
				return false;
			}
		}
		}

		final boolean interceptForDrag = mDragHelper
				.shouldInterceptTouchEvent(ev);

		return interceptForDrag || interceptTap;
	}

	private View findViewAtPosition(View parent, int x, int y) {
		if (parent instanceof ViewPager) {
			Rect rect = new Rect();
			parent.getGlobalVisibleRect(rect);
			if (rect.contains(x, y)) {
				return parent;
			}
		} else if (parent instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) parent;
			final int length = viewGroup.getChildCount();
			for (int i = 0; i < length; i++) {
				View child = viewGroup.getChildAt(i);
				View viewAtPosition = findViewAtPosition(child, x, y);
				if (viewAtPosition != null) {
					return viewAtPosition;
				}
			}
			return null;
		}
		return null;
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent ev) {
		if (!mCanSlide) {
			return super.onTouchEvent(ev);
		}

		mDragHelper.processTouchEvent(ev);

		final int action = ev.getAction();

		switch (action & MotionEventCompat.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN: {
			final float x = ev.getX();
			final float y = ev.getY();
			mInitialMotionX = x;
			mInitialMotionY = y;
			break;
		}

		case MotionEvent.ACTION_UP: {
			if (isDimmed(mSlideableView)) {
				final float x = ev.getX();
				final float y = ev.getY();
				final float dx = x - mInitialMotionX;
				final float dy = y - mInitialMotionY;
				final int slop = mDragHelper.getTouchSlop();
				if (dx * dx + dy * dy < slop * slop
						&& mDragHelper.isViewUnder(mSlideableView, (int) x,
								(int) y)) {
					closePane();
					break;
				}
			}
			break;
		}
		}

		return true;
	}

	public boolean closePane() {
		if (mFirstLayout || smoothSlideTo(0.f)) {
			mPreservedOpenState = false;
			return true;
		}
		return false;
	}

	public boolean openPane() {
		if (mFirstLayout || smoothSlideTo(1.f)) {
			mPreservedOpenState = true;
			return true;
		}
		return false;
	}

	public boolean isOpen() {
		return !mCanSlide || mSlideOffset == 1;
	}

	public boolean isSlideable() {
		return mCanSlide;
	}

	private void onPanelDragged(int newLeft) {
		if (mSlideableView == null) {
			return;
		}
		final LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();

		final int paddingStart = getPaddingLeft();
		final int lpMargin = lp.leftMargin;
		final int startBound = paddingStart + lpMargin;

		mSlideOffset = (float) (newLeft - startBound) / mSlideRange;

		parallaxOtherViews(mSlideOffset);

		if (lp.dimWhenOffset) {
			dimChildView(mSlideableView, mSlideOffset, mSliderFadeColor);
		}
		dispatchOnPanelSlide(mSlideableView);
	}

	private void dimChildView(View v, float mag, int fadeColor) {
		final LayoutParams lp = (LayoutParams) v.getLayoutParams();

		if (mag > 0 && fadeColor != 0) {
			final int baseAlpha = (fadeColor & 0xff000000) >>> 24;
			int imag = (int) (baseAlpha * mag);
			int color = imag << 24 | (fadeColor & 0xffffff);
			if (lp.dimPaint == null) {
				lp.dimPaint = new Paint();
			}
			lp.dimPaint.setColorFilter(new PorterDuffColorFilter(color,
					PorterDuff.Mode.SRC_OVER));
			if (ViewCompat.getLayerType(v) != ViewCompat.LAYER_TYPE_HARDWARE) {
				ViewCompat.setLayerType(v, ViewCompat.LAYER_TYPE_HARDWARE,
						lp.dimPaint);
			}
			invalidateChildRegion(v);
		} else if (ViewCompat.getLayerType(v) != ViewCompat.LAYER_TYPE_NONE) {
			if (lp.dimPaint != null) {
				lp.dimPaint.setColorFilter(null);
			}
			final DisableLayerRunnable dlr = new DisableLayerRunnable(v);
			mPostedRunnables.add(dlr);
			ViewCompat.postOnAnimation(this, dlr);
		}
	}

	@Override
	protected boolean drawChild(@NonNull Canvas canvas, @NonNull View child,
			long drawingTime) {
		final LayoutParams lp = (LayoutParams) child.getLayoutParams();
		boolean result;
		final int save = canvas.save(Canvas.ALL_SAVE_FLAG);

		if (mCanSlide && !lp.slideable && mSlideableView != null) {
			canvas.getClipBounds(mTmpRect);
			mTmpRect.right = Math.min(mTmpRect.right, mSlideableView.getLeft());
			canvas.scale(1.2f - 0.2f * mSlideOffset,
					1.2f - 0.2f * mSlideOffset, child.getRight(),
					getHeight() / 2);
		} else {
			assert mSlideableView != null;
			canvas.scale(1 - mSlideOffset / 3, 1 - mSlideOffset / 4,
					mSlideableView.getLeft(), getHeight() / 2);
			
		}

		if (!lp.slideable && mSlideOffset == 0) {
			result = true;
		} else {
			if (Build.VERSION.SDK_INT >= 11) { // HC
				result = super.drawChild(canvas, child, drawingTime);
			} else {
				if (lp.dimWhenOffset && mSlideOffset > 0) {
					if (!child.isDrawingCacheEnabled()) {
						child.setDrawingCacheEnabled(true);
					}
					final Bitmap cache = child.getDrawingCache();
					if (cache != null) {
						canvas.drawBitmap(cache, child.getLeft(),
								child.getTop(), lp.dimPaint);
						result = false;
					} else {
						Log.e(TAG, "drawChild: child view " + child
								+ " returned null drawing cache");
						result = super.drawChild(canvas, child, drawingTime);
					}
				} else {
					if (child.isDrawingCacheEnabled()) {
						child.setDrawingCacheEnabled(false);
					}
					result = super.drawChild(canvas, child, drawingTime);
				}
			}
		}

		canvas.restoreToCount(save);

		return result;
	}

	private void invalidateChildRegion(View v) {
		IMPL.invalidateChildRegion(this, v);
	}

	boolean smoothSlideTo(float slideOffset) {
		if (!mCanSlide) {
			return false;
		}
		final LayoutParams lp = (LayoutParams) mSlideableView.getLayoutParams();

		int startBound = getPaddingLeft() + lp.leftMargin;
		int x = (int) (startBound + slideOffset * mSlideRange);

		if (mDragHelper.smoothSlideViewTo(mSlideableView, x,
				mSlideableView.getTop())) {
			setAllChildrenVisible();
			ViewCompat.postInvalidateOnAnimation(this);
			return true;
		}
		return false;
	}

	@Override
	public void computeScroll() {
		if (mDragHelper.continueSettling(true)) {
			if (!mCanSlide) {
				mDragHelper.abort();
				return;
			}

			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private void parallaxOtherViews(float slideOffset) {
		final LayoutParams slideLp = (LayoutParams) mSlideableView
				.getLayoutParams();
		final boolean dimViews = slideLp.dimWhenOffset
				&& slideLp.leftMargin <= 0;
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View v = getChildAt(i);
			if (v == mSlideableView)
				continue;

			final int oldOffset = (int) ((1 - mParallaxOffset) * mParallaxBy);
			mParallaxOffset = slideOffset;
			final int newOffset = (int) ((1 - slideOffset) * mParallaxBy);
			final int dx = oldOffset - newOffset;

			v.offsetLeftAndRight(dx);

			if (dimViews) {
				dimChildView(v, 1 - mParallaxOffset, mCoveredFadeColor);
			}
		}
	}

	boolean isDimmed(View child) {
		if (child == null) {
			return false;
		}
		final LayoutParams lp = (LayoutParams) child.getLayoutParams();
		return mCanSlide && lp.dimWhenOffset && mSlideOffset > 0;
	}

	@Override
	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams();
	}

	@Override
	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams p) {
		return p instanceof MarginLayoutParams ? new LayoutParams(
				(MarginLayoutParams) p) : new LayoutParams(p);
	}

	@Override
	protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
		return p instanceof LayoutParams && super.checkLayoutParams(p);
	}

	@Override
	public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		SavedState ss = new SavedState(superState);
		ss.isOpen = isSlideable() ? isOpen() : mPreservedOpenState;

		return ss;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		SavedState ss = (SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		if (ss.isOpen) {
			openPane();
		} else {
			closePane();
		}
		mPreservedOpenState = ss.isOpen;
	}

	private class DragHelperCallback extends ViewDragHelper.Callback {

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return !mIsUnableToDrag
					&& ((LayoutParams) child.getLayoutParams()).slideable;
		}

		@Override
		public void onViewDragStateChanged(int state) {
			if (mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE) {
				if (mSlideOffset == 0) {
					updateObscuredViewsVisibility(mSlideableView);
					dispatchOnPanelClosed(mSlideableView);
					mPreservedOpenState = false;
				} else {
					dispatchOnPanelOpened(mSlideableView);
					mPreservedOpenState = true;
				}
			}
		}

		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			setAllChildrenVisible();
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			onPanelDragged(left);
			invalidate();
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			final LayoutParams lp = (LayoutParams) releasedChild
					.getLayoutParams();

			int left = getPaddingLeft() + lp.leftMargin;
			if (xvel > 0 || (xvel == 0 && mSlideOffset > 0.5f)) {
				left += mSlideRange;
			}
			mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
			invalidate();
		}

		@Override
		public int getViewHorizontalDragRange(View child) {
			return mSlideRange;
		}

		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			final LayoutParams lp = (LayoutParams) mSlideableView
					.getLayoutParams();

			int startBound = getPaddingLeft() + lp.leftMargin;
			int endBound = startBound + mSlideRange;

			return Math.min(Math.max(left, startBound), endBound);
		}

		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
			return child.getTop();
		}

		@Override
		public void onEdgeDragStarted(int edgeFlags, int pointerId) {
			mDragHelper.captureChildView(mSlideableView, pointerId);
		}
	}

	public static class LayoutParams extends MarginLayoutParams {
		private static final int[] ATTRS = new int[] { android.R.attr.layout_weight };

		public float weight = 0;

		boolean slideable;

		boolean dimWhenOffset;

		Paint dimPaint;

		public LayoutParams() {
			super(MATCH_PARENT, MATCH_PARENT);
		}

		public LayoutParams(ViewGroup.LayoutParams source) {
			super(source);
		}

		public LayoutParams(MarginLayoutParams source) {
			super(source);
		}

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);

			final TypedArray a = c.obtainStyledAttributes(attrs, ATTRS);
			this.weight = a.getFloat(0, 0);
			a.recycle();
		}

	}

	static class SavedState extends BaseSavedState {
		boolean isOpen;

		SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			isOpen = in.readInt() != 0;
		}

		@Override
		public void writeToParcel(@NonNull Parcel out, int flags) {
			super.writeToParcel(out, flags);
			out.writeInt(isOpen ? 1 : 0);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	interface SlidingPanelLayoutImpl {
		void invalidateChildRegion(ResideLayout parent, View child);
	}

	static class SlidingPanelLayoutImplBase implements SlidingPanelLayoutImpl {
		public void invalidateChildRegion(ResideLayout parent, View child) {
			ViewCompat.postInvalidateOnAnimation(parent, child.getLeft(),
					child.getTop(), child.getRight(), child.getBottom());
		}
	}

	static class SlidingPanelLayoutImplJB extends SlidingPanelLayoutImplBase {
		private Method mGetDisplayList;
		private Field mRecreateDisplayList;

		SlidingPanelLayoutImplJB() {
			try {
				mGetDisplayList = View.class.getDeclaredMethod(
						"getDisplayList", (Class[]) null);
			} catch (NoSuchMethodException e) {
				Log.e(TAG,
						"Couldn't fetch getDisplayList method; dimming won't work right.",
						e);
			}
			try {
				mRecreateDisplayList = View.class
						.getDeclaredField("mRecreateDisplayList");
				mRecreateDisplayList.setAccessible(true);
			} catch (NoSuchFieldException e) {
				Log.e(TAG,
						"Couldn't fetch mRecreateDisplayList field; dimming will be slow.",
						e);
			}
		}

		@Override
		public void invalidateChildRegion(ResideLayout parent, View child) {
			if (mGetDisplayList != null && mRecreateDisplayList != null) {
				try {
					mRecreateDisplayList.setBoolean(child, true);
					mGetDisplayList.invoke(child, (Object[]) null);
				} catch (Exception e) {
					Log.e(TAG, "Error refreshing display list state", e);
				}
			} else {
				child.invalidate();
				return;
			}
			super.invalidateChildRegion(parent, child);
		}
	}

	static class SlidingPanelLayoutImplJBMR1 extends SlidingPanelLayoutImplBase {
		@Override
		public void invalidateChildRegion(ResideLayout parent, View child) {
			ViewCompat.setLayerPaint(child,
					((LayoutParams) child.getLayoutParams()).dimPaint);
		}
	}

	private class DisableLayerRunnable implements Runnable {
		final View mChildView;

		DisableLayerRunnable(View childView) {
			mChildView = childView;
		}

		@Override
		public void run() {
			if (mChildView.getParent() == ResideLayout.this) {
				ViewCompat.setLayerType(mChildView, ViewCompat.LAYER_TYPE_NONE,
						null);
				invalidateChildRegion(mChildView);
			}
			mPostedRunnables.remove(this);
		}
	}
}

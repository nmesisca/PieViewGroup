package com.chipset.pieviewgroup;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.TextPaint;
import android.view.View;
import android.widget.FrameLayout;

/*
 * Created by nmesisca on 05/11/16 18:32
 * Copyright ® 2016. All rights reserved.
 * Last modified : 05/11/16 18:32
 */
class LegendMini extends FrameLayout {

	private static final int PAD_H = 22;
	private static final int PAD_V = 20; // Space between child views.
	private LegendTypes mType;
	private TextPaint mLegendPaint;
	private Paint mBoxPaint;
	private float mLegendTextSizePx;
	private LegendItem[] legendItems;
	private Drawable icon;
	private Context mContext;

	public LegendMini(@NonNull Context context) {
		super(context);
		init(context);
	}

	@Override
	protected void onLayout(boolean b, int left, int top, int right, int bottom) {
		final int width = right - left;
		int xpos = getPaddingLeft();
		int ypos = getPaddingTop();
		int height = 0;
		for(int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);
			if(child.getVisibility() != GONE) {
				final int childw = child.getMeasuredWidth();
				final int childh = child.getMeasuredHeight();
				height = Math.max(height, childh);
				if(xpos + childw > width) {
					xpos = getPaddingLeft();
					ypos += height + PAD_V;
				}
				child.layout(xpos, ypos, xpos + childw, ypos + childh);
				xpos += childw + PAD_H;
			}
		}
	}

	private void init(@NonNull Context context) {
		setWillNotDraw(false);
		setPaints();
		this.mContext = context;
		if (this.icon==null) {
			if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
				this.icon = VectorDrawableCompat.create(getResources(), R.drawable.ic_drop, context.getTheme());
			} else {
				this.icon = getResources().getDrawable(R.drawable.ic_drop, context.getTheme());
			}
		}
	}

// REGION Lifecycle
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		if (parentHeight<parentWidth) {
			int myWidth = parentWidth-parentHeight;
			setMeasuredDimension(myWidth, parentHeight);
		} else {
			int myHeight = parentHeight-parentWidth;
			setMeasuredDimension(parentWidth, myHeight);
		}
	}
//ENDREGION Lifecycle

	/**
	 * Set up paints and brushes
	 */
	private void setPaints() {
		mLegendPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		mLegendPaint.setTextSize(mLegendTextSizePx);
		mLegendPaint.setTextAlign(Paint.Align.LEFT);
		mBoxPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBoxPaint.setStyle(Paint.Style.FILL);
	}

	public void setLegendDrawable(Drawable icon) {
		this.icon = icon;
		start();
	}

	public void setLegendType(LegendTypes type) {
		this.mType = type;
		start();
	}

	public void setLegendItems(LegendItem[] legendItems) {
		this.legendItems = legendItems;
		start();
	}

	public void setLegendTextSizePx(float size) {
		this.mLegendTextSizePx = size;
		this.mLegendPaint.setTextSize(size);
		start();
	}

	private void start() {
		if (this.legendItems!=null) buildLegendViews();
	}

	private void buildLegendViews() {
		removeAllViews();
		for (LegendItem item : legendItems) {
			if (item.percent!=0) {
				item.icon=this.icon;
//				item.text = mType == LegendTypes.FULL ? String.format("%s : %d%%", item.text,
//						item.percent) :  item.text;
				LegendItemView itemView = new LegendItemView(mContext, item, mBoxPaint, mLegendPaint);
				addView(itemView);
			}
		}
	}
}

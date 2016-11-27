package com.chipset.pieviewgroup;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;

/*
 * Created by nmesisca on 05/11/16 18:32
 * Copyright ® 2016. All rights reserved.
 * Last modified : 05/11/16 18:32
 */
class LegendMini extends FrameLayout {

	private static final int PAD_H = 22;
	private static final int PAD_V = 20; // Space between child views.
	@Nullable
	public Typeface mLegendTypeface = null;
	public int mIconId = 0;
	public LegendTypes mLegendType = LegendTypes.SHORT;
	public float mLegendTextSize;
	public Slice[] mSlices;
	private ArrayList<LegendItem> legendItems;
	private final Context mContext;

	public LegendMini(@NonNull Context context) {
		super(context);
		this.mContext = context;
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
//ENDREGION Lifecycle

	/**
	 * Builds the Legend
	 */
	public void build() {
		if (mSlices==null || mSlices.length==0) return;
		this.legendItems = buildLegendItems();
		buildLegendViews();
	}

	/**
	 * Build the legend items in the form of an array of LegendItems from the Slice objects
	 *
	 * @return The array of LegendItem objects
	 */
	@NonNull
	private ArrayList<LegendItem> buildLegendItems() {
		final ArrayList<LegendItem> items = new ArrayList<>();
		for(Slice slice : mSlices) {
			final LegendItem item = new LegendItem();
			item.percent = slice.percent;
			item.text = mLegendType == LegendTypes.FULL ? String.format("%s : %d%%", slice.label,
					item.percent) :  slice.label;
			item.color = slice.sliceColor;
			items.add(item);
		}
		return items;
	}

	/**
	 * Builds the legend's Views and attaches the to the hierarchy
	 */
	public void buildLegendViews() {
		if (legendItems==null) return;
		removeAllViews();
		for (LegendItem item: legendItems) {
			if (item.percent!=0) {
				item.textsize = this.mLegendTextSize;
				item.typeface = this.mLegendTypeface;
				item.iconid = this.mIconId;
				final LegendItemView itemView = new LegendItemView(mContext, item);
				addView(itemView);
			}
		}
	}

	@Override
	public void addView(View child) {
		if (child instanceof LegendItemView) super.addView(child);
	}
}

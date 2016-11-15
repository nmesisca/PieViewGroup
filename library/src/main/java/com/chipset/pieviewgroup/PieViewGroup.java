package com.chipset.pieviewgroup;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Map;

/*
 * Created by nmesisca on 05/11/16 18:18
 * Copyright ® 2016. All rights reserved.
 * Last modified : 05/11/16 18:04
 */
public class PieViewGroup extends FrameLayout {

	private static final String TAG = "PieViewGroup";
	// defaults
	private static final int LEGEND_TEXT_SIZE_SP = 14;
	private static final int LABEL_TEXT_SIZE_SP = 12;
	private static final boolean PIE_SHOW_LABELS = true;
	private static final int DONUT_RADIUS_PERCENT = 43;
	private static final int PAD_H = 0;
	private static final int PAD_V = 0; // Space between child views.

	@Nullable private Map<String, Integer> mData;
	private Context mContext;
	private int colorPrimary;
	private ArrayList<Integer> mColors = new ArrayList<>();
	private Slice[] mSlices;
	private PieMini pieMini;
	private LegendMini legendMini;
	private LegendTypes mLegendType = LegendTypes.SHORT;
	private Drawable mLegendDrawable;

	public PieViewGroup(@NonNull Context context) {
		super(context);
		init(context, null);
	}

	public PieViewGroup(@NonNull Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

// REGION Lifecycle
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
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
					ypos += height;
				}
				child.layout(xpos, ypos, xpos + childw, ypos + childh);
				xpos += childw + PAD_H;
			}
		}
	}
//ENDREGION Lifecycle

	/**
	 * Initiate variables and read from xml attribs
	 *
	 * @param context The context object from constructor
	 * @param attrs The Attributes from constructor
	 */
	private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
		mContext = context;
		pieMini = new PieMini(context);
		legendMini = new LegendMini(mContext);
		colorPrimary = Utils.readThemeColor(context, R.attr.colorPrimary );
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			this.mLegendDrawable = getResources().getDrawable(R.drawable.ic_drop, context.getTheme());
		} else {
			this.mLegendDrawable = VectorDrawableCompat.create(getResources(), R.drawable.ic_drop, context.getTheme());
		}
		// read attributes from XML layout
		ReadAttrs(attrs);
	}

	private void ReadAttrs(AttributeSet attrs) {
		if (attrs != null) {
			final TypedArray ta = mContext.getTheme().obtainStyledAttributes(attrs, R.styleable.PieViewGroup, 0, 0);
			try {
				final int colorsId = ta.getResourceId(R.styleable.PieViewGroup_pvg_colorArray, 0);
				pieMini.showLabels(ta.getBoolean(R.styleable.PieViewGroup_pvg_showLabels, PIE_SHOW_LABELS));
				pieMini.setLabelTextSizePx(Utils.PVGConvert.sp2px(mContext,
						ta.getDimension(R.styleable.PieViewGroup_pvg_labelTextSize, LABEL_TEXT_SIZE_SP)));
				legendMini.setLegendTextSizePx(Utils.PVGConvert.sp2px(mContext,
						ta.getDimension(R.styleable.PieViewGroup_pvg_legendTextSize, LEGEND_TEXT_SIZE_SP)));
				pieMini.setDonutRadiusPercent(ta.getInt(R.styleable.PieViewGroup_pvg_donutRadiusPercent, DONUT_RADIUS_PERCENT));
				final int ctype = ta.getInt(R.styleable.PieViewGroup_pvg_chartType, ChartTypes.PIE.ordinal());
				pieMini.setChartType(ChartTypes.values()[ctype]);
				final int ltype = ta.getInt(R.styleable.PieViewGroup_pvg_legendType, LegendTypes.SHORT.ordinal());
				mLegendType = (LegendTypes.values()[ltype]);
				if (colorsId != 0) {
					Log.e(TAG, "Colors from XML");
					final int[] y = getResources().getIntArray(colorsId);
					for (Integer color: y) {
						mColors.add(color);
					}
				}
			} finally {
				ta.recycle();
			}
		}
	}

	/**
	 * Override to pass Background color to the PieMini object
	 *
	 * @param color The new background color
	 */
	@Override
	public void setBackgroundColor(int color) {
		super.setBackgroundColor(color);
		pieMini.setBackgroundColor(color);
	}

	/**
	 * Set the colors to be used in the pie chart
	 *
	 * @param colors The input array of colors
	 */
	public void setColors(ArrayList<Integer> colors) { this.mColors = colors; }

	/**
	 * Set the data source for the pie chart
	 *
	 * @param data Source data for the chart, as pairs of key,percentage
	 */
	public void setData(@NonNull Map<String, Integer> data) {
		if (!data.isEmpty()) {
			this.mData = data;
			this.mSlices = buildSlices(data.size());
			pieMini.setSlices(this.mSlices);
			legendMini.setLegendItems(buildLegendItems());
			if (getChildCount()==0) {
				addView(pieMini);
				if (mLegendType!=LegendTypes.NONE) addView(legendMini);
			}
			postInvalidate();
		}
	}

	public void setLegendDrawable(Drawable icon) {
		if (icon!=null)	this.mLegendDrawable = icon;
	}

	public void setLegendDrawableId(int iconId) {
		if (iconId!=0) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				this.mLegendDrawable = getResources().getDrawable(iconId, mContext.getTheme());
			} else {
				this.mLegendDrawable = VectorDrawableCompat.create(getResources(), iconId, mContext.getTheme());
			}
		}
	}

	/**
	 * Control the visual appearance of the chart
	 *
	 * @param type ChartType enum value : 0 for PIE, 1 for DONUT
	 */
	public void setChartType (ChartTypes type) {
		pieMini.setChartType(type);
	}

	/**
	 * Controls the visual appearance of the legend
	 *
	 * @param type Int value : 0 for NONE, 1 for SHORT, 2 for FULL size
	 */
	public void setLegendType (LegendTypes type) {
		this.mLegendType = type;
		//legendMini.setLegendType(type);
	}

	/**
	 * Control the size of the donut hole, in percentage of the pie's radius
	 *
	 * @param percent The percentage of the pie's radius that will be the donut radius
	 */
	public void setDonutRadiusPercent(int percent) {
		if (percent>-1 & percent<101) pieMini.setDonutRadiusPercent(percent);
	}

	/**
	 * Control whether the labels are visible or not.
	 *
	 * @param showLabels true if the labels should be visible, false otherwise
	 */
	public void showLabels(boolean showLabels) {
		pieMini.showLabels(showLabels);
	}

	/**
	 * Control the size of the text in the legend items.
	 *
	 * @param textSize size (in sp) of the text for the LegendItems
	 */
	public void setLegendTextSizeSp(float textSize) {
		if (textSize>0) legendMini.setLegendTextSizePx(Utils.PVGConvert.sp2px(mContext, textSize));
	}

	/**
	 * Control the size of the text in the labels.
	 *
	 * @param textSize size (in sp) of the text for the labels
	 */
	public void setLabelTextSizeSp(float textSize) {
		if (textSize>0) pieMini.setLabelTextSizePx(Utils.PVGConvert.sp2px(mContext, textSize));
	}

	/**
	 * Build the slices that compose the Pie chart, in the form of an array of Slice objects
	 *
	 * @param size The maximum number of elements the array will contain
	 * @return The resulting array of Slice objects
	 */
	@NonNull
	private Slice[] buildSlices(int size) {
		final Slice[] slices = new Slice[size];
		int total = 0;
		// checking data is valid and normalizing if necessary
		for (Map.Entry<String, Integer> entry : mData.entrySet())
			total += entry.getValue();
		// create slices and legend items
		int i = 0;
		float arcStart = 0;
		for (Map.Entry<String, Integer> entry : mData.entrySet()) {
			final Slice slice = new Slice();
			// normalize data if required
			slice.label = entry.getKey();
			slice.percent = ((total != 100) ? entry.getValue() * 100 / total : entry.getValue());
			//calculate start & end angle for each data value
			final float endAngle = total == 0 ? 0 : 360 * slice.percent / (float) total;
			final float newStartAngle = arcStart + endAngle;
			slice.labelOffset = getTextOffset(slice.toString(), pieMini.mLabelPaint);
			if (mColors.size() == i) mColors.add(Utils.PVGColors.generateRandomColor(colorPrimary));
			slice.sliceColor = mColors.get(i);
			slice.labelColor = Utils.PVGColors.getContrastTextColor(slice.sliceColor);
			slice.startDegree = arcStart;
			slice.endDegree = arcStart + endAngle;
			slice.sweepDegree = endAngle;
			slices[i] = slice;
			arcStart = newStartAngle;
			++i;
		}
		return slices;
	}

	/**
	 * Build the legend items in the form of an array of LegendItems from the Slice objects
	 *
	 * @return The array of LegendItem objects
	 */
	@NonNull
	private LegendItem[] buildLegendItems() {
		final LegendItem[] items = new LegendItem[mSlices.length];
		for(int i=0; i<mSlices.length; i++) {
			final LegendItem item = new LegendItem();
			//item.text = mSlices[i].label;
			item.percent = mSlices[i].percent;
			item.text = mLegendType == LegendTypes.FULL ? String.format("%s : %d%%", mSlices[i].label,
					item.percent) :  mSlices[i].label;
			item.color = mSlices[i].sliceColor;
			item.icon = this.mLegendDrawable;
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				item.icon.setTint(item.color);
			} else {
				Utils.PVGColors.tintMyDrawable(item.icon, item.color);
			}
			items[i]=item;
		}
		return items;
	}

	/**
	 * Measure the length of a string as it will be laid out
	 *
	 * @param text The string to be measured
	 * @param paint The paint that will be used to draw the string
	 * @return A Point() representing the x and y offsets
	 */
	@NonNull
	private Point getTextOffset(@NonNull String text, @NonNull Paint paint) {
		final Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		return new Point((bounds.left + bounds.width())/2, (bounds.bottom + bounds.height())/2);
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		final ObjectAnimator anim = ObjectAnimator.ofInt(pieMini, "donutRadiusPercent", 0, DONUT_RADIUS_PERCENT);
		anim.setDuration(700);
		anim.start();
	}
}

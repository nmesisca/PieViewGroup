package com.chipset.pieviewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.TextPaint;
import android.view.View;

/*
 * Created by nmesisca on 05/11/16 18:32
 * Copyright ® 2016. All rights reserved.
 * Last modified : 05/11/16 18:32
 */
class LegendMini extends View {

	private static final int LEGEND_ITEM_BOX_SIZE_DP = 15;
	private static final int LEGEND_ITEM_VERT_INTERLINE = 18;
	private static final int LEGEND_ITEM_MARGIN = 36;
	private static final int LEGEND_ITEM_BOX_MARGIN = 12;
	private LegendTypes mType;
	private TextPaint mLegendPaint;
	private Paint mBoxPaint;
	private float mLegendBoxSizePx;
	private float mLegendTextSizePx;
	private LegendItem[] legendItems;
	private Slice[] slices;
	private Drawable mDropVector;

	public LegendMini(@NonNull Context context) {
		super(context);
		init(context);
	}

	private void init(@NonNull Context context) {
		setWillNotDraw(false);
		// initialize variables
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			mDropVector = VectorDrawableCompat.create(getResources(), R.drawable.ic_drop, context.getTheme());
		} else {
			mDropVector = getResources().getDrawable(R.drawable.ic_drop, context.getTheme());
		}
		this.mLegendBoxSizePx = Utils.PVGConvert.dp2px(context, LEGEND_ITEM_BOX_SIZE_DP);
		// setup paints and colors
		setPaints();
	}

// REGION Lifecycle
	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		super.onDraw(canvas);
		drawLegend(canvas);
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

	public void setLegendType(LegendTypes type) { this.mType = type; }

	public void setSlices(Slice[] slices) {
		this.slices = slices;
		legendItems = buildLegend(this.slices.length);
	}

	public void setLegendTextSizePx(float size) {
		this.mLegendTextSizePx = size;
		mLegendPaint.setTextSize(size);
	}

	/**
	 * Calculate an array of LegendItem
	 */
	@NonNull
	private LegendItem[] buildLegend(int size) {
		LegendItem[] items = new LegendItem[size];
		int totWidth = 0;
		for (int i=0; i<slices.length; i++) {
			final LegendItem legendItem = new LegendItem();
			legendItem.percent = slices[i].percent;
			legendItem.text = mType == LegendTypes.FULL ? String.format("%s : %d%%", slices[i].label,
					legendItem.percent) :  slices[i].label;
			Rect bounds = new Rect();
			mLegendPaint.getTextBounds(legendItem.text, 0, legendItem.text.length(), bounds);
			float itemSize = mLegendBoxSizePx + bounds.width() + LEGEND_ITEM_BOX_MARGIN;
			RectF box_bounds = new RectF(0, 0, mLegendBoxSizePx, mLegendBoxSizePx );
			RectF text_bounds = new RectF(0,bounds.height(),bounds.width(),0);
			box_bounds.offset(totWidth, LEGEND_ITEM_VERT_INTERLINE);
			text_bounds.offset(box_bounds.right + LEGEND_ITEM_BOX_MARGIN, LEGEND_ITEM_VERT_INTERLINE);
			legendItem.boxrec = box_bounds;
			legendItem.textrec = text_bounds;
			items[i]=legendItem;
			if (legendItem.percent!=0) totWidth += itemSize+LEGEND_ITEM_MARGIN;
		}
		return items;
	}

	/**
	 * Draw the Legend of the chart
	 *
	 * @param canvas Canvas on which to draw
	 */
	private void drawLegend(@NonNull Canvas canvas) {
		float totWidth = 0;
		int row = 0;
		for (int i=0; i<legendItems.length; i++) {
			if (legendItems[i].percent == 0) continue;
			float width = legendItems[i].boxrec.width()+legendItems[i].textrec.width()+ LEGEND_ITEM_BOX_MARGIN;
			if (legendItems[i].textrec.right>getWidth()) {
				row++;
				legendItems[i].boxrec.offset(-totWidth, row*30 + LEGEND_ITEM_VERT_INTERLINE);
				legendItems[i].textrec.offset(-totWidth, row*30 + LEGEND_ITEM_VERT_INTERLINE);
			}
			mBoxPaint.setColor(slices[i].sliceColor);
			Utils.PVGColors.tintMyDrawable(mDropVector, slices[i].sliceColor);
			canvas.drawBitmap(Utils.getBitmapFromVectorDrawable(mDropVector), null, legendItems[i].boxrec, null);
			canvas.drawText(legendItems[i].text, legendItems[i].textrec.left,
					legendItems[i].textrec.top, mLegendPaint);
			totWidth += width;
		}
	}
}

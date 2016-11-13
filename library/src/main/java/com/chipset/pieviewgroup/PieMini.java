package com.chipset.pieviewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.View;

/*
 * Created by nmesisca on 05/11/16 18:34
 * Copyright ® 2016. All rights reserved.
 * Last modified : 05/11/16 18:34
 */
class PieMini extends View {

	private static final int PIE_VIEW_MARGIN = 6;
	private static final int PIE_VIEW_OUTER_RING = 8;
	@NonNull private final Point mPieCenter = new Point();
	@NonNull private final RectF mPieBounds = new RectF();
	public Paint mLabelPaint;
	private float mLabelTextSizePx;
	private int mMargin = PIE_VIEW_MARGIN;
	private int donutRadiusPercent;
	private int mDonutRadius = 0;
	private boolean mShowLabels = true;
	private Paint mPiePaint;
	private Paint mOuterPaint;
	private Paint mLinePaint;
	private ChartTypes mType;
	private int mOuterRing = PIE_VIEW_OUTER_RING;
	private int mPieRadius;
	private Slice[] slices;
	private int mBackgroundColor = Color.WHITE;

	public PieMini(Context context) {
		super(context);
		init();
	}

	private void init() {
		setWillNotDraw(false);
		setPaints();
	}

// REGION Lifecycle
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		int shorter = width>height ? height : width;
		mPieRadius = (shorter-getPaddingLeft()-getPaddingRight()- mMargin*2-mOuterRing*2)/2;
		mDonutRadius = donutRadiusPercent*mPieRadius/100;
		mPieCenter.set(mPieRadius+getPaddingLeft()+mMargin+mOuterRing, mPieRadius+getPaddingTop()+mMargin+mOuterRing);
		mPieBounds.set(mPieCenter.x-mPieRadius, mPieCenter.y-mPieRadius, mPieCenter.x+mPieRadius,
				mPieCenter.y+mPieRadius);
		setMeasuredDimension(shorter, shorter);
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawCircle(mPieCenter.x, mPieCenter.y, mPieRadius+mOuterRing, mOuterPaint);
		drawSlices(canvas);
	}
//ENDREGION Lifecycle

	@Override
	public void setBackgroundColor(int color) {
		this.mBackgroundColor = color;
		super.setBackgroundColor(color);
	}

	public void setSlices(Slice[] slices) { this.slices = slices; }

	public void showLabels(boolean show) {
		this.mShowLabels = show;
		invalidate();
	}

	public void setLabelTextSizePx(float sizePx) {
		this.mLabelTextSizePx = sizePx;
		mLabelPaint.setTextSize(mLabelTextSizePx);
		invalidate();
	}

	public void setDonutRadiusPercent(int radiusPercent) {
		this.donutRadiusPercent=radiusPercent;
		mDonutRadius = donutRadiusPercent*mPieRadius/100;
		invalidate();
	}

	public void setChartType(ChartTypes type) {
		this.mType = type;
		invalidate();
	}

	/**
	 * Set up paints and brushes
	 */
	private void setPaints() {
		mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setStrokeWidth(mType==ChartTypes.DONUT ? 2.8f : 1.8f);
		mLinePaint.setStrokeJoin(Paint.Join.ROUND);
		mLinePaint.setStrokeCap(Paint.Cap.ROUND);

		mLabelPaint = new Paint();
		mLabelPaint.setTextSize(mLabelTextSizePx);
		mLabelPaint.setTextAlign(Paint.Align.CENTER);

		mPiePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPiePaint.setStyle(Paint.Style.FILL);

		mOuterPaint = new Paint(mLinePaint);
		mOuterPaint.setColor(Color.LTGRAY);
	}

	/**
	 * Draw the Slice objects on the canvas
	 *
	 * @param canvas Canvas on which to draw
	 */
	private void drawSlices(@NonNull Canvas canvas) {
		if (slices != null) {
			mLinePaint.setColor(this.mBackgroundColor);
			for (Slice slice : slices) {
				mPiePaint.setColor(slice.sliceColor);
				canvas.drawArc(mPieBounds, slice.startDegree, slice.sweepDegree, true, mPiePaint);
				if (mType==ChartTypes.DONUT) {
					mPiePaint.setColor(mBackgroundColor);
					canvas.drawCircle(mPieCenter.x, mPieCenter.y, mDonutRadius, mPiePaint);
				}
				drawBorders(canvas, slice.startDegree);
				drawBorders(canvas, slice.endDegree);
				if (mShowLabels & slice.percent != 0) drawLabelItem(canvas, slice);
			}
		}
	}

	/**
	 * Draw a white separators between slices
	 *
	 * @param canvas Canvas on which to draw
	 * @param angle The angle the separator sits at
	 */
	private void drawBorders(@NonNull Canvas canvas, float angle) {
		final float x = (float) (mPieCenter.x + mPieRadius * Math.cos(Math.toRadians(angle)));
		final float y = (float) (mPieCenter.y + mPieRadius * Math.sin(Math.toRadians(angle)));
		canvas.drawLine(mPieCenter.x, mPieCenter.y, x, y, mLinePaint);
	}

	/**
	 * Draw the percentage labels inside each Slice
	 *
	 * @param canvas Canvas on which to draw
	 * @param slice The Slice the label refers to
	 */
	private void drawLabelItem(@NonNull Canvas canvas, @NonNull Slice slice) {
		if (slice.toString().isEmpty()) return;
		float angle = (slice.startDegree + slice.endDegree) /2;
		mLabelPaint.setColor(slice.labelColor);
		final int pos = mType==ChartTypes.DONUT ? ((mPieRadius-mDonutRadius)/2)+mDonutRadius : mPieRadius/2;
		final float x = (float) (mPieCenter.x + (pos) * Math.cos(Math.toRadians(angle)));
		final float y = (float) (mPieCenter.y + (pos) * Math.sin(Math.toRadians(angle)));
		canvas.drawText(slice.toString(), x, y+slice.labelOffset.y, mLabelPaint);
	}
}

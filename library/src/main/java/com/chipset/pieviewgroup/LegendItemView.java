package com.chipset.pieviewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.view.View;

class LegendItemView extends View {
	private static final String TAG = "LegendItemView";
	private static final int LEGEND_ITEM_BOX_MARGIN = 12;
	private static final int LEGEND_ITEM_BOX_SIZE_DP = 15;
	private static final int LEGEND_ITEM_MARGIN = 26;
	private TextPaint mTextPaint;
	private Paint mBoxPaint;
	private float mLegendBoxSizePx;
	private LegendItem item;
	private RectF box_bounds;
	private RectF text_bounds;
	private int itemSize;

	private LegendItemView (Context context) {
		super(context);
	}

	public LegendItemView(@NonNull Context context, LegendItem item, Paint boxPaint, TextPaint textPaint) {
		super(context);
		this.item = item;
		this.mBoxPaint = boxPaint;
		this.mTextPaint = textPaint;
		this.mLegendBoxSizePx = Utils.PVGConvert.dp2px(context, LEGEND_ITEM_BOX_SIZE_DP);
		buildView();
	}

// REGION Lifecycle
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(itemSize,
				(int)Math.max(box_bounds.height(), text_bounds.height()));
	}

	@Override
	protected void onDraw(@NonNull Canvas canvas) {
		super.onDraw(canvas);
		mBoxPaint.setColor(item.color);
		canvas.drawBitmap(Utils.getBitmapFromVectorDrawable(item.icon), null, box_bounds, null);
		canvas.drawText(item.text, text_bounds.left, text_bounds.top, mTextPaint);
	}
//ENDREGION Lifecycle

	private void buildView() {
		Rect bounds = new Rect();
		mTextPaint.getTextBounds(item.text, 0, item.text.length(), bounds);
		//float itemSize = mLegendBoxSizePx + bounds.width() + LEGEND_ITEM_BOX_MARGIN;
		box_bounds = new RectF(0, 0, mLegendBoxSizePx, mLegendBoxSizePx );
		text_bounds = new RectF(0,bounds.height(),bounds.width(),0);
		text_bounds.offset(box_bounds.right + LEGEND_ITEM_BOX_MARGIN, 0);
		this.itemSize = (int)(box_bounds.width()+text_bounds.width()+ LEGEND_ITEM_BOX_MARGIN + LEGEND_ITEM_MARGIN);
	}
}

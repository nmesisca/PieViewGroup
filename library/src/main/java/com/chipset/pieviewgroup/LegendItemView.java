package com.chipset.pieviewgroup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.ViewGroup;

public class LegendItemView extends AppCompatTextView {

	private static final String TAG = "LegendItemView";
	private Context mContext;
	private static final int LEGEND_ITEM_BOX_SIZE_DP = 13;

	private LegendItemView (@NonNull Context context) {
		super(context);
	}

	public LegendItemView(@NonNull Context context, @NonNull LegendItem item) {
		super(context);
		this.mContext = context;
		this.setText(item.text);
		this.setClickable(false);
		this.setFocusable(false);
		this.setTextSize(item.textsize);
		this.setCompoundDrawablePadding(6);
		this.setSingleLine(true);
		if (item.typeface!=null) this.setTypeface(item.typeface);
		this.setGravity(Gravity.CENTER_VERTICAL);
		this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		// detect RTL environment
		final boolean rtl = ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL;
		Drawable start, end;
		// Account for RTL and
		if (item.iconid==0) {
			final Drawable marker = createMarker(item.color);
			start = rtl ? null : marker;
			end = rtl ? marker : null;
		} else {
			// Obtain DrawableManager
			final AppCompatDrawableManager dm = AppCompatDrawableManager.get();
			start = rtl ? null : dm.getDrawable(context, item.iconid);
			end = rtl ? dm.getDrawable(context, item.iconid) : null;
			Utils.PVGColors.tintMyDrawable(start, item.color);
		}
		// apply the compound Drawables
		setCompoundDrawablesWithIntrinsicBounds(start, null, end, null);
	}

	@NonNull
	private Drawable createMarker(int color) {
		int size = (int) Utils.PVGConvert.dp2px(mContext, LEGEND_ITEM_BOX_SIZE_DP);
		ShapeDrawable marker = new ShapeDrawable (new RectShape());
		marker.setIntrinsicWidth (size);
		marker.setIntrinsicHeight (size);
		marker.getPaint().setColor(color);
		return marker;
	}
}

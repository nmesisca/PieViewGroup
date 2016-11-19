package com.chipset.pieviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.util.TypedValue;
import java.util.Random;

/*
 * Created by nmesisca on 05/11/16 18:23
 * Copyright ® 2016. All rights reserved.
 * Last modified : 05/11/16 18:23
 */
class Utils {

	static final String TAG = "Utils";

	static int readThemeColor(@NonNull Context context, int colorid) {
		final TypedValue typedValue = new TypedValue();
		final TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { colorid });
		final int color = a.getColor(0, 0);
		a.recycle();
		return color;
	}

	static class PVGColors {

		static int generateRandomColor(int mix) {
			final Random random = new Random();
			int red = random.nextInt(256);
			int green = random.nextInt(256);
			int blue = random.nextInt(256);
			// mix the color
			if (mix != 0) {
				red = (red + Color.red(mix)) / 2;
				green = (green + Color.green(mix)) / 2;
				blue = (blue + Color.blue(mix)) / 2;
			}
			return Color.argb(255, red, green, blue);
		}

		static void tintMyDrawable(@Nullable Drawable drawable, int color) {
			if (drawable==null) return;
			drawable = DrawableCompat.wrap(drawable);
			DrawableCompat.setTint(drawable.mutate(), color);
		}

		static int getContrastTextColor(int colorIntValue) {
			final int red = Color.red(colorIntValue);
			final int green = Color.green(colorIntValue);
			final int blue = Color.blue(colorIntValue);
			final double brightness_value=Math.pow(0.22475*Math.pow(red,2.235) + 0.7154*Math.pow(green,
					2.235) + 0.05575*Math.pow(blue,2.235), 1/2.235);
			return brightness_value > 164 ? 0xFF000000 : 0xFFFFFFFF;
		}
	}

	static class PVGConvert {

		/**
		 * Convert from DIP to PX
		 */
		static float dp2px(@NonNull Context context, float value) {
			return TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP,
					value,
					context.getResources().getDisplayMetrics());
		}

		/**
		 * Convert from SP to PX
		 */
		static float sp2px(@NonNull Context context, float value) {
			return TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_SP,
					value,
					context.getResources().getDisplayMetrics());
		}
	}
}

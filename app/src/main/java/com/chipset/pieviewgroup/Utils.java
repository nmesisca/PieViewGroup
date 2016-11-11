package com.chipset.pieviewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;
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

	static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
		Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			drawable = (DrawableCompat.wrap(drawable)).mutate();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	static Bitmap getBitmapFromVectorDrawable(Drawable drawable) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			drawable = (DrawableCompat.wrap(drawable)).mutate();
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
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

		static Drawable tintMyDrawable(Drawable drawable, int color) {
			drawable = DrawableCompat.wrap(drawable);
			DrawableCompat.setTint(drawable, color);
			DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
			return drawable;
		}

		static int getContrastTextColor(int colorIntValue) {
			int red = Color.red(colorIntValue);
			int green = Color.green(colorIntValue);
			int blue = Color.blue(colorIntValue);
			double lum = (((0.299 * red) + ((0.587 * green) + (0.114 * blue))));
			return lum > 186 ? 0xFF000000 : 0xFFFFFFFF;
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

		/**
		 * Convert from PX to DP
		 */
		static float px2dp(@NonNull Context context, float value) {
			return value / context.getResources().getDisplayMetrics().density;
		}
	}
}

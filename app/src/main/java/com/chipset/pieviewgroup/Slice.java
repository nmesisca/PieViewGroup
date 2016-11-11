package com.chipset.pieviewgroup;

import android.graphics.Point;
import android.support.annotation.NonNull;

/*
 * Created by nmesisca on 05/11/16 18:35
 * Copyright ® 2016. All rights reserved.
 * Last modified : 05/11/16 18:35
 */
class Slice {

	String label;
	int percent;
	float startDegree;
	float endDegree;
	Point labelOffset;
	int labelColor;
	float sweepDegree;
	int sliceColor;

	@Override
	@NonNull
	public String toString() {
		return percent+"%";
	}
}

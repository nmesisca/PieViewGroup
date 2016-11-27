package com.chipset.pieviewgroupdemo;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.chipset.pieviewgroup.ChartTypes;
import com.chipset.pieviewgroup.LegendTypes;
import com.chipset.pieviewgroup.PieViewGroup;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

	@NonNull
	private final Map<String, Integer> dataSource = new HashMap<>();

// REGION Lifecycle
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		LoadData();

		final PieViewGroup vertPie = (PieViewGroup) findViewById(R.id.default_v);
		final PieViewGroup horizPie = (PieViewGroup) findViewById(R.id.default_h);
		final PieViewGroup nonAdaptivePie = (PieViewGroup) findViewById(R.id.non_adaptive);
		final PieViewGroup customLegendDonut = (PieViewGroup) findViewById(R.id.donut_custom_legend);

		final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/CUSTOM.TTF");

		vertPie.setData(dataSource);
		vertPie.build();

		nonAdaptivePie.setAdaptiveColorsEnabled(false);
		nonAdaptivePie.setData(dataSource);
		nonAdaptivePie.build();

		horizPie.setData(dataSource);
		horizPie.build();

		customLegendDonut.setData(dataSource);
		customLegendDonut.setChartType(ChartTypes.DONUT);
		customLegendDonut.setLegendMode(LegendTypes.FULL);
		customLegendDonut.setLegendTypeface(custom_font);
		customLegendDonut.setLegendDrawableId(R.drawable.ic_star_black_24dp);
		customLegendDonut.build();
	}
//ENDREGION Lifecycle

	private void LoadData() {
		dataSource.put("Android",67);
		dataSource.put("iOS",24);
		dataSource.put("Others",9);
	}
}

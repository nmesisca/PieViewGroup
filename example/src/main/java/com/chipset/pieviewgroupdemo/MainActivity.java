package com.chipset.pieviewgroupdemo;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
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
		final PieViewGroup vertDonut = (PieViewGroup) findViewById(R.id.donut_v_legend);
		final PieViewGroup horizDonut = (PieViewGroup) findViewById(R.id.donut_h_legend);

		final Integer[] items = new Integer[] { 8,9,10,11,12,13,14,15 };
		final String[] fonts = new String[] { "default","custom" };
		final Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/CUSTOM.TTF");

		final Spinner labelSize = (Spinner) findViewById(R.id.label_size);
		final ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
		labelSize.setAdapter(adapter1);
		labelSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				vertDonut.setLabelTextSizeSp((int)labelSize.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		final Spinner legendSize = (Spinner) findViewById(R.id.legend_size);
		final ArrayAdapter<Integer> adapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
		legendSize.setAdapter(adapter2);
		legendSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				vertDonut.setLegendTextSizeSp((int)legendSize.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		final Spinner legendFont = (Spinner) findViewById(R.id.legend_font);
		final ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, fonts);
		legendFont.setAdapter(adapter3);
		legendFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
				switch (position) {
					case (0) :
						vertDonut.setLegendTypeface(null);
						break;
					case (1) :
						vertDonut.setLegendTypeface(custom_font);
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {

			}
		});

		final SwitchCompat switchType = (SwitchCompat) findViewById(R.id.switch_type);
		switchType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				vertDonut.setChartType(b ? ChartTypes.PIE : ChartTypes.DONUT);
			}
		});

		final SwitchCompat switchLabels = (SwitchCompat) findViewById(R.id.switch_labels);
		switchLabels.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				vertDonut.showLabels(b);
			}
		});

		final SwitchCompat switchLegend = (SwitchCompat) findViewById(R.id.switch_legend_type);
		switchLegend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				vertDonut.setLegendMode(b ? LegendTypes.SHORT : LegendTypes.FULL);
			}
		});

		vertDonut.showLabels(true);
		vertDonut.setLegendDrawableId(R.drawable.ic_star_black_24dp);
		vertDonut.setChartType(ChartTypes.DONUT);
		vertDonut.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				vertDonut.setSelected(!vertDonut.isSelected());
			}
		});
		vertDonut.setData(dataSource);
		vertDonut.build();

		horizDonut.setAdaptiveColorsEnabled(false);
		horizDonut.showLabels(true);
		horizDonut.setLegendMode(LegendTypes.SHORT);
		horizDonut.setChartType(ChartTypes.DONUT);
		horizDonut.setData(dataSource);
		horizDonut.build();

		legendSize.setSelection(4,true);
		labelSize.setSelection(2,true);
		legendFont.setSelection(0,true);
	}
//ENDREGION Lifecycle

	private void LoadData() {
		dataSource.put("CO2",27);
		dataSource.put("O2",23);
		dataSource.put("N",15);
		dataSource.put("Others",10);
		dataSource.put("K",10);
		dataSource.put("H2",7);
		dataSource.put("AU",8);
	}
}

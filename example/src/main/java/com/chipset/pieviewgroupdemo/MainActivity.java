package com.chipset.pieviewgroupdemo;

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

	private Map<String, Integer> dataSource = new HashMap<>();

// REGION Lifecycle
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LoadData();
		final PieViewGroup vertDonut = (PieViewGroup) findViewById(R.id.donut_v_legend);
		final PieViewGroup horizDonut = (PieViewGroup) findViewById(R.id.donut_h_legend);
		final Spinner textSize = (Spinner) findViewById(R.id.label_size);
		final Integer[] items = new Integer[]{8,9,10,11,12,13,14,15};
		final ArrayAdapter<Integer> adapter1 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
		textSize.setAdapter(adapter1);
		textSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				vertDonut.setLabelTextSizeSp((int)textSize.getSelectedItem());
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
				vertDonut.setLegendType(b ? LegendTypes.SHORT : LegendTypes.FULL);
			}
		});

		vertDonut.showLabels(true);
		vertDonut.setLegendType(LegendTypes.FULL);
		vertDonut.setChartType(ChartTypes.DONUT);
		vertDonut.setData(dataSource);

		horizDonut.showLabels(true);
		horizDonut.setLegendType(LegendTypes.SHORT);
		horizDonut.setChartType(ChartTypes.DONUT);
		horizDonut.setData(dataSource);
	}
//ENDREGION Lifecycle

	private void LoadData() {
		dataSource.put("CO2",20);
		dataSource.put("O2",23);
		dataSource.put("N",10);
		dataSource.put("Others",10);
		dataSource.put("K",10);
		dataSource.put("H2",5);
		dataSource.put("S",17);
		dataSource.put("AU",5);
	}
}

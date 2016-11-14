# PieViewGroup
Quick and easy Pie chart with legend for Android API 16+

#Setup
##1. Provide the gradle dependency

```gradle
compile('com.chipset:pieviewgroup:0.3.0')
```

##2. Add it to your layout

```xml
          <com.chipset.pieviewgroup.PieViewGroup
              android:id="@+id/myPie"
              android:layout_width="match_parent"
              android:layout_height="300dp"
              ealk:pvg_chartType="PIE"
              ealk:pvg_showLabels="true"
              ealk:pvg_legendType="SHORT"/>
```

##3. Reference it in your code ...

```java
          PieViewGroup pieChart = (PieViewGroup) findViewById(R.id.myPie);
```

Adjust the options of the PieViewGroup

```java
          PieViewGroup pieChart = (PieViewGroup) findViewById(R.id.myPie);
          ...
          pieChart.showLabels(true);
          pieChart.setLegendType(LegendTypes.FULL);
          pieChart.setChartType(ChartTypes.DONUT);
```

Setup the data source for the chart and pass it to the PieViewGroup

```java
          ...
          final Map<String, Integer> dataSource = new HashMap<>();
          dataSource.put("CO2",20);
          dataSource.put("O2",17);
          dataSource.put("N",30);
          dataSource.put("Others",23);
          dataSource.put("K",10);
          ...
          pieChart.setData(dataSource);
```



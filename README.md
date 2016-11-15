# PieViewGroup  [ ![Download](https://api.bintray.com/packages/nickmesisca/Android-Controls/PieViewGroup/images/download.svg) ](https://bintray.com/nickmesisca/Android-Controls/PieViewGroup/_latestVersion)
Quick and easy Pie chart with legend for Android API 16+

#Setup
##1. Provide the gradle dependency in your module-level gradle.build file

```gradle
compile('com.chipset:pieviewgroup:0.3.0')
```

Make sure the 'repositories' section in your project-level gradle.build file

```gradle
          ...
          repositories {
               jcenter()
          }
          ...
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

##3. Reference it in your code

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

#Developed By

* Nick Mesisca 

#License

    Copyright 2016 Nick Mesisca

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


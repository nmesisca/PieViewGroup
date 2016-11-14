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

##3. Reference it in your code

```java
          PieViewGroup pieChart = (PieViewGroup) findViewById(R.id.myPie);
```

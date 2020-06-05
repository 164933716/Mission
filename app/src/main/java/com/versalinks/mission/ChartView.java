package com.versalinks.mission;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.amap.api.maps.AMapUtils;
import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ChartView extends LineChart {

    private LineChart lineChart;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lineChart = this;
    }

    public void setPoints(List<Model_GPS> gpsList) {
        List<Entry> entries = new ArrayList<>();
        List<Float> distances = new ArrayList<>();
        float distance = 0;
        float minY = 0;
        float maxY = 0;
        float up = 0;
        float down = 0;
        for (int i = 0; i < gpsList.size(); i++) {
            Model_GPS modelGps = gpsList.get(i);
            if (modelGps == null) {
                continue;
            }

            float height = (float) modelGps.height;
            if (i == 0) {
                Entry entry = new Entry(0f, height);
                minY = height;
                maxY = height;
                entries.add(entry);
                distances.add(distance);
            } else {
                if (minY > height) {
                    minY = height;
                }
                if (maxY < height) {
                    maxY = height;
                }
                Model_GPS modelGpsLast = gpsList.get(i - 1);
                if (modelGpsLast == null) {
                    continue;
                }
                float v = AMapUtils.calculateLineDistance(modelGps.getLatLng(), modelGpsLast.getLatLng());
                distance += v;
//                LogUtils.e("distance    "+distance);
                Entry entry = new Entry(distance, height);
                entries.add(entry);
                distances.add(distance);

                if (modelGps.height > modelGpsLast.height) {
                    up += Math.abs(modelGps.height - modelGpsLast.height);
                }else {
                    down += Math.abs(modelGps.height - modelGpsLast.height);
                }
            }
        }
        LogUtils.e("minY    " + minY);
        LogUtils.e("maxY    " + maxY);
        LogUtils.e("up    " + up);
        LogUtils.e("down    " + down);
        LogUtils.e("distance    "+distance);
//        LogUtils.e("distance    "+distances);
        int y1 = new BigDecimal(minY).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        int y2 = new BigDecimal(maxY).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
        initLineChart(0, distance, y1, y2, entries);
    }

    /**
     * 初始化曲线图表
     *
     * @param minX
     * @param minY
     * @param entries 数据集
     */
    private void initLineChart(float minX, float maxX, float minY, float maxY, final List<Entry> entries) {
        //显示边界
        lineChart.setTouchEnabled(true);
        lineChart.setDrawBorders(false);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);

        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "route");
        //线颜色
        lineDataSet.setColor(Color.WHITE);
        //线宽度
        lineDataSet.setLineWidth(1.6f);
        //不显示圆点
        lineDataSet.setDrawCircles(false);
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillAlpha(100);
        lineDataSet.setFillColor(Color.WHITE);
        LineData data = new LineData(lineDataSet);

        //无数据时显示的文字
        lineChart.setNoDataText("暂无数据");
        //折线图不显示数值
        data.setDrawValues(false);
        //得到X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
//        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
        xAxis.setLabelCount(6, false);
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.setAxisMinimum(minX);
        xAxis.setAxisMaximum(maxX);
        //不显示网格线
        xAxis.setDrawGridLines(true);
        //设置X轴值为字符串
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(value);
            }
        });
        //得到Y轴
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        YAxis rightYAxis = lineChart.getAxisRight();
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        //设置y轴坐标之间的最小间隔
        //不显示网格线
        yAxis.setDrawGridLines(true);
        //设置Y轴坐标之间的最小间隔
//        yAxis.setGranularity(1);
        //设置y轴的刻度数量
        //+2：最大值n就有n+1个刻度，在加上y轴多一个单位长度，为了好看，so+2
        yAxis.setLabelCount(6, false);
        //设置从Y轴值
        yAxis.setAxisMinimum(minY);
        //+1:y轴多一个单位长度，为了好看
        yAxis.setAxisMaximum(maxY);

        //y轴
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf(value);
            }
        });
        //图例：得到Lengend
        Legend legend = lineChart.getLegend();
        //隐藏Lengend
        legend.setEnabled(false);
        //隐藏描述
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);
//        //折线图点的标记
//        MyMarkerView mv = new MyMarkerView(this);
//        lineChart.setMarker(new MarkerImage(getContext(),R.drawable.icon_c5));
        lineChart.setData(data);
        lineChart.animateXY(800, 800);
        lineChart.invalidate();
    }
}

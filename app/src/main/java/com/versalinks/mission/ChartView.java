package com.versalinks.mission;

import android.content.Context;
import android.graphics.Color;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerImage;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChartView extends FrameLayout {

    private LineChart lineChart;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        lineChart = new LineChart(context);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(lineChart, layoutParams);
//        lineChart.setViewPortOffsets(0, 0, 0, 0);
        List<Double> floats = new ArrayList<>();
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        floats.add(DataUtils.randomHeight());
        initLineChart(floats);
    }

    /**
     * 初始化曲线图表
     *
     * @param list 数据集
     */
    private void initLineChart(final List<Double> list) {
        //显示边界
        lineChart.setDrawBorders(false);
        //设置数据
        List entries = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry(i, list.get(i).floatValue()));
        }
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        //线颜色
        lineDataSet.setColor(Color.parseColor("#F15A4A"));
        //线宽度
        lineDataSet.setLineWidth(1.6f);
        //不显示圆点
        lineDataSet.setDrawCircles(false);
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        LineData data = new LineData(lineDataSet);
        //无数据时显示的文字
        lineChart.setNoDataText("暂无数据");
        //折线图不显示数值
        data.setDrawValues(false);
        //得到X轴
        XAxis xAxis = lineChart.getXAxis();
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
        xAxis.setLabelCount(list.size() / 2, false);
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.setAxisMinimum(0f);
        xAxis.setAxisMaximum((float) list.size());
        //不显示网格线
        xAxis.setDrawGridLines(true);
        //设置X轴值为字符串
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int IValue = (int) value;
                CharSequence format = DateFormat.format("MM/dd",
                        System.currentTimeMillis() - (long) (list.size() - IValue) * 24 * 60 * 60 * 1000);
                return format.toString();
            }
        });
        //得到Y轴
        YAxis yAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        //设置y轴坐标之间的最小间隔
        //不显示网格线
        yAxis.setDrawGridLines(true);
        //设置Y轴坐标之间的最小间隔
        yAxis.setGranularity(1);
        //设置y轴的刻度数量
        //+2：最大值n就有n+1个刻度，在加上y轴多一个单位长度，为了好看，so+2
        yAxis.setLabelCount(list.size(), false);
        //设置从Y轴值
        yAxis.setAxisMinimum(0f);
        //+1:y轴多一个单位长度，为了好看
        yAxis.setAxisMaximum((float) (Collections.max(list) + 1));

        //y轴
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int IValue = (int) value;
                return String.valueOf(IValue);
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
        //设置数据
        lineChart.setData(data);
        //图标刷新
        lineChart.invalidate();
    }
}

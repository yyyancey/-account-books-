package com.example.yancey.dailyapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by yancey on 2017/8/24.
 */

public class ChartsActivity extends Activity {
    private LineChartView mchart;
    private Map<String, Float> table = new TreeMap<>();
    private LineChartData mDate;
    private Axis axisY = new Axis().setHasLines(true);// Y轴属性
    private Axis axisX = new Axis();// X轴属性
    private float max;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_view);
        mDate = new LineChartData();
        mchart = (LineChartView) findViewById(R.id.chart);
        List<CostBean> cast_list = (List<CostBean>) getIntent().getSerializableExtra("cast_list");

        generateValue(cast_list);
        generateAxis();
       // resetViewport();
       generateDate();

    }

    private void generateAxis() {
        List<AxisValue> axisValuesX = new ArrayList<>();
        int j = 0;
        for (String key : table.keySet()) {
            axisValuesX.add(new AxisValue(j).setValue(j).setLabel(
                    key));
            j++;
        }
        axisX.setLineColor(ChartUtils.DEFAULT_COLOR);
        axisX.setTextSize(8);// 设置X轴文字大小
        axisX.setName("日期");
        axisX.setValues(axisValuesX);
        axisY.setName("价格");
        axisY.setTextSize(8);

    }

    private void generateDate() {
        List<Line> lines = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        int index = 0;
        for (Float value : table.values()) {
            values.add(new PointValue(index, value));
            index++;
        }
        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_ORANGE);
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(ChartUtils.COLOR_RED);
        // line.setHasLabels(true);

        lines.add(line);

        mDate.setLines(lines);
        mDate.setAxisXBottom(axisX);
        mDate.setAxisYLeft(axisY);
        mchart.setLineChartData(mDate);

    }

    private void generateValue(List<CostBean> cast_list) {
        max=0;
        if (cast_list != null) {
            for (int i = 0; i < cast_list.size(); i++) {
                CostBean costbean = cast_list.get(i);
                String costDate = costbean.costDate;
                float costMoney = 0;
                if (!costbean.costMoney.isEmpty()) {
                    costMoney = Float.parseFloat((costbean.costMoney));
                }

                if (!table.containsKey(costDate)) {
                    if(costMoney>max){
                        max=costMoney;
                    }
                    table.put(costDate, costMoney);
                } else {
                    float originMoney = table.get(costDate);
                    table.put(costDate, originMoney + costMoney);
                    if(costMoney>max){
                        max=costMoney+originMoney;
                    }
                }
            }
        }
        if (table.size() > 8) {
            for (String key : table.keySet()) {
                table.remove(key);
                if (table.size() < 9) {
                    break;
                }
            }
        }


    }
    private void resetViewport() {
        // Reset viewport height range to (0,100)
        final Viewport v = new Viewport(mchart.getMaximumViewport());
        v.bottom = 0;
        v.top = max;
        v.left = 0;
        v.right = table.size() - 1;
        mchart.setMaximumViewport(v);
        mchart.setCurrentViewport(v);
    }
}

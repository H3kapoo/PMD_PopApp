package com.hekapoo.popapp.Charts;

import android.os.Bundle;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Funnel;
import com.anychart.charts.Pie;
import com.anychart.charts.Scatter;
import com.anychart.charts.TagCloud;
import com.anychart.core.Chart;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.core.scatter.series.Marker;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HAlign;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.GradientKey;
import com.anychart.graphics.vector.LinearGradientStroke;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.Stroke;
import com.anychart.scales.OrdinalColor;
import com.hekapoo.popapp.R;

import java.util.ArrayList;
import java.util.List;

public class ChartModel {

    private String type;
    private List<DataEntry> data;
    private Bundle extras;

    public ChartModel(String type, List<DataEntry> data, Bundle extras) {
        this.type = type;
        this.data = data;
        this.extras = extras;
    }

    private Funnel funnelChart() {
        Funnel funnel = AnyChart.funnel();
        funnel.data(data);

        funnel.margin(new String[]{"10", "20%", "10", "20%"});
        funnel.baseWidth("70%")
                .neckWidth("17%");

        funnel.labels()
                .position("outsideleft")
                .format("{%X} - {%Value}");

        //funnel.animation(true);

        return funnel;
    }

    private TagCloud tagCloudChart() {
        TagCloud tagCloud = AnyChart.tagCloud();

        tagCloud.title(extras.getString("TITLE"));

        OrdinalColor ordinalColor = OrdinalColor.instantiate();
        ordinalColor.colors(new String[]{
                "#26959f", "#f18126", "#3b8ad8", "#60727b", "#e24b26"
        });
        tagCloud.colorScale(ordinalColor);
        tagCloud.angles(new Double[]{-90d, 0d, 90d});

        tagCloud.colorRange().enabled(true);
        tagCloud.colorRange().colorLineSize(15d);
        tagCloud.data(data);

        return tagCloud;
    }

    private Pie pieChart() {
        Pie pie = AnyChart.pie();

        pie.data(data);

        pie.title("Fruits imported in 2015 (in kg)");
        pie.title(extras.getString("TITLE"));


        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title().text(extras.getString("SUB_TITLE")).padding(0d, 0d, 10d, 0d);

        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        return pie;
    }

    private Cartesian columnChart() {

        Cartesian cartesian = AnyChart.column();

        cartesian.background().fill("#F5F9EF");
        Column column = cartesian.column(data);
     //   column.fill("#fff");
     //   column.stroke("#fff");

        column.tooltip().titleFormat("{%X}").position(Position.CENTER).anchor(Anchor.CENTER).offsetX(0d).offsetY(5d).format("{%Value}");
        column.tooltip().vAlign("center");
        column.tooltip().hAlign("center");
       // column.tooltip().background().fill("#ffffff00");

        cartesian.animation(true);
        cartesian.title(extras.getString("TITLE"));
      //  cartesian.title().fontColor("#fff");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value} ");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

       // cartesian.xAxis(0).title().fontColor("#fff");
        //cartesian.xAxis(0).labels().fontColor("#fff");

        //cartesian.yAxis(0).title().fontColor("#fff");
        //cartesian.yAxis(0).labels().fontColor("#fff");


        return cartesian;
    }

    public Chart populate() {

        switch (type) {
            case "COLUMN":
                return columnChart();
            case "PIE":
                return pieChart();
            case "TAG_CLOUD":
                return tagCloudChart();
            case "FUNNEL":
                return funnelChart();
            default:
                return null;
        }
    }
}

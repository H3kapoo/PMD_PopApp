package com.hekapoo.popapp.Charts;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

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
import com.hekapoo.popapp.ChartsActivity;
import com.hekapoo.popapp.HomeActivity;
import com.hekapoo.popapp.Login.LoginHandler;
import com.hekapoo.popapp.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChartModel {


    private String type;
    private List<DataEntry> data;
    private Bundle extras;
    private Cartesian cartesian;
    private Pie pie;
    private TagCloud tagCloud;

    public ChartModel(String type, List<DataEntry> data, Bundle extras) {
        this.type = type;
        this.data = data;
        this.extras = extras;
    }

    private TagCloud tagCloudChart() {
        tagCloud = AnyChart.tagCloud();
        tagCloud.background().fill("#F5F9EF");

        tagCloud.title(extras.getString("TITLE"));
        tagCloud.title().fontColor("#303030");


        OrdinalColor ordinalColor = OrdinalColor.instantiate();
        ordinalColor.colors(new String[]{"#26959f", "#f18126", "#3b8ad8", "#60727b", "#e24b26"});

        tagCloud.colorScale(ordinalColor);
        tagCloud.angles(new Double[]{-90d, 0d, 90d});

        tagCloud.colorRange().enabled(true);
        tagCloud.colorRange().colorLineSize(15d);
        tagCloud.data(data);

        return tagCloud;
    }

    private Pie pieChart() {
        pie = AnyChart.pie();
        pie.background().fill("#F5F9EF");

        pie.data(data);
        pie.title(extras.getString("TITLE"));
        pie.title().fontColor("#303030");


        pie.labels().position("outside");
        pie.labels().fontColor("#303030");

        pie.legend().title().enabled(false);

        pie.legend().position("center-bottom").itemsLayout(LegendLayout.HORIZONTAL).align(Align.CENTER);

        return pie;
    }

    private Cartesian columnChart() {

        cartesian = AnyChart.column();

        cartesian.background().fill("#F5F9EF");
        Column column = cartesian.column(data);

        column.tooltip().titleFormat("{%X}").position(Position.CENTER).anchor(Anchor.CENTER).offsetX(0d).offsetY(5d).format("{%Value}");
        column.tooltip().vAlign("center");
        column.tooltip().hAlign("center");

        cartesian.animation(true);
        cartesian.title(extras.getString("TITLE"));
        cartesian.title().fontColor("#303030");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value} ");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).labels().fontColor("#303030");
        cartesian.yAxis(0).labels().fontColor("#303030");

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
            default:
                return null;
        }
    }

    @SuppressLint("DefaultLocale")
    public void handleDownload(View v, AnyChartView c) {

        c.buildDrawingCache();
        Bitmap bitmap = c.getDrawingCache();

        OutputStream outputStream = null;

        String type = cartesian != null ? "column" : (pie != null ? "pie" : "tagCloud");

        int loginType = LoginHandler.getInstance().getLoginType();

        String socialType = loginType == 1 ? "Facebook" : (loginType == 2 ? "Twitter" : "unknown");

        String filename = String.format("%s_%s_%d.png", socialType, type, System.currentTimeMillis());
        File outFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), filename);

        try {
            outputStream = new FileOutputStream(outFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        try {
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}











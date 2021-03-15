package com.hekapoo.popapp.Charts;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.hekapoo.popapp.R;

import java.util.ArrayList;

public class ChartModelAdapter extends RecyclerView.Adapter<ChartModelAdapter.ViewHolder> {

    private ArrayList<ChartModel> charts;

    public ChartModelAdapter(ArrayList<ChartModel> charts) {
        this.charts = charts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final AnyChartView chartView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            chartView = view.findViewById(R.id.chart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_model_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChartModel chart = charts.get(position);
        holder.chartView.setChart(chart.populate());
        holder.view.findViewById(R.id.download_btn).setOnClickListener(e-> chart.handleDownload(e,holder.chartView));

    }


    @Override
    public int getItemCount() {
        return charts == null ? 0 : charts.size();
    }
}

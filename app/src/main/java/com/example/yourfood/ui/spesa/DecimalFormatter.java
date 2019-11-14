package com.example.yourfood.ui.spesa;

import com.github.mikephil.charting.charts.PieChart;

import java.text.DecimalFormat;

class DecimalFormatter extends com.github.mikephil.charting.formatter.ValueFormatter {

        public DecimalFormat mFormat;
        private PieChart pieChart;

        public DecimalFormatter() {
            mFormat = new DecimalFormat("###,###,##0.00");
        }


    }


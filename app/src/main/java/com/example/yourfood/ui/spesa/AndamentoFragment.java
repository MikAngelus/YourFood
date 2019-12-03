package com.example.yourfood.ui.spesa;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yourfood.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class AndamentoFragment extends Fragment {

    private SpesaViewModel spesaViewModel;
    final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
    final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
    final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/");

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_andamento, container, false);
        final BarChart chart = root.findViewById(R.id.bar_chart);

        final ValueEventListener messageListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String index = dataSnapshot.child("index").getValue().toString();
                float value[] = new float[13];
                for (int count_mese = 0; count_mese < 13; count_mese++) {

                    value[count_mese] = 0;

                }

                final Integer intIndex = parseInt(index);
                int y = 0;
                float parziale = 0;
                for (int i = 0; i < intIndex; i++) {

                    parziale = 0;
                    boolean flag = false;
                    final String count = Integer.toString(i);
                    String nodo = "Prodotto_" + i;
                    boolean check = dataSnapshot.child(nodo).exists();

                    if (check) {

                        String acquisto = dataSnapshot.child(nodo).child("Data_acquisto").getValue().toString();
                        String costo = dataSnapshot.child(nodo).child("Costo").getValue().toString();
                        String quantita = dataSnapshot.child(nodo).child("Quantita").getValue().toString();
                        float x = Float.parseFloat(costo);
                        float z = Float.parseFloat(quantita);

                        parziale = parziale + (x * z);
                        String[] separated = acquisto.split("/");
                        String[] mese = separated[1].split("/");
                        int read_mese = parseInt(mese[0]);
                        for (int count_mese = 0; count_mese < 13; count_mese++) {
                            if (count_mese == read_mese) {
                                value[count_mese] = value[count_mese] + parziale;
                            }


                        }

                    } else {

                    }

                }
                int monthNames[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

                ArrayList<BarEntry> barEntries = new ArrayList<>();

                for (int w = 1; w < 13; w++) {

                    barEntries.add(new BarEntry(monthNames[w], value[w]));

                }


                BarDataSet dataSet = new BarDataSet(barEntries, "");

                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                dataSet.setValueFormatter(new LargeValueFormatter(" €"));
                dataSet.setValueTextSize(11f);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                dataSet.setValueTextColor(Color.WHITE);


                BarData data = new BarData(dataSet);
                chart.setDrawGridBackground(false);
                chart.setData(data);
                chart.setFitBars(true);
                chart.invalidate();

                XAxis xAxis = chart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                xAxis.setDrawGridLines(false);
                xAxis.setGranularity(1f); // only intervals of 1 day
                xAxis.setLabelCount(12);

                ValueFormatter custom = new LargeValueFormatter(" €");

                YAxis leftAxis = chart.getAxisLeft();
                leftAxis.setLabelCount(12, false);
                leftAxis.setValueFormatter(custom);
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                leftAxis.setSpaceTop(15f);
                leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

                YAxis rightAxis = chart.getAxisRight();
                rightAxis.setDrawGridLines(false);
                rightAxis.setLabelCount(12, false);
                rightAxis.setValueFormatter(custom);
                rightAxis.setSpaceTop(15f);
                rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
                chart.getDescription().setEnabled(false);
                chart.getLegend().setEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        DBRef.addListenerForSingleValueEvent(messageListener);


        return root;
    }
}









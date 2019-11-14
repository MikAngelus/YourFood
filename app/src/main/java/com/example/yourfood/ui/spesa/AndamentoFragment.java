package com.example.yourfood.ui.spesa;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.yourfood.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AndamentoFragment extends Fragment {

    private SpesaViewModel spesaViewModel;
    final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
    final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
    final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/");




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_andamento, container, false);


        BarChart chart = root.findViewById(R.id.bar_chart);

        int monthNames[] = {1,2,3,4,5,6,7,8,9,10,11,12};


        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for(int w=0; w<monthNames.length; w++){


                barEntries.add(new BarEntry(monthNames[w], monthNames[w]));

        }


        BarDataSet dataSet = new BarDataSet(barEntries, "ciao");

        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

       // dataSet.setSelectionShift(12f);
        //dataSet.setValueFormatter(new LargeValueFormatter(" €"));
        dataSet.setValueTextSize(11f);
        dataSet.setValueTextColor(Color.WHITE);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        BarData data = new BarData(dataSet);
        chart.setData(data);
        chart.setFitBars(true);




        final ArrayList<Float> SpesaMeseArrayList = new ArrayList<>();
        final ArrayAdapter<Float> myArrayAdapterMeseCategoria = new ArrayAdapter<Float>(getActivity(), android.R.layout.simple_list_item_1, SpesaMeseArrayList);

        //SpesaMeseArrayList.add( id_categoria[p]);





/*
        //data.setValueTypeface(tfLight);
        chart.setRotationAngle(180f);
        chart.getDescription().setEnabled(false);
        chart.animateY(1500);
        chart.setHoleRadius(50);
        chart.getLegend().setEnabled(false);



        chart.setCenterText("Categorie");
        chart.setCenterTextSize(20);
        chart.setCenterTextColor(Color.DKGRAY);

        dataSet.setValueTextSize(20);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTypeface(Typeface.DEFAULT);
        chart.setEntryLabelTextSize(16);
        // chart.setUsePercentValues(true);
        chart.invalidate();*/
        return root;
    }


}

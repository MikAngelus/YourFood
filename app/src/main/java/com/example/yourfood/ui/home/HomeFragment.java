package com.example.yourfood.ui.home;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.Login;
import com.example.yourfood.R;
import com.example.yourfood.ui.lista.ListaFragment;
import com.example.yourfood.ui.spesa.SpesaFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final long ONE_MINUTE = 60000;
    private static final long ASAP = 2;
    private NotificationManager mNotificationManager;

    private PieChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
    final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
    final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID);
    String strMNome = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView nome_user = root.findViewById(R.id.textViewUser);
        final TextView spreco_user = root.findViewById(R.id.spreco);

        final TextView label_prod_scaduti = root.findViewById(R.id.prod_scaduti);
        final TextView label_prod_consumati = root.findViewById(R.id.prod_consumati);
        final TextView label_prod_scaduti_consumati = root.findViewById(R.id.prod_scaduti_consumati);
        final TextView label_prod_non_consumati = root.findViewById(R.id.prod_non_consumati);

        final Button logout = root.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);

            }
        });


        final Date ora_notify = new Date();


        if (strMNome != null) {
            nome_user.setText("Ciao " + strMNome);
        }

        final int[] prodotti_scaduti = {0};
        final int[] prodotti_consumati = {0};
        final int[] prodotti_scaduti_consumati = {0};
        final int[] prodotti_non_consumati = {0};


        final Button spese = root.findViewById(R.id.view_spesa);

        spese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Fragment someFragment = new SpesaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });

        final Button prodotti = root.findViewById(R.id.view_prodotti);
        prodotti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment someFragment = new ListaFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack("Home");  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        ValueEventListener messageListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean check_prodotti = dataSnapshot.child("Prodotti").child("index").exists();


                if (check_prodotti) {
                    String index = dataSnapshot.child("Prodotti").child("index").getValue().toString();

                    final Integer intIndex = parseInt(index);
                    int y = 0;
                    float parziale = 0;

                    int count_presence = 0;

                    for (int i = 0; i < intIndex; i++) {

                        final String count = Integer.toString(i);
                        String nodo = "Prodotto_" + i;
                        boolean check = dataSnapshot.child("Prodotti").child(nodo).exists();


                        if (check) {


                            System.out.println(y);

                            String costo = dataSnapshot.child("Prodotti").child(nodo).child("Costo").getValue().toString();
                            String consumato = dataSnapshot.child("Prodotti").child(nodo).child("Consumato").getValue().toString();
                            String quantita = dataSnapshot.child("Prodotti").child(nodo).child("Quantita").getValue().toString();
                            String scaduto = dataSnapshot.child("Prodotti").child(nodo).child("Scaduto").getValue().toString();


                            int read_scaduto = Integer.parseInt(scaduto);
                            int read_consumato = Integer.parseInt(consumato);
                            float x = Float.parseFloat(costo);
                            float z = Float.parseFloat(quantita);


                            if (read_scaduto == 1 && read_consumato == 1) {


                                prodotti_scaduti_consumati[0]++;

                            } else if (read_scaduto == 1 && read_consumato == 0) {

                                parziale = parziale + (x * z);
                                prodotti_scaduti[0]++;

                            } else if (read_consumato == 1) {


                                prodotti_consumati[0]++;
                            } else {

                                prodotti_non_consumati[0]++;

                            }

                        } else {

                        }


                    }

                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    String output = formatter.format(parziale);
                    spreco_user.setText(output + "€");

                    label_prod_scaduti.setText("" + prodotti_scaduti[0]);
                    label_prod_consumati.setText("" + prodotti_consumati[0]);
                    label_prod_non_consumati.setText("" + prodotti_non_consumati[0]);
                    label_prod_scaduti_consumati.setText("" + prodotti_scaduti_consumati[0]);


                    int rainfall[] = {prodotti_consumati[0] + prodotti_scaduti_consumati[0], prodotti_scaduti[0], prodotti_non_consumati[0]};
                    String monthNames[] = {"Consumati", "Scaduti", "Non Consumati"};

                    List<PieEntry> pieEntries = new ArrayList<>();
                    int count = 0;
                    for (int i = 0; i < rainfall.length; i++) {


                        if (rainfall[i] != 0) {
                            count++;
                            pieEntries.add(new PieEntry(rainfall[i], monthNames[i]));
                        }

                    }

                    PieDataSet dataSet = new PieDataSet(pieEntries, "Stato prodotti acquistati");

                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(12f);

                    dataSet.setValueTextSize(11f);
                    dataSet.setValueTextColor(Color.WHITE);

                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    PieData data = new PieData(dataSet);


                    //GET THE CHART
                    PieChart chart = root.findViewById(R.id.chart1);
                    chart.setData(data);
                    dataSet.setValueFormatter(new PercentFormatter(chart));

                    //data.setValueTypeface(tfLight);
                    chart.setRotationAngle(180f);
                    chart.getDescription().setEnabled(false);
                    chart.animateY(1500);
                    chart.setHoleRadius(50);
                    chart.getLegend().setEnabled(false);
                    chart.setCenterText("Stato prodotti acquistati");
                    chart.setCenterTextSize(20);
                    chart.setCenterTextColor(Color.DKGRAY);

                    dataSet.setValueTextSize(20);
                    chart.setEntryLabelColor(Color.BLACK);
                    chart.setEntryLabelTypeface(Typeface.DEFAULT);
                    chart.setEntryLabelTextSize(16);
                    chart.setUsePercentValues(true);
                    chart.invalidate();


                    if (count == 0) {

                        chart.setVisibility(View.GONE);

                    } else {

                        chart.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        DBRef.addListenerForSingleValueEvent(messageListener);


        return root;

    }


}
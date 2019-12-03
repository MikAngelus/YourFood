package com.example.yourfood.ui.spesa;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

public class SpesaFragment extends Fragment {

    private SpesaViewModel spesaViewModel;
    final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
    final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
    final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/");


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        spesaViewModel =
                ViewModelProviders.of(this).get(SpesaViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_spesa, container, false);

        final TextView totale = root.findViewById(R.id.spesa_totale);
        final TextView spesa_mensile = root.findViewById(R.id.spesa_mese);
        final HorizontalScrollView scroll = root.findViewById(R.id.scroll_chip);
        final Chip[] chips = new Chip[12];
        chips[0] = root.findViewById(R.id.chip_gennaio);
        chips[1] = root.findViewById(R.id.chip_febbraio);
        chips[2] = root.findViewById(R.id.chip_marzo);
        chips[3] = root.findViewById(R.id.chip_aprile);
        chips[4] = root.findViewById(R.id.chip_maggio);
        chips[5] = root.findViewById(R.id.chip_giugno);
        chips[6] = root.findViewById(R.id.chip_luglio);
        chips[7] = root.findViewById(R.id.chip_agosto);
        chips[8] = root.findViewById(R.id.chip_settembre);
        chips[9] = root.findViewById(R.id.chip_ottobre);
        chips[10] = root.findViewById(R.id.chip_novembre);
        chips[11] = root.findViewById(R.id.chip_dicembre);

        final PieChart chart = root.findViewById(R.id.chart_spese);

        final Button andamento = root.findViewById(R.id.view_bar);

        andamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment someFragment = new AndamentoFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.nav_host_fragment, someFragment); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        });
        final View categoria = root.findViewById(R.id.categoriaView);
        final int[] scelta_mese = new int[1];

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int mese;
        mese = c.get(Calendar.MONTH);

        System.out.println(mese);
        scelta_mese[0] = mese;
        chips[mese].setChecked(true);
        chips[mese].setTextColor(Color.WHITE);

        final String[] mese_selezionato = {"31/" + (mese) + "/" + year + ""};
        System.out.println(mese_selezionato[0]);

        final boolean[] clear = {false};

        int i = 0;

        for (i = 0; i <= mese; i++) {

            final int finalI = i;

            chips[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clear[0] = true;
                    chips[scelta_mese[0]].setChecked(false);
                    chips[finalI].setChecked(true);
                    scelta_mese[0] = finalI;

                    if (scelta_mese[0] < 9) {
                        mese_selezionato[0] = "31/0" + (scelta_mese[0] + 1) + "/" + year + "";
                        System.out.println(mese_selezionato[0]);

                    } else {
                        mese_selezionato[0] = "31/" + (scelta_mese[0] + 1) + "/" + year + "";
                        System.out.println(mese_selezionato[0]);

                    }
                    readDB(mese_selezionato[0], scelta_mese[0], year, root, totale, spesa_mensile, chips, chart, categoria);

                }

            });

        }

        for (i = mese + 1; i < chips.length; i++) {
            chips[i].setVisibility(View.GONE);
        }

        mese_selezionato[0] = "31/" + (scelta_mese[0] + 1) + "/" + year + "";
        readDB(mese_selezionato[0], scelta_mese[0], year, root, totale, spesa_mensile, chips, chart, categoria);
        return root;
    }


    public void readDB(final String mese_selezionato, final int scelta_mese, final int year, final View root, final TextView totale, final TextView spesa_mensile, final Chip[] chips, final PieChart chart, final View categoria) {

        final ArrayList<String> CategoriaArrayList = new ArrayList<>();
        final ListView lista_categoria;
        lista_categoria = (ListView) root.findViewById(R.id.lista_categoria);
        final ArrayAdapter<String> myArrayAdapterCategoria = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, CategoriaArrayList);

        CategoriaArrayList.add("Pane/Pasta");
        CategoriaArrayList.add("Carne");
        CategoriaArrayList.add("Pesce");
        CategoriaArrayList.add("Vegano/Bio");
        lista_categoria.setAdapter(myArrayAdapterCategoria);

        final ArrayList<Float> SpesaCategoriaArrayList = new ArrayList<>();
        final ListView spese_categoria;
        spese_categoria = (ListView) root.findViewById(R.id.lista_spese_categoria);
        final ArrayAdapter<Float> myArrayAdapterSpeseCategoria = new ArrayAdapter<Float>(getActivity(), android.R.layout.simple_list_item_1, SpesaCategoriaArrayList);
        final TextView rapporto = root.findViewById(R.id.rapporto_totale_mese);
        final ValueEventListener messageListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String index = dataSnapshot.child("index").getValue().toString();

                final Integer intIndex = parseInt(index);
                int y = 0;

                float parziale = 0;


                float[] id_categoria = new float[5];

                for (int p = 0; p < 5; p++) {

                    id_categoria[p] = 0;

                }

                int count_presence = 0;
                int h = -1;

                for (int i = 0; i < intIndex; i++) {

                    boolean flag = false;
                    final String count = Integer.toString(i);
                    String nodo = "Prodotto_" + i;
                    boolean check = dataSnapshot.child(nodo).exists();

                    if (check) {

                        System.out.println(y);
                        String acquisto = dataSnapshot.child(nodo).child("Data_acquisto").getValue().toString();
                        String costo = dataSnapshot.child(nodo).child("Costo").getValue().toString();
                        String categoria = dataSnapshot.child(nodo).child("Categoria").getValue().toString();
                        String quantita = dataSnapshot.child(nodo).child("Quantita").getValue().toString();
                        int read_categoria = Integer.parseInt(categoria);
                        float x = Float.parseFloat(costo);
                        float z = Float.parseFloat(quantita);

                        parziale = parziale + (x * z);
                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");

                        try {

                            Date date1 = myFormat.parse(acquisto);
                            Date date2 = myFormat.parse(mese_selezionato);
                            String mese_precedente = "31/" + (scelta_mese) + "/" + year + "";
                            Date date3 = myFormat.parse(mese_precedente);

                            if (date2.getTime() > date1.getTime() && date1.getTime() > date3.getTime()) {

                                flag = true;

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        System.out.println(h);
                        if (flag) {
                            h++;
                            id_categoria[read_categoria] = id_categoria[read_categoria] + (x * z);
                        } else {

                        }

                        if (h < 0) {
                            count_presence++;
                        }

                        if (count_presence > 3) {
                            System.out.println("nessun prodotto questo mese");
                            SpesaCategoriaArrayList.clear();
                            spese_categoria.setAdapter(myArrayAdapterSpeseCategoria);

                        } else {
                            spese_categoria.setVisibility(View.INVISIBLE);
                            lista_categoria.setVisibility(View.INVISIBLE);
                        }

                    } else {

                    }
                }

                float parziale_mensile = 0;
                for (int p = 0; p < 4; p++) {

                    if (id_categoria[p] == 0) {
                        SpesaCategoriaArrayList.add(id_categoria[p]);
                        spese_categoria.setAdapter(myArrayAdapterSpeseCategoria);

                    } else {
                        SpesaCategoriaArrayList.add(id_categoria[p]);
                        spese_categoria.setAdapter(myArrayAdapterSpeseCategoria);
                    }


                    String Categoria[] = {"Pasta/Pane", "Carne", "Pesce", "Vegano/Bio"};
                    List<PieEntry> pieEntries = new ArrayList<>();
                    int count = 0;
                    for (int w = 0; w < SpesaCategoriaArrayList.size(); w++) {

                        if (SpesaCategoriaArrayList.get(w) != 0) {

                            count++;
                            pieEntries.add(new PieEntry((SpesaCategoriaArrayList.get(w)), Categoria[w]));

                        }
                    }

                    PieDataSet dataSet = new PieDataSet(pieEntries, "Spese mensili");
                    dataSet.setSelectionShift(12f);
                    dataSet.setValueFormatter(new LargeValueFormatter(" €"));
                    dataSet.setValueTextSize(11f);
                    dataSet.setValueTextColor(Color.WHITE);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    PieData data = new PieData(dataSet);


                    //GET THE CHART
                    chart.setData(data);
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
                    chart.invalidate();

                    if (count == 0) {

                        chart.setVisibility(View.GONE);
                        categoria.setVisibility(View.GONE);

                    } else {
                        chart.setVisibility(View.VISIBLE);
                        categoria.setVisibility(View.VISIBLE);

                    }

                    NumberFormat formatter = NumberFormat.getNumberInstance();
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    String output = formatter.format(parziale);
                    totale.setText(output + "€");

                    parziale_mensile += id_categoria[p];
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    output = formatter.format(parziale_mensile);
                    spesa_mensile.setText("" + output + "€");

                    float div = (parziale_mensile / parziale) * 100;
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    output = formatter.format(div);
                    rapporto.setText(output + "%");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        DBRef.addListenerForSingleValueEvent(messageListener);

    }

}

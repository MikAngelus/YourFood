package com.example.yourfood.ui.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourfood.Login;
import com.example.yourfood.MainActivity;
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
import java.util.Random;

import static java.lang.Integer.parseInt;

public class HomeFragment extends Fragment {

    // private HomeViewModel homeViewModel;
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

        // homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
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


        final Button spese=root.findViewById(R.id.view_spesa);

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

        final Button prodotti=root.findViewById(R.id.view_prodotti);
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


                        //String count= i.toString();
                        final String count = Integer.toString(i);
                        String nodo = "Prodotto_" + i;
                        // String nome = dataSnapshot.child("Prodotto_" + count).child("Nome").getValue().toString();
                        boolean check = dataSnapshot.child("Prodotti").child(nodo).exists();


                        if (check) {


                            System.out.println(y);

                            // String nome = dataSnapshot.child(nodo).child("Nome").getValue().toString();
                            //String scadenza = dataSnapshot.child(nodo).child("Data_scadenza").getValue().toString();
                            // String acquisto = dataSnapshot.child(nodo).child("Data_acquisto").getValue().toString();
                            String costo = dataSnapshot.child("Prodotti").child(nodo).child("Costo").getValue().toString();
                            String consumato = dataSnapshot.child("Prodotti").child(nodo).child("Consumato").getValue().toString();
                            // String pasto = dataSnapshot.child(nodo).child("Pasto").getValue().toString();
                            //String categoria = dataSnapshot.child(nodo).child("Categoria").getValue().toString();
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

                            //System.out.println(parziale);

                            //   SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                            //String inputString1 = acquisto.getText().toString();
                            //String inputString2 = mese_selezionato[0];

/*

                        try {

                            Date date1 = myFormat.parse(acquisto);
                            Date date2 = myFormat.parse(mese_selezionato);
                            String mese_precedente ="31/"+(scelta_mese)+"/"+year+"";
                            Date date3 = myFormat.parse(mese_precedente);
                            long diff = date2.getTime() - date1.getTime();

                            //System.out.println(date1+" "+date2+" "+date3);

                            if (date2.getTime() > date1.getTime() && date1.getTime() > date3.getTime()) {

                                flag=true;

                            }

                            //System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                        } catch (ParseException e) {
                            // flag=false;
                            e.printStackTrace();
                        }

                        System.out.println(h);

                        if(flag) {

                            h++;
                            id_categoria[read_categoria] = id_categoria[read_categoria] + (x * z);
                            //SpesaCategoriaArrayList.add(id_categoria[read_categoria] + "€");
                            //spese_categoria.setAdapter(myArrayAdapterSpeseCategoria);

                        }

                        else {

                        }




                        if(h<0){

                            count_presence++;


                            // SpesaCategoriaArrayList.add("N/D");

                        }

                        if (count_presence>3){

                            System.out.println("nessun prodotto questo mese");
                            //spese_categoria.setVisibility(View.GONE);
                            // lista_categoria.setVisibility(View.GONE);
                            SpesaCategoriaArrayList.clear();
                           /* SpesaCategoriaArrayList.add("N/D");
                            SpesaCategoriaArrayList.add("N/D");
                            SpesaCategoriaArrayList.add("N/D");
                            SpesaCategoriaArrayList.add("N/D");*//*
                            spese_categoria.setAdapter(myArrayAdapterSpeseCategoria);

                        }
                        else {

                            spese_categoria.setVisibility(View.VISIBLE);
                            lista_categoria.setVisibility(View.VISIBLE);


                        }




                    /*

                       /* myArrayList.add(nome);
                        myListView.setAdapter(myArrayAdapter);*/


/*

                        lista_nome[y] = nome;
                        lista_dataScadenza[y] = scadenza;
                        lista_dataAcquisto[y] = acquisto;
                        lista_costo[y] = costo;
                        lista_pasto[y] = pasto;
                        lista_categoria[y] = categoria;
                        lista_quantita[y] = quantita;
                        lista_nodo[y] = nodo;*/

                            y++;

/*
                        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                Toast.makeText(getActivity(), lista_nome[i] + " " + lista_dataScadenza[i] ,Toast.LENGTH_SHORT).show();
                                openDialog(lista_nome[i], lista_dataScadenza[i], lista_dataAcquisto[i], lista_costo[i], lista_pasto[i], lista_categoria[i], lista_quantita[i], lista_nodo[i]);

                            }
                        });
*/
                        } else {

                        }


                    }


/*


                float parziale_mensile=0;
                for(int p=0; p<4; p++){



                    if(id_categoria[p]==0){

                        SpesaCategoriaArrayList.add(id_categoria[p] + "€");
                        spese_categoria.setAdapter(myArrayAdapterSpeseCategoria);



                    }

                    else {

                        SpesaCategoriaArrayList.add(id_categoria[p] + "€");

                    }
*/
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
                    //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

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
                        // categoria.setVisibility(View.GONE);

                    } else {

                        chart.setVisibility(View.VISIBLE);

                        // categoria.setVisibility(View.VISIBLE);

                    }


/*


                chart.getDescription().setEnabled(false);

                //chart.setCenterTextTypeface(tfLight);
                //chart.setCenterText(generateCenterSpannableText());

                chart.setDrawHoleEnabled(true);
                chart.setHoleColor(Color.WHITE);

                chart.setTransparentCircleColor(Color.WHITE);
                chart.setTransparentCircleAlpha(110);

                chart.setHoleRadius(58f);
                chart.setTransparentCircleRadius(61f);

                chart.setDrawCenterText(true);

                chart.setRotationEnabled(false);
                chart.setHighlightPerTapEnabled(true);

                chart.setMaxAngle(180f); // HALF CHART
                chart.setRotationAngle(180f);
                chart.setCenterTextOffset(0, -20);

                //setData(4, 100);

                chart.animateY(1400, Easing.EaseInOutQuad);*/







/*
                    parziale_mensile+=id_categoria[p];
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    output = formatter.format(parziale_mensile);
                    spesa_mensile.setText(""+output+"€");

                    float div=(parziale_mensile /parziale) *100;
                    formatter.setMinimumFractionDigits(2);
                    formatter.setMaximumFractionDigits(2);
                    output = formatter.format(div);
                    rapporto.setText(output+"%");
                    */

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        DBRef.addListenerForSingleValueEvent(messageListener);



/*
        final ValueEventListener messageListener = new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String orario = dataSnapshot.child("Notifiche").child("Orario").getValue().toString();


                String[] separated = orario.split(":");
                String hour = separated[0];
                String minute = separated[1];

                int int_hour=Integer.parseInt(hour);
                int int_minute=Integer.parseInt(minute);

                Calendar rightNow = Calendar.getInstance();
                int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                int currentMinute = rightNow.get(Calendar.MINUTE);



                ora_notify.setHours(currentHour);
                ora_notify.setMinutes(currentMinute);
                ora_notify.setSeconds(01);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        DBRef.addListenerForSingleValueEvent(messageListener);

        Timer myTimer = new Timer("MyTimer", true);
        myTimer.scheduleAtFixedRate(new MyTask(), ora_notify, ONE_MINUTE);
        return root;

    }


    private class MyTask extends TimerTask {

        public void run(){



            final int[] read_notifiche = new int[1];

            ValueEventListener messageListener = new ValueEventListener() {



                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String orario = dataSnapshot.child("Notifiche").child("Orario").getValue().toString();

                    String[] separated = orario.split(":");
                    String hour = separated[0];
                    String minute = separated[1];

                    int int_hour = Integer.parseInt(hour);
                    int int_minute = Integer.parseInt(minute);

                    Calendar rightNow = Calendar.getInstance();
                    int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = rightNow.get(Calendar.MINUTE);



                    if (int_hour == currentHour && int_minute == currentMinute) {


                        String notifiche = dataSnapshot.child("Notifiche").child("Attivo").getValue().toString();
                        String read_day = dataSnapshot.child("Notifiche").child("Day_before").getValue().toString();
                        int _day = Integer.parseInt(read_day);

                        _day = _day * 10;

                        read_notifiche[0] = Integer.parseInt(notifiche);
                        //Toast.makeText(getActivity(), "" + read_notifiche[0], Toast.LENGTH_SHORT).show();

                        if (read_notifiche[0] == 1) {


                            String index = dataSnapshot.child("Prodotti").child("index").getValue().toString();

                            //Toast.makeText(getActivity(), index, Toast.LENGTH_SHORT).show();

                            final Integer intIndex = parseInt(index);

                            final String[] lista_nome = new String[intIndex];
                            final String[] lista_dataScadenza = new String[intIndex];
                            final String[] lista_dataAcquisto = new String[intIndex];
                            final String[] lista_pasto = new String[intIndex];
                            final String[] lista_costo = new String[intIndex];
                            final String[] lista_categoria = new String[intIndex];
                            final String[] lista_quantita = new String[intIndex];
                            final String[] lista_nodo = new String[intIndex];
                            final String[] lista_consumato = new String[intIndex];
                            int real_index = 0;
                            int y = 0;

                            int consumo = 0;

                            for (int i = 0; i < intIndex; i++) {

                                //String count= i.toString();
                                final String count = Integer.toString(i);
                                String nodo = "Prodotto_" + i;
                                // String nome = dataSnapshot.child("Prodotto_" + count).child("Nome").getValue().toString();
                                boolean check = dataSnapshot.child("Prodotti").child(nodo).exists();


                                if (check) {

                                    real_index++;
                                    //System.out.println(y);
                                    String nome = dataSnapshot.child("Prodotti").child(nodo).child("Nome").getValue().toString();

                                    Toast.makeText(getActivity(), nome, Toast.LENGTH_SHORT).show();
                                    String scadenza = dataSnapshot.child("Prodotti").child(nodo).child("Data_scadenza").getValue().toString();
                                    String acquisto = dataSnapshot.child("Prodotti").child(nodo).child("Data_acquisto").getValue().toString();
                                    String costo = dataSnapshot.child("Prodotti").child(nodo).child("Costo").getValue().toString();
                                    String pasto = dataSnapshot.child("Prodotti").child(nodo).child("Pasto").getValue().toString();
                                    String categoria = dataSnapshot.child("Prodotti").child(nodo).child("Categoria").getValue().toString();
                                    String quantita = dataSnapshot.child("Prodotti").child(nodo).child("Quantita").getValue().toString();
                                    String consumato = dataSnapshot.child("Prodotti").child(nodo).child("Consumato").getValue().toString();
                                    String scaduto = dataSnapshot.child("Prodotti").child(nodo).child("Scaduto").getValue().toString();

                                    consumo = parseInt(consumato);
                                    int scadere = parseInt(scaduto);
                                    System.out.println(nome);
                                    lista_nome[y] = nome;
                                    lista_dataScadenza[y] = scadenza;

                                    final Date c = Calendar.getInstance().getTime();
                                    //System.out.println("Current time => " + c);
                                    final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                    final String currentDate = df.format(c);

                                    SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    String scad = lista_dataScadenza[y];
                                    System.out.println(scad);

                                    if (scad != null) {


                                        try {

                                            Date date1 = myFormat.parse(scad);
                                            Date date2 = myFormat.parse(currentDate);

                                            long diff = date1.getTime() - date2.getTime();
                                            //long diff=0;
                                            diff = diff / 10000000;

                                            Toast.makeText(getActivity(), "" + diff, Toast.LENGTH_SHORT).show();

                                            if (diff < _day) {

                                                addNotification(lista_nome[y]);

                                            }

                                            //System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                                        } catch (ParseException e) {
                                            //data_scadenza.setText(null);
                                            e.printStackTrace();
                                        }

                                    }

                                    y++;


                                } else {

                                }


                            }


                        }


                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };
            DBRef.addListenerForSingleValueEvent(messageListener);




        */

        return root;

    }

    private void addNotification(String nome) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext(), "notify_001");
        Intent ii = new Intent(getContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Cerca di consumarlo quanto prima per evitare sprechi!");
        bigText.setBigContentTitle("Il prodotto " + nome + " sta per scadere!");
        bigText.setSummaryText("in scadenza");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Scadenze", NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;

        mNotificationManager.notify(m, mBuilder.build());

    }


private void setupPieChart(){






}

}
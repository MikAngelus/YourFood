package com.example.yourfood.ui.spesa;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.R;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.parseInt;

public class SpesaFragment extends Fragment {

    private SpesaViewModel spesaViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        spesaViewModel =
                ViewModelProviders.of(this).get(SpesaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_spesa, container, false);


        final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
        final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/");

        final TextView totale =root.findViewById(R.id.spesa_totale);

        final HorizontalScrollView scroll = root.findViewById(R.id.scroll_chip);



        final Chip[] chips = new Chip[12];
        chips[0]= root.findViewById(R.id.chip_gennaio);
        chips[1]=root.findViewById(R.id.chip_febbraio);
        chips[2]=root.findViewById(R.id.chip_marzo);
        chips[3]=root.findViewById(R.id.chip_aprile);
        chips[4]=root.findViewById(R.id.chip_maggio);
        chips[5]=root.findViewById(R.id.chip_giugno);
        chips[6]=root.findViewById(R.id.chip_luglio);
        chips[7]=root.findViewById(R.id.chip_agosto);
        chips[8]=root.findViewById(R.id.chip_settembre);
        chips[9]=root.findViewById(R.id.chip_ottobre);
        chips[10]=root.findViewById(R.id.chip_novembre);
        chips[11]=root.findViewById(R.id.chip_dicembre);

        final int[] scelta_mese = new int[1];

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int mese;
        mese = c.get(Calendar.MONTH);



        System.out.println(mese);

        scelta_mese[0]=mese;
        chips[mese].setChecked(true);
        chips[mese].setTextColor(Color.WHITE);

        final String[] mese_selezionato = {"01/" + (mese + 1) + "/" + year + ""};
        System.out.println(mese_selezionato[0]);



        int i=0;

        for(i=0; i<=mese; i++){

            final int finalI = i;


            chips[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chips[scelta_mese[0]].setChecked(false);
                //chips[scelta_mese[0]].setTextColor(Color.DKGRAY);

                chips[finalI].setChecked(true);
                   //chips[finalI].setTextColor(Color.WHITE);
                scelta_mese[0] = finalI;

                    if(scelta_mese[0]<9){

                        mese_selezionato[0] ="01/0"+(scelta_mese[0]+1)+"/"+year+"";
                        System.out.println(mese_selezionato[0]);

                    }

                    else{
                        mese_selezionato[0] ="01/"+(scelta_mese[0]+1)+"/"+year+"";
                        System.out.println(mese_selezionato[0]);

                    }


               }

        });

        }

        for(i=mese+1; i<chips.length; i++){
            chips[i].setVisibility(View.GONE);
            //chips[i].setEnabled(false);
        }


        final ArrayList<String> CategoriaArrayList = new ArrayList<>();
        final ListView lista_categoria;
        lista_categoria = (ListView) root.findViewById(R.id.lista_categoria);
        final ArrayAdapter<String> myArrayAdapterCategoria = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, CategoriaArrayList);

       // String[] mess = getResources().getStringArray(R.string.);
        CategoriaArrayList.add("Pane");
        CategoriaArrayList.add("Carne");
        CategoriaArrayList.add("Pesce");
        CategoriaArrayList.add("Vegano");
        lista_categoria.setAdapter(myArrayAdapterCategoria);


        final ArrayList<String> SpesaCategoriaArrayList = new ArrayList<>();
        final ListView spese_categoria;
        spese_categoria = (ListView) root.findViewById(R.id.lista_spese_categoria);
        final ArrayAdapter<String> myArrayAdapterSpeseCategoria = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, SpesaCategoriaArrayList);

        // String[] mess = getResources().getStringArray(R.string.);






        ValueEventListener messageListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String index = dataSnapshot.child("index").getValue().toString();

                final Integer intIndex = parseInt(index);
/*
                final String[] lista_nome = new String[intIndex];
                final String[] lista_dataScadenza = new String[intIndex];
                final String[] lista_dataAcquisto = new String[intIndex];
                final String[] lista_pasto = new String[intIndex];
                final String[] lista_costo = new String[intIndex];
                final String[] lista_categoria = new String[intIndex];
                final String[] lista_quantita = new String[intIndex];
                final String[] lista_nodo = new String[intIndex];*/
                int y = 0;

                float parziale = 0;

                float[] id_categoria = new float[5];

                for (int i = 0; i < intIndex; i++) {

                    //String count= i.toString();
                    final String count = Integer.toString(i);
                    String nodo = "Prodotto_" + i;
                    // String nome = dataSnapshot.child("Prodotto_" + count).child("Nome").getValue().toString();
                    boolean check = dataSnapshot.child(nodo).exists();


                    if (check) {


                        System.out.println(y);

                        // String nome = dataSnapshot.child(nodo).child("Nome").getValue().toString();
                        //String scadenza = dataSnapshot.child(nodo).child("Data_scadenza").getValue().toString();
                        String acquisto = dataSnapshot.child(nodo).child("Data_acquisto").getValue().toString();
                        String costo = dataSnapshot.child(nodo).child("Costo").getValue().toString();
                        // String pasto = dataSnapshot.child(nodo).child("Pasto").getValue().toString();
                        String categoria = dataSnapshot.child(nodo).child("Categoria").getValue().toString();
                        String quantita = dataSnapshot.child(nodo).child("Quantita").getValue().toString();

                        float x = Float.parseFloat(costo);
                        float z = Float.parseFloat(quantita);

                        boolean flag=false;

                        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                        //String inputString1 = acquisto.getText().toString();
                        //String inputString2 = mese_selezionato[0];

                        try {

                            Date date1 = myFormat.parse(acquisto);
                            Date date2 = myFormat.parse(mese_selezionato[0]);
                            long diff = date2.getTime() - date1.getTime();

                            if (diff > 0) {

                               flag=true;


                            }

                            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                        } catch (ParseException e) {
                            flag=false;
                            e.printStackTrace();
                        }

                        parziale = parziale + (x * z);

                        if(flag) {

                            int read_categoria = Integer.parseInt(categoria);

                            for (int p = 0; p < id_categoria.length; p++) {

                                if (read_categoria == p) {

                                    id_categoria[p] = id_categoria[p] + (x * z);

                                    if(id_categoria[p]==0){

                                        SpesaCategoriaArrayList.add("0.0€");

                                    }

                                    else {

                                        SpesaCategoriaArrayList.add(id_categoria[p] + "€");
                                    }
                                }
                            }

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


                totale.setText(String.valueOf(parziale) + "€");

                spese_categoria.setAdapter(myArrayAdapterSpeseCategoria);




            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){

            }

        }

                ;DBRef.addListenerForSingleValueEvent(messageListener);










        /*
        spesaViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}
package com.example.yourfood.ui.lista;

import android.R.layout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourfood.ListDialog;
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

import static java.lang.Integer.parseInt;

public class ListaFragment extends Fragment {


    public RecyclerView mRecyclerView;
    public ExampleAdapter mAdapter;
    public RecyclerView.LayoutManager mLayoutManager;

    final Date c = Calendar.getInstance().getTime();
    //System.out.println("Current time => " + c);

    final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    final String currentDate = df.format(c);

    final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
    final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
    final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/");

    private ListaViewModel listaViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listaViewModel =
                ViewModelProviders.of(this).get(ListaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lista, container, false);


        final ArrayList<String> myArrayList = new ArrayList<>();
        final ArrayList<String> myArrayList2 = new ArrayList<>();
        final ListView myListView;
        final ListView myListView2;

        final ArrayList<item> list = new ArrayList<>();
        final RecyclerView mRecyclerView = root.findViewById(R.id.recyler_view);

        myListView = (ListView) root.findViewById(R.id.daconsumare);
        final Spinner tipo = root.findViewById(R.id.tipo_prodotto);

        System.out.println(currentDate);

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), layout.simple_list_item_1, myArrayList);
        final ArrayAdapter<String> myArrayAdapter2 = new ArrayAdapter<String>(getActivity(), layout.simple_list_item_1, myArrayList2);

        final Guideline verticale_colazione = root.findViewById(R.id.guideline35);
        final Guideline verticale_merenda = root.findViewById(R.id.guideline36);
        final Guideline verticale_pranzo = root.findViewById(R.id.guideline37);


        final int[] scelta_pasto = new int[1];
        scelta_pasto[0] = -1;

        final TextView prova = (TextView) root.findViewById(R.id.textViewProva);

        final Chip colazione = (Chip) root.findViewById(R.id.chip_colazione);
        final Chip merenda = (Chip) root.findViewById(R.id.chip_merenda);
        final Chip pranzo = (Chip) root.findViewById(R.id.chip_pranzo);
        final Chip cena = (Chip) root.findViewById(R.id.chip_cena);

        colazione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scelta_pasto[0] == 0) {
                    scelta_pasto[0] = -1;
                    colazione.setChecked(false);
                    verticale_colazione.setGuidelinePercent(0.30f);
                } else {

                    scelta_pasto[0] = 0;
                    colazione.setChecked(true);
                    verticale_colazione.setGuidelinePercent(0.33f);
                    merenda.setChecked(false);
                    pranzo.setChecked(false);
                    cena.setChecked(false);

                    verticale_merenda.setGuidelinePercent(0.55f);
                }
                list.clear();
                myArrayList.clear();
                setLista(scelta_pasto, tipo, myArrayList, myArrayAdapter, myListView, list, mRecyclerView);

            }
        });

        merenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scelta_pasto[0] == 1) {

                    scelta_pasto[0] = -1;

                    merenda.setChecked(false);
                    verticale_colazione.setGuidelinePercent(0.30f);
                } else {

                    scelta_pasto[0] = 1;
                    merenda.setChecked(true);
                    verticale_colazione.setGuidelinePercent(0.27f);
                    verticale_merenda.setGuidelinePercent(0.55f);

                    colazione.setChecked(false);
                    pranzo.setChecked(false);
                    cena.setChecked(false);


                }
                list.clear();
                myArrayList.clear();
                setLista(scelta_pasto, tipo, myArrayList, myArrayAdapter, myListView, list, mRecyclerView);

            }
        });

        pranzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scelta_pasto[0] == 2) {

                    scelta_pasto[0] = -1;
                    pranzo.setChecked(false);
                    verticale_merenda.setGuidelinePercent(0.55f);
                } else {

                    scelta_pasto[0] = 2;
                    pranzo.setChecked(true);
                    verticale_merenda.setGuidelinePercent(0.51f);

                    colazione.setChecked(false);
                    merenda.setChecked(false);
                    cena.setChecked(false);
                    verticale_colazione.setGuidelinePercent(0.30f);
                    verticale_pranzo.setGuidelinePercent(0.75f);


                }
                list.clear();
                myArrayList.clear();
                setLista(scelta_pasto, tipo, myArrayList, myArrayAdapter, myListView, list, mRecyclerView);

            }
        });

        cena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scelta_pasto[0] == 3) {

                    scelta_pasto[0] = -1;
                    cena.setChecked(false);
                    verticale_pranzo.setGuidelinePercent(0.75f);

                } else {

                    scelta_pasto[0] = 3;

                    cena.setChecked(true);
                    verticale_pranzo.setGuidelinePercent(0.73f);

                    colazione.setChecked(false);
                    merenda.setChecked(false);
                    pranzo.setChecked(false);
                    ;
                    verticale_merenda.setGuidelinePercent(0.55f);
                    verticale_colazione.setGuidelinePercent(0.30f);


                }
                list.clear();
                myArrayList.clear();
                setLista(scelta_pasto, tipo, myArrayList, myArrayAdapter, myListView, list, mRecyclerView);

            }
        });


        Button recycler = root.findViewById(R.id.recyler);
        recycler.setVisibility(View.INVISIBLE);

        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                myArrayList.clear();
                list.clear();
                setLista(scelta_pasto, tipo, myArrayList, myArrayAdapter, myListView, list, mRecyclerView);

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });


        return root;

    }


    private void setLista(final int[] scelta_pasto, final Spinner tipo, final ArrayList<String> myArrayList, final ArrayAdapter<String> myArrayAdapter, final ListView myListView, final ArrayList<item> list, final RecyclerView mRecyclerView) {

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(list);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ValueEventListener messageListener = new ValueEventListener() {

            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {


                String index = dataSnapshot.child("index").getValue().toString();

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

                    final String count = Integer.toString(i);
                    final String nodo = "Prodotto_" + i;
                    boolean check = dataSnapshot.child(nodo).exists();


                    if (check) {


                        real_index++;
                        String nome = dataSnapshot.child(nodo).child("Nome").getValue().toString();
                        String scadenza = dataSnapshot.child(nodo).child("Data_scadenza").getValue().toString();
                        String acquisto = dataSnapshot.child(nodo).child("Data_acquisto").getValue().toString();
                        String costo = dataSnapshot.child(nodo).child("Costo").getValue().toString();
                        String pasto = dataSnapshot.child(nodo).child("Pasto").getValue().toString();
                        String categoria = dataSnapshot.child(nodo).child("Categoria").getValue().toString();
                        String quantita = dataSnapshot.child(nodo).child("Quantita").getValue().toString();
                        String consumato = dataSnapshot.child(nodo).child("Consumato").getValue().toString();
                        String scaduto = dataSnapshot.child(nodo).child("Scaduto").getValue().toString();

                        consumo = parseInt(consumato);
                        int scadere = parseInt(scaduto);
                        int var_pasto = parseInt(pasto);

                        float g = 1.0f;

                        if (scelta_pasto[0] < 0 || scelta_pasto[0] == var_pasto) {

                            if (scadere == 1 && tipo.getSelectedItemId() == 2) {

                                if (consumo == 0) {
                                    list.add(new item(nome, "Scadenza: " + scadenza, "Consuma!", R.drawable.ic_checked));

                                } else if (consumo == 1) {
                                    list.add(new item(nome, "Scadenza: " + scadenza, "", g, "Consumato"));

                                }


                                myArrayList.add(nome);
                                myListView.setAdapter(myArrayAdapter);

                                lista_nome[y] = nome;
                                lista_dataScadenza[y] = scadenza;
                                lista_dataAcquisto[y] = acquisto;
                                lista_costo[y] = costo;
                                lista_pasto[y] = pasto;
                                lista_categoria[y] = categoria;
                                lista_quantita[y] = quantita;
                                lista_nodo[y] = nodo;
                                lista_consumato[y] = consumato;
                                y++;

                                myListView.setBackgroundResource(R.drawable.list_dacosumare);


                            }

                            if (consumo < 1 && tipo.getSelectedItemId() == 0) {
                                list.add(new item(nome, "Scadenza: " + scadenza, "Consuma!", R.drawable.ic_checked));


                                myArrayList.add(nome);
                                myListView.setAdapter(myArrayAdapter);

                                lista_nome[y] = nome;
                                lista_dataScadenza[y] = scadenza;
                                lista_dataAcquisto[y] = acquisto;
                                lista_costo[y] = costo;
                                lista_pasto[y] = pasto;
                                lista_categoria[y] = categoria;
                                lista_quantita[y] = quantita;
                                lista_nodo[y] = nodo;
                                lista_consumato[y] = consumato;
                                y++;

                                myListView.setBackgroundResource(R.drawable.list_dacosumare);


                            }

                            if (consumo > 0 && tipo.getSelectedItemId() == 1) {
                                list.add(new item(nome, "Scadenza: " + scadenza, "", g, "Consumato"));


                                myArrayList.add(nome);
                                myListView.setAdapter(myArrayAdapter);

                                lista_nome[y] = nome;
                                lista_dataScadenza[y] = scadenza;
                                lista_dataAcquisto[y] = acquisto;
                                lista_costo[y] = costo;
                                lista_pasto[y] = pasto;
                                lista_categoria[y] = categoria;
                                lista_quantita[y] = quantita;
                                lista_nodo[y] = nodo;
                                lista_consumato[y] = consumato;
                                y++;

                                myListView.setBackgroundResource(R.drawable.list_dacosumare);


                            }


                            mRecyclerView.setAdapter(mAdapter);


                        }


                        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                mAdapter.notifyItemChanged(position);
                                openDialog(lista_nome[position], lista_dataScadenza[position], lista_dataAcquisto[position], lista_costo[position], lista_pasto[position], lista_categoria[position], lista_quantita[position], lista_nodo[position], lista_consumato[position]);

                            }

                            @Override
                            public void onDeleteClick(int position) {

                                list.remove(position);
                                mAdapter.notifyItemRemoved(position);
                                final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
                                final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
                                final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/" + lista_nodo[position]);
                                DBRef.removeValue();
                            }

                            @Override
                            public void onConsumatoClick(int position) {

                                int read_consumato = parseInt(lista_consumato[position]);

                                if (tipo.getSelectedItemId() == 0 || (tipo.getSelectedItemId() == 2 && read_consumato == 0)) {
                                    list.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                    final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
                                    final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
                                    final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/" + lista_nodo[position]);
                                    DBRef.child("Consumato").setValue("1");

                                } else {


                                }

                                Fragment someFragment = new ListaFragment();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.nav_host_fragment, someFragment); // give your fragment container id in first parameter
                                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                                transaction.commit();
                            }


                        });


                        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                openDialog(lista_nome[i], lista_dataScadenza[i], lista_dataAcquisto[i], lista_costo[i], lista_pasto[i], lista_categoria[i], lista_quantita[i], lista_nodo[i], lista_consumato[i]);

                            }
                        });

                    } else {

                    }

                }


                if (myArrayList.size() < 1) {
                    myListView.setVisibility(View.INVISIBLE);

                } else if (myArrayList.size() > 0) {
                    controllo_scadenza(lista_dataScadenza, lista_nodo, real_index, lista_consumato);
                    myListView.setVisibility(View.INVISIBLE);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };
        DBRef.addListenerForSingleValueEvent(messageListener);


    }

    private void controllo_scadenza(String[] lista_dataScadenza, String[] lista_nodo, Integer real_index, String[] cons) {

        String[] scadenze = lista_dataScadenza;
        final String[] nodi = lista_nodo;
        int index = real_index;
        int i = 0;
        String[] read_consumato = cons;

        for (i = 0; i < index; i++) {

            SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
            String scad = lista_dataScadenza[i];
            System.out.println(scad);


            if (read_consumato[i] != null) {

                int read_cons = Integer.parseInt(read_consumato[i]);

                if (scad != null && read_cons == 0) {


                    try {

                        Date date1 = myFormat.parse(scad);
                        Date date2 = myFormat.parse(currentDate);

                        long diff = date1.getTime() - date2.getTime();

                        if (diff < 0) {

                            System.out.println("scaduto");

                            final int finalI = i;
                            ValueEventListener messageListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                    DBRef.child(nodi[finalI]).child("Scaduto").setValue("1");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };
                            DBRef.addListenerForSingleValueEvent(messageListener);


                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    public void openDialog(String title, String scadenza, String acquisto, String costo, String pasto, String categoria, String quantita, String nodo, String consumato) {

        ListDialog listDialog = new ListDialog();
        listDialog.nome_prodotto = title;
        listDialog.data_scadenza = scadenza;
        listDialog.data_acquisto = acquisto;
        listDialog.costo = costo;
        listDialog.pasto = pasto;
        listDialog.categoria = categoria;
        listDialog.quantita = quantita;
        listDialog.posizione = nodo;
        listDialog.consumato = consumato;
        listDialog.show(getFragmentManager(), "Lista Dialog");

    }

}
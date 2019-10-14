package com.example.yourfood.ui.lista;

import android.R.layout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.ListDialog;
import com.example.yourfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class ListaFragment extends Fragment {

    private ListaViewModel listaViewModel;

    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        listaViewModel =
                ViewModelProviders.of(this).get(ListaViewModel.class);
        View root = inflater.inflate(R.layout.fragment_lista, container, false);


        final ArrayList<String> myArrayList = new ArrayList<>();
        final ListView myListView;

        final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
        final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/");

        final TextView prova = (TextView) root.findViewById(R.id.textViewProva);

        myListView = (ListView) root.findViewById(R.id.lista_prodotti);
        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(getActivity(), layout.simple_list_item_1, myArrayList);


        ValueEventListener messageListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


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
                int y=0;

                for (int i = 0; i < intIndex; i++) {

                    //String count= i.toString();
                    final String count = Integer.toString(i);
                    String nodo = "Prodotto_" + i;
                   // String nome = dataSnapshot.child("Prodotto_" + count).child("Nome").getValue().toString();
                    boolean  check = dataSnapshot.child(nodo).exists();


                       if(check) {



                           System.out.println(y);

                           String nome = dataSnapshot.child(nodo).child("Nome").getValue().toString();
                           String scadenza = dataSnapshot.child(nodo).child("Data_scadenza").getValue().toString();
                           String acquisto = dataSnapshot.child(nodo).child("Data_acquisto").getValue().toString();
                           String costo = dataSnapshot.child(nodo).child("Costo").getValue().toString();
                           String pasto = dataSnapshot.child(nodo).child("Pasto").getValue().toString();
                           String categoria = dataSnapshot.child(nodo).child("Categoria").getValue().toString();
                           String quantita = dataSnapshot.child(nodo).child("Quantita").getValue().toString();

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

                           y++;


                           myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                               @Override
                               public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                   Toast.makeText(getActivity(), lista_nome[i] + " " + lista_dataScadenza[i] ,Toast.LENGTH_SHORT).show();
                                   openDialog(lista_nome[i], lista_dataScadenza[i], lista_dataAcquisto[i], lista_costo[i], lista_pasto[i], lista_categoria[i], lista_quantita[i], lista_nodo[i]);

                               }
                           });

                       }

                       else {

                        }

                    }






                }

                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }

            }

            ;DBRef.addListenerForSingleValueEvent(messageListener);



        return root;

        }


    public void openDialog(String title, String scadenza, String acquisto, String costo, String pasto, String categoria, String quantita, String nodo){

        //String prova="Petto di pollo";
        //String scad="11/10/2019";
        ListDialog listDialog = new ListDialog();
        listDialog.nome_prodotto=title;
        listDialog.data_scadenza=scadenza;
        listDialog.data_acquisto=acquisto;
        listDialog.costo=costo;
        listDialog.pasto=pasto;
        listDialog.categoria=categoria;
        listDialog.quantita=quantita;
        listDialog.posizione=nodo;

        listDialog.show(getFragmentManager(),"Lista Dialog");



    }




}
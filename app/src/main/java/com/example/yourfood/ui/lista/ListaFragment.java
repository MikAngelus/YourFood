package com.example.yourfood.ui.lista;

import android.R.layout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static java.lang.Integer.getInteger;
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

                String parentName = "Prodotto_" + index;
               // DatabaseReference DBRef2 = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/" + parentName);

               Integer intIndex = parseInt(index);

              for(int i = 0; i < intIndex; i++){
                  //String count= i.toString();
                  String count= Integer.toString(i);

                   String nome= dataSnapshot.child("Prodotto_"+count).child("Nome").getValue().toString();
                    myArrayList.add(nome);
                    myListView.setAdapter(myArrayAdapter);
                   // prova.setText(nome);

              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };DBRef.addListenerForSingleValueEvent(messageListener);

        
        return root;
    }
}
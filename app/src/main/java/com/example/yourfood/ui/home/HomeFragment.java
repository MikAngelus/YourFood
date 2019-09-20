package com.example.yourfood.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
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

import static java.lang.Integer.parseInt;

public class HomeFragment extends Fragment {

   // private HomeViewModel homeViewModel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       // homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView nome_user = root.findViewById(R.id.textViewUser);
        FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
        String strMNome = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();

        if (strMNome!=null){
        nome_user.setText("Ciao " + strMNome);}



        //String nome="Michelangelo";


        //PRODOTTO 0 -> CATEGORIA: PANE

        /*final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/Prodotto_1");

        ValueEventListener messageListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tipo_categoria = dataSnapshot.child("Categoria").getValue().toString();
                nome_user.setText(tipo_categoria);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };DBRef.addListenerForSingleValueEvent(messageListener);*/


        return root;
    }
}

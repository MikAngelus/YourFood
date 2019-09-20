package com.example.yourfood.ui.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Objects;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class AddFragment extends Fragment {

    private AddViewModel addViewModel;

   /* final Integer[] dataControll = {0};
    String data = null;*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        addViewModel =ViewModelProviders.of(this).get(AddViewModel.class);

        View root = inflater.inflate(R.layout.fragment_add, container, false);

        final EditText nome_prodotto=root.findViewById(R.id.editTextNomeProdotto);
        final EditText data_acquisto=root.findViewById(R.id.editTextAcquisto);
        final EditText data_scadenza=root.findViewById(R.id.editTextScadenza);
        final Spinner pasto=root.findViewById(R.id.spinnerPasto);
        final Spinner categoria=root.findViewById(R.id.spinnerCategoria);
        final EditText num_quantita=root.findViewById(R.id.editTextQuantita);
        final EditText costo=root.findViewById(R.id.editTextCosto);
        final CalendarView calendarView = root.findViewById(R.id.calendarView2);


        final Button reset=root.findViewById(R.id.resetField);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetCampi();

            }

            private void resetCampi() {

                nome_prodotto.setText(null);
                data_acquisto.setText(null);
                data_scadenza.setText(null);
                num_quantita.setText(null);
                costo.setText(null);
            }

        });

        Button salva=root.findViewById(R.id.save);
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
                final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
                final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti");

                final String nomeProdotto = nome_prodotto.getText().toString();
                final String dataScadenza = data_scadenza.getText().toString();
                final String dataAcquisto = data_acquisto.getText().toString();
                final String dataPasto = pasto.getSelectedItem().toString();
                final String dataCategoria = categoria.getSelectedItem().toString();
                final String dataQuantita = num_quantita.getText().toString();
                final String dataCosto = costo.getText().toString();

                /*
                data_scadenza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendarView.setVisibility(View.VISIBLE);
                        dataControll[0] = 1;
                    }
                });

                reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendarView.setVisibility(View.VISIBLE);
                        dataControll[0] = 2;
                    }
                });

                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                        data = i + "/" + i1 + "/" + i2;
                        if(dataControll[0] == 1){
                            data_scadenza.setText(data.toString());
                        }else {
                            data_acquisto.setText(data.toString());
                        }
                        calendarView.setVisibility(View.INVISIBLE);
                    }
                });*/

                ValueEventListener messageListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String index = dataSnapshot.child("index").getValue().toString();

                        String parentName = "Prodotto_" + index;
                        DatabaseReference DBRef2 = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti/" + parentName);
                        DBRef2.child("Nome").setValue(nomeProdotto);
                        DBRef2.child("Data_scadenza").setValue(dataScadenza);
                        DBRef2.child("Data_acquisto").setValue(dataAcquisto);
                        DBRef2.child("Pasto").setValue(dataPasto);
                        DBRef2.child("Categoria").setValue(dataCategoria);
                        DBRef2.child("Quantita").setValue(dataQuantita);
                        DBRef2.child("Costo").setValue(dataCosto);
                        Integer intIndex = parseInt(index);
                        intIndex++;
                        DBRef.child("index").setValue(intIndex.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                };DBRef.addListenerForSingleValueEvent(messageListener);

                resetCampi();

                Toast.makeText(getActivity(),"Prodotto aggiunto alla lista!",Toast.LENGTH_SHORT).show();



            }

            private void resetCampi() {

                nome_prodotto.setText(null);
                data_acquisto.setText(null);
                data_scadenza.setText(null);
                num_quantita.setText(null);
                costo.setText(null);
            }


        });


        final TextView textView = root.findViewById(R.id.text_add);
        addViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
            
            
        });
        return root;


        
    }



}
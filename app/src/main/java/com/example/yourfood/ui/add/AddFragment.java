package com.example.yourfood.ui.add;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class AddFragment extends Fragment {

    private AddViewModel addViewModel;
    private DatePickerDialog.OnDateSetListener mDateSetListenerAcquisto, mDateSetListenerScadenza;

    final Date c = Calendar.getInstance().getTime();
    final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    final String currentDate = df.format(c);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        addViewModel = ViewModelProviders.of(this).get(AddViewModel.class);

        View root = inflater.inflate(R.layout.fragment_add, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        final EditText nome_prodotto = root.findViewById(R.id.editTextNomeProdotto);
        final EditText data_acquisto = root.findViewById(R.id.editTextAcquisto);
        final EditText data_scadenza = root.findViewById(R.id.editTextScadenza);
        final Spinner pasto = root.findViewById(R.id.spinnerPasto);
        final Spinner categoria = root.findViewById(R.id.spinnerCategoria);
        final EditText num_quantita = root.findViewById(R.id.editTextQuantita);
        final EditText costo = root.findViewById(R.id.editTextCosto);

        Calendar c = Calendar.getInstance();
        final int mDay = c.get(Calendar.DAY_OF_MONTH);
        final int mMonth = c.get(Calendar.MONTH);
        final int mYear = c.get(Calendar.YEAR);

        final ImageButton btnDataAccquisto = root.findViewById(R.id.btnDataAcquisto);
        btnDataAccquisto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, mDateSetListenerAcquisto, mYear, mMonth, mDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListenerAcquisto = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mDay, int mMonth, int mYear) {
                // data_acquisto.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                String day = "" + mYear;
                String month = "" + (mMonth + 1);
                String year = "" + mDay;

                if (mYear > 0 && mYear < 10) {
                    day = "0" + day;
                }

                if (mMonth + 1 > 0 && mMonth + 1 < 10) {
                    month = "0" + month;
                }

                data_acquisto.setText(day + "/" + month + "/" + year);
                Calendar rightNow = Calendar.getInstance();
                int currentDay = rightNow.get(Calendar.DAY_OF_MONTH);
                int currentMounth = rightNow.get(Calendar.MONTH);
                int currentYear = rightNow.get(Calendar.YEAR);


                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputString1 = data_acquisto.getText().toString();
                String inputString2;

                try {

                    Date date1 = myFormat.parse(inputString1);
                    Date date2 = myFormat.parse(currentDate);

                    long diff = date1.getTime() - date2.getTime();
                    //long diff=0;

                    if (diff > 0) {

                        data_acquisto.setText(null);
                        Toast.makeText(getActivity(), "Non puoi viaggiare nel tempo!", Toast.LENGTH_SHORT).show();

                    }

                } catch (ParseException e) {
                    data_acquisto.setText(null);
                    e.printStackTrace();
                }


            }
        };

        data_acquisto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputString1 = data_acquisto.getText().toString();


                try {

                    Date date1 = myFormat.parse(inputString1);
                    System.out.println(date1);

                } catch (ParseException e) {

                    data_acquisto.setText(null);
                    data_scadenza.setText(null);
                    Toast.makeText(getActivity(), "Inserire una data acquisto/scadenza valida: dd/mm/yy", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        final ImageButton btnDataScadenza = root.findViewById(R.id.btnDataScadenza);
        btnDataScadenza.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                DatePickerDialog dialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, mDateSetListenerScadenza, mYear, mMonth, mDay);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListenerScadenza = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mDay, int mMonth, int mYear) {
                // data_acquisto.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
                String day = "" + mYear;
                String month = "" + (mMonth + 1);
                String year = "" + mDay;

                if (mYear > 0 && mYear < 10) {

                    day = "0" + day;

                }

                if (mMonth + 1 > 0 && mMonth + 1 < 10) {

                    month = "0" + month;
                }

                data_scadenza.setText(day + "/" + month + "/" + year);

                SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                String inputString1 = data_acquisto.getText().toString();
                String inputString2 = data_scadenza.getText().toString();

                try {

                    Date date1 = myFormat.parse(inputString1);
                    Date date2 = myFormat.parse(inputString2);
                    long diff = date2.getTime() - date1.getTime();

                    if (diff < 0) {

                        data_scadenza.setText(null);
                        Toast.makeText(getActivity(), "Seleziona una data scadenza maggiore della data di acquisto!", Toast.LENGTH_SHORT).show();


                    }

                    System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                } catch (ParseException e) {
                    data_scadenza.setText(null);
                    e.printStackTrace();
                }

            }

        };


        data_scadenza.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String inputString1 = data_acquisto.getText().toString();
                    String inputString2 = data_scadenza.getText().toString();

                    try {

                        Date date1 = myFormat.parse(inputString1);
                        System.out.println(date1);
                        Date date2 = myFormat.parse(inputString2);
                        System.out.println(date2);
                        long diff = date2.getTime() - date1.getTime();

                        if (diff < 0) {

                            data_scadenza.setText(null);
                            Toast.makeText(getActivity(), "Seleziona una data scadenza maggiore della data di acquisto!", Toast.LENGTH_SHORT).show();

                        }

                        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                    } catch (ParseException e) {

                        data_scadenza.setText(null);
                        Toast.makeText(getActivity(), "Inserire una data scadenza valida: dd/mm/yy", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        });


        final Button reset = root.findViewById(R.id.resetField);
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

        Button salva = root.findViewById(R.id.save);
        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((nome_prodotto.getText().toString().trim().length()) > 0 &&
                        (data_acquisto.getText().toString().trim().length()) > 0 &&
                        (data_scadenza.getText().toString().trim().length()) > 0 &&
                        (num_quantita.getText().toString().trim().length()) > 0 &&
                        (costo.getText().toString().trim().length()) > 0
                ) {


                    if (parseInt(num_quantita.getText().toString()) > 0 && parseFloat(costo.getText().toString()) > 0 && (nome_prodotto.getText().toString().trim().length()) > 0) {


                        final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
                        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
                        final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID + "/Prodotti");


                        final String nomeProdotto = nome_prodotto.getText().toString();
                        final String dataScadenza = data_scadenza.getText().toString();
                        final String dataAcquisto = data_acquisto.getText().toString();
                        int idPasto = pasto.getSelectedItemPosition();
                        final String dataPasto = String.valueOf(idPasto);
                        int idCategoria = categoria.getSelectedItemPosition();
                        final String dataCategoria = String.valueOf(idCategoria);
                        final String dataQuantita = num_quantita.getText().toString();
                        final String dataCosto = costo.getText().toString();

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
                                DBRef2.child("Consumato").setValue("0");
                                DBRef2.child("Scaduto").setValue("0");
                                DBRef2.child("Costo").setValue(dataCosto);
                                Integer intIndex = parseInt(index);
                                intIndex++;
                                DBRef.child("index").setValue(intIndex.toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        DBRef.addListenerForSingleValueEvent(messageListener);

                        // resetCampi();

                        Snackbar.make(getView(), R.string.saved, Snackbar.LENGTH_SHORT)
                                .show();

                        nome_prodotto.setText(null);
                        data_acquisto.setText(null);
                        data_scadenza.setText(null);
                        num_quantita.setText(null);
                        costo.setText(null);

                    } else {

                        Snackbar.make(getView(), "Inserisci una quantità o costo valida!", Snackbar.LENGTH_SHORT).show();

                    }
                } else {

                    Snackbar.make(getView(), "Attenzione: dati mancanti!", Snackbar.LENGTH_SHORT).show();

                }


            }


        });


        return root;

    }


}
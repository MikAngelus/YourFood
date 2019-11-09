package com.example.yourfood.ui.notifications;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.yourfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class NotificationsFragment extends Fragment{

    private NotificationsViewModel notificationsViewModel;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
    final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
    final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID);

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);


        final Switch attiva_notifiche = root.findViewById(R.id.attivo);
        ImageButton time = root.findViewById(R.id.btnTime);

        final View nascondi = root.findViewById(R.id.nascondi_view);
        final Spinner day = root.findViewById(R.id.day);

        final boolean[] attivo = {false};

        final TextView ora_selezionata = root.findViewById(R.id.orario);
        final int[] read_notifiche = new int[1];


        final Button salva = root.findViewById(R.id.save);
        final Button disable = root.findViewById(R.id.disableNotif);
        salva.setVisibility(View.GONE);
        disable.setVisibility(View.GONE);


        ValueEventListener messageListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String notifiche = dataSnapshot.child("Notifiche").child("Attivo").getValue().toString();
                String orario= dataSnapshot.child("Notifiche").child("Orario").getValue().toString();
                String read_day= dataSnapshot.child("Notifiche").child("Day_before").getValue().toString();
                int _day = Integer.parseInt(read_day);

                day.setSelection(_day-1);


                ora_selezionata.setText(orario);

                read_notifiche[0] = Integer.parseInt(notifiche);
                Toast.makeText(getActivity(), ""+ read_notifiche[0],Toast.LENGTH_SHORT).show();

                if(read_notifiche[0]==1){

                    nascondi.setVisibility(View.VISIBLE);
                    attivo[0] =false;
                    attiva_notifiche.setChecked(true);

                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        DBRef.addListenerForSingleValueEvent(messageListener);


        if(read_notifiche[0]==1){

            nascondi.setVisibility(View.VISIBLE);
            attivo[0] =false;
            attiva_notifiche.setChecked(true);
            salva.setVisibility(View.VISIBLE);
            disable.setVisibility(View.VISIBLE);
        }

        attiva_notifiche.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                nascondi.setVisibility(View.VISIBLE);
                attivo[0] =false;
                salva.setVisibility(View.GONE);
                disable.setVisibility(View.GONE);


                if(isChecked){

                    nascondi.setVisibility(View.GONE);
                    attivo[0] =true;
                    salva.setVisibility(View.VISIBLE);
                    disable.setVisibility(View.VISIBLE);

                   // final int[] read_notifiche = new int[1];




                }

                Toast.makeText(getActivity(), ""+attivo[0],Toast.LENGTH_SHORT).show();


                ValueEventListener messageListener = new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!attivo[0]){

                        DBRef.child("Notifiche").child("Attivo").setValue("0");
                        }

                        else {
                            DBRef.child("Notifiche").child("Attivo").setValue("1");
                        }

                        //Toast.makeText(getActivity(), ""+ read_notifiche[0],Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                };
                DBRef.addListenerForSingleValueEvent(messageListener);



            }
        });


        ora_selezionata.setText("");

        Calendar c = Calendar.getInstance();

        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);


        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                TimePickerDialog dialog = new TimePickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, mTimeSetListener, hour, minute, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                ora_selezionata.setText(hourOfDay + ":" + minute);

                String selectedTime = (String) ora_selezionata.getText();
                DBRef.child("Notifiche").child("Orario").setValue(selectedTime);


            }

        };


        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idDay = day.getSelectedItemPosition();
                idDay++;
                final String dataDay = String.valueOf(idDay);
                DBRef.child("Notifiche").child("Day_before").setValue(dataDay);

            }
        });


        /*
        day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nascondi.setVisibility(View.VISIBLE);
                attivo[0] =false;
                attiva_notifiche.setChecked(false);
                DBRef.child("Notifiche").child("Attivo").setValue("0");
                salva.setVisibility(View.GONE);
                disable.setVisibility(View.GONE);
            }
        });
        return root;
    }


}

package com.example.yourfood;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Integer.parseInt;


public class MainActivity extends AppCompatActivity {
    private NotificationManager mNotificationManager;
    private static final long ONE_MINUTE = 60000;
    private static final long ASAP = 2;


    final FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
    final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
    final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID);
    String strMNome = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        Button sendNotify = findViewById(R.id.notify);

        sendNotify.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                           //   addNotification();
                                          }
                                      }
        );





        final Object[] obj = new Object[1];


        FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();

        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
        final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID);
        //mAuth.removeAuthStateListener(mAuthListener);


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object obj = dataSnapshot.child("Prodotti").child("index").getValue();
                if (obj == null) {
                    DBRef.child("Prodotti").child("index").setValue(0);
                    String strMNome = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    String strMemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String strMnumero = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
                    DBRef.child("Nome").setValue(strMNome);
                    DBRef.child("Email").setValue(strMemail);
                    DBRef.child("Telefono").setValue(strMnumero);
                    DBRef.child("Notifiche").setValue(0);
                    DBRef.child("Notifiche").child("Orario").setValue("13:00");
                    DBRef.child("Notifiche").child("Attivo").setValue(0);
                    DBRef.child("Notifiche").child("Day_before").setValue("3");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        DBRef.addListenerForSingleValueEvent(eventListener);




        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
               // Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }*/
        // [END handle_data_extras]

        final Date ora_notify = new Date();


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


        Intent inteLCambioActiviy = new Intent(MainActivity.this, bottomNavigation.class);
        startActivity(inteLCambioActiviy);
        ActivityCompat.finishAffinity(MainActivity.this);
        finish();

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

                                    Toast.makeText(getApplicationContext(), nome, Toast.LENGTH_SHORT).show();
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

                                            Toast.makeText(getApplicationContext(), "" + diff, Toast.LENGTH_SHORT).show();

                                            if (diff < _day) {

                                                addNotification(lista_nome[y], scad);

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


        }

    }


    private void addNotification(String nome, String scadenza) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");
        Intent ii = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("Consumalo entro il "+ scadenza +" per evitare sprechi!");
        bigText.setBigContentTitle("Il prodotto "+ nome +" sta per scadere!");
        bigText.setSummaryText("in scadenza");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle("Il prodotto "+ nome +" sta per scadere!");
        mBuilder.setContentText("Consumalo entro il "+ scadenza +" per evitare sprechi!");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
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




}







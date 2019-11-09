package com.example.yourfood;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        final Object[] obj = new Object[1];


        FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();

        final String strMCodiceUID = FirebaseAuth.getInstance().getUid();
        final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID);
        //mAuth.removeAuthStateListener(mAuthListener);


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object obj = dataSnapshot.child("Prodotti").child("index").getValue();
                if(obj == null){
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
        };DBRef.addListenerForSingleValueEvent(eventListener);


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


        Intent inteLCambioActiviy = new Intent(MainActivity.this, bottomNavigation.class);
        startActivity(inteLCambioActiviy);
        ActivityCompat.finishAffinity(MainActivity.this);
        finish();


    }

}

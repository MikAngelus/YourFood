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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };DBRef.addListenerForSingleValueEvent(eventListener);


        Intent inteLCambioActiviy = new Intent(MainActivity.this, bottomNavigation.class);
        startActivity(inteLCambioActiviy);
        ActivityCompat.finishAffinity(MainActivity.this);
        finish();


    }

}

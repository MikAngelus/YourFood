package com.example.yourfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private static final String TAG = "HOME";
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        //REGISTRAZIONE MANUALE SALVATAGGIO DB

/*  final EditText email= (EditText) findViewById(R.id.editTextEmail) ;
        final EditText password= (EditText) findViewById(R.id.editTextPassword) ;
        Button register=(Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email_user = email.getText().toString();
                String pass_user = password.getText().toString();
                FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
                //String strMCodiceUID = FirebaseAuth.getInstance().getUid();
                DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti");
                /*DBRef.child("user").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                DBRef = dbFireBase.getReference("DB_Utenti/" + email_user);
                DBRef.child("Email").setValue(email_user);
                DBRef.child("Password").setValue(pass_user);



            }
        });
*/

        Button btn_google = (Button)findViewById(R.id.login_google);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null)
                {
                    Intent inteLCambioActiviy = new Intent(Login.this, MainActivity.class);
                    startActivity(inteLCambioActiviy);
                    ActivityCompat.finishAffinity(Login.this);
                    finish();
                }
            }

        };mAuth.addAuthStateListener(mAuthListener);

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(Login.this, "C'è stato qualche errore", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    protected void onStart(){
        super.onStart();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            mAuth.removeAuthStateListener(mAuthListener);
                            FirebaseDatabase dbFireBase = FirebaseDatabase.getInstance();
                            String strMCodiceUID = FirebaseAuth.getInstance().getUid();
                            final DatabaseReference DBRef = dbFireBase.getReference("DB_Utenti/" + strMCodiceUID);

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


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Autentificazione fallita.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


}


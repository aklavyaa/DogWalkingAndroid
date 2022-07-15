package com.example.dogwalkerandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private String TAG = Login.class.getSimpleName();
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    private FirebaseFirestore db;

    private Boolean isVisiblePass = false;

    ProgressDialog progressDialog;

    private void startProgressDialog(){
        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Wait a moment"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    private void stopProgressDialog(){
        progressDialog.cancel();
    }



    private void signIn(String email,String password){
        startProgressDialog();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        stopProgressDialog();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();




//                            Toast.makeText(Login.this, "LoginSuccessfull", Toast.LENGTH_SHORT).show();

                            switch(SharedPref.getString(SharedPref.USER_STATE,"")) {
                                case SharedPref.DOGOWNER:
//                                    signIn(email,password);
                                    db.collection("DogOwner").whereEqualTo("id",user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                 if (task.isSuccessful()){
                                                     for (QueryDocumentSnapshot document : task.getResult()) {
                                                         Log.d(TAG, document.getId() + " => " + document.getData());
                                                          if (((Boolean)document.getData().get("isEnable"))){
                                                              startActivity(new Intent(Login.this,OwnerDashboard.class));
                                                          }else {
                                                              Toast.makeText(Login.this, "You are revoked by admin", Toast.LENGTH_SHORT).show();
                                                          }
                                                     }

                                                 }
                                        }
                                    });

                                    break;
                                case SharedPref.DOGWALKER:

                                    db.collection("DogWalker").whereEqualTo("id",user.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()){
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                                    if (((Boolean)document.getData().get("isEnable"))){
                                                        startActivity(new Intent(Login.this,DashboardWalker.class));
                                                    }else {
                                                        Toast.makeText(Login.this, "You are revoked by admin", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                            }
                                        }
                                    });



//                                    startActivity(new Intent(Login.this,DashboardWalker.class));
                                    break;
                                default:
//                        setContentView(R.layout.default);
                                    break;
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure" );
                            Toast.makeText(Login.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private GoogleSignInClient mGoogleSignInClient;

    private void initialiseGoogleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);


                            switch(SharedPref.getString(SharedPref.USER_STATE,"")) {
                                case SharedPref.DOGOWNER:

                                    db.collection("DogOwner")
                                            .whereEqualTo("id", user.getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        if (task.getResult().isEmpty()){

                                                            Map<String, Object> docData = new HashMap<>();
                                                            docData.put("id", user.getUid());
                                                            docData.put("user_address", "");
                                                            docData.put("user_age", "");
                                                            docData.put("user_description", "");
                                                            docData.put("user_email", user.getEmail());
                                                            docData.put("user_id", user.getUid());
                                                            docData.put("user_image", "");
                                                            docData.put("user_name", user.getDisplayName());
                                                            docData.put("user_password", "");
                                                            docData.put("isEnable",true);
                                                            docData.put("isReserved",false);

                                                            db.collection("DogOwner").add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    startActivity(new Intent(Login.this,OwnerDashboard.class));
                                                                    Toast.makeText(Login.this, "Succes", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(Login.this, "Failure", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });



                                                        }else {

                                                            Toast.makeText(Login.this, "Already has the data", Toast.LENGTH_SHORT).show();
                                                        }
//                                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                                }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });




//                                    startActivity(new Intent(Login.this,SignUpOwner.class));
                                    break;
                                case SharedPref.DOGWALKER:

                                    db.collection("DogWalker")
                                            .whereEqualTo("id", user.getUid())
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {

                                                        if (task.getResult().isEmpty()){

                                                            Map<String, Object> docData = new HashMap<>();
//                                                            docData.put("id", user.getUid());
//                                                            docData.put("user_address", "");
//                                                            docData.put("user_age", "");
//                                                            docData.put("user_description", "");
//                                                            docData.put("user_email", user.getEmail());
//                                                            docData.put("user_id", user.getUid());
//                                                            docData.put("user_image", "");
//                                                            docData.put("user_name", user.getDisplayName());
//                                                            docData.put("user_password", "");


                                                            docData.put("id", user.getUid());
                                                            docData.put("user_address", "");
                                                            docData.put("user_age", "");
                                                            docData.put("user_description", "");
                                                            docData.put("user_email", user.getEmail());
                                                            docData.put("user_id", user.getUid());
                                                            docData.put("user_image", "");
                                                            docData.put("user_name", user.getDisplayName());
                                                            docData.put("user_password", "");
                                                            docData.put("experience", "");
                                                            docData.put("timingTo", "");
                                                            docData.put("timingFrom", "");
                                                            docData.put("date", "");
                                                            docData.put("isEnable",true);
                                                            docData.put("isReserved",false);


                                                            db.collection("DogOwner").add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    startActivity(new Intent(Login.this,OwnerDashboard.class));
                                                                    Toast.makeText(Login.this, "Succes", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(Login.this, "Failure", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });



                                                        }else {

                                                            Toast.makeText(Login.this, "Already has the data", Toast.LENGTH_SHORT).show();
                                                        }
//                                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                                }
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });


//                                    startActivity(new Intent(Login.this,SignupWalker.class));
                                    break;
                                default:
//                        setContentView(R.layout.default);
                                    break;
                            }




                            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



//        UserRecord userRecord = FirebaseAuth.getInstance().


        initialiseGoogleSignIn();
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        ((ImageView)findViewById(R.id.eye)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVisiblePass){
                    ((ImageView)findViewById(R.id.eye)).setImageResource(R.drawable.ic_visibility_24);
                    ((EditText)findViewById(R.id.password)).setTransformationMethod(null);
                }else {
                    ((ImageView)findViewById(R.id.eye)).setImageResource(R.drawable.ic_visibility_off_24);
                    ((EditText)findViewById(R.id.password)).setTransformationMethod(new PasswordTransformationMethod());
                }
                isVisiblePass = !isVisiblePass;
            }
        });



        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = ((EditText)findViewById(R.id.name)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(Login.this, "Please fill out the details", Toast.LENGTH_SHORT).show();

                } else {

                    switch(SharedPref.getString(SharedPref.USER_STATE,"")) {
                        case SharedPref.DOGOWNER:
                            signIn(email,password);
                            break;
                        case SharedPref.DOGWALKER:
                            signIn(email,password);
//                            startActivity(new Intent(Login.this,DashboardWalker.class));
                            break;
                        default:
                            startActivity(new Intent(Login.this,AdminDashboard.class));
//                        setContentView(R.layout.default);
                            break;
                    }


                }


            }
        });

        findViewById(R.id.createaccount_sentence).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(SharedPref.getString(SharedPref.USER_STATE,"")) {
                    case SharedPref.DOGOWNER:
                        startActivity(new Intent(Login.this,SignUpOwner.class));
                        break;
                    case SharedPref.DOGWALKER:
                        startActivity(new Intent(Login.this,SignupWalker.class));
                        break;
                    default:
//                        setContentView(R.layout.default);
                        break;
                }
            }
        });


        findViewById(R.id.loginwithgoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }
}
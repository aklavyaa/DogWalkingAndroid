package com.example.dogwalkerandroid;

import static com.example.dogwalkerandroid.utils.Utility.PASSWORD_PATTERN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpOwner extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String TAG = SignUpOwner.class.getSimpleName();
    private FirebaseFirestore db;
    private Boolean isVisiblePass = false;
    private Boolean isVisibleRePass = false;
    StorageReference storageRef;
    String imageUri = "";
    ProgressDialog progressDialog;

    private void startProgressDialog(){
        progressDialog = new ProgressDialog(SignUpOwner.this);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle("Wait a moment"); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.show(); // Display Progress Dialog
        progressDialog.setCancelable(false);
    }

    private void stopProgressDialog(){
        progressDialog.cancel();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_owner);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        ((RelativeLayout)findViewById(R.id.profile_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(SignUpOwner.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        (findViewById(R.id.proceed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

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



        ((ImageView)findViewById(R.id.eye_repassword)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVisibleRePass){
                    ((ImageView)findViewById(R.id.eye_repassword)).setImageResource(R.drawable.ic_visibility_24);
                    ((EditText)findViewById(R.id.repassword)).setTransformationMethod(null);
                }else {
                    ((ImageView)findViewById(R.id.eye_repassword)).setImageResource(R.drawable.ic_visibility_off_24);
                    ((EditText)findViewById(R.id.repassword)).setTransformationMethod(new PasswordTransformationMethod());
                }
                isVisibleRePass = !isVisibleRePass;
            }
        });


    }

    private void registerUser(){
        String name = ((EditText)findViewById(R.id.name)).getText().toString();
        String email = ((EditText)findViewById(R.id.email)).getText().toString();
        String age = ((EditText)findViewById(R.id.age)).getText().toString();
        String address = ((EditText)findViewById(R.id.address)).getText().toString();
        String description = ((EditText)findViewById(R.id.description)).getText().toString();
        String password = ((EditText)findViewById(R.id.password)).getText().toString();
        String repassword = ((EditText)findViewById(R.id.repassword)).getText().toString();

        if(name.isEmpty() || email.isEmpty() || age.isEmpty() ||
                address.isEmpty() || description.isEmpty() || password.isEmpty()
                || repassword.isEmpty()){
            Toast.makeText(SignUpOwner.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(repassword)){
            Toast.makeText(SignUpOwner.this, "Please enter the same password!", Toast.LENGTH_SHORT).show();
        }
        else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            Toast.makeText(SignUpOwner.this, "Password is too weak!! Please enter atleast 8 alphanumeric character.", Toast.LENGTH_SHORT).show();
        }
        else {

            startProgressDialog();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.e(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.e(TAG, "onComplete: "+user.getEmail());
                                String uid = user.getUid();

                                Map<String, Object> docData = new HashMap<>();
                                docData.put("id", uid);
                                docData.put("user_address", address);
                                docData.put("user_age", age);
                                docData.put("user_description", description);
                                docData.put("user_email", email);
                                docData.put("user_id", uid);
                                docData.put("user_image", imageUri);
                                docData.put("user_name", name);
                                docData.put("user_password", password);
                                docData.put("isEnable",true);
                                docData.put("isReserved",false);

                                db.collection("DogOwner").add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        stopProgressDialog();
                                        startActivity(new Intent(SignUpOwner.this,OwnerDashboard.class));
                                        Toast.makeText(SignUpOwner.this, "Succes", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        stopProgressDialog();
                                        Toast.makeText(SignUpOwner.this, "Failure", Toast.LENGTH_SHORT).show();
                                    }
                                });



                            } else {
                                stopProgressDialog();
                                // If sign in fails, display a message to the user.
                                Log.e(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpOwner.this, Objects.requireNonNull(task.getException().getMessage()).toString(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_mm_dd_HH_mm_ss", Locale.CANADA);
            Date now = new Date();
            String filename = sdf.format(now);
            storageRef = FirebaseStorage.getInstance().getReference("images/"+filename);
            storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SignUpOwner.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUri = uri.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpOwner.this, "Unable to get downloadable url", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpOwner.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            });

            ((CircleImageView)findViewById(R.id.profile_image)).setImageURI(uri);
//            profile_image
            // Use Uri object instead of File to avoid storage permissions
//                    imgProfile.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
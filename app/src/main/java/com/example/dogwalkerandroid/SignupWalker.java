package com.example.dogwalkerandroid;

import static com.example.dogwalkerandroid.utils.Utility.PASSWORD_PATTERN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupWalker extends AppCompatActivity {

    private String TAG = SignupWalker.class.getSimpleName();
    private Boolean isVisiblePass = false;
    private Boolean isVisibleRePass = false;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    StorageReference storageRef;
    String imageUri = "";
    ProgressDialog progressDialog;


    private void startProgressDialog(){
        progressDialog = new ProgressDialog(SignupWalker.this);
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
                    Toast.makeText(SignupWalker.this, "Successfully uploaded", Toast.LENGTH_SHORT).show();
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUri = uri.toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignupWalker.this, "Unable to get downloadable url", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignupWalker.this, "Failure", Toast.LENGTH_SHORT).show();
                }
            });

            ((CircleImageView)findViewById(R.id.profile_image)).setImageURI(uri);
//            profile_image
//            Use Uri object instead of File to avoid storage permissions
//            imgProfile.setImageURI(fileUri)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_dogwalker);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ((RelativeLayout)findViewById(R.id.profile_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(SignupWalker.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
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



        ((TextView)findViewById(R.id.timingfrom)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(SignupWalker.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((TextView)findViewById(R.id.timingfrom)).setText(hourOfDay+":"+minute);
                    }
                },hour,minute,false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });



        ((TextView)findViewById(R.id.timingto)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(SignupWalker.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((TextView)findViewById(R.id.timingto)).setText(hourOfDay+":"+minute);
                    }
                },hour,minute,false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });




        ((RadioGroup)findViewById(R.id.availability_grp)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton)group.findViewById(checkedId);
                Log.e(TAG, "onCheckedChanged: "+radioButton.getText().toString());
                if (radioButton.getText().toString().equalsIgnoreCase("daily")){
                    ((LinearLayout)findViewById(R.id.dateview)).setVisibility(View.GONE);
                    ((TextView)findViewById(R.id.from)).setText("Date");
                }
                else {
                    ((LinearLayout)findViewById(R.id.dateview)).setVisibility(View.VISIBLE);
                }
            }
        });


//        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(Pair.create(MaterialDatePicker.thisMonthInUtcMilliseconds(),MaterialDatePicker.todayInUtcMilliseconds())).build();

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
//        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.now();

        CalendarConstraints.Builder constraintsBuilder =
                new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now());
        builder.setCalendarConstraints(constraintsBuilder.build());


        MaterialDatePicker materialDatePicker = builder.build();

//        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setCalendarConstraints()

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                ((TextView)findViewById(R.id.from)).setText(materialDatePicker.getHeaderText());
            }
        });


        ((LinearLayout)findViewById(R.id.dateview)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"TagPicker");

            }
        });

        ((Button)findViewById(R.id.proceed)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.name)).getText().toString();
                String email = ((EditText)findViewById(R.id.email)).getText().toString();
                String experience = ((EditText)findViewById(R.id.experience)).getText().toString();
                String age = ((EditText)findViewById(R.id.rate)).getText().toString();
                String address = ((EditText)findViewById(R.id.address)).getText().toString();
                String description = ((EditText)findViewById(R.id.description)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();
                String repassword = ((EditText)findViewById(R.id.repassword)).getText().toString();
                String timingFrom = ((TextView)findViewById(R.id.timingfrom)).getText().toString();
                String timingTo = ((TextView)findViewById(R.id.timingto)).getText().toString();
                String date = ((TextView)findViewById(R.id.from)).getText().toString();
                if(name.isEmpty() || email.isEmpty() || age.isEmpty() ||
                        address.isEmpty() || description.isEmpty() || password.isEmpty()
                        || repassword.isEmpty() || experience.isEmpty() ||
                        timingFrom.equalsIgnoreCase("Time From") ||
                        timingTo.equalsIgnoreCase("Time To")
                        ){
                    Toast.makeText(SignupWalker.this, "Please fill all the details", Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals(repassword)){
                    Toast.makeText(SignupWalker.this, "Please enter the same password!", Toast.LENGTH_SHORT).show();
                }
                else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                    Toast.makeText(SignupWalker.this, "Password is too weak!! Please enter atleast 8 alphanumeric character.", Toast.LENGTH_SHORT).show();
                } else {
                    startProgressDialog();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupWalker.this, new OnCompleteListener<AuthResult>() {
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
                                        docData.put("experience", experience);
                                        docData.put("timingTo", timingTo);
                                        docData.put("timingFrom", timingFrom);
                                        docData.put("date", date);
                                        docData.put("isEnable",true);
                                        docData.put("isReserved",false);
                                        docData.put("lat",45);
                                        docData.put("lng",55);
                                        docData.put("rating",5);
                                        docData.put("user_hourly_rate",19);

//                                        ,,,




                                        db.collection("DogWalker").add(docData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                stopProgressDialog();
                                                startActivity(new Intent(SignupWalker.this,DashboardWalker.class));
                                                Toast.makeText(SignupWalker.this, "Succes", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                stopProgressDialog();
                                                Toast.makeText(SignupWalker.this, "Failure", Toast.LENGTH_SHORT).show();
                                            }
                                        });



                                    } else {
                                        stopProgressDialog();

                                        // If sign in fails, display a message to the user.
                                        Log.e(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignupWalker.this, Objects.requireNonNull(task.getException().getMessage()).toString(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
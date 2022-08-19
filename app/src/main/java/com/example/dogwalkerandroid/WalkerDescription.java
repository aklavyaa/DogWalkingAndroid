package com.example.dogwalkerandroid;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dogwalkerandroid.utils.DashboardDogOwnerModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WalkerDescription extends AppCompatActivity {

    DashboardDogOwnerModel obj;
    private int hrsQty = 1;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_description);
        db = FirebaseFirestore.getInstance();

        obj = (DashboardDogOwnerModel)getIntent().getParcelableExtra("pass_obj");

        Picasso.get().load(obj.getImage()).into((ImageView)findViewById(R.id.image));

        ((Button)findViewById(R.id.proceed)).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                /**
                 * order_date
                 * order_id
                 * order_time
                 * status
                 * total_cost
                 * total_time
                 * user_id
                 * walker_id
                 */
 ((TextView)findViewById(R.id.name)).setText(obj.getName());
        ((TextView)findViewById(R.id.price)).setText("$"+obj.getPrice()+"/hr");
        ((TextView)findViewById(R.id.rating)).setText(obj.getRating());
        ((TextView)findViewById(R.id.availability)).setText(obj.getAvailability());
        ((TextView)findViewById(R.id.description)).setText(obj.getDescription());
        ((TextView)findViewById(R.id.total_price)).setText("$"+obj.getPrice());

        ((LinearLayout)findViewById(R.id.date_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
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
                ((TextView)findViewById(R.id.select_date)).setText(materialDatePicker.getHeaderText());
            }
        });


        ((LinearLayout)findViewById(R.id.date_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(),"TagPicker");

            }
        });


        ((LinearLayout)findViewById(R.id.time_view)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker = new TimePickerDialog(WalkerDescription.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        ((TextView)findViewById(R.id.select_time)).setText(hourOfDay+":"+minute);
                    }
                },hour,minute,false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });



                DocumentReference ref = db.collection("DogWalker").document(obj.getId()).collection("OwnerRequest").document();

                String orderid = ref.getId();


                Date date = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
                String strDate  = dateFormat.format(date);

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
                String strTime = timeFormat.format(date);


                Map<String, Object> docData = new HashMap<>();
                docData.put("walker_id", obj.getId());
                docData.put("user_id",SharedPref.getString("user_id","") );
                docData.put("total_time", String.valueOf(hrsQty));
                docData.put("total_cost", (Double.valueOf(obj.getPrice())*hrsQty));
                docData.put("order_time", strTime);
                docData.put("order_id", orderid);
                docData.put("order_date", strDate);
                docData.put("date", ((TextView)findViewById(R.id.select_date)).getText().toString());
                docData.put("time", ((TextView)findViewById(R.id.select_time)).getText().toString());




                db.collection("DogWalker").document(obj.getId()).collection("OwnerRequest").document(ref.getId()).set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(WalkerDescription.this, "Added successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(WalkerDescription.this,RequestSent.class));
                    }
                });








            }
        });





        ((TextView)findViewById(R.id.positive)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hrsQty++;
                ((TextView)findViewById(R.id.no_hrs)).setText(String.valueOf(hrsQty)+"hrs");

                Double totalValue = Double.valueOf(hrsQty)*Double.valueOf(obj.getPrice());
                ((TextView)findViewById(R.id.total_price)).setText("$"+totalValue.toString());


            }
        });
        ((TextView)findViewById(R.id.negative)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hrsQty == 1){
                    return;
                }
                hrsQty--;
                ((TextView)findViewById(R.id.no_hrs)).setText(String.valueOf(hrsQty)+"hrs");
                Double totalValue = Double.valueOf(hrsQty)*Double.valueOf(obj.getPrice());
                ((TextView)findViewById(R.id.total_price)).setText("$"+totalValue.toString());
            }
        });




       









    }
}

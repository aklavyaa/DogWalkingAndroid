package com.example.dogwalkerandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dogwalkerandroid.utils.Sender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaymentScreen extends AppCompatActivity {

    private String TAG  = PaymentScreen.class.getSimpleName();
    private  String secret_key = "sk_test_51LPKR3C9lPGkPn5gMScYtcFI31TgzhKPkbo3JvePUiR2weghDquADEIWZqUODYtIrpo6uLzlbDl9iVmiUB4uzNEu00PgPnkGMQ";
    private String publish_key = "pk_test_51LPKR3C9lPGkPn5gCT0A0W1Px9c0OpB1ltcIe2RsR1qkqOUmRdJKHDXndfOFK9SRZcsPTmxGQFZushEXf94ehLL900S4rTdRWc";
    private PaymentSheet paymentSheet;
    private String customerId;
    private String empericalKey;
    private String clientSecretKey;
    private String price;
    private String user_name;
    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_screen);
        db = FirebaseFirestore.getInstance();

       price =  getIntent().getStringExtra("price");
       user_name =  getIntent().getStringExtra("username");

       String[] priceArr = price.split("[.]");
       price = priceArr[0];
        Log.e(TAG, "price: "+price );
        PaymentConfiguration.init(this,publish_key);
        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {
            if (paymentSheetResult instanceof PaymentSheetResult.Completed){
                Toast.makeText(this, "PaymentSuccesful", Toast.LENGTH_SHORT).show();

                db.collection("DogWalker").whereEqualTo("user_name",user_name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                        String email =    task.getResult().getDocuments().get(0).getData().get("user_email").toString();

                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("message/rfc822");
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Confirmation");
                            i.putExtra(Intent.EXTRA_TEXT   , "Hi you have been confiremed as a Dog walker please visit us at prescribe place");
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                                Toast.makeText(PaymentScreen.this, "Thank you for Payment", Toast.LENGTH_SHORT).show();
                                finishAffinity();
                                startActivity(new Intent(PaymentScreen.this,OwnerDashboard.class));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(PaymentScreen.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });



//                new Thread(){
//                    public void run(){
//                        try {
//                            Sender sender = new Sender("reply.yuvraj@gmail.com", "Yuva@123");
//                            sender.sendMail("Have You received mail",
//                                    "Please check this email for demo",
//                                    "reply.yuvraj@gmail.com",
//                                    "yuvraj.dev19@gmail.com");
//                        } catch (Exception e) {
//                            Log.e("SendMail", e.getMessage(), e);
//                        }
//                    }
//                }.start();


            }

        });







        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    customerId=object.getString("id");
                    getEmpericalKey(customerId);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Bearer "+secret_key);

                return header;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentScreen.this);
        requestQueue.add(stringRequest);


    }

    private void getEmpericalKey(String customerId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    empericalKey=object.getString("id");
                    ClientSecretKey(customerId,empericalKey);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Bearer "+secret_key);
                header.put("Stripe-Version","2020-08-27");

                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("customer",customerId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentScreen.this);
        requestQueue.add(stringRequest);
    }

    private void ClientSecretKey(String customerId,String empericalKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    clientSecretKey=object.getString("client_secret");
                    paymentFlow();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error );
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> header = new HashMap<>();
                header.put("Authorization","Bearer "+secret_key);

                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("customer",customerId);
                params.put("amount",price+"00");
                params.put("currency","usd");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PaymentScreen.this);
        requestQueue.add(stringRequest);
    }
    private void paymentFlow(){
        paymentSheet.presentWithPaymentIntent(clientSecretKey,new PaymentSheet.Configuration("DogWalkerApp",new PaymentSheet.CustomerConfiguration(customerId,empericalKey)));
    }
}
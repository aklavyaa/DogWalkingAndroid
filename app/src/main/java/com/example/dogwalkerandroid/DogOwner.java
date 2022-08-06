package com.example.dogwalkerandroid;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dogwalkerandroid.utils.DogOwnerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DogOwner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DogOwner extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private String TAG = DogOwner.class.getSimpleName();

    FirebaseFirestore db;
    RecyclerView recyclerView;
    AdminDogOwnerAdap adap;

    private ArrayList<DogOwnerModel> ownerList = new ArrayList<>();

    public DogOwner() {
        // Required empty public constructor
    }


    public static DogOwner newInstance() {
        DogOwner fragment = new DogOwner();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dog_owner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        adap = new AdminDogOwnerAdap(getActivity(),ownerList);
         recyclerView = view.findViewById(R.id.recyler_dogowner);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adap);


        db.collection("DogOwner").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData().get);

                        String id = "POIUY";

                        if (document.getData().get("id")!=null){
                            id = document.getData().get("id").toString();
                        }

                        String username = document.getData().get("user_name").toString();
                        String userage = document.getData().get("user_age").toString();
                        String userAddress = document.getData().get("user_address").toString();
                        String userImage = document.getData().get("user_image").toString();
                        Boolean isEnable = (Boolean) document.getData().get("isEnable");

                        ownerList.add(new DogOwnerModel(id,username,userage,userAddress,userImage,isEnable));
                        adap.notifyDataSetChanged();




                    }
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
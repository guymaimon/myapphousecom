package com.example.myapphousecom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapphousecom.Adapters.MyRecycleViewAdapter;
import com.example.myapphousecom.Handlers.Payment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class Tenant_Payments extends AppCompatActivity {
    private static final String TAG = "payment_by_tenant";
    public static ArrayList<Payment> countryList;
    private MyRecycleViewAdapter myAdapter;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private Intent intent;
    String id, apartmentNumber, buildingNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant__payments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.MyRecycleView);
        intent = getIntent();
        countryList = new ArrayList<>();
        id = intent.getStringExtra("user");
        apartmentNumber =  intent.getStringExtra("apartmentNumber");
        buildingNumber =  intent.getStringExtra("buildingNumber");


        db = FirebaseFirestore.getInstance();
        db.collection("users").document(apartmentNumber)
                .collection("payments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    countryList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Timestamp t = (Timestamp) document.get("date");
                        Log.d(TAG, document.getId() + " => " + document.get("sum") + " " + t.toDate().toString());
                        Payment p = new Payment(t, document.get("sum").toString());
                        p.setApartmentNumber(apartmentNumber);
                        p.setBuildingNumber(buildingNumber);
                        countryList.add(p);
                    }
                    recyclerView.setAdapter(myAdapter);
                }
            }
        });
        myAdapter = new MyRecycleViewAdapter(countryList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
    }


}
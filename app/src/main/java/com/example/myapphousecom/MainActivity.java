package com.example.myapphousecom;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapphousecom.Adapters.Tenant;
import com.example.myapphousecom.fragments.add_payment;
import com.example.myapphousecom.fragments.building_payments;
import com.example.myapphousecom.fragments.building_summary;
import com.example.myapphousecom.fragments.payment_by_tenant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        payment_by_tenant.OnPaymentByTenantFragmentInteractionListener, building_payments.OnBPFragmentInteractionListener, building_summary.OnBuildingFragmentInteractionListener, add_payment.OnAddFragmentInteractionListener {
    private static final String TAG = "MainActivity";

    Intent intent;
    String user, apartmentNumber, buildingNumber;
    private Toolbar toolbar;
    private boolean isCommittee;
    private FirebaseFirestore db;
    Bundle bundle = new Bundle();
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final List<Tenant> tenantsList = new ArrayList<>();
        tenantsList.add(new Tenant("SELECT TENANT", "0", "0"));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        db = FirebaseFirestore.getInstance();
        intent = getIntent();
        user = intent.getStringExtra("user");
        apartmentNumber = intent.getStringExtra("apartmentNumber");
        buildingNumber = intent.getStringExtra("buildingNumber");
        isCommittee = intent.getStringExtra("role").equals("Committee");
        bundle.putString("buildingNumber", buildingNumber);
        bundle.putString("apartmentNumber", apartmentNumber);
        Log.d(TAG, isCommittee + " is Committee");
        final ArrayAdapter<Tenant> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, tenantsList);

// Create an ArrayAdapter using the string array and a default spinner layout

//        get all payment by user
        db.collection("users").whereEqualTo("buildingNumber", "22").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //                                Log.d(TAG, document.getId() + " => " + document.get("sum") + " ");
                                tenantsList.add(new Tenant((String) document.get("fullName"), (String) document.getId(), (String) document.get("apartmentNumber")));
                            }

                        }
                    }
                });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add_payment add_payment = new add_payment();
                add_payment.setArguments(bundle);
                final FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();

                // put the fragment in place
                transaction.replace(R.id.fragment_container, add_payment);

                // this is the part that will cause a fragment to be added to backstack,
                // this way we can return to it at any time using this tag
                transaction.addToBackStack(MainActivity.class.getName());

                transaction.commit();

            }
        });

        try {
           Log.d(TAG, user);
//            get all payment by user
//            db.collection("users").document(user)
//                    .collection("payments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            Timestamp t = (Timestamp) document.get("date");
//                            Log.d(TAG, document.getId() + " => " + document.get("sum") + " " + t.toDate().toString());
//                            Payment p = new Payment(t, document.get("sum").toString());
//                            countryList.add(p);
//                        }
//                        recyclerView.setAdapter(myAdapter);
//                    }
//                }
//            });


//
//            db.collection("payments").whereEqualTo("idTenants", user)
//            .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    Timestamp t = (Timestamp) document.get("paymentDate");
//                                    Log.d(TAG, document.getId() + " => " + document.get("paymentSum")+" "+ t.toDate().toString());
//                                    Payment p = new Payment(t, document.get("paymentSum").toString());
//                                    countryList.add(p);
//                                }
//                                recyclerView.setAdapter(myAdapter);
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // menu


        switch (menuItem.getItemId()) {
            case R.id.nav_by_tenant:
                payment_by_tenant payment_by_tenant = new payment_by_tenant();
                payment_by_tenant.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, payment_by_tenant).commit();
                break;
            case R.id.nav_by_building:
                building_payments building_payments = new building_payments();
                building_payments.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, building_payments).commit();
                break;
            case R.id.nav_building_sum:
                building_summary building_summary = new building_summary();
                building_summary.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, building_summary).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBPFragmentInteraction(Uri uri) {

    }


}

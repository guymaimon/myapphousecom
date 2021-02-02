package com.example.myapphousecom.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapphousecom.Adapters.MyRecycleViewAdapter;
import com.example.myapphousecom.Adapters.Tenant;
import com.example.myapphousecom.Handlers.Payment;
import com.example.myapphousecom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link payment_by_tenant.OnPaymentByTenantFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link payment_by_tenant#newInstance} factory method to
 * create an instance of this fragment.
 */
public class payment_by_tenant extends Fragment {

    private static final String ARG_PARAM1 = "apartmentNumber";
    private static final String ARG_PARAM2 = "buildingNumber";
    private static final String TAG = "payment_by_tenant";
    public static ArrayList<Payment> countryList;
    private MyRecycleViewAdapter myAdapter;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    // TODO: Rename and change types of parameters
    private String mParam1,apartmentNumber;
    private String mParam2, buildingNumber;
    private View curView;
    private FirebaseFirestore db;
    private OnPaymentByTenantFragmentInteractionListener mListener;

    public payment_by_tenant() {
        // Required empty public constructor
    }

    public static payment_by_tenant newInstance(String param1, String param2) {
        payment_by_tenant fragment = new payment_by_tenant();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Create the view for this fragment, using the arguments given to it.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        curView = inflater.inflate(R.layout.fragment_payment_by_tenant, container, false);
        // Initializing a String Array

        final List<Tenant> tenantsList = new ArrayList<>();
        tenantsList.add(new Tenant("SELECT TENANT","0","0"));
        recyclerView = curView.findViewById(R.id.MyRecycleView);
        db = FirebaseFirestore.getInstance();

        countryList = new ArrayList<>();
        Spinner spinner = curView.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout

//        get all payment by user
        db.collection("users").whereEqualTo("buildingNumber", buildingNumber).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.get("sum") + " ");
                                tenantsList.add(new Tenant((String) document.get("fullName") ,(String)document.getId(), (String) document.get("apartmentNumber")));
                            }


                        }
                    }
                });
        ArrayAdapter<Tenant> adapter = new ArrayAdapter<>(curView.getContext(),R.layout.spinner_item,tenantsList);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Tenant t = (Tenant) parent.getSelectedItem();
                Log.d(TAG,t.getFullName()+" "+t.getId());
                getPayments(t.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        myAdapter = new MyRecycleViewAdapter(countryList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return curView;
    }

    private void getPayments(String apartmentNumber) {

            db.collection("users").document(apartmentNumber)
                    .collection("payments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        countryList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Timestamp t = (Timestamp) document.get("date");
                            Payment p = new Payment(t, document.get("sum").toString());
                            countryList.add(p);
                        }
                        recyclerView.setAdapter(myAdapter);
                    }
                }
            });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        if (getArguments() != null) {
            apartmentNumber = getArguments().getString(ARG_PARAM1);
            buildingNumber = getArguments().getString(ARG_PARAM2);
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnPaymentByTenantFragmentInteractionListener) {
            mListener = (OnPaymentByTenantFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnPaymentByTenantFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

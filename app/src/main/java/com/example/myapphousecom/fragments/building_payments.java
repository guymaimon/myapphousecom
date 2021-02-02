package com.example.myapphousecom.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapphousecom.R;
import com.example.myapphousecom.Adapters.MyRecycleViewAdapter;
import com.example.myapphousecom.Handlers.Payment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link building_payments.OnBPFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link building_payments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class building_payments extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "apartmentNumber";
    private static final String ARG_PARAM2 = "buildingNumber";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyRecycleViewAdapter myAdapter;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    public static ArrayList<Payment> countryList;
    private Intent intent;
    String apartmentNumber, buildingNumber;

    private View curView;
    private FirebaseFirestore db;

    private static final String TAG = "building_payments";
    private OnBPFragmentInteractionListener mListener;


    public building_payments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment building_payments.
     */
    // TODO: Rename and change types and number of parameters
    public static building_payments newInstance(String param1, String param2) {
        building_payments fragment = new building_payments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apartmentNumber = getArguments().getString(ARG_PARAM1);
            buildingNumber = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        curView = inflater.inflate(R.layout.fragment_building_payments, container, false);
        recyclerView = curView.findViewById(R.id.MyRecycleView);
        db = FirebaseFirestore.getInstance();

        countryList = new ArrayList<>();
//        Payment p = new Payment(Timestamp.now(), "tatatatatta");
//        countryList.add(p);
        // get all payments by apartmentNumber And buildingNumber .whereEqualTo("apartmentNumber",apartmentNumber )
        Log.d(TAG, buildingNumber);
        db.collection("users")
                .whereEqualTo("buildingNumber",buildingNumber ).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document1 : task.getResult()) {

                        document1.getReference().collection("payments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                if (task1.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task1.getResult()) {

                                        Timestamp t = (Timestamp) document.get("date");
//                                          Log.d(TAG, document.getId() + " => " + document.get("paymentSum")+" "+ t.toDate().toString());
                                        Payment p = new Payment(t, document.get("sum").toString());
                                        p.setApartmentNumber((String) document1.get("apartmentNumber"));
                                        p.setBuildingNumber((String) document1.get("buildingNumber"));
                                        Log.d(TAG, (String) document1.get("apartmentNumber"));
                                        Log.d(TAG, (String) document1.get("buildingNumber"));
                                        countryList.add(p);
                                    }
                                    recyclerView.setAdapter(myAdapter);
                                }
                            }
                        });

                    }
                    recyclerView.setAdapter(myAdapter);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: " + e.getMessage());
            }
        });
        myAdapter = new MyRecycleViewAdapter(countryList, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return curView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onBPFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBPFragmentInteractionListener) {
            mListener = (OnBPFragmentInteractionListener) context;
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
    public interface OnBPFragmentInteractionListener {
        // TODO: Update argument type and name
        void onBPFragmentInteraction(Uri uri);
    }
}

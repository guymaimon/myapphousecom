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

import com.example.myapphousecom.Adapters.MyRecycleViewAdapter;
import com.example.myapphousecom.Handlers.Payment;
import com.example.myapphousecom.R;
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
 * {@link building_summary.OnBuildingFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link building_summary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class building_summary extends Fragment {

    private static final String ARG_PARAM1 = "apartmentNumber";
    private static final String ARG_PARAM2 = "buildingNumber";
    private static final String TAG = "building_summary";
    public static ArrayList<Payment> countryList;
    String apartmentNumber, buildingNumber;
    private MyRecycleViewAdapter myAdapter;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private View curView;
    private FirebaseFirestore db;
    private Intent intent;
    private String mParam1;
    private String mParam2;

    private OnBuildingFragmentInteractionListener mListener;

    public building_summary() {
        // Required empty public constructor
    }

    public static building_summary newInstance(String param1, String param2) {
        building_summary fragment = new building_summary();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        curView = inflater.inflate(R.layout.fragment_building_summary, container, false);
        recyclerView = curView.findViewById(R.id.MyRecycleView);
        db = FirebaseFirestore.getInstance();
//        apartmentNumber = intent.getStringExtra("apartmentNumber");
//        buildingNumber = intent.getStringExtra("buildingNumber");
        countryList = new ArrayList<>();
        Log.d(TAG, buildingNumber);
        // get all payments by apartmentNumber And buildingNumber  .whereEqualTo("apartmentNumber", apartmentNumber)
        db.collection("users")
                .whereEqualTo("buildingNumber", buildingNumber).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (final QueryDocumentSnapshot document1 : task.getResult()) {

                        final Payment p = new Payment(null,"");
                        document1.getReference().collection("payments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task1) {
                                if (task1.isSuccessful()) {
                                    double sum =0;
                                    for (QueryDocumentSnapshot document : task1.getResult()) {

                                        Timestamp t = (Timestamp) document.get("date");
//                                          Log.d(TAG, document.getId() + " => " + document.get("paymentSum")+" "+ t.toDate().toString());
                                        sum += Double.parseDouble( document.get("sum").toString());


//                                        p.setApartmentNumber((String) document1.get("apartmentNumber"));
//                                        p.setBuildingNumber((String) document1.get("buildingNumber"));


                                    }

                                    p.setSum(sum+"");
                                    p.setApartmentNumber((String) document1.get("apartmentNumber"));
                                    p.setBuildingNumber((String) document1.get("buildingNumber"));
                                    Log.d(TAG+" getSum",p.getSum());
                                    countryList.add(p);

                                }
                                recyclerView.setAdapter(myAdapter);
                            }
                        });





                    }

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
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBuildingFragmentInteractionListener) {
            mListener = (OnBuildingFragmentInteractionListener) context;
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
    public interface OnBuildingFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

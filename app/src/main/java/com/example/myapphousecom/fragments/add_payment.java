package com.example.myapphousecom.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapphousecom.Adapters.MyRecycleViewAdapter;
import com.example.myapphousecom.Adapters.Tenant;
import com.example.myapphousecom.Handlers.Payment;
import com.example.myapphousecom.Handlers.tenant_payment;
import com.example.myapphousecom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link add_payment.OnAddFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link add_payment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_payment extends Fragment {
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
    private Button addPayment;
    private CalendarView calendarView;
    private EditText sum_text;
    private Date date;
    private OnAddFragmentInteractionListener mListener;

    public add_payment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            apartmentNumber = getArguments().getString(ARG_PARAM1);
            buildingNumber = getArguments().getString(ARG_PARAM2);
        }
    }
    public static add_payment newInstance(String param1, String param2) {
        add_payment fragment = new add_payment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        curView = inflater.inflate(R.layout.fragment_add_payment, container, false);
        final SimpleDateFormat sdf =  new SimpleDateFormat("dd/MM/yyyy");

        sum_text = curView.findViewById(R.id.payment_sum_edit);
        addPayment = curView.findViewById(R.id.add_payment_btn);
        calendarView = curView.findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                int d = dayOfMonth;
                try {
                    date = sdf.parse(dayOfMonth+"/"+month+"/"+year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });


        final List<Tenant> tenantsList = new ArrayList<>();
        tenantsList.add(new Tenant("SELECT TENANT","0","0"));
        recyclerView = curView.findViewById(R.id.MyRecycleView);
        db = FirebaseFirestore.getInstance();

        countryList = new ArrayList<>();
        final Spinner spinner = curView.findViewById(R.id.spinner2);
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
                Log.d(TAG,t.getFullName()+" "+t.getApartment());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Tenant t = (Tenant) spinner.getSelectedItem();
                db.collection("users").document(t.getId()).collection("payments")
                        .add(new tenant_payment(date, sum_text.getText().toString()))
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(getContext(),"complete",Toast.LENGTH_LONG).show();
                        hideKeyboardFrom(getActivity(),v);
                        assert getFragmentManager() != null;
                        getFragmentManager().popBackStack();
                    }
                });
            }
        });
        return curView;
    }
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
        if (context instanceof OnAddFragmentInteractionListener) {
            mListener = (OnAddFragmentInteractionListener) context;
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
    public interface OnAddFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

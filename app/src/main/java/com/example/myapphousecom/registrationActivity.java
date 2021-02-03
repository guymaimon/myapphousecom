package com.example.myapphousecom;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.myapphousecom.data.emailValidation.isValidEmail;

//import android.support.v7.app.AppCompatActivity;

public class registrationActivity extends AppCompatActivity {
    private static final String TAG = "registrationActivity";
    private Button registerBtn;
    private LinearLayout formLayout;
    private ProgressBar loading;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private CheckBox terms;
    private EditText email, fullName, apartmentNumber, buildingNumber, password, password_again,seniority,numOfRooms;
    private Switch aSwitch;
    private boolean isCommittee = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.registration);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        registerBtn = new Button(this);
        aSwitch = findViewById(R.id.switch1);
        formLayout = findViewById(R.id.registration_form);
        aSwitch.setChecked(isCommittee);
        registerBtn = findViewById(R.id.registerBtn);
        email = findViewById(R.id.email);
        loading = findViewById(R.id.loading);
        password = findViewById(R.id.password);
        fullName = findViewById(R.id.full_name_txt);
        password_again = findViewById(R.id.password_again);
        apartmentNumber = findViewById(R.id.apartment_number);
        buildingNumber = findViewById(R.id.building_number);
        seniority = findViewById(R.id.seniority);
        numOfRooms = findViewById(R.id.num_of_rooms);
        numOfRooms.setVisibility(View.GONE);
        terms = findViewById(R.id.terms);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCommittee = isChecked;
                aSwitch.setText(isCommittee ? "Committee" : "Tenant");
                if(isCommittee)
                {
                    seniority.setVisibility(View.VISIBLE);
                    numOfRooms.setVisibility(View.GONE);

                }
                else{
                    seniority.setVisibility(View.GONE);
                    numOfRooms.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void register() {
        //Timestamp t = new Timestamp();
        if (!isValidEmail(email.getText())) {
            email.setError("please enter a valid Email Address!");
        }
        else if (TextUtils.isEmpty(email.getText())) {
            email.setError("Email is required!");
        } else if (TextUtils.isEmpty(password.getText())) {
            password.setError("Password is required!");
        } else if (TextUtils.isEmpty(password_again.getText())) {
            password_again.setError("Password is required!");
        } else if (!password.getText().toString().equals(password_again.getText().toString())) {
            password.setError("two passwords must be equals!");
        } else if (password.getText().length() < 5) {
            password.setError("Password length must be greater then 5!");
        } else if (password_again.getText().length() < 5) {
            password_again.setError("Password length must be greater then 5!");
        }
        else if (TextUtils.isEmpty(buildingNumber.getText())){
            buildingNumber.setError("You must enter building Number");
        }
        else if (TextUtils.isEmpty(apartmentNumber.getText())){
            apartmentNumber.setError("You must enter apartment Number");
        }
        else if ( isCommittee && TextUtils.isEmpty(seniority.getText())){
            seniority.setError("You must enter seniority");
        }
        else if (!isCommittee && TextUtils.isEmpty(numOfRooms.getText())){
            numOfRooms.setError("You must enter number of rooms!");
        }
        else if (!terms.isChecked()){
            terms.setError("You must read and accept the terms");
        }else {

            formLayout.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);

//            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
//                    .setTimestampsInSnapshotsEnabled(true)
//                    .build();
//            db.setFirestoreSettings(settings);
            final Map<String, Object> useri = new HashMap<>();
            useri.put("email", email.getText().toString());
//                useri.put("hashedPassword", Encoder.strEncoder(password.getText().toString(), "SHA-256"));
            useri.put("registrationDate", Calendar.getInstance().getTime());
            useri.put("lastLogin", Calendar.getInstance().getTime());
            useri.put("fullName", fullName.getText().toString());
            useri.put("role", isCommittee ? "Committee" : "Tenant");
            useri.put("apartmentNumber", apartmentNumber.getText().toString());
            useri.put("buildingNumber", buildingNumber.getText().toString());
            int paymentPerRoom = 200;
            if(isCommittee)
                useri.put("seniority", seniority.getText().toString());
            else
                useri.put("monthlyPayment", paymentPerRoom * Integer.parseInt(numOfRooms.getText().toString()));
// Add a new document with a generated ID

            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in useri's information
                                Log.d(TAG, "createUserWithEmail:success");
                                final FirebaseUser user = mAuth.getCurrentUser();
                                useri.put("Uid", user.getUid());
                                db.collection("users").document(user.getUid())
                                        .set(useri)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                loading.setVisibility(View.INVISIBLE);
                                                updateUI(user);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error adding document", e);
                                            }
                                        });
//

                            } else {
                                // If sign in fails, display a message to the useri.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(registrationActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                updateUI(null);
                            }

                            // ...
                        }
                    });


        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            registrationActivity.super.onBackPressed();


        } else {
            Toast.makeText(this, "Error, Please try again", Toast.LENGTH_LONG).show();
            formLayout.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }

    }
}

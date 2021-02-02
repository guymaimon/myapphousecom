package com.example.myapphousecom.data;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapphousecom.data.model.LoggedInUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private FirebaseAuth mAuth;
    private static final String TAG = "LoginDataSource";
    public Result<LoggedInUser> login(String username, String password) {

        try {

            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            Log.d(TAG,"login "+username+" "+password );

            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
    public FirebaseUser readData(final MyCallBack myCallBack, String username, String password){

        return null;
    }
    public interface MyCallBack{
        FirebaseUser onCallback(FirebaseUser user);
    }
    public void logout() {
        // TODO: revoke authentication
    }
}

package com.example.fitnessapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserData {
    private static String TAG = "UserData";
    public interface UserDataCallback {
        void onUserDataReceived(User user);
        void onError(String errorMessage);
    }

    public static void getCurrentUserData(final UserDataCallback callback) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        Log.d(TAG, "getCurrentUserData: " + firebaseUser.toString());
        if (firebaseUser != null){
            String uid = firebaseUser.getUid();
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            DatabaseReference currentUserRef = usersRef.child(uid);

            currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    if (callback != null){
                        callback.onUserDataReceived(user);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if(callback != null) {
                        callback.onError(error.getMessage());
                    }
                }
            });
        }
        else {
           if(callback != null) {
               callback.onError("No user is currently logged in!");
           }
        }
    }

}

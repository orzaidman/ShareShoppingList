package com.or.shareshoppinglist.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.or.shareshoppinglist.R;
import com.or.shareshoppinglist.data.AllFamilies;
import com.or.shareshoppinglist.data.AllUsers;
import com.or.shareshoppinglist.data.Family;
import com.or.shareshoppinglist.data.MySheredP;
import com.or.shareshoppinglist.data.User;


public class SplashScreen extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");
    private AllUsers allUsers;
    private String uuid;
    private Family newFamily;
    private AllFamilies allFamilies;
    private User newUser = new User();
    private MySheredP msp;
    private Gson gson = new Gson();
    private ImageView panel_IMG_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        msp = new MySheredP(this);

        allUsers = new AllUsers();
        allFamilies = new AllFamilies();
        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        panel_IMG_back = findViewById(R.id.panel_IMG_back);
        loadImageByDrawableName();

        getFamilyFromDB();


    }


    private void getFamilyFromDB() {
        // Read from the database
        myRef.child("Families").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Family familyToAdd = ds.getValue(Family.class);
                        allFamilies.add(familyToAdd);
                    }
                }
                getUsersFromDB();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("or", "Failed to read value.", error.toException());
            }
        });
    }


    private void getUsersFromDB() {
        // Read from the database
        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User userToAdd = ds.getValue(User.class);

                        allUsers.add(userToAdd);
                        if (userToAdd.getUuid().equals(uuid)) {
                            newUser = userToAdd;
                            newFamily = allFamilies.getFamilyByID(newUser.getFamilyID());
                        }
                    }
                    msp.putOnMSP(allFamilies,allUsers,newUser,newFamily);

                    }
                if(newUser.getUuid() != null)
                    openActivityMain();
                else
                    openActivityLogin();
                }


            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("or", "Failed to read value.", error.toException());
            }
        });
    }

    private void openActivityMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void openActivityLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }


    public void loadImageByDrawableName() {
        Glide
                .with(this)
                .load(R.drawable.background)
                .into(panel_IMG_back);
    }
}
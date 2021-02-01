package com.or.shareshoppinglist.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.or.shareshoppinglist.R;
import com.or.shareshoppinglist.data.AllFamilies;
import com.or.shareshoppinglist.data.AllUsers;
import com.or.shareshoppinglist.data.Constants;
import com.or.shareshoppinglist.data.Family;
import com.or.shareshoppinglist.data.MySheredP;
import com.or.shareshoppinglist.data.User;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {
    static FirebaseStorage storage;
    static StorageReference storageRef;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    private MaterialButton settings_BTN_logout;
    private View view;
    private TextInputLayout settings_EDIT_FirstName, settings_EDIT_LastName, settings_EDIT_phone_number, settings_EDIT_familyID;
    private User user;
    private MySheredP msp;
    private AllUsers allUsers;
    private AllFamilies allFamilies;
    private Family family;
    private ImageView main_IMG_user, panel_IMG_back;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msp = new MySheredP(getContext());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_settings, container, false);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        findViews(view);
        loadImageByDrawableName();

        getFromMSP();
        settings_EDIT_FirstName.getEditText().setText(user.getFirstName());
        settings_EDIT_LastName.getEditText().setText(user.getLastName());
        settings_EDIT_phone_number.getEditText().setText(user.getPhoneNumber());
        settings_EDIT_familyID.getEditText().setText(user.getFamilyID());

        downloadImage();

        settings_BTN_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertMessage();
            }
        });
        return view;
    }


    private void openAlertMessage() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setMessage("Are You Sure You Want To Logout?");
        alert.setTitle("Alert");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                remove();
                msp.putOnMSP(allFamilies, allUsers, user, family);

                openActivityLogin();
            }
        });


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        alert.show();
    }


    private void findViews(View view) {
        settings_EDIT_FirstName = view.findViewById(R.id.settings_EDIT_FirstName);
        settings_EDIT_LastName = view.findViewById(R.id.settings_EDIT_LastName);
        settings_EDIT_phone_number = view.findViewById(R.id.settings_EDIT_phone_number);
        settings_EDIT_familyID = view.findViewById(R.id.settings_EDIT_familyID);
        settings_BTN_logout = view.findViewById(R.id.settings_BTN_logout);
        main_IMG_user = view.findViewById(R.id.main_IMG_user);
        panel_IMG_back = view.findViewById(R.id.panel_IMG_back);
    }

    public void loadImageByDrawableName() {
        Glide
                .with(this)
                .load(R.drawable.background)
                .into(panel_IMG_back);
    }


    private void remove() {
        allUsers.removeUser(user);
        family.removeUser(user);
        msp.putOnMSP(allFamilies, allUsers, user, family);
        myRef.child("Families").child(family.getFamilyID()).setValue(family);
        myRef.child("Users").setValue(allUsers.getAllUsers());
    }

    private void openActivityLogin() {
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }


    private void downloadImage() {
        if (user.getImageUser().equals("null"))
            main_IMG_user.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_profile_image));

        else
            storageRef.child(user.getImageUser()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(getContext()).load(uri).into(main_IMG_user);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
    }

    private void getFromMSP() {
        String data = msp.getString(Constants.KEY_USER, "NA");
        if (!data.equals("NA"))
            user = new Gson().fromJson(data, User.class);

        String dataAllFamilies = msp.getString(Constants.KEY_ALL_FAMILIES, "NA");
        allFamilies = new Gson().fromJson(dataAllFamilies, AllFamilies.class);

        String dataAllUsers = msp.getString(Constants.KEY_ALL_USERS, "NA");
        allUsers = new Gson().fromJson(dataAllUsers, AllUsers.class);

        String dataFamily = msp.getString(Constants.KEY_FAMILY, "NA");
        if (!data.equals("NA"))
            family = new Gson().fromJson(dataFamily, Family.class);

    }

}
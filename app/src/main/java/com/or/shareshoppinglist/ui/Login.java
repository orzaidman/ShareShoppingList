package com.or.shareshoppinglist.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.or.shareshoppinglist.R;
import com.or.shareshoppinglist.data.AllFamilies;
import com.or.shareshoppinglist.data.AllUsers;
import com.or.shareshoppinglist.data.Constants;
import com.or.shareshoppinglist.data.Family;
import com.or.shareshoppinglist.data.MySheredP;
import com.or.shareshoppinglist.data.User;

public class Login extends AppCompatActivity {

    private ImageView panel_IMG_back;
    FirebaseStorage storage;
    StorageReference storageReference;
    private MaterialButton login_BTN_signIn;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("message");

    private TextInputLayout mainPage_EDIT_FirstName, mainPage_EDIT_LastName,
            mainPage_EDIT_phone_number, mainPage_EDIT_password;
    private User newUser = new User();
    private AllUsers allUsers;
    private String uuid;
    private Family newFamily;
    private AllFamilies allFamilies;
    private MySheredP msp;
    private ImageView main_IMG_user;
    private String filePath = "";
    private Uri fileUri;
    private String tempName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        msp = new MySheredP(this);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        allUsers = new AllUsers();
        allFamilies = new AllFamilies();
        uuid = android.provider.Settings.Secure.getString(
                this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        findViews();
        loadImageByDrawableName();
        getFromMSP();


        main_IMG_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });


        login_BTN_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllDataManager()) {
                    newUser = new User(uuid, mainPage_EDIT_FirstName.getEditText().getText().toString(),
                            mainPage_EDIT_LastName.getEditText().getText().toString(),
                            fileUri + "",
                            mainPage_EDIT_phone_number.getEditText().getText().toString(), User.USER_TYPE.MANAGER,
                            allFamilies.getAllFamilies().size() + "", mainPage_EDIT_password.getEditText().getText().toString());

                    allUsers.add(newUser);

                    myRef.child("Users").setValue(allUsers.getAllUsers());

                    newFamily = new Family();
                    newFamily.setFamilyPassword(mainPage_EDIT_password.getEditText().getText().toString());
                    allFamilies.add(newFamily);
                    newFamily.add(newUser);
                    newFamily.setFamilyID(newUser.getFamilyID() + "");
                    myRef.child("Families").child(newFamily.getFamilyID()).setValue(newFamily);
                    msp.putOnMSP(allFamilies, allUsers, newUser, newFamily);

                    openActivityNewFamily();

                } else if (checkAllDataClient()) {
                    newUser = new User(uuid, mainPage_EDIT_FirstName.getEditText().getText().toString(),
                            mainPage_EDIT_LastName.getEditText().getText().toString(),
                            fileUri + "",
                            mainPage_EDIT_phone_number.getEditText().getText().toString(), User.USER_TYPE.CLIENT,
                            "", "");


                    allUsers.add(newUser);
                    msp.putOnMSP(allFamilies, allUsers, newUser, newFamily);

                    myRef.child("Users").setValue(allUsers.getAllUsers());
                    openAlertMessage();
                }

                if (mainPage_EDIT_FirstName.getEditText().getText().length() == 0)
                    mainPage_EDIT_FirstName.getEditText().setError("Enter a first name");
                if (mainPage_EDIT_LastName.getEditText().getText().length() == 0)
                    mainPage_EDIT_LastName.getEditText().setError("Enter a last name");

                if (mainPage_EDIT_phone_number.getEditText().getText().length() == 0)
                    mainPage_EDIT_phone_number.getEditText().setError("Enter a phone number");


            }

        });

    }

    private boolean checkAllDataClient() {
        if (mainPage_EDIT_FirstName.getEditText().getText().length() != 0 &&
                mainPage_EDIT_LastName.getEditText().getText().length() != 0 &&
                mainPage_EDIT_phone_number.getEditText().getText().length() != 0)
            return true;
        return false;
    }


    private boolean checkAllDataManager() {
        if (mainPage_EDIT_FirstName.getEditText().getText().length() != 0 &&
                mainPage_EDIT_LastName.getEditText().getText().length() != 0 &&
                mainPage_EDIT_phone_number.getEditText().getText().length() != 0 &&
                mainPage_EDIT_password.getEditText().getText().length() != 0
        )
            return true;
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            main_IMG_user.setImageURI(fileUri);


            //You can also get File Path from intent
            filePath = new ImagePicker().Companion.getFilePath(data);
            uploadImage();
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, new ImagePicker().Companion.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }


    private void getImage() {
        ImagePicker.Companion
                .with(this)
                .crop()
                .cropOval()
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }


    private void openAlertMessage() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final TextView tt = new TextView(this);
        tt.setText("Enter family ID:");
        layout.addView(tt);


        final EditText editTextID = new EditText(this);
        alert.setTitle("Alert");

        layout.addView(editTextID);

        final TextView t = new TextView(this);
        t.setText("Enter family Password:");
        layout.addView(t);

        final EditText editTextPassword = new EditText(this);

        layout.addView(editTextPassword);

        alert.setView(layout);


        alert.setNeutralButton("Join", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String familyID = editTextID.getText().toString();
                newFamily = allFamilies.getFamilyByID(familyID);

                if (newFamily != null) {
                    if (editTextPassword.getText().toString().equals(newFamily.getFamilyPassword())) {
                        newUser.setFamilyID(familyID);

                        newFamily.add(newUser);
                        myRef.child("Users").setValue(allUsers.getAllUsers());
                        myRef.child("Families").child(newFamily.getFamilyID()).setValue(newFamily);
                        msp.putOnMSP(allFamilies, allUsers, newUser, newFamily);

                        openActivityNewFamily();
                    }
                } else {
                    openAlertMessage();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();

    }

    private void openActivityNewFamily() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {
            StorageReference ref = storageReference.child(fileUri.toString());
            newUser.setImageUser(fileUri.toString());
            tempName = fileUri.toString();
            // adding listeners on upload
            // or failure of image
            ref.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(
                        UploadTask.TaskSnapshot taskSnapshot) {

                }
            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                }
                            });
        }
    }


    private void getFromMSP() {
        String dataUser = msp.getString(Constants.KEY_USER, "NA");
        String dataFamily = msp.getString(Constants.KEY_FAMILY, "NA");
        String dataAllUsers = msp.getString(Constants.KEY_ALL_USERS, "NA");
        String dataAllFamily = msp.getString(Constants.KEY_ALL_FAMILIES, "NA");

        if (!dataUser.equals("NA"))
            newUser = new Gson().fromJson(dataUser, User.class);

        if (!dataAllFamily.equals("NA"))
            allFamilies = new Gson().fromJson(dataAllFamily, AllFamilies.class);

        if (!dataAllUsers.equals("NA"))
            allUsers = new Gson().fromJson(dataAllUsers, AllUsers.class);

        if (!dataFamily.equals("NA"))
            newFamily = new Gson().fromJson(dataFamily, Family.class);
    }


    private void findViews() {
        login_BTN_signIn = findViewById(R.id.login_BTN_signIn);
        mainPage_EDIT_FirstName = findViewById(R.id.mainPage_EDIT_FirstName);
        mainPage_EDIT_LastName = findViewById(R.id.mainPage_EDIT_LastName);
        mainPage_EDIT_phone_number = findViewById(R.id.mainPage_EDIT_phone_number);
        main_IMG_user = findViewById(R.id.main_IMG_user);
        mainPage_EDIT_password = findViewById(R.id.mainPage_EDIT_password);
        panel_IMG_back = findViewById(R.id.panel_IMG_back);
    }


    public void loadImageByDrawableName() {
        Glide
                .with(this)
                .load(R.drawable.background)
                .into(panel_IMG_back);
    }


}

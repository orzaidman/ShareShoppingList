package com.or.shareshoppinglist.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.or.shareshoppinglist.R;
import com.or.shareshoppinglist.data.Adapter_All_Family;

import com.or.shareshoppinglist.data.Constants;
import com.or.shareshoppinglist.data.Family;
import com.or.shareshoppinglist.data.MySheredP;
import com.or.shareshoppinglist.data.User;

public class MyFamilyFragment extends Fragment {
    private View view;
    private MySheredP msp;

    private RecyclerView new_Family_LST_allFamily;
    private ImageView panel_IMG_back;
    private User user;
    private Family family = new Family();
    FloatingActionButton new_Family_BTN_add;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_family, container, false);
        msp = new MySheredP(getContext());

        getFromMSP();

        new_Family_LST_allFamily = view.findViewById(R.id.new_Family_LST_allFamily);
        new_Family_BTN_add = view.findViewById(R.id.new_Family_BTN_add);
        panel_IMG_back = view.findViewById(R.id.panel_IMG_back);

        loadImageByDrawableName();
        showAll();
        if (user.getUserType() == User.USER_TYPE.CLIENT)
            new_Family_BTN_add.setVisibility(View.INVISIBLE);

        new_Family_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlertMessage();
            }
        });
        return view;
    }


    private void openAlertMessage() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Alert");
        alert.setMessage("Enter The Next Data:" +
                "\nFamily ID: " + user.getFamilyID() +
                "\nFamily Password: " + user.getPassword());


        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }


    private void getFromMSP() {
        String dataUser = msp.getString(Constants.KEY_USER, "NA");
        String dataFamily = msp.getString(Constants.KEY_FAMILY, "NA");

        if (!dataUser.equals("NA"))
            user = new Gson().fromJson(dataUser, User.class);

        if (!dataFamily.equals("NA"))
            family = new Gson().fromJson(dataFamily, Family.class);
    }


    public void showAll() {
        Adapter_All_Family adapter_all_family = new Adapter_All_Family(getContext(), family);
        new_Family_LST_allFamily.setLayoutManager(new LinearLayoutManager(getContext()));
        new_Family_LST_allFamily.setItemAnimator(new DefaultItemAnimator());
        new_Family_LST_allFamily.setAdapter(adapter_all_family);
    }


    public void loadImageByDrawableName() {
        Glide
                .with(this)
                .load(R.drawable.background)
                .into(panel_IMG_back);
    }


}


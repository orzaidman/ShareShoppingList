package com.or.shareshoppinglist.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.or.shareshoppinglist.data.AllUsers;
import com.or.shareshoppinglist.data.Constants;
import com.or.shareshoppinglist.data.Family;
import com.or.shareshoppinglist.data.Item;
import com.or.shareshoppinglist.R;
import com.or.shareshoppinglist.data.Adapter_item;
import com.or.shareshoppinglist.data.MySheredP;
import com.or.shareshoppinglist.data.User;

public class MyListFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    private MySheredP msp;
    private Gson gson = new Gson();

    private View view;
    private FloatingActionButton mylist_BTN_add, mylist_BTN_remove;
    private Button mylist_BTN_send;
    private RecyclerView main_LST_news;
    private AllUsers allUsers = new AllUsers();
    private User user;
    private Family family = new Family();
    private ImageView panel_IMG_back;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msp = new MySheredP(getContext());
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_my_list, container, false);
        findViews();
        family = getFromMSP();
        loadImageByDrawableName();

        showAll();

        mylist_BTN_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });
        mylist_BTN_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family.clearAllItems();
                myRef.child("Families").child(family.getFamilyID()).setValue(family);
                msp.putOnMSP(allUsers, user, family);

                showAll();
            }
        });
        mylist_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                final EditText edittext = new EditText(getContext());
                alert.setMessage("Add Ingredient");
                alert.setTitle("Enter the name of the Ingredient");

                alert.setView(edittext);
                alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String nameIngredient = edittext.getText().toString();
                        if (nameIngredient.length() > 0) {
                            Item newItem = new Item(nameIngredient, false);
                            family.getAllItems().add(newItem);
                            myRef.child("Families").child(family.getFamilyID()).setValue(family);
                            msp.putOnMSP(allUsers, user, family);

                            showAll();
                        }
                    }
                });

                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alert.show();
            }
        });

        return view;
    }


    public void showAll() {
        Adapter_item adapter_position = new Adapter_item(getContext(), family);
        main_LST_news.setLayoutManager(new LinearLayoutManager(getContext()));
        main_LST_news.setItemAnimator(new DefaultItemAnimator());
        main_LST_news.setAdapter(adapter_position);
    }


    private void findViews() {
        main_LST_news = view.findViewById(R.id.main_LST_news);
        mylist_BTN_add = view.findViewById(R.id.mylist_BTN_add);
        mylist_BTN_remove = view.findViewById(R.id.mylist_BTN_remove);
        mylist_BTN_send = view.findViewById(R.id.mylist_BTN_send);
        panel_IMG_back = view.findViewById(R.id.panel_IMG_back);
    }

    static class ViewHolder_Normal extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView position_LBL_title;

        private ImageButton position_IMG_back;

        public ViewHolder_Normal(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            position_LBL_title = itemView.findViewById(R.id.position_LBL_title);
            position_IMG_back = itemView.findViewById(R.id.position_IMG_back);


        }


        @Override
        public void onClick(View view) {
            Log.d("onclick", "onClick " + getLayoutPosition());
        }
    }


    private Family getFromMSP() {

        String dataAllUsers = msp.getString(Constants.KEY_ALL_USERS, "NA");
        allUsers = new Gson().fromJson(dataAllUsers, AllUsers.class);

        String dataUser = msp.getString(Constants.KEY_USER, "NA");
        user = new Gson().fromJson(dataUser, User.class);

        String dataFamily = msp.getString(Constants.KEY_FAMILY, "NA");
        family = new Gson().fromJson(dataFamily, Family.class);
        if (family == null)
            family = new Family();
        return family;
    }


    private void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = family.getStrList();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Shopping List");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void loadImageByDrawableName() {
        Glide
                .with(this)
                .load(R.drawable.background)
                .into(panel_IMG_back);
    }

}
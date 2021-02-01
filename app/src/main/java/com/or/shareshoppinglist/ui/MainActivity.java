package com.or.shareshoppinglist.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.button.MaterialButton;
import com.or.shareshoppinglist.R;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private FrameLayout order_FRAME_main;
    private MaterialButton main_BTN_my_list, main_BTN_my_family, main_BTN_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        showMyList();
        main_BTN_my_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyFamily();
            }
        });

        main_BTN_my_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyList();
            }
        });

        main_BTN_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSettings();
            }
        });

    }

    private void findViews() {
        order_FRAME_main = findViewById(R.id.order_FRAME_main);
        main_BTN_settings = findViewById(R.id.main_BTN_settings);
        main_BTN_my_list = findViewById(R.id.main_BTN_my_list);
        main_BTN_my_family = findViewById(R.id.main_BTN_my_family);
    }

    private void showMyList() {
        MyListFragment myListFragment = new MyListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.order_FRAME_main, myListFragment);
        transaction.commit();
    }


    private void showMyFamily() {
        MyFamilyFragment myFamilyFragment = new MyFamilyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.order_FRAME_main, myFamilyFragment);
        transaction.commit();
    }

    private void showSettings() {
        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.order_FRAME_main, settingsFragment);
        transaction.commit();
    }
}
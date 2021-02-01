package com.or.shareshoppinglist.data;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.or.shareshoppinglist.R;

import java.util.ArrayList;

public class Adapter_item extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    private MySheredP msp;
    private Gson gson = new Gson();

    private final int VIEW_TYPE_NORMAL = 0;

    private Context context;
    private ArrayList<Item> items;
    private Family family;

    public Adapter_item(Context context, Family family) {
        this.context = context;
        this.items = family.getAllItems();
        this.family = family;
        msp = new MySheredP(context);

    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    // specify the row layout file and click for each row
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
            return new ViewHolder_Normal(view);
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items, parent, false);
        ViewHolder_Normal myViewHolderNormal = new ViewHolder_Normal(view);
        return myViewHolderNormal;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        ViewHolder_Normal mHolder = (ViewHolder_Normal) holder;
        mHolder.position_LBL_title.setText(family.getAllItems().get(pos).getName());
        if (family.getAllItems().get(pos).isTaken())
            mHolder.position_IMG_back.setImageResource(R.drawable.check);
        else
            mHolder.position_IMG_back.setImageResource(R.drawable.uncheck);

        mHolder.position_IMG_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                family.getAllItems().get(pos).setTaken(!family.getAllItems().get(pos).isTaken());


                if (family.getAllItems().get(pos).isTaken()) {
                    mHolder.position_IMG_back.setImageResource(R.drawable.check);
                } else
                    mHolder.position_IMG_back.setImageResource(R.drawable.uncheck);
                updateFamily();
            }
        });

    }

    private void updateFamily() {
        myRef.child("Families").child(family.getFamilyID()).setValue(family);
        msp.putOnMSP(family);
    }

    private Item getItem(int position) {
        return family.getAllItems().get(position);
    }


    static class ViewHolder_Normal extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView position_LBL_title;
        private ImageView position_IMG_back;

        public ViewHolder_Normal(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            position_LBL_title = itemView.findViewById(R.id.position_LBL_title);
            position_IMG_back = itemView.findViewById(R.id.position_IMG_back);

        }

        @Override
        public void onClick(View view) {
        }
    }
}

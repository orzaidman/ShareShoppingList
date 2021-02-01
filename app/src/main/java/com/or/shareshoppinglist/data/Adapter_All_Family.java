package com.or.shareshoppinglist.data;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.or.shareshoppinglist.R;
import com.squareup.picasso.Picasso;

public class Adapter_All_Family extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_NORMAL = 0;

    private Context context;
    private Family family;
    private User currentUser;

    static FirebaseStorage storage;
    static StorageReference storageRef;


    public Adapter_All_Family(Context context, Family family) {
        this.context = context;
        this.family = family;
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return family == null ? 0 : family.getAllFamilyUsers().size();
    }


    // specify the row layout file and click for each row
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_all_family, parent, false);
            return new ViewHolder_Normal(view);
        }
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items_all_family, parent, false);
        ViewHolder_Normal myViewHolderNormal = new ViewHolder_Normal(view);
        return myViewHolderNormal;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        currentUser = getItem(pos);
        ViewHolder_Normal mHolder = (ViewHolder_Normal) holder;
        mHolder.all_family_TXT_name.setText(currentUser.getFirstName());
        mHolder.downloadImage(currentUser, context);
    }

    private User getItem(int position) {
        return family.getAllFamilyUsers().get(position);
    }


    static class ViewHolder_Normal extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView all_family_TXT_name;
        private ImageView all_family_IMG_profile;

        public ViewHolder_Normal(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            all_family_TXT_name = itemView.findViewById(R.id.all_family_TXT_name);
            all_family_IMG_profile = itemView.findViewById(R.id.all_family_IMG_profile);
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();
        }

        @Override
        public void onClick(View view) {
        }


        private void downloadImage(User user, Context context) {
            if (user.getImageUser().equals("null"))
                all_family_IMG_profile.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile_image));

            else
                storageRef.child(user.getImageUser()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.with(context).load(uri).into(all_family_IMG_profile);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });


        }
    }

}

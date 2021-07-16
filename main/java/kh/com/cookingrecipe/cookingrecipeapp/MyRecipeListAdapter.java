package kh.com.cookingrecipe.cookingrecipeapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MyRecipeListAdapter extends RecyclerView.Adapter<MyRecipeListAdapter.ViewHolder>{
    private final ArrayList<RecipeDataList> listdata;

    public MyRecipeListAdapter(ArrayList<RecipeDataList> listdata) {
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_list_my_recipe, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.get().load(listdata.get(position).getImgId()).into(holder.imgView);
        holder.nameView.setText(listdata.get(position).getRecipeName());
        holder.timeView.setText(String.valueOf(listdata.get(position).getTime()));
        holder.servingView.setText(String.valueOf(listdata.get(position).getServing()));
        holder.ratingView.setText(String.valueOf(listdata.get(position).getRating()));

        holder.imgDelete.setOnClickListener(v -> {

            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        FirebaseStorage storageReference = FirebaseStorage.getInstance();
                        StorageReference photoRef = storageReference.getReferenceFromUrl(listdata.get(position).getImgId());
                        photoRef.delete().addOnSuccessListener(aVoid -> {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            String recipe_id = listdata.get(position).getRecipeId();
                            DatabaseReference mRef_recipe =  databaseReference.child("Recipe");
                            DatabaseReference mRef_userReview =  databaseReference.child("UserReview");
                            mRef_recipe.child(recipe_id).removeValue();
                            mRef_userReview.child(recipe_id).removeValue();
                            Query query = databaseReference.child("Favorite");
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                                        String user_id = child.getKey();
                                        for (DataSnapshot recipeID : child.child("recipeID").getChildren()){
                                            String id = recipeID.getKey();
                                            assert id != null;
                                            if(id.equals(recipe_id)){
                                                DatabaseReference mRef_favorite =  databaseReference.child("Favorite/"+user_id+"/recipeID");
                                                mRef_favorite.child(recipe_id).removeValue();
                                                listdata.remove(holder.getAdapterPosition());
                                                notifyItemRemoved(holder.getAdapterPosition());
                                                notifyItemRangeChanged(holder.getAdapterPosition(), listdata.size());
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Toast.makeText(v.getContext(), "Delete Recipe Successfully.", Toast.LENGTH_LONG).show();
                        }).addOnFailureListener(exception -> Toast.makeText(v.getContext(), "Can not delete recipe.", Toast.LENGTH_LONG).show());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            AlertDialog.Builder ab = new AlertDialog.Builder(v.getContext());
            ab.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });

        holder.relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecipeDetailActivity.class);

            String recipe_id = String.valueOf(listdata.get(position).getRecipeId());
            intent.putExtra("recipeID", recipe_id);

            v.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView;
        public TextView nameView;
        public TextView timeView;
        public TextView servingView;
        public TextView ratingView;
        public ImageView imgDelete;
        //public ImageView imgEdit;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imgView = (ImageView) itemView.findViewById(R.id.my_recipe_img);
            this.nameView = (TextView) itemView.findViewById(R.id.my_recipe_name);
            this.timeView = (TextView) itemView.findViewById(R.id.my_recipe_time);
            this.servingView = (TextView) itemView.findViewById(R.id.my_recipe_serving);
            this.ratingView = (TextView) itemView.findViewById(R.id.my_recipe_rating);
            this.imgDelete = (ImageView) itemView.findViewById(R.id.my_recipe_delete);
            //this.imgEdit = (ImageView) itemView.findViewById(R.id.my_recipe_edit);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);

        }

    }
}

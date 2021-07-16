package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder>{
    private final ArrayList<RecipeDataList> listdata;

    public HomeListAdapter(ArrayList<RecipeDataList> listdata) {
        this.listdata = listdata;
    }
    @NonNull
    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_list_recipe_home, parent, false);
        HomeListAdapter.ViewHolder viewHolder = new HomeListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeListAdapter.ViewHolder holder, final int position) {
        Picasso.get().load(listdata.get(position).getImgId()).into(holder.imgView);
        holder.nameView.setText(listdata.get(position).getRecipeName());
        holder.chefnameView.setText(listdata.get(position).getChef());
        holder.timeView.setText(String.valueOf(listdata.get(position).getTime()));
        holder.servingView.setText(String.valueOf(listdata.get(position).getServing()));
        holder.ratingView.setText(String.valueOf(listdata.get(position).getRating()));
        holder.relativeLayout.setOnClickListener(v -> {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            String userId = user.getUid();
            Query query = databaseReference.child("Recipe/" + listdata.get(position).getRecipeId());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    RecipeDataList data = dataSnapshot.getValue(RecipeDataList.class);
                    assert data != null;
                    int num_click = data.getNumber_click();
                    String user_id = data.getChef();
                    if(!user_id.equals(userId)){
                        int add_click = num_click + 1;
                        DatabaseReference mRef = databaseReference.child("Recipe/" + listdata.get(position).getRecipeId());
                        mRef.child("number_click").setValue(add_click);
                        if(!user.isAnonymous()){
                            DatabaseReference mRef_click = databaseReference.child("UserClick/" + userId + "/recipeID");
                            mRef_click.child(listdata.get(position).getRecipeId()).setValue("true");
                        }

                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            String recipe_id = listdata.get(position).getRecipeId();

            Intent intent = new Intent(v.getContext(), RecipeDetailActivity.class);
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
        public TextView chefnameView;
        public TextView timeView;
        public TextView servingView;
        public TextView ratingView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imgView = itemView.findViewById(R.id.home_recipe_img);
            this.nameView = itemView.findViewById(R.id.home_recipe_name);
            this.chefnameView = itemView.findViewById(R.id.home_chef_name);
            this.timeView = itemView.findViewById(R.id.home_recipe_time);
            this.servingView = itemView.findViewById(R.id.home_recipe_serving);
            this.ratingView = itemView.findViewById(R.id.home_recipe_rating);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_home);
        }
    }
}

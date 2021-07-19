package kh.com.cookingrecipe.cookingrecipeapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>{
    private final ArrayList<RecipeDataList> favoritelist;

    public FavoriteListAdapter(ArrayList<RecipeDataList> favoritelist) {
        this.favoritelist = favoritelist;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View list_favorite= layoutInflater.inflate(R.layout.activity_list_favorite, parent, false);
        ViewHolder Holder = new ViewHolder(list_favorite);
        return Holder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Picasso.get().load(favoritelist.get(position).getImgId()).into(holder.fav_img);
        holder.fav_name.setText(favoritelist.get(position).getRecipeName());
        holder.fav_time.setText(String.valueOf(favoritelist.get(position).getTime()));
        holder.fav_serving.setText(String.valueOf(favoritelist.get(position).getServing()));
        holder.fav_rating.setText(String.valueOf(favoritelist.get(position).getRating()));
        holder.fav_chef.setText(favoritelist.get(position).getChef());
        holder.relativeLayout.setOnClickListener(v -> {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            String userId = user.getUid();
            Query query = databaseReference.child("Recipe/" + favoritelist.get(position).getRecipeId());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    RecipeDataList data = dataSnapshot.getValue(RecipeDataList.class);
                    assert data != null;
                    int num_click = data.getNumber_click();
                    String user_id = data.getChef();
                    if(!user_id.equals(userId)){
                        int add_click = num_click + 1;
                        DatabaseReference mRef = databaseReference.child("Recipe/" + favoritelist.get(position).getRecipeId());
                        mRef.child("number_click").setValue(add_click);
                    }

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            Intent intent = new Intent(v.getContext(), RecipeDetailActivity.class);

            String recipe_id = String.valueOf(favoritelist.get(position).getRecipeId());
            intent.putExtra("recipeID", recipe_id);

            v.getContext().startActivity(intent);
        });
        holder.fav_toggle.setOnClickListener(v -> {
            if (!holder.fav_toggle.isChecked()) {
                FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String userId = user.getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                DatabaseReference mRef =  databaseReference.child("Favorite/"+userId+"/recipeID");
                mRef.child(String.valueOf(favoritelist.get(position).getRecipeId())).removeValue();
                Toast.makeText(v.getContext(), "Delete from Favorite.", Toast.LENGTH_SHORT).show();
                favoritelist.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), favoritelist.size());

            }

        });
    }
    @Override
    public int getItemCount() {
        return favoritelist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView fav_img;
        public TextView fav_name;
        public TextView fav_time;
        public TextView fav_serving;
        public TextView fav_rating;
        public TextView fav_chef;
        public ToggleButton fav_toggle;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.fav_img = itemView.findViewById(R.id.favorite_img);
            this.fav_name = itemView.findViewById(R.id.favorite_name);
            this.fav_time = itemView.findViewById(R.id.favorite_time);
            this.fav_serving = itemView.findViewById(R.id.favorite_serving);
            this.fav_rating= itemView.findViewById(R.id.favorite_rating);
            this.fav_chef = itemView.findViewById(R.id.favorite_chef);
            this.fav_toggle = itemView.findViewById(R.id.toggleButton_favorite1);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_favorite);
        }
    }
}

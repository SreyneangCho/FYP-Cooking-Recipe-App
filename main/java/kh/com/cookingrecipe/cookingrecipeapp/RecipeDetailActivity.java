package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        Intent intent = getIntent();

        final String recipe_id = intent.getStringExtra("recipeID");

        final ToggleButton simpleToggleButton = findViewById(R.id.toggleButton_favorite);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        Query query = databaseReference.child("Favorite/"+userId+"/recipeID");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final ArrayList<String> list = new ArrayList();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    String recipeID = child.getKey();
                    list.add(recipeID);
                }
                if(list.contains(recipe_id)){
                    simpleToggleButton.setChecked(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       simpleToggleButton.setOnClickListener(v -> {
           if(!user.isAnonymous()){
               DatabaseReference mRef =  databaseReference.child("Favorite/"+userId+"/recipeID");
               if(simpleToggleButton.isChecked()){

                           mRef.child(recipe_id).setValue("true");
                           Toast.makeText(RecipeDetailActivity.this, "Add to Favorite.", Toast.LENGTH_SHORT).show();

               }else{

                   mRef.child(recipe_id).removeValue();
                   Toast.makeText(RecipeDetailActivity.this, "Delete From Favorite.", Toast.LENGTH_SHORT).show();

               }
               onResumeFragments();

           }else{
               simpleToggleButton.setChecked(false);
               Toast.makeText(RecipeDetailActivity.this, "Login to Save Your Favorite Recipe.", Toast.LENGTH_SHORT).show();
           }

       });


        Query query_recipe = databaseReference.child("Recipe/"+recipe_id);
        query_recipe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    RecipeData recipeInfo = dataSnapshot.getValue(RecipeData.class);
                assert recipeInfo != null;
                String chef = recipeInfo.getChef();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mRef = database.getReference().child("UserInfo/"+chef);
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                            String name = userSnapshot.child("userName").getValue(String.class);
                            TextView recipe_chef = findViewById(R.id.recipe_chef);
                            recipe_chef.setText(name);
                            String recipe_name = recipeInfo.getRecipe_name();
                            int cooking_time = recipeInfo.getTime();
                            int serving = recipeInfo.getServing();
                            String image = recipeInfo.getImgId();
                            ImageView imageView = findViewById(R.id.recipe_detail_image);
                            Picasso.get().load(image).into(imageView);
                            int rating = recipeInfo.getRating();

                            TextView recipe = findViewById(R.id.recipe_name);
                            recipe.setText(recipe_name);

                            TextView recipe_time = findViewById(R.id.recipe_time);
                            recipe_time.setText(String.valueOf(cooking_time));
                            TextView recipe_serving= findViewById(R.id.recipe_serving);
                            recipe_serving.setText(String.valueOf(serving));
                            TextView recipe_rating= findViewById(R.id.recipe_rating);
                            recipe_rating.setText(String.valueOf(rating));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            throw databaseError.toException();
                        }
                    });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        showIngredient(recipe_id);

        final TextView IngredientClick = findViewById(R.id.ingredient_ingredient_click);
        final TextView txtInstruction = findViewById(R.id.instruction_click);
        txtInstruction.setOnClickListener(v -> {
            txtInstruction.setTextColor(Color.parseColor("#8730F7"));
            IngredientClick.setTextColor(Color.parseColor("#000000"));
            Query query_instruction = databaseReference.child("Recipe/"+recipe_id+"/instruction");
            query_instruction.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<RecipeInstructionDataList> list = new ArrayList();
                    int i = 0;
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        i++;
                        String instruction_name = child.child("instruction_name").getValue(String.class);
                        String instruction_detail = child.child("instruction_detail").getValue(String.class);
                        list.add(new RecipeInstructionDataList(recipe_id, i, instruction_name, instruction_detail));
                    }

                    RecyclerView recyclerView = findViewById(R.id.recylerView_ingredient);
                    RecipeInstructionListAdapter adapter = new RecipeInstructionListAdapter(list);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(RecipeDetailActivity.this));
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
        ImageView imgIngredientBack = findViewById(R.id.ingredient_back);
        imgIngredientBack.setOnClickListener(v -> finish());

        IngredientClick.setOnClickListener(v -> {
            txtInstruction.setTextColor(Color.parseColor("#000000"));
            IngredientClick.setTextColor(Color.parseColor("#8730F7"));
            showIngredient(recipe_id);
        });


    }

    private void showIngredient(String recipe_id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query_ingredient = databaseReference.child("Recipe/"+recipe_id+"/ingredient");
        query_ingredient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RecipeIngredientDataList> list = new ArrayList();
                int i = 0;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    i++;
                    String recipe_name = child.child("ingredient_name").getValue(String.class);
                    String quantity = child.child("ingredient_quantity").getValue(String.class);
                    list.add(new RecipeIngredientDataList(recipe_id, i, recipe_name, quantity));
                }

                RecyclerView recyclerView = findViewById(R.id.recylerView_ingredient);
                RecipeIngredientListAdapter adapter = new RecipeIngredientListAdapter(list);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(RecipeDetailActivity.this));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ImageView img_review = findViewById(R.id.user_review);
        img_review.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UserReviewMainActivity.class);
            intent.putExtra("recipeID", recipe_id);
            startActivity(intent);
        });
    }

}

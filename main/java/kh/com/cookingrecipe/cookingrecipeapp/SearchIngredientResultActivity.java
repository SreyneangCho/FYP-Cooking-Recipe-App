package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchIngredientResultActivity extends AppCompatActivity {

    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_ingredient_result);

        Intent intent = getIntent();
        final String ingredient1 = intent.getStringExtra("ingredient1");
        final String ingredient2 = intent.getStringExtra("ingredient2");
        final String ingredient3 = intent.getStringExtra("ingredient3");

        ImageView img_back = findViewById(R.id.back_from_search_by_ingredient_result);
        img_back.setOnClickListener(v -> finish());
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Recipe");
            ref.orderByChild("rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<RecipeDataList> list = new ArrayList();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        ArrayList<String> ingredient_list = new ArrayList<>();
                        for(DataSnapshot ingredient : child.child("ingredient").getChildren()){
                            String ingredient_name = ingredient.child("ingredient_name").getValue(String.class);
                            ingredient_list.add(ingredient_name);
                        }
                        if(!ingredient1.equals("")&&ingredient2.equals("")&&ingredient3.equals("")||
                                ingredient1.equals("")&&!ingredient2.equals("")&&ingredient3.equals("")||
                                ingredient1.equals("")&&ingredient2.equals("")&&!ingredient3.equals("")){
                            if(ingredient_list.contains(ingredient1)||ingredient_list.contains(ingredient2)||ingredient_list.contains(ingredient3)){
                                RecipeData recipeInfo = child.getValue(RecipeData.class);
                                assert recipeInfo != null;
                                String recipe_name = recipeInfo.getRecipe_name();
                                String recipe_id = child.getKey();
                                int cooking_time = recipeInfo.getTime();
                                int serving = recipeInfo.getServing();
                                String image = recipeInfo.getImgId();
                                int rating = recipeInfo.getRating();
                                String chef = recipeInfo.getChef();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference mRef = database.getReference().child("UserInfo/" + chef);
                                mRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        String name = userSnapshot.child("userName").getValue(String.class);
                                        list.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                                        RecyclerView recyclerView = findViewById(R.id.recylerView_searches_ingredient);
                                        HomeListAdapter adapter = new HomeListAdapter( list);
                                        recyclerView.setHasFixedSize(true);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchIngredientResultActivity.this);
                                        layoutManager.setReverseLayout(true);
                                        layoutManager.setStackFromEnd(true);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapter);

                                        emptyView = findViewById(R.id.search_by_ingredient_no_result);
                                        if (list.isEmpty()) {
                                            recyclerView.setVisibility(View.GONE);
                                            emptyView.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            recyclerView.setVisibility(View.VISIBLE);
                                            emptyView.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }

                                });

                            }
                        }else if(!ingredient1.equals("")&&!ingredient2.equals("")&&ingredient3.equals("")||
                                ingredient1.equals("")&&!ingredient2.equals("")&&!ingredient3.equals("")||
                                !ingredient1.equals("")&&ingredient2.equals("")&&!ingredient3.equals("")){
                            if(ingredient_list.contains(ingredient1)&&ingredient_list.contains(ingredient2)||
                                    ingredient_list.contains(ingredient1)&&ingredient_list.contains(ingredient3)||
                                    ingredient_list.contains(ingredient2)&&ingredient_list.contains(ingredient3)){
                                RecipeData recipeInfo = child.getValue(RecipeData.class);
                                assert recipeInfo != null;
                                String recipe_name = recipeInfo.getRecipe_name();
                                String recipe_id = child.getKey();
                                int cooking_time = recipeInfo.getTime();
                                int serving = recipeInfo.getServing();
                                String image = recipeInfo.getImgId();
                                int rating = recipeInfo.getRating();
                                String chef = recipeInfo.getChef();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference mRef = database.getReference().child("UserInfo/" + chef);
                                mRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        String name = userSnapshot.child("userName").getValue(String.class);
                                        list.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                                        RecyclerView recyclerView = findViewById(R.id.recylerView_searches_ingredient);
                                        HomeListAdapter adapter = new HomeListAdapter(list);
                                        recyclerView.setHasFixedSize(true);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchIngredientResultActivity.this);
                                        layoutManager.setReverseLayout(true);
                                        layoutManager.setStackFromEnd(true);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapter);

                                        emptyView = findViewById(R.id.search_by_ingredient_no_result);
                                        if (list.isEmpty()) {
                                            recyclerView.setVisibility(View.GONE);
                                            emptyView.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            recyclerView.setVisibility(View.VISIBLE);
                                            emptyView.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }

                                });

                            }
                        }else if(!ingredient1.equals("") && !ingredient2.equals("") && !ingredient3.equals("")){
                            if(ingredient_list.contains(ingredient1)&&ingredient_list.contains(ingredient2)&&ingredient_list.contains(ingredient3)){
                                RecipeData recipeInfo = child.getValue(RecipeData.class);
                                assert recipeInfo != null;
                                String recipe_name = recipeInfo.getRecipe_name();
                                String recipe_id = child.getKey();
                                int cooking_time = recipeInfo.getTime();
                                int serving = recipeInfo.getServing();
                                String image = recipeInfo.getImgId();
                                int rating = recipeInfo.getRating();
                                String chef = recipeInfo.getChef();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference mRef = database.getReference().child("UserInfo/" + chef);
                                mRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        String name = userSnapshot.child("userName").getValue(String.class);
                                        list.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                                        RecyclerView recyclerView = findViewById(R.id.recylerView_searches_ingredient);
                                        HomeListAdapter adapter = new HomeListAdapter( list);
                                        recyclerView.setHasFixedSize(true);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchIngredientResultActivity.this);
                                        layoutManager.setReverseLayout(true);
                                        layoutManager.setStackFromEnd(true);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setAdapter(adapter);

                                        emptyView = findViewById(R.id.search_by_ingredient_no_result);
                                        if (list.isEmpty()) {
                                            recyclerView.setVisibility(View.GONE);
                                            emptyView.setVisibility(View.VISIBLE);
                                        }
                                        else {
                                            recyclerView.setVisibility(View.VISIBLE);
                                            emptyView.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }

                                });

                            }
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}

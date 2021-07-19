package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchbyNameResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);

        ImageView imgBack = findViewById(R.id.back_from_search_by_name_result);
        imgBack.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        final String query = intent.getStringExtra("query");

        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Recipe");
            ref.orderByChild("rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<RecipeDataList> list = new ArrayList();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        RecipeData recipeInfo = child.getValue(RecipeData.class);
                        assert recipeInfo != null;
                        String recipe_name = recipeInfo.getRecipe_name();
                        if(recipe_name.toLowerCase().contains(query.toLowerCase())){
                            String recipe_id = child.getKey();
                            int cooking_time = recipeInfo.getTime();
                            int serving = recipeInfo.getServing();
                            String image = recipeInfo.getImgId();
                            int rating = recipeInfo.getRating();
                            String chef = recipeInfo.getChef();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference mRef = database.getReference().child("UserInfo/" + chef);
                            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                    String name = userSnapshot.child("userName").getValue(String.class);
                                    list.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                                    HomeListAdapter adapter = new HomeListAdapter(list);
                                    RecyclerView recyclerView = findViewById(R.id.recylerView_searches_name);
                                    recyclerView.setHasFixedSize(true);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(SearchbyNameResultActivity.this);
                                    layoutManager.setReverseLayout(true);
                                    layoutManager.setStackFromEnd(true);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(adapter);
                                    if(list.isEmpty()){
                                        TextView emptyView = findViewById(R.id.search_by_ingredient_no_result);
                                        recyclerView.setVisibility(View.GONE);
                                        emptyView.setVisibility(View.VISIBLE);
                                        Toast.makeText(SearchbyNameResultActivity.this, "No Result", Toast.LENGTH_LONG).show();
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

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });

        }


       final SearchView searchView = findViewById(R.id.search_by_name_result);
        searchView.setActivated(true);
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setQuery(query,false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                Intent intent = new Intent(SearchbyNameResultActivity.this, SearchbyNameResultActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }
}

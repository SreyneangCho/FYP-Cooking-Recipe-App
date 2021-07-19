package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeNoLoginFragment extends Fragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_home_no_login, null);

    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("KH-RECIPE");
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Recipe");
        ref.orderByChild("number_click").limitToLast(5).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RecipeDataList> trending_data_list = new ArrayList();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    RecipeData recipeInfo = child.getValue(RecipeData.class);
                    assert recipeInfo != null;
                    String recipe_id = child.getKey();
                    String recipe_name = recipeInfo.getRecipe_name();
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
                            trending_data_list.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                            RecyclerView recyclerView_trending = view.findViewById(R.id.recylerView_home_trending);
                            HomeListAdapter adapter_trending = new HomeListAdapter(trending_data_list);
                            recyclerView_trending.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setReverseLayout(true);
                            layoutManager.setStackFromEnd(true);
                            recyclerView_trending.setLayoutManager(layoutManager);
                            recyclerView_trending.setAdapter(adapter_trending);
                            recyclerView_trending.setNestedScrollingEnabled(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            throw databaseError.toException();
                        }

                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("Recipe").orderByKey().limitToLast(4);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RecipeDataList> new_recipe_data_list = new ArrayList();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    RecipeData recipeInfo = child.getValue(RecipeData.class);
                    assert recipeInfo != null;
                    String recipe_id = child.getKey();
                    String recipe_name = recipeInfo.getRecipe_name();
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
                            new_recipe_data_list.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                            RecyclerView recyclerView_trending = view.findViewById(R.id.recylerView_home_new_recipe);
                            HomeListAdapter adapter_trending = new HomeListAdapter(new_recipe_data_list);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setReverseLayout(true);
                            layoutManager.setStackFromEnd(true);
                            recyclerView_trending.setHasFixedSize(true);
                            recyclerView_trending.setLayoutManager(layoutManager);
                            recyclerView_trending.setAdapter(adapter_trending);
                            recyclerView_trending.setNestedScrollingEnabled(false);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            throw databaseError.toException();
                        }

                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
    }



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
        ref.orderByChild("number_click").limitToLast(4).addListenerForSingleValueEvent(new ValueEventListener() {
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
    /*@Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
      /*  DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("Recipe");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<RecipeDataList> tredingdatalist = new ArrayList();
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    RecipeDataList recipeInfo = child.getValue(RecipeDataList.class);
                    int recipe_id = recipeInfo.getRecipeId();
                    String recipe_name = recipeInfo.getRecipeName();
                    int cooking_time = recipeInfo.getTime();
                    int serving = recipeInfo.getServing();
                    int image = recipeInfo.getImgId();
                    int rating = recipeInfo.getRating();
                    String chef = recipeInfo.getChef();
                    tredingdatalist.add(new RecipeDataList(recipe_id, recipe_name, R.drawable.amok, cooking_time, serving, rating, chef));
                }
                Toast.makeText(getContext(), "recipe is "+ String.valueOf(tredingdatalist.size()), Toast.LENGTH_SHORT).show();
                RecyclerView recyclerView_trending = (RecyclerView) view.findViewById(R.id.recylerView_home_trending);
                HomeListAdapter adapter_trending = new HomeListAdapter(tredingdatalist);
                recyclerView_trending.setHasFixedSize(true);
                recyclerView_trending.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView_trending.setAdapter(adapter_trending);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        String url_new = "http://172.20.10.12/cooking_recipe_app/recipe_new.php";
        StringRequest stringRequest_new = new StringRequest(url_new, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<RecipeDataList> newdatalist = new ArrayList();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for(int i=0; i<result.length(); i++){
                        JSONObject jo = result.getJSONObject(i);
                        int recipeID = jo.getInt("recipe_id");
                        String recipe = jo.getString("recipe_name");
                        String chef = jo.getString("user_id");
                        int time = jo.getInt("cooking_time");
                        int serving = jo.getInt("number_serving");
                        int rating= jo.getInt("recipe_rating");
                        String img = jo.getString("image_path");
                        newdatalist.add(new RecipeDataList(recipeID, recipe, R.drawable.amok, time, serving, rating, chef));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RecyclerView recyclerView_new = (RecyclerView) view.findViewById(R.id.recylerView_home_new_recipe);
                HomeListAdapter adapter_new = new HomeListAdapter(newdatalist);
                recyclerView_new.setHasFixedSize(true);
                recyclerView_new.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView_new.setAdapter(adapter_new);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();;
            }
        });
        RequestQueue requestQueue_new = Volley.newRequestQueue(getActivity());
        requestQueue_new.add(stringRequest_new);

    }*/


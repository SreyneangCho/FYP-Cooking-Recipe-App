package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class HomeFragment extends Fragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_home, null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("KH-RECIPE");
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();

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
                            recyclerView_trending.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setReverseLayout(true);
                            layoutManager.setStackFromEnd(true);
                            recyclerView_trending.setLayoutManager(layoutManager);

                            HomeListAdapter adapter_trending = new HomeListAdapter(trending_data_list);
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
            }
        });

        PersonalizedRecommendation(view, userId);

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
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                            layoutManager.setReverseLayout(true);
                            layoutManager.setStackFromEnd(true);
                            recyclerView_trending.setHasFixedSize(true);
                            recyclerView_trending.setLayoutManager(layoutManager);


                            HomeListAdapter adapter_trending = new HomeListAdapter(new_recipe_data_list);
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
            }
        });
    }
    protected void PersonalizedRecommendation(View v, String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query_recipe_id = databaseReference.child("UserClick/" + userID + "/recipeID");
        query_recipe_id.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> recipeID = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String recipe_id = child.getKey();
                    recipeID.add(recipe_id);
                }
                Query query_search = databaseReference.child("Recipe");
                query_search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long recipe_num = dataSnapshot.getChildrenCount();
                        HashMap<String, Integer> ingredient_list = new HashMap<>();
                        HashMap<String, Integer> ingredient_all_list = new HashMap<>();
                        HashMap<String, Double> ingredient_idf = new HashMap<>();
                        ArrayList<String> ingredient_list_array = new ArrayList<>();
                        ArrayList<String> ingredient_all_list_array = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String key = child.getKey();
                            for (DataSnapshot ingredient : child.child("ingredient").getChildren()) {
                                String ingredient_name = ingredient.child("ingredient_name").getValue(String.class);
                                if (recipeID.contains(key)) {
                                    ingredient_list_array.add(ingredient_name);
                                }
                                ingredient_all_list_array.add(ingredient_name);
                            }
                        }

                        Set<String> st_user_ingredient = new HashSet<>(ingredient_list_array);
                        Set<String> st_all_ingredient = new HashSet<>(ingredient_all_list_array);

                        for (String s_user : st_user_ingredient)
                            ingredient_list.put(s_user, Collections.frequency(ingredient_list_array, s_user));
                        for (String s_all : st_all_ingredient)
                            ingredient_all_list.put(s_all, Collections.frequency(ingredient_all_list_array, s_all));

                        for(Map.Entry<String, Integer> entry : ingredient_all_list.entrySet()){
                            String ingredient_name = entry.getKey();
                            int ing_num = entry.getValue();
                            double idf = Math.log(recipe_num/ing_num) / Math.log(2);
                            ingredient_idf.put(ingredient_name, idf);
                        }

                        HashMap<String, Double> ingredient_idf_user = new HashMap<>();

                        for(Map.Entry<String, Integer> entry : ingredient_all_list.entrySet()){
                            String ingredient_name = entry.getKey();
                            int ing_num_all = entry.getValue();
                            double idf = 0;
                            if(ingredient_list.containsKey(ingredient_name)){
                                int ing_num = ingredient_list.get(ingredient_name);
                                idf = (1+(Math.log(ing_num) / Math.log(2)))*(Math.log(recipe_num/ing_num_all) / Math.log(2));
                            }else{
                                idf = 0;
                            }
                            ingredient_idf_user.put(ingredient_name, idf);
                        }

                        HashMap<String, Double> ingredient_recipe = new HashMap<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String key = child.getKey();
                            double total = 0;
                            double sum_ingredient_recipe = 0;
                            double sum_ingredient_user = 0;
                            ArrayList<String> recipe_ing_list = new ArrayList<>();
                            for (DataSnapshot ingredient : child.child("ingredient").getChildren()) {
                                String ingredient_name = ingredient.child("ingredient_name").getValue(String.class);
                                recipe_ing_list.add(ingredient_name);
                            }
                            HashMap<String, Double> ingredient_score = new HashMap<String, Double>();
                            for(Map.Entry<String, Double> entry : ingredient_idf.entrySet()){
                                String ingredient_name = entry.getKey();
                                double ing_num = 0;
                                if(recipe_ing_list.contains(ingredient_name)){
                                    ing_num = entry.getValue();
                                }else{
                                    ing_num = 0;
                                }
                                ingredient_score.put(ingredient_name, ing_num);
                            }
                            for(Map.Entry<String, Double> entry : ingredient_score.entrySet()){
                                String ingredient_name = entry.getKey();
                                double ingredient_tf_idf = entry.getValue();
                                double ingredient_tf_idf_user = ingredient_idf_user.get(ingredient_name);
                                double temp = ingredient_tf_idf*ingredient_tf_idf_user;
                                double temp1 = Math.pow(ingredient_tf_idf, 2);
                                double temp2 = Math.pow(ingredient_tf_idf_user, 2);
                                total += temp;
                                sum_ingredient_recipe += temp1;
                                sum_ingredient_user += temp2;
                            }

                            double recipe_score = total/(Math.sqrt(sum_ingredient_recipe)*Math.sqrt(sum_ingredient_user));
                            ingredient_recipe.put(key, recipe_score);

                        }

                        ArrayList<String> recipe_recommend = new ArrayList<>();

                        Map.Entry<String, Double> max = null;
                        for (Map.Entry<String, Double> entry : ingredient_recipe.entrySet()) {
                            if (max == null || max.getValue() < entry.getValue()) {
                                if(!recipeID.contains(entry.getKey())){
                                    max = entry;
                                }
                            }
                        }
                        assert max != null;
                        String recipe_code = max.getKey();
                        recipe_recommend.add(recipe_code);

                        Map.Entry<String, Double> max2 = null;
                        for (Map.Entry<String, Double> entry : ingredient_recipe.entrySet()) {
                            if (max2 == null || max2.getValue() < entry.getValue()) {
                                if(!recipeID.contains(entry.getKey()) && !recipe_code.equals(entry.getKey())){
                                    max2 = entry;
                                }
                            }
                        }
                        assert max2 != null;
                        String recipe_code2 = max2.getKey();
                        recipe_recommend.add(recipe_code2);

                        Map.Entry<String, Double> max3 = null;
                        for (Map.Entry<String, Double> entry : ingredient_recipe.entrySet()) {
                            if (max3 == null || max3.getValue() < entry.getValue()) {
                                if(!recipeID.contains(entry.getKey()) && !recipe_code.equals(entry.getKey()) && !recipe_code2.equals(entry.getKey())){
                                    max3 = entry;
                                }
                            }
                        }
                        assert max3 != null;
                        String recipe_code3 = max3.getKey();
                        recipe_recommend.add(recipe_code3);

                        Map.Entry<String, Double> max4 = null;
                        for (Map.Entry<String, Double> entry : ingredient_recipe.entrySet()) {
                            if (max4 == null || max4.getValue() < entry.getValue()) {
                                if(!recipeID.contains(entry.getKey()) && !recipe_code.equals(entry.getKey()) && !recipe_code2.equals(entry.getKey()) &&!recipe_code3.equals(entry.getKey())){
                                    max4 = entry;
                                }
                            }
                        }
                        assert max4 != null;
                        String recipe_code4 = max4.getKey();
                        recipe_recommend.add(recipe_code4);

                        ArrayList<RecipeDataList> list = new ArrayList();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            RecipeData recipeInfo = child.getValue(RecipeData.class);
                            String key = child.getKey();
                            assert key != null;
                            if (key.equals(recipe_code) || key.equals(recipe_code2) || key.equals(recipe_code3)||key.equals(recipe_code4)) {
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
                                        list.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                                        RecyclerView recyclerView = v.findViewById(R.id.recylerView_home_recommend);
                                        HomeListAdapter adapter = new HomeListAdapter(list);
                                        recyclerView.setHasFixedSize(true);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setNestedScrollingEnabled(false);

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

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

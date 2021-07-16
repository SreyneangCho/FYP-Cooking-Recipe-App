package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyRecipeFragment extends Fragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_my_recipe_main, null);


    }
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        getActivity().setTitle("My Recipe");
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();

        TextView txt_my_recipe_num = view.findViewById(R.id.my_reicpe_number);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = databaseReference.child("Recipe");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<RecipeDataList> list = new ArrayList();

                int i = 0;
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    RecipeData recipeInfo = child.getValue(RecipeData.class);
                    assert recipeInfo != null;
                    String chef = recipeInfo.getChef();
                    if(chef.equals(userId)){
                        i++;
                        String recipe_id = child.getKey();
                        String recipe_name = recipeInfo.getRecipe_name();
                        int cooking_time = recipeInfo.getTime();
                        int serving = recipeInfo.getServing();
                        String image = recipeInfo.getImgId();
                        int rating = recipeInfo.getRating();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = database.getReference().child("UserInfo/"+chef);
                        mRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                String name = userSnapshot.child("userName").getValue(String.class);
                                list.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recylerView);
                                MyRecipeListAdapter adapter = new MyRecipeListAdapter(list);
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                throw databaseError.toException();
                            }
                        });

                    }
                }
                txt_my_recipe_num.setText(String.valueOf(i));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });


        TextView txtAddRecipe = (TextView) view.findViewById(R.id.new_recipe);
        txtAddRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddRecipeActivity.class);
            startActivity(intent);
        });

    }
}

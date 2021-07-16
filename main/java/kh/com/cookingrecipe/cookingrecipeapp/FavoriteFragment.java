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

public class FavoriteFragment extends Fragment {

   @SuppressLint("InflateParams")
   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.activity_favorite_main, null);

   }

   public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
       getActivity().setTitle("Favorite");
       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       if (user != null) {
           String userId = user.getUid();
           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
           Query query_recipe_id = databaseReference.child("Favorite/" + userId + "/recipeID");
           query_recipe_id.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   ArrayList<String> recipeID = new ArrayList<>();
                   for (DataSnapshot child : dataSnapshot.getChildren()) {
                       String recipe_id = child.getKey();
                       recipeID.add(recipe_id);
                   }
                   Query query = databaseReference.child("Recipe");
                   query.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           ArrayList<RecipeDataList> favdatalist = new ArrayList();
                           for (DataSnapshot child : dataSnapshot.getChildren()) {
                               RecipeData recipeInfo = child.getValue(RecipeData.class);
                               String key = child.getKey();
                               if (recipeID.contains(key)) {
                                   assert recipeInfo != null;
                                   String recipe_id = child.getKey();
                                   String recipe_name = recipeInfo.getRecipe_name();
                                   int cooking_time = recipeInfo.getTime();
                                   int serving = recipeInfo.getServing();
                                   String image = recipeInfo.getImgId();
                                   int rating = recipeInfo.getRating();
                                   String chef = recipeInfo.getChef();
                                   FirebaseDatabase database = FirebaseDatabase.getInstance();
                                   DatabaseReference mRef = database.getReference().child("UserInfo/"+chef);
                                   mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                           String name = userSnapshot.child("userName").getValue(String.class);
                                           favdatalist.add(new RecipeDataList(recipe_id, recipe_name, image, cooking_time, serving, rating, name));
                                           RecyclerView recyclerView = view.findViewById(R.id.recylerView_favorite);
                                           FavoriteListAdapter adapter = new FavoriteListAdapter(favdatalist);
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

                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                           // Handle possible errors.
                       }
                   });
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {
                   // Handle possible errors.
               }
           });
       } else {
           throw new AssertionError();
       }
   }
      /* private String getUserName(String userId){
           FirebaseDatabase database = FirebaseDatabase.getInstance();
           DatabaseReference mRef = database.getReference().child("UserInfo/"+userId);
           final String name;
           mRef.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot userSnapshot) {
                   Toast.makeText(getContext(), userSnapshot.child("userName").getValue(String.class), Toast.LENGTH_SHORT).show();
                   String username = userSnapshot.child("userName").getValue(String.class);
                   setName(username);

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {
                   throw databaseError.toException();
               }
           });
           return getName();
       }*/
       /*super.onViewCreated(view, savedInstanceState);

       SharedPreferences preferences = getActivity().getSharedPreferences("mySharedPreferences", Context.MODE_PRIVATE);
       final String user_id = preferences.getString("user_id", "");

       String url = "http://172.20.10.12/cooking_recipe_app/user_favorite.php";

       RequestQueue queue = Volley.newRequestQueue(getActivity());

       StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               ArrayList<RecipeDataList> favdatalist = new ArrayList();
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
                       favdatalist.add(new RecipeDataList(recipeID, recipe, R.drawable.amok, time, serving, rating, chef));
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recylerView_favorite);
               FavoriteListAdapter adapter = new FavoriteListAdapter(favdatalist);
               recyclerView.setHasFixedSize(true);
               recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
               recyclerView.setAdapter(adapter);
           }
       }, new com.android.volley.Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {

               Toast.makeText(getActivity(), "Fail to get course" + error, Toast.LENGTH_SHORT).show();
           }
       }) {
           @Override
           public String getBodyContentType() {

               return "application/x-www-form-urlencoded; charset=UTF-8";
           }

           @Override
           protected Map<String, String> getParams() {

               Map<String, String> params = new HashMap<String, String>();

               params.put("user_id", user_id);

               return params;
           }
       };

       queue.add(request);*/


}

package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserReviewMainActivity extends AppCompatActivity {

   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);

        Intent intent = getIntent();

        final String recipe_id = intent.getStringExtra("recipeID");

       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
       Query query = databaseReference.child("UserReview/"+recipe_id);
       query.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               ArrayList<UserReviewDataList> list = new ArrayList();
               long num_review = dataSnapshot.getChildrenCount();
               TextView review_num = findViewById(R.id.number_review);
               review_num.setText(String.valueOf(num_review));
               for (DataSnapshot child: dataSnapshot.getChildren()) {
                       ReviewDataList review = child.getValue(ReviewDataList.class);
                       assert review != null;
                       String userID = review.getUserID();
                       String date = review.getReview_date();
                       String description = review.getReview_description();
                       String rating = review.getReview_rating();
                       @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                       @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM yyyy hh:mm");
                       try {
                           Date date2 = simpleDateFormat.parse(date);
                           assert date2 != null;
                           String format = simpleDateFormat2.format(date2);
                           assert rating != null;
                           int rating_int = Math.round(Float.parseFloat(rating));
                           FirebaseDatabase database = FirebaseDatabase.getInstance();
                           DatabaseReference mRef = database.getReference().child("UserInfo/"+userID);
                           mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                   String name = userSnapshot.child("userName").getValue(String.class);
                                   list.add(new UserReviewDataList(R.drawable.ic_account_circle_black_24dp,name,format, description, rating_int));
                                   RecyclerView recyclerView = findViewById(R.id.recylerView_user_review);
                                   UserReviewListAdapter adapter = new UserReviewListAdapter(list);
                                   recyclerView.setHasFixedSize(true);
                                   recyclerView.setLayoutManager(new LinearLayoutManager(UserReviewMainActivity.this));
                                   recyclerView.setAdapter(adapter);
                               }

                               @Override
                               public void onCancelled(@NonNull DatabaseError databaseError) {
                                   throw databaseError.toException();
                               }
                           });

                       } catch (ParseException e) {
                           e.printStackTrace();
                       }
                   }


               }


           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }

       });
       Query query_recipe = databaseReference.child("Recipe/"+recipe_id);
       query_recipe.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               RecipeDataList recipeInfo = dataSnapshot.getValue(RecipeDataList.class);
               assert recipeInfo != null;
               int rating = recipeInfo.getRating();
               TextView recipe_rating= findViewById(R.id.review_rating);
               recipe_rating.setText(String.valueOf(rating));

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

       ImageView imgReviewBack = findViewById(R.id.user_review_back);
       imgReviewBack.setOnClickListener(v -> finish());

       Button btnAddNewReview = findViewById(R.id.btn_addnewreview);
       btnAddNewReview.setOnClickListener(v -> {
           FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
           assert user != null;
           if (user.isAnonymous()) {
               Toast.makeText(UserReviewMainActivity.this, "Login to post your review.", Toast.LENGTH_LONG).show();
           } else{
               String userId = user.getUid();
               Query query_recipe1 = databaseReference.child("Recipe/" + recipe_id);
               query_recipe1.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       RecipeDataList recipeInfo = dataSnapshot.getValue(RecipeDataList.class);
                       if (!userId.equals("")) {
                           assert recipeInfo != null;
                           String chef = recipeInfo.getChef();
                           if (!userId.equals(chef)) {
                               Intent intent1 = new Intent(v.getContext(), AddNewReviewActivity.class);
                               intent1.putExtra("recipeID", recipe_id);
                               startActivity(intent1);
                           } else {
                               Toast.makeText(UserReviewMainActivity.this, "You can not add review to your own recipe.", Toast.LENGTH_LONG).show();
                           }

                       } else {
                           AlertDialog alertDialog = new AlertDialog.Builder(UserReviewMainActivity.this).create();
                           alertDialog.setTitle("Login");
                           alertDialog.setMessage("To Add Your Review");
                           alertDialog.show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });


       }
   });
    }

}

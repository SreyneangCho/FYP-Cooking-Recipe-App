package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddNewReviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_add_new_review);

        ImageView imgAddReviewBack = findViewById(R.id.add_new_review_back);
        imgAddReviewBack.setOnClickListener(v -> finish());

        Intent intent = getIntent();
        final String recipe_id = intent.getStringExtra("recipeID");

        Button submitButton = findViewById(R.id.btn_submitnewreview);
        submitButton.setOnClickListener(v -> addUserReview(recipe_id));

}
private void addUserReview(String recipeID){
    final RatingBar simpleRatingBar = findViewById(R.id.new_review_star_rating);
    String star_rating = String.valueOf(simpleRatingBar.getRating());

    EditText etxt_review = findViewById(R.id.etxt_review);
    String review = etxt_review.getText().toString();

            if(review.equals("")) {
                Toast.makeText(AddNewReviewActivity.this, "Add your review.", Toast.LENGTH_SHORT).show();
            }else {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String userId = user.getUid();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());

                ReviewDataList list = new ReviewDataList(userId, review, star_rating, format);

                DatabaseReference mRef = database.getReference().child("UserReview/" + recipeID).push();

                mRef.setValue(list);

                finish();

            }

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Query query = databaseReference.child("UserReview/"+recipeID);
    query.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            long num_review = dataSnapshot.getChildrenCount();
            int number_review = Math.round(num_review);
            int total_rating = 0;
            for (DataSnapshot child: dataSnapshot.getChildren()) {
                ReviewDataList review = child.getValue(ReviewDataList.class);
                assert review != null;
                String rating = review.getReview_rating();
                int rating_int = Math.round(Float.parseFloat(rating));
                total_rating += rating_int;
            }
            if(num_review!=0){
                int final_rating = total_rating/number_review;
                DatabaseReference updateData = FirebaseDatabase.getInstance().getReference("Recipe").child(recipeID);
                updateData.child("rating").setValue(final_rating);
            }

        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    });
}

}

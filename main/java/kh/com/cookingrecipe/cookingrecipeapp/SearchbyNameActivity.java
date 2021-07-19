package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchbyNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name_main);

        LinearLayout app_layer = findViewById (R.id.toSearchIngredient);
        app_layer.setOnClickListener(v -> {
            Intent intent = new Intent(SearchbyNameActivity.this, SearchbyIngredient.class);
            startActivity(intent);
        });

        ImageView imgBack = findViewById(R.id.back_from_search_to_home);
        imgBack.setOnClickListener(v -> finish());

        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            Query query = databaseReference.child("Search_history/" + userId).orderByKey().limitToLast(10);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<RecentSearchDataList> list = new ArrayList();
                    ArrayList<RecentSearchDataList> new_list = new ArrayList();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String query = child.getValue(String.class);
                        list.add(new RecentSearchDataList(query));
                    }
                    for(int n = list.size()-1 ; n >= 0 ; n--){
                        String data = list.get(n).getQuery();
                        new_list.add(new RecentSearchDataList(data));
                    }
                    RecyclerView recyclerView = findViewById(R.id.recylerView_searches_name_history);
                    RecentSearchListAdapter adapter = new RecentSearchListAdapter(new_list);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SearchbyNameActivity.this));
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });


        SearchView searchView = findViewById(R.id.search_by_name);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String query) {
                if( !user.isAnonymous() ) {
                    String userId = user.getUid();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    Query query_search = databaseReference.child("Search_history/"+userId);
                    DatabaseReference mRef =  databaseReference.child("Search_history/"+userId).push();
                    query_search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot child: dataSnapshot.getChildren()) {
                                    String query_search = child.getValue(String.class).toLowerCase();
                                    String query_lowercase = query.toLowerCase();
                                    String query_key = child.getKey();
                                    if(query_search.equals(query_lowercase)){
                                        DatabaseReference mRef1 =  databaseReference.child("Search_history/"+userId+"/"+query_key);
                                        mRef1.removeValue();
                                    }
                                    mRef.setValue(query);
                                }
                            }else{
                                mRef.setValue(query);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle possible errors.
                        }
                    });

                }

                Intent intent = new Intent(SearchbyNameActivity.this, SearchbyNameResultActivity.class);
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
}
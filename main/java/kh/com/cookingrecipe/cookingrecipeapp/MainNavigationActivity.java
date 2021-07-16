package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainNavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    private boolean loadFragment(Fragment fragment) {

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.search_icon, menu);
            return true;
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            Intent a = new Intent(MainNavigationActivity.this, SearchbyNameActivity.class);
            startActivity(a);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            if(user.isAnonymous()){
                loadFragment(new HomeNoLoginFragment());
            }else{
                loadFragment(new HomeFragment());
            }

            BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            if(user.isAnonymous()){
                                loadFragment(new HomeNoLoginFragment());
                            }else{
                                loadFragment(new HomeFragment());
                            }
                            break;
                        case R.id.navigation_favorite:
                            if(user.isAnonymous()){
                                loadFragment(new FavoriteNoLoginFragment());
                            }else{
                                loadFragment(new FavoriteFragment());
                            }

                            break;
                        case R.id.navigation_myrecipe:
                            if(user.isAnonymous()){
                                loadFragment(new MyRecipeNoLoginFragment());
                            }else{
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String userId = user.getUid();
                                DatabaseReference mRef = database.getReference().child("UserInfo/"+userId);
                                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                        String user_role = userSnapshot.child("userRole").getValue(String.class);
                                        assert user_role != null;
                                        if(user_role.equals("student")){
                                            loadFragment(new MyRecipeStudentFragment());
                                        }else {
                                            loadFragment(new MyRecipeFragment());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }
                                });
                            }

                            break;
                        case R.id.navigation_myaccount:
                            if(user.isAnonymous()){
                                loadFragment(new MyAccountNoLoginFragment());
                            }else{
                                loadFragment(new MyAccountFragment());
                            }
                            break;
                    }
                    return true;
                }
            });
        }


    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

   public void signout(){
       FirebaseAuth.getInstance().signOut();
       startActivity(new Intent(MainNavigationActivity.this, LoginActivity.class));
       finish();

   }

}

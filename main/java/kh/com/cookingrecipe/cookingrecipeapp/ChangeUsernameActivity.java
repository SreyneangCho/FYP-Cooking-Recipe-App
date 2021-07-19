package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

public class ChangeUsernameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);

        TextView txtCancel = findViewById(R.id.cancel_change_username);
        txtCancel.setOnClickListener(v -> finish());
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query_userinfo = databaseReference.child("UserInfo/"+userId);
        query_userinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("userName").getValue(String.class);
                TextView name = findViewById(R.id.change_username_username);
                name.setText(username);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        EditText name = findViewById(R.id.etxt_new_username);

        Button btnChangeUsername = findViewById(R.id.btn_change_username);
        btnChangeUsername.setOnClickListener(v -> {
            final String new_username = name.getText().toString();
            if(!new_username.equals("")){
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mRef = database.getReference().child("UserInfo");
                mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                        ArrayList<String> username_list = new ArrayList<>();
                        String username_lowercase = new_username.toLowerCase();
                        for(DataSnapshot child : userSnapshot.getChildren()){
                            String username = child.child("userName").getValue(String.class);
                            username_list.add(username);
                        }
                        if(username_list.contains(username_lowercase)){
                            Toast.makeText(ChangeUsernameActivity.this, "This username already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            DatabaseReference mRef1 =  databaseReference.child("UserInfo/"+userId);
                            mRef1.child("userName").setValue(new_username);
                            Intent intent = new Intent();
                            setResult(1,intent);
                            finish();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        throw databaseError.toException();
                    }

                });

            }else {
                Toast.makeText(ChangeUsernameActivity.this, "Input You Username", Toast.LENGTH_LONG).show();
            }

        });
    }
}

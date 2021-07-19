package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    EditText name, email, password;
    String str_name, str_email, str_password;
    String user_role;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Intent intent = getIntent();
        user_role = intent.getStringExtra("user_role");

        name = findViewById(R.id.etxt_signup_username);
        email = findViewById(R.id.etxt_signup_email);
        password = findViewById(R.id.etxt_signup_password);

       final CheckBox show_password = findViewById(R.id.checkbox_show_password_signup);

        show_password.setOnClickListener(v -> {
            if(show_password.isChecked()){
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        Button btn_signup = findViewById(R.id.btn_signup_signup);
        btn_signup.setOnClickListener(v -> {
            str_name = name.getText().toString();
            str_password = password.getText().toString();
            str_email = email.getText().toString();

            UserProfile userProfile = new UserProfile(str_name, user_role);

            if (TextUtils.isEmpty(str_email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(str_password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                return;
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mRef = database.getReference().child("UserInfo");
            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    ArrayList<String> username_list = new ArrayList<>();
                    String username_lowercase = str_name.toLowerCase();
                    for(DataSnapshot child : userSnapshot.getChildren()){
                        String username = child.child("userName").getValue(String.class);
                        username_list.add(username);
                    }
                    if(username_list.contains(username_lowercase)){
                        Toast.makeText(SignupActivity.this, "This username already exists.",
                                Toast.LENGTH_SHORT).show();
                    }else{
                        DatabaseReference mRef1 =  database.getReference().child("UserInfo");
                        auth = FirebaseAuth.getInstance();
                        auth.createUserWithEmailAndPassword(str_email, str_password)
                                .addOnCompleteListener(SignupActivity.this, task -> {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignupActivity.this, "This email address is already in use by another user. ",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                                        assert user != null;
                                        String userId = user.getUid();
                                        mRef1.child(userId).setValue(userProfile);
                                        sendVerificationEmail();

                                    }
                                });
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    throw databaseError.toException();
                }

            });


        });
    }

    public void SignUp_Login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(SignupActivity.this, "Please Sign up Again", Toast.LENGTH_LONG).show();
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());

            }
        });
    }

}


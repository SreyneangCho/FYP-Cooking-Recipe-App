package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    EditText UsernameEt, PasswordEt;
    private FirebaseAuth auth;

    FirebaseDatabase firebaseDatabase;
    UserProfile userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            finish();
       }else{
            UsernameEt = (EditText)findViewById(R.id.etxt_login_username);
            PasswordEt = (EditText)findViewById(R.id.etxt_login_password);

            final CheckBox show_password = (CheckBox)findViewById(R.id.checkbox_show_password);

            show_password.setOnClickListener(v -> {
                if(show_password.isChecked()){
                    PasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    PasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            });

            Button btn_login = (Button) findViewById(R.id.btn_login_login);
            btn_login.setOnClickListener(v -> {
                String email = UsernameEt.getText().toString();
                final String password = PasswordEt.getText().toString();
                firebaseDatabase = FirebaseDatabase.getInstance();
                userInfo = new UserProfile();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Incorrect Password or Email.", Toast.LENGTH_LONG).show();
                            } else {
                                checkIfEmailVerified();
                            }
                        });
            });
        }



    }
    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if (user.isEmailVerified()) {
            Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(LoginActivity.this,
                    "Please Verify your account by clicking the link that was sent to your email.",
                        Toast.LENGTH_LONG).show();

            FirebaseAuth.getInstance().signOut();
            recreate();
        }
    }

    public void onCreateAccount(View view) {
        Intent intent = new Intent(this, SignUpAsActivity.class);
        this.startActivity(intent);
    }

    public void onSkipLogin(View view) {
        auth.signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(LoginActivity.this, MainNavigationActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onForgetPassword(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        this.startActivity(intent);
    }
}

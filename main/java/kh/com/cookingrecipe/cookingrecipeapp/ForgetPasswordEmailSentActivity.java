package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordEmailSentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_sent_email);

        Button btnlogin = findViewById(R.id.btn_back_to_login_from_forget_password);

        btnlogin.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ForgetPasswordEmailSentActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
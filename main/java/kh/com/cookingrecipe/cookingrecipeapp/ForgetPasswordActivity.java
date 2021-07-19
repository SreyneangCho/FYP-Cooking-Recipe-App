package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText edtEmail;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        edtEmail = findViewById(R.id.etxt_email_forget_password);
        Button btnResetPassword = findViewById(R.id.btn_forget_password_send_email);
        Button btnBack = findViewById(R.id.btn_forget_password_cancel);

        mAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(v -> {

            String email = edtEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter your email!", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener( task -> {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(ForgetPasswordActivity.this, ForgetPasswordEmailSentActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "Fail to send reset password email!", Toast.LENGTH_SHORT).show();
                }
            });

        });

        btnBack.setOnClickListener(v -> finish());

    }
}
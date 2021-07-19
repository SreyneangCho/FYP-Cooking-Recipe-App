package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SignUpAsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_as);

        TextView txt_role_chef = findViewById(R.id.txt_signupas_chef);

        txt_role_chef.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpAsActivity.this, SignupActivity.class);
            intent.putExtra("user_role", "chef");
            startActivity(intent);
        });

        TextView txt_role_student = findViewById(R.id.txt_signupas_student);

        txt_role_student.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpAsActivity.this, SignupActivity.class);
            intent.putExtra("user_role", "student");
            startActivity(intent);
        });
    }
}

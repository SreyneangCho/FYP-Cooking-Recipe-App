package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextView txtCancel = findViewById(R.id.cancel_edit_profile);
        txtCancel.setOnClickListener(v -> finish());

        final TextView txtChangeUsername = findViewById(R.id.my_account_change_username);
        final TextView txtChangePassword = findViewById(R.id.my_account_change_password);

        txtChangeUsername.setOnClickListener(v -> {
            Intent myIntent = new Intent(EditProfileActivity.this, ChangeUsernameActivity.class);
            startActivity(myIntent);
        });

        txtChangePassword.setOnClickListener(v -> {
            Intent intent = new Intent(EditProfileActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });
    }
}

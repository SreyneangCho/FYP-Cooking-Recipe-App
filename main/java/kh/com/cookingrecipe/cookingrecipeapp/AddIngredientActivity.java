package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AddIngredientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        final EditText etxt_name = findViewById(R.id.etxt_ingredient_name);
        final EditText etxt_quantity = findViewById(R.id.etxt_ingredient_quantity);
        Button button_add = findViewById(R.id.btn_add_ingredient);
        button_add.setOnClickListener(v -> {
            String name = etxt_name.getText().toString();
            String quantity = etxt_quantity.getText().toString();
            if (name.matches("")){
                AlertDialog alertDialog = new AlertDialog.Builder(AddIngredientActivity.this).create();
                alertDialog.setTitle("Forget:");
                alertDialog.setMessage("Input Name of Ingredient!");
                alertDialog.show();
            }else {
                Intent intent = new Intent();
                intent.putExtra("Ingredient_Name", name);
                intent.putExtra("Ingredient_Quantity", quantity);
                intent.putExtra("Status", "Add New Ingredient");
                setResult(2, intent);
                finish();
            }
        });

        ImageView imgCancel = findViewById(R.id.cancel_ingredient);
        imgCancel.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("Status", "Cancel New Ingredient");
            setResult(2, intent);
            finish();
        });
    }
}

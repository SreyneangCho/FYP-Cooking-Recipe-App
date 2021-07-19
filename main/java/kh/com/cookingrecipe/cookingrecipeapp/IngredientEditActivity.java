package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class IngredientEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_edit);

        Intent getIntent = getIntent();
        String ingredient = getIntent.getStringExtra("ingredient");
        String quantity = getIntent.getStringExtra("quantity");
        String position = getIntent.getStringExtra("position");

        final EditText etxt_name = findViewById(R.id.edit_ingredient_name);
        final EditText etxt_quantity = findViewById(R.id.edit_ingredient_quantity);

        etxt_name.setText(ingredient);
        etxt_quantity.setText(quantity);

        Button button_add = findViewById(R.id.btn_edit_ingredient);
        button_add.setOnClickListener(v -> {
            String name = etxt_name.getText().toString();
            String quantity1 = etxt_quantity.getText().toString();
            if (name.matches("")){
                AlertDialog alertDialog = new AlertDialog.Builder(IngredientEditActivity.this).create();
                alertDialog.setTitle("Forget:");
                alertDialog.setMessage("Input Name of Ingredient!");
                alertDialog.show();
            }else {
                Intent intent = new Intent();
                intent.putExtra("Ingredient_Name", name);
                intent.putExtra("Ingredient_Quantity", quantity1);
                intent.putExtra("Status", "Edited Ingredient");
                intent.putExtra("position", position);
                setResult(1000, intent);
                finish();
            }
        });

        ImageView imgCancel = findViewById(R.id.cancel_ingredient);
        imgCancel.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("Status", "Cancel Edit Ingredient");
            setResult(100, intent);
            finish();
        });
    }
}
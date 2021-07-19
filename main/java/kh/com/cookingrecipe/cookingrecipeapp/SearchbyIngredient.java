package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class SearchbyIngredient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_ingredient);

        ImageView img_back = findViewById(R.id.back_from_search_by_ingredient);
        img_back.setOnClickListener(v -> finish());

        final EditText etxt_ingredient1 = findViewById(R.id.etxt_first_ingredient);
        final EditText etxt_ingredient2 = findViewById(R.id.etxt_second_ingredient);
        final EditText etxt_ingredient3 = findViewById(R.id.etxt_third_ingredient);

        Button mButton = findViewById(R.id.btn_find_recipe_by_ingredient);
        mButton.setOnClickListener(v -> {
            final String ingredient1 = etxt_ingredient1.getText().toString();
            final String ingredient2 = etxt_ingredient2.getText().toString();
            final String ingredient3 = etxt_ingredient3.getText().toString();
            if(ingredient1.equals("")&&ingredient2.equals("")&&ingredient3.equals("")){
                AlertDialog alertDialog = new AlertDialog.Builder(SearchbyIngredient.this).create();
                alertDialog.setTitle("Forget Input:");
                alertDialog.setMessage("Enter At Least ONE Ingredient!");
                alertDialog.show();
            }else{
                Intent intent = new Intent(SearchbyIngredient.this, SearchIngredientResultActivity.class);
                intent.putExtra("ingredient1", ingredient1);
                intent.putExtra("ingredient2", ingredient2);
                intent.putExtra("ingredient3", ingredient3);
                startActivity(intent);
            }

        });

    }
}

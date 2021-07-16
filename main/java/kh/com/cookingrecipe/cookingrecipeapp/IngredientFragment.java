package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class IngredientFragment extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);

        final String recipe_id = "1";
        showIngredient(recipe_id);
        final TextView IngredientClick = (TextView) findViewById(R.id.ingredient_ingredient_click);
        final TextView txtInstruction = (TextView) findViewById(R.id.instruction_click);
        txtInstruction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                txtInstruction.setTextColor(Color.parseColor("#2195B1"));
                IngredientClick.setTextColor(Color.parseColor("#000000"));

            }
        });
        ImageView imgIngredientBack = (ImageView) findViewById(R.id.ingredient_back);
        imgIngredientBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        IngredientClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtInstruction.setTextColor(Color.parseColor("#000000"));
                IngredientClick.setTextColor(Color.parseColor("#2195B1"));
                showIngredient(recipe_id);
            }
        });


    }
    private void showIngredient(final String recipe_id){
    }
}

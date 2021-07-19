package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecipeIngredientListAdapter extends RecyclerView.Adapter<MyRecipeIngredientListAdapter.ViewHolder> {

    private final ArrayList<RecipeIngredientDataList> myrecipeIngredientDataLists;

    public MyRecipeIngredientListAdapter(java.util.ArrayList<RecipeIngredientDataList> myrecipeIngredientDataLists) {
        this.myrecipeIngredientDataLists = myrecipeIngredientDataLists;
    }
    @NonNull
    @Override
    public MyRecipeIngredientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_list_ingredient_edit, parent, false);
        MyRecipeIngredientListAdapter.ViewHolder viewHolder = new MyRecipeIngredientListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecipeIngredientListAdapter.ViewHolder holder, final int position) {
        final RecipeIngredientDataList myListData = myrecipeIngredientDataLists.get(position);
        holder.ingredientView.setText(myrecipeIngredientDataLists.get(position).getIngredient());
        holder.numberView.setText(String.valueOf(myrecipeIngredientDataLists.get(position).getNumber()));
        holder.quantityView.setText(myrecipeIngredientDataLists.get(position).getQuantity());
        holder.imgView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), IngredientEditActivity.class);

            String ingredient = String.valueOf(myrecipeIngredientDataLists.get(position).getIngredient());
            String quantity = String.valueOf(myrecipeIngredientDataLists.get(position).getQuantity());
            intent.putExtra("ingredient", ingredient);
            intent.putExtra("quantity", quantity);
            intent.putExtra("position", String.valueOf(myListData.getNumber()));

            ((Activity) v.getContext()).startActivityForResult(intent, 1000);
        });


    }
    @Override
    public int getItemCount() {
        return myrecipeIngredientDataLists.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView numberView;
        public TextView ingredientView;
        public TextView quantityView;
        public RelativeLayout relativeLayout;
        public ImageView imgView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.numberView = itemView.findViewById(R.id.ingredient_step);
            this.ingredientView = itemView.findViewById(R.id.ingredient_name);
            this.quantityView = itemView.findViewById(R.id.ingredient_quantity);
            this.imgView = itemView.findViewById(R.id.ingredient_edit);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_ingredient);
        }
    }
}
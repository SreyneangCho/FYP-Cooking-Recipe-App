package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeIngredientListAdapter extends RecyclerView.Adapter<RecipeIngredientListAdapter.ViewHolder> {

    private final ArrayList<RecipeIngredientDataList> recipeIngredientDataLists;

    public RecipeIngredientListAdapter(ArrayList<RecipeIngredientDataList> recipeIngredientDataLists) {
        this.recipeIngredientDataLists = recipeIngredientDataLists;
    }
    @NonNull
    @Override
    public RecipeIngredientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_list_ingredient, parent, false);
        RecipeIngredientListAdapter.ViewHolder viewHolder = new RecipeIngredientListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeIngredientListAdapter.ViewHolder holder, final int position) {
        holder.ingredientView.setText(recipeIngredientDataLists.get(position).getIngredient());
        holder.numberView.setText(String.valueOf(recipeIngredientDataLists.get(position).getNumber()));
        holder.quantityView.setText(recipeIngredientDataLists.get(position).getQuantity());

    }
    @Override
    public int getItemCount() {
        return recipeIngredientDataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView numberView;
        public TextView ingredientView;
        public TextView quantityView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.numberView = itemView.findViewById(R.id.ingredient_step);
            this.ingredientView = itemView.findViewById(R.id.ingredient_name);
            this.quantityView = itemView.findViewById(R.id.ingredient_quantity);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_ingredient);
        }
    }
}

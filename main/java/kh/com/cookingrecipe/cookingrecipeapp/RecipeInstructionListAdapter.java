package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeInstructionListAdapter extends RecyclerView.Adapter<RecipeInstructionListAdapter.ViewHolder>  {

    private final ArrayList<RecipeInstructionDataList> recipeInstructionDataLists;

    public RecipeInstructionListAdapter(ArrayList<RecipeInstructionDataList> recipeInstructionDataLists) {
        this.recipeInstructionDataLists= recipeInstructionDataLists;
    }
    @NonNull
    @Override
    public RecipeInstructionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_list_instruction, parent, false);
        RecipeInstructionListAdapter.ViewHolder viewHolder = new RecipeInstructionListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeInstructionListAdapter.ViewHolder holder, int position) {
        holder.nameView.setText(recipeInstructionDataLists.get(position).getName());
        holder.stepView.setText(String.valueOf(recipeInstructionDataLists.get(position).getStep()));
        holder.detailView.setText(recipeInstructionDataLists.get(position).getDetail());
    }
    @Override
    public int getItemCount() {
        return recipeInstructionDataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView stepView;
        public TextView detailView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.nameView = itemView.findViewById(R.id.instruction_step_name);
            this.stepView = itemView.findViewById(R.id.instruction_step);
            this.detailView = itemView.findViewById(R.id.instruction_step_detail);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_instruction);
        }
    }
}

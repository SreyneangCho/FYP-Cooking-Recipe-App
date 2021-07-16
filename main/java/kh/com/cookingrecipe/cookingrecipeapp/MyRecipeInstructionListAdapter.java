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

public class MyRecipeInstructionListAdapter extends RecyclerView.Adapter<MyRecipeInstructionListAdapter.ViewHolder>  {

    private final ArrayList<RecipeInstructionDataList> recipeInstructionDataLists;

    public MyRecipeInstructionListAdapter(ArrayList<RecipeInstructionDataList> recipeInstructionDataLists) {
        this.recipeInstructionDataLists= recipeInstructionDataLists;
    }
    @NonNull
    @Override
    public MyRecipeInstructionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_list_instruction_edit, parent, false);
        MyRecipeInstructionListAdapter.ViewHolder viewHolder = new MyRecipeInstructionListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyRecipeInstructionListAdapter.ViewHolder holder, int position) {
        final RecipeInstructionDataList myListData = recipeInstructionDataLists.get(position);
        holder.nameView.setText(recipeInstructionDataLists.get(position).getName());
        holder.stepView.setText(String.valueOf(recipeInstructionDataLists.get(position).getStep()));
        holder.detailView.setText(recipeInstructionDataLists.get(position).getDetail());
        holder.imgView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), InstructionEditActivity.class);

            String instruction = String.valueOf(recipeInstructionDataLists.get(position).getDetail());
            String name = String.valueOf(recipeInstructionDataLists.get(position).getName());
            intent.putExtra("instruction", instruction);
            intent.putExtra("step", name);
            intent.putExtra("position", String.valueOf(myListData.getStep()));

            ((Activity) v.getContext()).startActivityForResult(intent, 1001);
        });

    }
    @Override
    public int getItemCount() {
        return recipeInstructionDataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameView;
        public TextView stepView;
        public TextView detailView;
        public ImageView imgView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.nameView = (TextView) itemView.findViewById(R.id.instruction_step_name);
            this.stepView = (TextView) itemView.findViewById(R.id.instruction_step);
            this.detailView = (TextView) itemView.findViewById(R.id.instruction_step_detail);
            this.imgView = (ImageView) itemView.findViewById(R.id.instruction_edit);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout_instruction);
        }
    }
}
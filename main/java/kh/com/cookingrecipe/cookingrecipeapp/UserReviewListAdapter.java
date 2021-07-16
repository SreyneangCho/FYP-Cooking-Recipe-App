package kh.com.cookingrecipe.cookingrecipeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserReviewListAdapter extends RecyclerView.Adapter<UserReviewListAdapter.ViewHolder> {

    private final ArrayList<UserReviewDataList> userReviewDataLists ;

    public UserReviewListAdapter(ArrayList<UserReviewDataList> userReviewDataLists) {
        this.userReviewDataLists = userReviewDataLists;
    }
    @NonNull
    @Override
    public UserReviewListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.activity_list_user_review, parent, false);
        UserReviewListAdapter.ViewHolder viewHolder = new UserReviewListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UserReviewListAdapter.ViewHolder holder, int position) {
        holder.imgView.setImageResource(userReviewDataLists.get(position).getImgId());
        holder.nameView.setText(userReviewDataLists.get(position).getName());
        holder.ratingView.setText(String.valueOf(userReviewDataLists.get(position).getRating()));
        holder.dateView.setText(String.valueOf(userReviewDataLists.get(position).getDate()));
        holder.descriptionView.setText(userReviewDataLists.get(position).getDescription());
    }
    @Override
    public int getItemCount() {
        return userReviewDataLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgView;
        public TextView nameView;
        public TextView ratingView;
        public TextView dateView;
        public TextView descriptionView;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imgView = (ImageView) itemView.findViewById(R.id.review_img);
            this.nameView = (TextView) itemView.findViewById(R.id.review_name);
            this.ratingView = (TextView) itemView.findViewById(R.id.review_rating);
            this.dateView = (TextView) itemView.findViewById(R.id.review_date);
            this.descriptionView= (TextView) itemView.findViewById(R.id.review_description);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout_user_review);
        }
    }
}

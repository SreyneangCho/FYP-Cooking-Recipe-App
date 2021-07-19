package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentSearchListAdapter extends RecyclerView.Adapter<RecentSearchListAdapter.ViewHolder> {

    private final ArrayList<RecentSearchDataList> listdata;

    public RecentSearchListAdapter(ArrayList<RecentSearchDataList> listdata) {
        this.listdata = listdata;
    }

    @NonNull
    public RecentSearchListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.activity_recent_search_name, parent, false);
        RecentSearchListAdapter.ViewHolder viewHolder = new RecentSearchListAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecentSearchListAdapter.ViewHolder holder, final int position) {

        holder.query.setText(listdata.get(position).getQuery());

        holder.imgDelete.setOnClickListener(v -> {
            FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                Query query_search = databaseReference.child("Search_history/" + userId);
                DatabaseReference mRef = databaseReference.child("Search_history/" + userId);
                query_search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String query_search = child.getValue(String.class);
                            String search_id = child.getKey();
                            assert query_search != null;
                            if (query_search.equals(listdata.get(position).getQuery())) {
                                assert search_id != null;
                                mRef.child(search_id).removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle possible errors.
                    }
                });
            }
        });

        holder.relativeLayout.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SearchbyNameResultActivity.class);

            String query = listdata.get(position).getQuery();
            intent.putExtra("query", query);

            v.getContext().startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgDelete;
        public TextView query;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imgDelete = itemView.findViewById(R.id.recent_search_delete);
            this.query = itemView.findViewById(R.id.recent_search);
            relativeLayout = itemView.findViewById(R.id.relativeLayout_recent_searches);

        }

    }
}
package kh.com.cookingrecipe.cookingrecipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyAccountFragment extends Fragment {

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_my_account, null);

    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("My Account");

        super.onViewCreated(view, savedInstanceState);

        final TextView txtEditProfile = view.findViewById(R.id.my_account_edit_profile);

        txtEditProfile.setOnClickListener(v -> {
            Intent myIntent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(myIntent);
        });


        final TextView txtLogout = view.findViewById(R.id.my_account_logout);



        txtLogout.setOnClickListener(v -> ((MainNavigationActivity) getActivity()).signout());


        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userId = user.getUid();
        String user_email = user.getEmail();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query query_userinfo = databaseReference.child("UserInfo/"+userId);
        query_userinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("userName").getValue(String.class);
                TextView name = view.findViewById(R.id.my_account_name);
                name.setText(username);
                TextView email = view.findViewById(R.id.my_account_email);
                email.setText(user_email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}

package com.example.sprintv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private UserAdapter userAdapter;
    private UserAdapter.OnUserClickListener onUserClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        users = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);

        onUserClickListener = new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClicked(int position) {
                startActivity(new Intent(FriendListActivity.this, MessageActivity.class)
                        .putExtra("username_of_mate",users.get(position).getUsername())
                        .putExtra("email_of_mate", users.get(position).getEmail()));
                Toast.makeText(FriendListActivity.this, "Clicked on user", Toast.LENGTH_SHORT).show();
            }
        };

        getUsers();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.profile){
            startActivity(new Intent(FriendListActivity.this, ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUsers(){

        FirebaseDatabase.getInstance().getReference("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        if(dataSnapshot.getValue(User.class).getEmail().equals(user.getEmail())){

                        }else{
                            users.add(dataSnapshot.getValue(User.class));
                        }

                    }



                userAdapter = new UserAdapter(users,FriendListActivity.this, onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendListActivity.this));
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
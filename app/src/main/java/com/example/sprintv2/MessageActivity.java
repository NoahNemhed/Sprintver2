package com.example.sprintv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText editText;
    private TextView chattingWith;
    private ImageView sendMsg;

    private ArrayList<Message> messageArrayList;

    private MessageAdapter messageAdapter;
    String usernameOfRoommate, emailOfRoommate, roomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        usernameOfRoommate = getIntent().getStringExtra("username_of_mate");
        emailOfRoommate = getIntent().getStringExtra("email_of_mate");

        recyclerView = findViewById(R.id.recyclerMessages);

        editText = findViewById(R.id.edtText);

        chattingWith = findViewById(R.id.chattingWith);

        sendMsg = findViewById(R.id.sendMsgView);

        chattingWith.setText("Chatting with - " + usernameOfRoommate);

        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("messages/"+roomId)
                        .push()
                        .setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(),emailOfRoommate,editText.getText().toString()));
                editText.setText("");
            }
        });

        messageArrayList = new ArrayList<>();

        messageAdapter = new MessageAdapter(messageArrayList,MessageActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);
        chatRoom();

    }

    private void chatRoom(){
        FirebaseDatabase.getInstance().getReference("user/" + FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String myUsername = snapshot.getValue(User.class).getUsername();

                if(usernameOfRoommate.compareTo(myUsername)>0){
                    roomId = myUsername + usernameOfRoommate;
                }else if(usernameOfRoommate.compareTo(myUsername)==0){
                    roomId = myUsername + usernameOfRoommate;
                }else{
                    roomId = usernameOfRoommate + myUsername;
                }
                attachMessagesListener(roomId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void attachMessagesListener(String chatRoomId){
        FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    messageArrayList.add(dataSnapshot.getValue(Message.class));
                }
                messageAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messageArrayList.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
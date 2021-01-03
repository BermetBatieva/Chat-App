package com.example.chatapp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Adapter.MessageAdapter;
import com.example.chatapp.FCM.MyFirebaseMessagingService;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.Message;
import com.example.chatapp.model.User;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private EditText editText;
    private TextView title, userName;
    private User user;
    private Chat chat;
    private Message message;
    private RecyclerView recyclerView;
    private List<Message> list = new ArrayList<>();
    private MessageAdapter adapter;

    private MyFirebaseMessagingService fcm;
    private boolean chatExist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chatRecycler);
        editText = findViewById(R.id.inputMessage);
        title = findViewById(R.id.nameContact);
        userName = findViewById(R.id.chat_user_name);

        fcm = new MyFirebaseMessagingService();
        adapter = new MessageAdapter(this, list);

        user = (User) getIntent().getSerializableExtra("user");
        chat = (Chat) getIntent().getSerializableExtra("chat");
        message = (Message) getIntent().getSerializableExtra("message");

        if (chat == null) {
            ArrayList<String> userIds = new ArrayList<>();
            userIds.add(user.getId());
            userIds.add(FirebaseAuth.getInstance().getUid());
            if (!exist(userIds)) {
                chat = new Chat();
                chat.setUserIds(userIds);
                title.setText(user.getDisplayName());
            }
        } else if (user == null) {
            getMessages();
            initList();
            FirebaseFirestore.getInstance().collection("users")
                    .document(chat.getUserIds().get(0))
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        user = documentSnapshot.toObject(User.class);
                        assert user != null;
                        title.setText(user.getDisplayName());
                    });
        }
    }

    private boolean exist(ArrayList<String> userIds) {
        chatExist = false;
        FirebaseFirestore.getInstance().collection("chats")
                .whereEqualTo("userIds", userIds)
                .get()
                .addOnSuccessListener(snapshots -> {
                    if (snapshots != null) {
                        chatExist = true;
                        for (DocumentSnapshot snapshot : snapshots) {
                            chat = snapshot.toObject(Chat.class);
                            chat.setId(snapshot.getId());
                        }
                    }
                });
        return chatExist;
    }

    private void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void onClickSend(View view) {
        String text = editText.getText().toString().trim();
        if (chat.getId() != null) {
            sendMessage(text);
        } else {
            createChat(text);
        }
    }

    private void sendMessage(final String text) {
        Map<String, Object> map = new HashMap<>();
        map.put("text", text);
        map.put("senderId", FirebaseAuth.getInstance().getUid());
        map.put("timestamp", new Timestamp(new java.util.Date()));

        final DocumentReference document = FirebaseFirestore.getInstance().collection("chats").document(chat.getId());
        document.update("timestamp", new Timestamp(new Date()));
        document.collection("messages")
                .add(map)
                .addOnSuccessListener(documentReference -> {
                    editText.setText("");
                    initList();
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(ChatActivity.this, "Can't send message", Toast.LENGTH_SHORT).show());
    }

    private void createChat(final String text) {
        Map<String, Object> map = new HashMap<>();
        map.put("userIds", chat.getUserIds());
        FirebaseFirestore.getInstance().collection("chats")
                .add(map)
                .addOnSuccessListener(documentReference -> {
                    chat.setId(documentReference.getId());
                    sendMessage(text);
                    getMessages();
                    initList();
                });
    }

    private void getMessages() {
        FirebaseFirestore.getInstance()
                .collection("chats")
                .document(chat.getId())
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshot, e) -> {
                    for (DocumentChange change : snapshot.getDocumentChanges()) {
                        switch (change.getType()) {
                            case ADDED:
                                Message message = change.getDocument().toObject(Message.class);
                                message.setId(change.getDocument().getId());
                                message.setTime(change.getDocument().getTimestamp("timestamp"));
                                list.add(message);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }

                });
    }


    public void onClickBack(View view) {
        startActivity(new Intent(ChatActivity.this, MainActivity.class));
        finish();
    }
}
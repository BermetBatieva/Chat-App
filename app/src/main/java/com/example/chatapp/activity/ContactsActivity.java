package com.example.chatapp.activity;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Adapter.ContactsAdapter;
import com.example.chatapp.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public ContactsAdapter adapter;
    private List<User> userList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        recyclerView = findViewById(R.id.recyclerViewAllNumbers);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        getContacts();
        initList();
    }

    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new ContactsAdapter(this, userList);
        adapter.setOnItemClickListener(position -> {
            Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
            intent.putExtra("user", userList.get(position));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void getContacts() {
        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnSuccessListener(snapshots -> {
                    for (DocumentSnapshot snapshot : snapshots) {
                        User user = snapshot.toObject(User.class);
                        if (user != null) {
                            user.setId(snapshot.getId());
                            userList.add(user);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });

    }
}
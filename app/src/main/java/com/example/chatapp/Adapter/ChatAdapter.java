package com.example.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.activity.MainActivity;
import com.example.chatapp.interfaceApp.OnItemClickListener;
import com.example.chatapp.model.Chat;


import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<Chat> chatList;
    OnItemClickListener onItemClick;

    public ChatAdapter(Context context, List<Chat> chatList) {
        this.inflater = LayoutInflater.from(context);
        this.chatList = chatList;
    }

    public ChatAdapter(MainActivity context, List<Chat> chats) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.contacts_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(chatList.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClicklistener) {
        this.onItemClick = onItemClicklistener;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> onItemClick.onItemClick(getAdapterPosition()));
            name = itemView.findViewById(R.id.contact_name);


        }


        public void bind(final Chat chat) {
            name.setText(chat.getId());


        }
    }
}

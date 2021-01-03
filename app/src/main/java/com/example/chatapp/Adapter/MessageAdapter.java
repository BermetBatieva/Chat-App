package com.example.chatapp.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Message> messageList;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.inflater = LayoutInflater.from(context);
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.chat_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(messageList.get(position));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView anotherTextView;
        TextView myTextView;
        TextView anotherTime;
        TextView myTime;
        View another;
        View messageView;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.myText);
            anotherTextView = itemView.findViewById(R.id.anotherTV);
            myTime = itemView.findViewById(R.id.myTime);
            anotherTime = itemView.findViewById(R.id.anotherTime);
            messageView = itemView.findViewById(R.id.myMessage);
            another = itemView.findViewById(R.id.another);
            date = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(Message message) {
            Date d = message.getTime().toDate();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            String time = sdf.format(d);

            if (message.getSenderId().equals(FirebaseAuth.getInstance().getUid())) {
                myTime.setText(time);
                messageView.setVisibility(View.VISIBLE);
                another.setVisibility(View.GONE);
                myTextView.setText(message.getText());
            } else {
                anotherTime.setText(time);
                another.setVisibility(View.VISIBLE);
                messageView.setVisibility(View.GONE);
                anotherTextView.setText(message.getText());
            }
        }
    }
}
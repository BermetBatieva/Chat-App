package com.example.chatapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.interfaceApp.OnItemClickListener;
import com.example.chatapp.model.User;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {


    LayoutInflater inflater;
    List<User> userList;
    OnItemClickListener onItemClick;

    public ContactsAdapter(Context context, List<User> userList) {
        this.inflater = LayoutInflater.from(context);
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.contacts_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClicklistener) {
        this.onItemClick = onItemClicklistener;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> onItemClick.onItemClick(getAdapterPosition()));
            name = itemView.findViewById(R.id.contact_name);

        }
        public void bind(User user) {
            name.setText(user.getDisplayName());


        }
    }
}
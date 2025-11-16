package com.example.passapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    private Context context;
    private List<PasswordItem> list;
    private PasswordStore store;

    public PasswordAdapter(Context context, List<PasswordItem> list) {
        this.context = context;
        this.list = list;
        this.store = new PasswordStore(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_password, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PasswordItem item = list.get(position);

        holder.txtApp.setText(item.getApp());
        holder.txtUser.setText(item.getUsername());
        holder.txtPass.setText("••••••");

        // COPY button
        holder.btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard =
                    (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("password", item.getPassword());
            clipboard.setPrimaryClip(clip);

            Toast.makeText(context, "Password Copied!", Toast.LENGTH_SHORT).show();
        });

        // DELETE button
        holder.btnDelete.setOnClickListener(v -> {
            store.deleteItem(position);
            list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());

            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ----------------- VIEWHOLDER -------------------
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtApp, txtUser, txtPass;
        ImageButton btnCopy, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtApp = itemView.findViewById(R.id.txtApp);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtPass = itemView.findViewById(R.id.txtPass);
            btnCopy = itemView.findViewById(R.id.btnCopy);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

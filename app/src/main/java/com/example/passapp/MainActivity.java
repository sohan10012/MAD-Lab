package com.example.passapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText searchInput;
    RecyclerView passwordList;
    FloatingActionButton btnAdd;

    PasswordStore store;
    PasswordAdapter adapter;

    List<PasswordItem> allItems = new ArrayList<>();
    List<PasswordItem> filteredItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchInput = findViewById(R.id.searchInput);
        passwordList = findViewById(R.id.passwordList);
        btnAdd = findViewById(R.id.btnAdd);

        store = new PasswordStore(this);

        passwordList.setLayoutManager(new LinearLayoutManager(this));

        loadPasswords();

        // ➕ FAB → Add new password
        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddPasswordActivity.class))
        );

        // 🔍 Search filter
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPasswords();  // refresh when user returns from add/edit screen
    }

    private void loadPasswords() {
        allItems = store.getAll();
        filteredItems = new ArrayList<>(allItems);

        adapter = new PasswordAdapter(this, filteredItems);
        passwordList.setAdapter(adapter);
    }

    private void filterList(String query) {
        filteredItems.clear();

        for (PasswordItem item : allItems) {
            if (item.getApp().toLowerCase().contains(query.toLowerCase()) ||
                    item.getUsername().toLowerCase().contains(query.toLowerCase())) {

                filteredItems.add(item);
            }
        }

        adapter.notifyDataSetChanged();
    }
}

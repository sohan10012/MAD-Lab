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

    // SINGLE LIST (Fix for your issue)
    List<PasswordItem> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchInput = findViewById(R.id.searchInput);
        passwordList = findViewById(R.id.passwordList);
        btnAdd = findViewById(R.id.btnAdd);

        store = new PasswordStore(this);

        passwordList.setLayoutManager(new LinearLayoutManager(this));

        loadPasswords(); // load first time

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, AddPasswordActivity.class))
        );

        // SEARCH BAR
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
        loadPasswords();   // refresh list after adding a new password
    }

    private void loadPasswords() {
        items.clear();
        items.addAll(store.getAll());

        adapter = new PasswordAdapter(this, items);
        passwordList.setAdapter(adapter);
    }

    private void filterList(String query) {
        List<PasswordItem> filtered = new ArrayList<>();

        for (PasswordItem item : items) {
            if (item.getApp().toLowerCase().contains(query.toLowerCase()) ||
                    item.getUsername().toLowerCase().contains(query.toLowerCase())) {

                filtered.add(item);
            }
        }

        adapter = new PasswordAdapter(this, filtered);
        passwordList.setAdapter(adapter);
    }
}

package com.example.passapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddPasswordActivity extends AppCompatActivity {

    EditText etApp, etUsername, etPassword;
    Button btnGenerate, btnSave;

    PasswordStore store;

    // NEW API — modern result launcher
    ActivityResultLauncher<Intent> generatorLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        etApp = findViewById(R.id.etApp);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnSave = findViewById(R.id.btnSave);

        store = new PasswordStore(this);

        // INITIALIZE LAUNCHER
        generatorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String pass = result.getData().getStringExtra("generated_password");
                        etPassword.setText(pass);
                    }
                }
        );

        // OPEN GENERATOR SCREEN
        btnGenerate.setOnClickListener(v -> {
            Intent intent = new Intent(AddPasswordActivity.this, GeneratorActivity.class);
            generatorLauncher.launch(intent);
        });

        // SAVE
        btnSave.setOnClickListener(v -> savePassword());
    }

    private void savePassword() {
        String app = etApp.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (app.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        PasswordItem item = new PasswordItem(app, username, password);
        store.addItem(item);

        Toast.makeText(this, "Password Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}

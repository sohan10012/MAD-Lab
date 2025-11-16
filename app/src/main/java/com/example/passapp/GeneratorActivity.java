package com.example.passapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.SecureRandom;

public class GeneratorActivity extends AppCompatActivity {

    SeekBar seekLength;
    TextView txtLength;
    CheckBox checkUpper, checkNumbers, checkSymbols;
    EditText etOutput;
    Button btnGenerate, btnUse;

    SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);

        seekLength = findViewById(R.id.seekLength);
        txtLength = findViewById(R.id.txtLength);
        checkUpper = findViewById(R.id.checkUpper);
        checkNumbers = findViewById(R.id.checkNumbers);
        checkSymbols = findViewById(R.id.checkSymbols);
        etOutput = findViewById(R.id.etOutput);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnUse = findViewById(R.id.btnUse);

        // Update length text
        seekLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 4) progress = 4; // minimum 4 chars
                txtLength.setText("Length: " + progress);
                seekBar.setProgress(progress);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Generate password
        btnGenerate.setOnClickListener(v -> generatePassword());

        // Use password → send back to AddPasswordActivity
        btnUse.setOnClickListener(v -> {
            String pass = etOutput.getText().toString();
            if (pass.isEmpty()) return;

            Intent intent = new Intent();
            intent.putExtra("generated_password", pass);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void generatePassword() {
        int length = seekLength.getProgress();
        if (length < 4) length = 4;

        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*()-_=+[]{};:,.<>?/";

        String combined = lower;

        if (checkUpper.isChecked()) combined += upper;
        if (checkNumbers.isChecked()) combined += numbers;
        if (checkSymbols.isChecked()) combined += symbols;

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(combined.length());
            password.append(combined.charAt(index));
        }

        etOutput.setText(password.toString());
    }
}

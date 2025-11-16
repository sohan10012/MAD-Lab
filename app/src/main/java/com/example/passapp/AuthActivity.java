package com.example.passapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class AuthActivity extends AppCompatActivity {

    EditText pinInput;
    Button btnSavePin, btnFingerprint;

    SharedPreferences prefs;
    private static final String FILE = "secure_auth";
    private static final String KEY_PIN = "user_pin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        prefs = getSharedPreferences(FILE, MODE_PRIVATE);

        pinInput = findViewById(R.id.pinInput);
        btnSavePin = findViewById(R.id.btnSavePin);
        btnFingerprint = findViewById(R.id.btnFingerprint);

        String savedPin = prefs.getString(KEY_PIN, null);

        // If PIN already saved → show fingerprint first
        if (savedPin != null) {
            showFingerprint();
        }

        btnSavePin.setOnClickListener(v -> {
            String pin = pinInput.getText().toString().trim();

            if (pin.length() != 4) {
                Toast.makeText(this, "Enter a valid 4-digit PIN", Toast.LENGTH_SHORT).show();
                return;
            }

            // Save PIN securely
            prefs.edit().putString(KEY_PIN, pin).apply();
            Toast.makeText(this, "PIN Saved!", Toast.LENGTH_SHORT).show();

            goToMain();
        });

        btnFingerprint.setOnClickListener(v -> showFingerprint());
    }

    private void showFingerprint() {
        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt prompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        goToMain();
                    }



                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                    }
                });

        BiometricPrompt.PromptInfo info = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Fingerprint Unlock")
                .setSubtitle("Use your fingerprint to unlock PassApp")
                .setNegativeButtonText("Cancel")
                .build();

        prompt.authenticate(info);
    }

    private void goToMain() {
        startActivity(new Intent(AuthActivity.this, MainActivity.class));
        finish();
    }
}

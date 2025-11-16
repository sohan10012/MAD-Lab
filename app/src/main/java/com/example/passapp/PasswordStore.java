package com.example.passapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

public class PasswordStore {

    private static final String PREF_NAME = "encrypted_passwords";
    private static final String KEY_LIST = "password_list";

    private SharedPreferences prefs;
    private Gson gson = new Gson();

    public PasswordStore(Context context) {

        try {
            // Create SPECIFIC modern AES-256 key
            KeyGenParameterSpec keyGenParameterSpec =
                    new KeyGenParameterSpec.Builder(
                            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
                            KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
                    )
                            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                            .setKeySize(256)
                            .build();

            // Create master key
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyGenParameterSpec(keyGenParameterSpec)
                    .build();

            // Encrypted shared prefs (still recommended by Google)
            prefs = EncryptedSharedPreferences.create(
                    context,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Encryption setup failed!", e);
        }
    }

    public List<PasswordItem> getAll() {
        String json = prefs.getString(KEY_LIST, null);
        if (json == null) return new ArrayList<>();

        Type type = new TypeToken<List<PasswordItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    private void saveAll(List<PasswordItem> list) {
        prefs.edit().putString(KEY_LIST, gson.toJson(list)).apply();
    }

    public void addItem(PasswordItem item) {
        List<PasswordItem> list = getAll();
        list.add(item);
        saveAll(list);
    }

    public void deleteItem(int index) {
        List<PasswordItem> list = getAll();
        if (index >= 0 && index < list.size()) {
            list.remove(index);
            saveAll(list);
        }
    }

    public void updateItem(int index, PasswordItem item) {
        List<PasswordItem> list = getAll();
        if (index >= 0 && index < list.size()) {
            list.set(index, item);
            saveAll(list);
        }
    }
}

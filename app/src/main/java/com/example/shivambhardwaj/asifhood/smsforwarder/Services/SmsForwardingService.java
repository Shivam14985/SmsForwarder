package com.example.shivambhardwaj.asifhood.smsforwarder.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shivambhardwaj.asifhood.smsforwarder.Model.ForwardedHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SmsForwardingService extends Service {
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sender = intent.getStringExtra("sender");
        String message = intent.getStringExtra("message");
        String packageName = intent.getStringExtra("packageName");
        String chatId = intent.getStringExtra("telegramId");
        String Token = intent.getStringExtra("Token");
        String FordwardType = intent.getStringExtra("FordwardType");
        forwardToTelegram(sender, message, chatId, Token, packageName, FordwardType);
        return START_NOT_STICKY;
    }

    public void forwardToTelegram(String sender, String message, String chatId, String Token, String packageName, String FordwardType) {
        new Thread(() -> {
            try {
                String urlString = "https://api.telegram.org/bot" + Token + "/sendMessage";

                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("chat_id", chatId)
                        .addFormDataPart("text", "App: " + packageName + "\n " + sender + ": " + message)
                        .build();

                Request request = new Request.Builder()
                        .url(urlString)
                        .post(body)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        showToast("Chat ID: " + chatId + " Response: " + responseBody);
                        //Creating Users History
                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).child("History").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ForwardedHistory model=new ForwardedHistory();
                                model.setForwardedAt(new Date().getTime());
                                model.setPackageName(packageName);
                                model.setSender(sender);
                                model.setMessage(message);
                                model.setFordwardType(FordwardType);
                                snapshot.getRef().push().setValue(model);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        showToast("Request failed with code: " + response.code());
                    }
                }
            } catch (Exception e) {
                showToast("Error forwarding SMS to Telegram: " + e.getMessage());
            }
        }).start();
    }

    private void showToast(final String message) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(() -> Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show());
    }
}

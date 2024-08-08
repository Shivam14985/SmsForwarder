package com.example.shivambhardwaj.asifhood.smsforwarder.Services;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class MyNotificationListenerService extends NotificationListenerService {

    private static final String TAG = "NotificationListener";

    // List of SMS-related package names to ignore
    private static final String[] SMS_PACKAGES = {
            "com.google.android.apps.messaging", // Google Messages
            "com.android.mms", // Default SMS app on some devices
            "com.android.messaging" // Another common SMS app
            // Add other known SMS package names if needed
    };

    private static final int MEMORY_DURATION_MS = 30000; // Memory duration in milliseconds (e.g., 30 seconds)
    private Map<String, Long> recentNotifications;

    @Override
    public void onCreate() {
        super.onCreate();
        recentNotifications = new HashMap<>();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        String packageName = sbn.getPackageName();
        Notification notification = sbn.getNotification();
        String message = notification.extras.getString(Notification.EXTRA_TEXT);
        String sender = notification.extras.getString(Notification.EXTRA_TITLE);

        if (message == null || sender == null) {
            return; // Skip if message or sender is null
        }

        String uniqueId = packageName + "|" + sender + "|" + message;

        // Check for duplicate notifications
        if (recentNotifications.containsKey(uniqueId)) {
            long timestamp = recentNotifications.get(uniqueId);
            if (System.currentTimeMillis() - timestamp < MEMORY_DURATION_MS) {
                Log.d(TAG, "Duplicate notification ignored from: " + packageName);
                return;
            }
        }

        // Store the notification
        recentNotifications.put(uniqueId, System.currentTimeMillis());

        // Clean up old entries
        cleanUpOldEntries();

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String telegramId = sharedPreferences.getString("name", "");
        String telegramToken = sharedPreferences.getString("Token", "");
        String notificationSwitch = sharedPreferences.getString("notificationSwitch", "");
        String FordwardType="Notifications";

        // Check if the notification is from an SMS-related package
        if (isSmsPackage(packageName)) {
            Log.d(TAG, "SMS notification ignored from package: " + packageName);
            return; // Ignore SMS notifications
        } else {
            if (notificationSwitch.equals("Enabled")) {
                // Log notification details for debugging
                Log.d(TAG, "Notification received from: " + sender + ", message: " + message + ", package: " + packageName);

                // Forward the message to Telegram
                forwardMessageToTelegram(packageName, sender, message, telegramId, telegramToken,FordwardType);
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Handle notification removal if needed
    }

    private boolean isSmsPackage(String packageName) {
        for (String smsPackage : SMS_PACKAGES) {
            if (smsPackage.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private void forwardMessageToTelegram(String packageName, String sender, String message, String telegramId, String telegramToken, String FordwardType) {
        // Check if TelegramId and TelegramToken are available before forwarding
        if (telegramId != null && !telegramId.isEmpty() && telegramToken != null && !telegramToken.isEmpty()) {
            Intent serviceIntent = new Intent(this, SmsForwardingService.class);
            serviceIntent.putExtra("packageName", packageName);
            serviceIntent.putExtra("sender", sender);
            serviceIntent.putExtra("message", message);
            serviceIntent.putExtra("telegramId", telegramId);
            serviceIntent.putExtra("Token", telegramToken);
            serviceIntent.putExtra("FordwardType", FordwardType);
            startService(serviceIntent);
        } else {
            Log.e(TAG, "TelegramId or Token is missing, cannot forward message.");
        }
    }

    private void cleanUpOldEntries() {
        long currentTime = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recentNotifications.entrySet().removeIf(entry -> currentTime - entry.getValue() > MEMORY_DURATION_MS);
        }
    }
}

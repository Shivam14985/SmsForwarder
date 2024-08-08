package com.example.shivambhardwaj.asifhood.smsforwarder.BroadcastReciever;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.shivambhardwaj.asifhood.smsforwarder.Services.SmsForwardingService;

import java.util.Arrays;
import java.util.List;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    // Define OTP keywords
    private static final List<String> OTP_KEYWORDS = Arrays.asList("OTP", "One-time password", "Verification code", "code");

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                StringBuilder messageBody = new StringBuilder();
                String sender = null;

                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    if (sender == null) {
                        sender = smsMessage.getDisplayOriginatingAddress();
                    }
                    messageBody.append(smsMessage.getMessageBody());
                }

                SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                String telegramId = sharedPreferences.getString("name", "");
                String telegramToken = sharedPreferences.getString("Token", "");

                if (telegramId == null) {
                    Toast.makeText(context, "Telegram Id Not Found", Toast.LENGTH_SHORT).show();
                }
                if (telegramToken == null) {
                    Toast.makeText(context, "Telegram Token not found", Toast.LENGTH_SHORT).show();
                }
                if (sender != null && telegramId != null && telegramToken != null) {
                    Log.d(TAG, "SMS received from: " + sender + ", message: " + messageBody.toString());
                    Toast.makeText(context, "SMS received from: " + sender + ", message: " + messageBody.toString(), Toast.LENGTH_LONG).show();
                    SharedPreferences sh = context.getSharedPreferences("MySharedPref", MODE_APPEND);
                    String onlyOtpForward = sh.getString("OnlyOtpForward", "");
                    String allIncomingMessages = sh.getString("AllIncomingMessages", "");

                    boolean isOtp = isOtpMessage(messageBody.toString());

                    if (onlyOtpForward.equals("Enabled") && isOtp) {
                        forwardSmsToTelegram(context, sender, messageBody.toString(), telegramId, telegramToken, "Messages", "OTPs");
                        Toast.makeText(context, "Otp", Toast.LENGTH_SHORT).show();
                    } else if (allIncomingMessages.equals("Enabled") && !isOtp) {
                        forwardSmsToTelegram(context, sender, messageBody.toString(), telegramId, telegramToken, "Messages", "All SMS");
                        Toast.makeText(context, "Not Otp", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private boolean isOtpMessage(String messageBody) {
        for (String keyword : OTP_KEYWORDS) {
            if (messageBody.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void forwardSmsToTelegram(Context context, String sender, String messageBody, String telegramId, String telegramToken, String packageName, String forwardType) {
        Intent serviceIntent = new Intent(context, SmsForwardingService.class);
        serviceIntent.putExtra("sender", sender);
        serviceIntent.putExtra("packageName", packageName);
        serviceIntent.putExtra("message", messageBody);
        serviceIntent.putExtra("telegramId", telegramId);
        serviceIntent.putExtra("FordwardType", forwardType);
        serviceIntent.putExtra("Token", telegramToken);

        context.startService(serviceIntent);
    }
}

package com.example.shivambhardwaj.asifhood.smsforwarder.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shivambhardwaj.asifhood.smsforwarder.R;
import com.example.shivambhardwaj.asifhood.smsforwarder.databinding.ActivityForwardSmsSettingsBinding;

public class ForwardSmsSettings extends AppCompatActivity {
    ActivityForwardSmsSettingsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityForwardSmsSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        if (intent.getStringExtra("typeOfSetting").equals("Telegram")) {
            binding.ForwardSmsToTelegramSettings.setVisibility(View.VISIBLE);
            binding.saveChatID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String telegramUsername = binding.telegramChatID.getText().toString().trim();
                    if (!telegramUsername.isEmpty()) {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("name", binding.telegramChatID.getText().toString());
                        telegramId.commit();
                        // Retrieving the value using its keys the file name must be same in both saving and retrieving the data
                        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
                        String ChatID = sh.getString("name", "");
                        binding.telegramChatID.setText(ChatID);
                        Toast.makeText(ForwardSmsSettings.this, "Telegram username saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForwardSmsSettings.this, "Please enter a Telegram username", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            binding.saveTokenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String telegramToken = binding.telegramToken.getText().toString().trim();
                    if (!telegramToken.isEmpty()) {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("Token", binding.telegramToken.getText().toString());
                        telegramId.commit();
                        // Retrieving the value using its keys the file name must be same in both saving and retrieving the data
                        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
                        String BotToken = sh.getString("Token", "");
                        binding.telegramToken.setText(BotToken);
                        Toast.makeText(ForwardSmsSettings.this, "Telegram Token saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForwardSmsSettings.this, "Please enter a Telegram username", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            binding.allIncomingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("AllIncomingMessages", "Enabled");
                        telegramId.commit();
                    } else {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("AllIncomingMessages", "Disabled");
                        telegramId.commit();
                    }
                }
            });
            binding.OtpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("OnlyOtpForward", "Enabled");
                        telegramId.commit();
                    } else {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("OnlyOtpForward", "Disabled");
                        telegramId.commit();
                    }
                }
            });
            binding.RcsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("RcsSwitch", "Enabled");
                        telegramId.commit();
                    } else {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("RcsSwitch", "Disabled");
                        telegramId.commit();
                    }
                }
            });
            binding.notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("notificationSwitch", "Enabled");
                        telegramId.commit();
                    } else {
                        // Storing data into SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor telegramId = sharedPreferences.edit();
                        telegramId.putString("notificationSwitch", "Disabled");
                        telegramId.commit();
                    }
                }
            });

            // Retrieving the value using its keys the file name must be same in both saving and retrieving the data
            SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_APPEND);
            String ChatId = sh.getString("name", "");
            binding.telegramChatID.setText(ChatId);

            String Token = sh.getString("Token", "");
            binding.telegramToken.setText(Token);

            String AllIncomingMessages = sh.getString("AllIncomingMessages", "");
            if (AllIncomingMessages.equals("Enabled")) {
                binding.allIncomingSwitch.setChecked(true);
            } else {
                binding.allIncomingSwitch.setChecked(false);
            }
            String OnlyOtpForward = sh.getString("OnlyOtpForward", "");
            if (OnlyOtpForward.equals("Enabled")) {
                binding.OtpSwitch.setChecked(true);
            } else {
                binding.OtpSwitch.setChecked(false);
            }
            String RcsSwitch = sh.getString("RcsSwitch", "");
            if (RcsSwitch.equals("Enabled")) {
                binding.RcsSwitch.setChecked(true);
            } else {
                binding.RcsSwitch.setChecked(false);
            }
            String notificationSwitch = sh.getString("notificationSwitch", "");
            if (notificationSwitch.equals("Enabled")) {
                binding.notificationSwitch.setChecked(true);
            } else {
                binding.notificationSwitch.setChecked(false);
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
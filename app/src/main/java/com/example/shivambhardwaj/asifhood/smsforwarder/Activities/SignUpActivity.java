package com.example.shivambhardwaj.asifhood.smsforwarder.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.shivambhardwaj.asifhood.smsforwarder.Model.UsersModel;
import com.example.shivambhardwaj.asifhood.smsforwarder.R;
import com.example.shivambhardwaj.asifhood.smsforwarder.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult o) {
            if (o.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(o.getData());
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String NAme = auth.getCurrentUser().getDisplayName();
                                String Email = auth.getCurrentUser().getEmail();
                                UsersModel usersModel = new UsersModel(NAme, Email);
                                database.getReference().child("Users").child(task.getResult().getUser().getUid()).setValue(usersModel);
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseApp.initializeApp(SignUpActivity.this);

        //Sign Up button
        binding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Signing Up");
                String Email = binding.EtEmail.getText().toString();
                String Name = binding.EtName.getText().toString();
                String Password = binding.EtPassword.getText().toString();
                if (binding.EtEmail.getText().toString().isEmpty() || binding.EtName.getText().toString().isEmpty() || binding.EtPassword.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, "Fill all fields", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();

                } else {
                    progressDialog.show();
                    auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                binding.LayoutForEmailAuthentication.setVisibility(View.GONE);
                                binding.registeredsuccess.setVisibility(View.VISIBLE);
                                binding.registeredsuccess.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        UsersModel model = new UsersModel(Name, Email);
                                        String id = task.getResult().getUser().getUid();
                                        database.getReference().child("Users").child(id).setValue(model);
                                        Toast.makeText(SignUpActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
//                                        // This is how an Image to be displayed in our Notification
//                                        // is decoded and stored in a variable. I've added a picture
//                                        // named "download.jpeg" in the "Drawables".
//                                        Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.thanks);
//                                        Bitmap myBitmapS = BitmapFactory.decodeResource(getResources(), R.drawable.youtube);
//                                        Intent notificationIntent = new Intent(SignUpActivity.this, MainActivity.class);
//                                        PendingIntent playcontentIntent = PendingIntent.getActivity(SignUpActivity.this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

//                                        // If Min. API level of the phone is 26, then notification could be
//                                        // made aesthetic
//                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                            notifChannel = new NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH);
//                                            notifChannel.enableLights(true);
//                                            notifChannel.setLightColor(Color.RED);
//                                            notifChannel.enableVibration(true);
//                                            notifManager.createNotificationChannel(notifChannel);
//
//                                            notifBuilder = new Notification.Builder(SignUpActivity.this, channelID)
//                                                    .setContentTitle("Thanks !!!!")
//                                                    .setContentText("We thanks you to choose on my platform. I will keep try to provide best user Interface, Services and Features.")
//                                                    .setSmallIcon(R.drawable.youtube)
//                                                    .setPriority(Notification.PRIORITY_HIGH)
//                                                    .setStyle(new Notification.BigPictureStyle().bigPicture(myBitmap))
//                                                    .setLargeIcon(myBitmapS).setContentIntent(playcontentIntent);
//
//                                        }
//                                        // Else the Android device would give out default UI attributes
//                                        else {
//                                            notifBuilder = new Notification.Builder(SignUpActivity.this).setContentTitle("Welcome Back").setContentText("We thanks you to return on our platform");
//                                        }
//
//                                        // Everything is done now and the Manager is to be notified about
//                                        // the Builder which built a Notification for the application
//                                        notifManager.notify(1234, notifBuilder.build());
                                    }
                                }, 4500);
                            } else {
                                Snackbar snackbar = Snackbar.make(v, task.getException().getLocalizedMessage(), Snackbar.LENGTH_SHORT);
                                snackbar.setTextColor(Color.WHITE);
                                snackbar.setBackgroundTint(Color.RED);
                                snackbar.show();
                            }
                        }
                    });
                }
            }
        });

        binding.logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.clienId))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        binding.AuthGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}
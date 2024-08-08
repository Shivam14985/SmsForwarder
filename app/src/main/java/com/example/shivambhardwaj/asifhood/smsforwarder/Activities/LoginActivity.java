package com.example.shivambhardwaj.asifhood.smsforwarder.Activities;

import android.annotation.SuppressLint;
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
import com.example.shivambhardwaj.asifhood.smsforwarder.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth auth;
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
                                FirebaseDatabase.getInstance().getReference().child("Users").child(task.getResult().getUser().getUid()).setValue(usersModel);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    });
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog GoogleSignInprogressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseApp.initializeApp(LoginActivity.this);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Signing In");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        //SignIn button
        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Please Wait...");
                String Email = binding.EtEmail.getText().toString();
                String Password = binding.EtPassword.getText().toString();

                if (Email.isEmpty() || Password.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, "Fill above fields", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                } else {
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Welcome Back", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
//                                // This is how an Image to be displayed in our Notification
//                                // is decoded and stored in a variable. I've added a picture
//                                // named "download.jpeg" in the "Drawables".
//                                Bitmap myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.welcome);
//                                Bitmap myBitmapS = BitmapFactory.decodeResource(getResources(), R.drawable.youtube);
//                                Intent notificationIntent = new Intent(LoginActivity.this, MainActivity.class);
//                                PendingIntent playcontentIntent = PendingIntent.getActivity(LoginActivity.this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//
//                                // If Min. API level of the phone is 26, then notification could be
//                                // made aesthetic
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                    notifChannel = new NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH);
//                                    notifChannel.enableLights(true);
//                                    notifChannel.setLightColor(Color.RED);
//                                    notifChannel.enableVibration(true);
//
//                                    notifManager.createNotificationChannel(notifChannel);
//
//                                    notifBuilder = new Notification.Builder(LoginActivity.this, channelID).setContentTitle("Welcome Back").setContentText("We thanks you to return on our platform. I will keep try to provide best user Interface, Services and Features.")
//                                            .setSmallIcon(R.drawable.youtube)
//                                            .setPriority(Notification.PRIORITY_HIGH)
//                                            .setStyle(new Notification.BigPictureStyle()
//                                                    .bigPicture(myBitmap)
//                                                    .setContentDescription("We thanks you to return on our platform. I will keep try to provide best user Interface, Services and Features.")).setLargeIcon(myBitmapS).setContentIntent(playcontentIntent);
//
//                                }
//                                // Else the Android device would give out default UI attributes
//                                else {
//                                    notifBuilder = new Notification.Builder(LoginActivity.this).setContentTitle("Welcome Back").setContentText("We thanks you to return on our platform. I will keep try to provide best user Interface, Services and Features.");
//                                }
//
//                                // Everything is done now and the Manager is to be notified about
//                                // the Builder which built a Notification for the application
//                                notifManager.notify(1234, notifBuilder.build());
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
        //SigUp activity
        binding.SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        binding.ForGotTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.SignInLAyout.setVisibility(View.GONE);
                binding.ForgotPAssWordLAyout.setVisibility(View.VISIBLE);
            }
        });
        binding.GOBAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.SignInLAyout.setVisibility(View.VISIBLE);
                binding.ForgotPAssWordLAyout.setVisibility(View.GONE);
            }
        });
        binding.ResetPAssword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = binding.EtEmailForgot.getText().toString();
                if (Email.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(v, "Enter Email First", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();

                } else {
                    auth.sendPasswordResetEmail(Email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Snackbar snackbar = Snackbar.make(v, "Reset password link has send to your Email ", Snackbar.LENGTH_SHORT);
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.setBackgroundTint(Color.GREEN);
                            snackbar.show();
                            binding.SignInLAyout.setVisibility(View.VISIBLE);
                            binding.ForgotPAssWordLAyout.setVisibility(View.GONE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar snackbar = Snackbar.make(v, e.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.setBackgroundTint(Color.RED);
                            snackbar.show();

                        }
                    });
                }
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
        if (auth.getCurrentUser() != null) {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
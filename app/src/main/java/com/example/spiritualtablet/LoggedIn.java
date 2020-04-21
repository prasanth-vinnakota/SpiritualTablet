package com.example.spiritualtablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoggedIn extends AppCompatActivity {

    Button newUser, logIn, forgetPassword;
    ImageView image;
    TextView logoText, slogan;
    TextInputEditText username, password;
    ProgressBar mProgressBar;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_logged_in);

        newUser = findViewById(R.id.new_user);
        logIn = findViewById(R.id.login);
        forgetPassword = findViewById(R.id.forget_password);

        mProgressBar = findViewById(R.id.log_in_progress_bar);

        image = findViewById(R.id.logoImage);

        logoText = findViewById(R.id.welcome_text);
        slogan = findViewById(R.id.slogan_name);

        username = findViewById(R.id.login_email);
        password = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if ((user != null)){

                    if (user.isEmailVerified()) {
                        startActivity(new Intent(getApplicationContext(), DashBoard.class));
                    }
                    else {

                        //create a Builder object
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedIn.this);

                        //set title to builder
                        builder.setTitle("Email is not verified");

                        //set icon
                        builder.setIcon(R.drawable.danger);

                        //set message
                        builder.setMessage("Your e-mail address "+ user.getEmail() + " is not verified, please verify your email address and login again");

                        //set Button
                        builder.setPositiveButton("send verification email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //set progress bar visible
                                mProgressBar.setVisibility(View.VISIBLE);

                                //send verification email
                                Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(LoggedIn.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //email send
                                        if (task.isSuccessful()) {

                                            //set progress bar gone
                                            mProgressBar.setVisibility(View.GONE);

                                            //show message
                                            Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();

                                        }
                                        //email not sent
                                        else{

                                            //set progress bar gone
                                            mProgressBar.setVisibility(View.GONE);

                                            //show message
                                            Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });

                        mProgressBar.setVisibility(View.GONE);

                        //build and show builder
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            }
        };

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkInternetConnection())
                    startActivity(new Intent(LoggedIn.this,NoInternet.class));

                Intent intent = new Intent(LoggedIn.this, SignUp.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(image, "home_image");
                pairs[1] = new Pair<View, String>(logoText, "home_text");
                pairs[2] = new Pair<View, String>(slogan, "login_desc");
                pairs[3] = new Pair<View, String>(username, "login_username");
                pairs[4] = new Pair<View, String>(password, "login_password");
                pairs[5] = new Pair<View, String>(logIn, "login_go_button");
                pairs[6] = new Pair<View, String>(newUser, "login_sign_up");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoggedIn.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkInternetConnection())
                    startActivity(new Intent(LoggedIn.this,NoInternet.class));

                String email = Objects.requireNonNull(username.getText()).toString();

                if (!validateUsername())
                    return;

                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(LoggedIn.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            //set progress bar gone
                            mProgressBar.setVisibility(View.GONE);

                            //show message
                            Toast.makeText(getApplicationContext(), "Password reset email sent to your email", Toast.LENGTH_SHORT).show();
                        }
                        //email not send
                        else {

                            //set progress bar gone
                            mProgressBar.setVisibility(View.GONE);

                            //show message
                            Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    private boolean validateUsername() {

        String userName = Objects.requireNonNull(username.getText()).toString();
        if (userName.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String passWord = Objects.requireNonNull(password.getText()).toString();

        if (passWord.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void loginUser(View view) {

        if (!checkInternetConnection())
            startActivity(new Intent(LoggedIn.this,NoInternet.class));

        if (!validateUsername() | !validatePassword())
            return;

        mProgressBar.setVisibility(View.VISIBLE);

        String userName = Objects.requireNonNull(username.getText()).toString();
        String passWord = Objects.requireNonNull(password.getText()).toString();

        mAuth.signInWithEmailAndPassword(userName, passWord).addOnCompleteListener(LoggedIn.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){

                        mProgressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(LoggedIn.this,DashBoard.class);
                        Toast.makeText(getApplicationContext(),"Logging in..",Toast.LENGTH_LONG).show();
                        startActivity(intent);

                        finish();
                    }
                    else{

                        //create a Builder object
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoggedIn.this);

                        //set title to builder
                        builder.setTitle("Email is not verified");

                        //set icon
                        builder.setIcon(R.drawable.danger);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        //set message
                        assert user != null;
                        builder.setMessage("Your e-mail address " + user.getEmail() + " is not verified, please verify your email address and login again");

                        //set Button
                        builder.setPositiveButton("send verification email", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //set progress bar visible
                                mProgressBar.setVisibility(View.VISIBLE);

                                //send verification email
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(LoggedIn.this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        //email send
                                        if (task.isSuccessful()) {

                                            //set progress bar gone
                                            mProgressBar.setVisibility(View.GONE);

                                            //show message
                                            Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();

                                        }
                                        //email not sent
                                        else{

                                            //set progress bar gone
                                            mProgressBar.setVisibility(View.GONE);

                                            //show message
                                            Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });

                        mProgressBar.setVisibility(View.GONE);

                        //build and show builder
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }else {

                    //set progress bar gone
                    mProgressBar.setVisibility(View.GONE);

                    //show message
                    Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public boolean checkInternetConnection() {

        //initialize connectivityManager to get the statuses of connectivity services
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);


        NetworkInfo mobile_data = null;
        NetworkInfo wifi = null;

        //connectivityManager have statuses of connection services
        if (connectivityManager != null) {

            //get the status of mobile data
            mobile_data = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            //get status of wifi
            wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }

        //mobile data or wifi is connected
        if ((mobile_data != null && mobile_data.isConnected()) || (wifi != null && wifi.isConnected())) {

            //exit
            return true;
        }

        //hide progress bar
        mProgressBar.setVisibility(View.GONE);

        //show toast message
        Toast.makeText(LoggedIn.this, "No internet connection", Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //add firebaseAuthStateListener
        if (checkInternetConnection())
        mAuth.addAuthStateListener(firebaseAuthListener);
        else
            startActivity(new Intent(LoggedIn.this,NoInternet.class));
    }

    @Override
    protected void onStop() {
        super.onStop();

        //remove firebaseAuthStateListener
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

    //this method shuts application when back is pressed
    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}

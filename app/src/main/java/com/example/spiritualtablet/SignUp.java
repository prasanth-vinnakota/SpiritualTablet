package com.example.spiritualtablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    Button login, signUp;
    ImageView signUpImage;
    TextView signUpText, signUpSlogan;
    TextInputEditText signUpFullName, signUpUsername, signUpEmail, signUpMobileNumber, signUpPassword;
    ProgressBar mProgressBar;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);


        login = findViewById(R.id.sign_up_login);
        signUp = findViewById(R.id.sign_up);

        mProgressBar = findViewById(R.id.sign_up_progress_bar);

        signUpImage = findViewById(R.id.sign_up_image);
        signUpSlogan = findViewById(R.id.sign_up_slogan);

        signUpFullName = findViewById(R.id.sign_up_full_name);
        signUpUsername = findViewById(R.id.sign_up_user_name);
        signUpEmail = findViewById(R.id.sign_up_email);
        signUpMobileNumber = findViewById(R.id.sign_up_mobile_no);
        signUpPassword = findViewById(R.id.sign_up_password);

        signUpText = findViewById(R.id.sign_up_text);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LoggedIn.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(signUpImage, "home_image");
                pairs[1] = new Pair<View, String>(signUpText, "home_text");
                pairs[2] = new Pair<View, String>(signUpSlogan, "login_desc");
                pairs[3] = new Pair<View, String>(signUpUsername, "login_mobile_number");
                pairs[4] = new Pair<View, String>(signUpPassword, "login_password");
                pairs[5] = new Pair<View, String>(signUp, "login_go_button");
                pairs[6] = new Pair<View, String>(login, "login_sign_up");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });
    }

    private boolean validateFullName() {

        String fullName = Objects.requireNonNull(signUpFullName.getText()).toString();
        if (fullName.isEmpty()) {
            signUpFullName.setError("Field cannot be empty");
            return false;
        } else {
            signUpFullName.setError(null);
            return true;
        }
    }

    private boolean validateUserName() {

        String userName = Objects.requireNonNull(signUpUsername.getText()).toString();

        if (userName.isEmpty()) {
            signUpUsername.setError("Field cannot be empty");
            return false;
        } else if (userName.length() > 25) {
            signUpUsername.setError("Username too long");
            return false;

        } else {
            signUpUsername.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {

        String email = Objects.requireNonNull(signUpEmail.getText()).toString();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.isEmpty()) {
            signUpEmail.setError("Field cannot be empty");
            return false;
        } else if (!email.matches(pattern)) {
            signUpEmail.setError("Invalid email address");
            return false;
        } else {
            signUpEmail.setError(null);
            return true;
        }
    }

    private boolean validateMobileNo() {

        String mobileNO = Objects.requireNonNull(signUpMobileNumber.getText()).toString();
        if (mobileNO.isEmpty()) {
            signUpMobileNumber.setError("Field cannot be empty");
            return false;
        } else {
            signUpMobileNumber.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {

        String password = Objects.requireNonNull(signUpPassword.getText()).toString();
        String validation = "^" +
                "(?=.*[a-zA-Z])" + //any letter
                "(?=.*[@#$%^&+=])" + //one special chatter
                ".{6,}" +  //at least six characters
                "$";

        if (password.isEmpty()) {
            signUpPassword.setError("Field cannot be empty");
            return false;
        } else if (!password.matches(validation)) {
            signUpPassword.setError("Password should have one special character and must contain at least 6 characters");
            return false;
        } else {
            signUpPassword.setError(null);
            return true;
        }
    }

    //Save data in firebase
    public void registerUser(View view) {

        mProgressBar.setVisibility(View.VISIBLE);

        //show error
        if (!validateUserName() | !validateFullName() | !validateEmail() | !validateMobileNo() | !validatePassword()) {
            return;
        }

        final String fullName = Objects.requireNonNull(signUpFullName.getText()).toString();
        final String userName = Objects.requireNonNull(signUpUsername.getText()).toString();
        final String email = Objects.requireNonNull(signUpEmail.getText()).toString();
        final String mobileNo = Objects.requireNonNull(signUpMobileNumber.getText()).toString();
        String password = Objects.requireNonNull(signUpPassword.getText()).toString();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    reference.setValue(true);

                    DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                    HashMap<String, Object> info = new HashMap<>();

                    info.put("full_name", fullName);
                    info.put("user_name", userName);
                    info.put("email", email);
                    info.put("mobile_no", mobileNo);
                    info.put("premium","no");

                    db_ref.updateChildren(info);

                    mProgressBar.setVisibility(View.GONE);

                    //create a Builder object
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);

                    //set builder title
                    builder.setTitle("Registered Successfully");

                    //set builder icon
                    builder.setIcon(R.drawable.success);

                    //set message
                    builder.setMessage("You have to verify your email address, and login with your credentials");

                    //set Button
                    builder.setPositiveButton("send verification email", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //set progress bar visible
                            mProgressBar.setVisibility(View.VISIBLE);

                            //send verification email
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(SignUp.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    //email send
                                    if (task.isSuccessful()) {

                                        //set progress bar gone
                                        mProgressBar.setVisibility(View.GONE);

                                        //show message
                                        Toast.makeText(getApplicationContext(), "Verification Email Sent", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(SignUp.this,LoggedIn.class));
                                        finish();


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

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else {

                    mProgressBar.setVisibility(View.GONE);


                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        /*Intent intent = new Intent(getApplicationContext(), VerifyMobileNo.class);
        intent.putExtra("fullName",fullName);
        intent.putExtra("userName",userName);
        intent.putExtra("email",email);
        intent.putExtra("mobileNo",mobileNo);
        intent.putExtra("password",password);

        startActivity(intent);*/


        });


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


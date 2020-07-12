package com.example.spiritualtablet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

public class SignUp extends AppCompatActivity {

    Button login, signUp;
    ImageView signUpImage, imgUserPhoto;
    TextView signUpText, signUpSlogan;
    TextInputEditText signUpFullName, signUpEmail, signUpMobileNumber, signUpPassword;
    ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    static int REQUEST_CODE = 1;
    Uri pickedImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        login = findViewById(R.id.sign_up_login);
        signUp = findViewById(R.id.sign_up);

        mProgressBar = findViewById(R.id.sign_up_progress_bar);

        signUpImage = findViewById(R.id.sign_up_image);
        imgUserPhoto = findViewById(R.id.user_image);
        signUpSlogan = findViewById(R.id.sign_up_slogan);

        signUpFullName = findViewById(R.id.sign_up_full_name);
        signUpEmail = findViewById(R.id.sign_up_email);
        signUpMobileNumber = findViewById(R.id.sign_up_mobile_no);
        signUpPassword = findViewById(R.id.sign_up_password);

        signUpText = findViewById(R.id.sign_up_text);

        imgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.startPickImageActivity(SignUp.this);
               /* if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermissions();
                } else {
                    openGallery();
                }*/
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LoggedIn.class);
                Pair[] pairs = new Pair[7];

                pairs[0] = new Pair<View, String>(signUpImage, "home_image");
                pairs[1] = new Pair<View, String>(signUpText, "home_text");
                pairs[2] = new Pair<View, String>(signUpSlogan, "login_desc");
                pairs[3] = new Pair<View, String>(signUpMobileNumber, "login_mobile_number");
                pairs[4] = new Pair<View, String>(signUpPassword, "login_password");
                pairs[5] = new Pair<View, String>(signUp, "login_go_button");
                pairs[6] = new Pair<View, String>(login, "login_sign_up");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }

    private void checkAndRequestForPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "Please accept required permissions", Toast.LENGTH_LONG).show();
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            pickedImageUrl = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, pickedImageUrl)) {
                Uri uri = pickedImageUrl;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }
            } else {
                startCrop(pickedImageUrl);
            }
        }
       /* if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {

            pickedImageUrl = data.getData();
            imgUserPhoto.setImageURI(pickedImageUrl);
        }*/
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgUserPhoto.setImageURI(result.getUri());
            }
        }
    }

    private void startCrop(Uri pickedImageUrl) {

        CropImage.activity(pickedImageUrl)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);
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
        if (!validateFullName() | !validateEmail() | !validateMobileNo() | !validatePassword() | !validateImage()) {
            mProgressBar.setVisibility(View.GONE);
            return;
        }

        final String fullName = Objects.requireNonNull(signUpFullName.getText()).toString();
        final String email = Objects.requireNonNull(signUpEmail.getText()).toString();
        final String mobileNo = Objects.requireNonNull(signUpMobileNumber.getText()).toString();
        String password = Objects.requireNonNull(signUpPassword.getText()).toString();

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    final String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    reference.setValue(true);


                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("user_photos");
                    final StorageReference imageFilePath = mStorage.child(Objects.requireNonNull(pickedImageUrl.getLastPathSegment()));
                    imageFilePath.putFile(pickedImageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(fullName)
                                            .setPhotoUri(uri)
                                            .build();


                                    mAuth.getCurrentUser().updateProfile(profileUpdate)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                                                        final HashMap<String, Object> info = new HashMap<>();

                                                        info.put("full_name", fullName);
                                                        info.put("email", email);
                                                        info.put("mobile_no", mobileNo);
                                                        if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {

                                                            taskSnapshot.getMetadata().getReference().getDownloadUrl()
                                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                        @Override
                                                                        public void onSuccess(Uri uri) {

                                                                            info.put("user_image", uri.toString());
                                                                        }
                                                                    });
                                                        }

                                                        db_ref.updateChildren(info);

                                                        signUpFullName.setText("");
                                                        signUpEmail.setText("");
                                                        signUpMobileNumber.setText("");
                                                        signUpPassword.setText("");
                                                        signUpImage.setImageURI(null);

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
                                                                            FirebaseAuth.getInstance().signOut();
                                                                            startActivity(new Intent(SignUp.this, LoggedIn.class));

                                                                            finish();


                                                                        }
                                                                        //email not sent
                                                                        else {

                                                                            //set progress bar gone
                                                                            mProgressBar.setVisibility(View.GONE);

                                                                            //show message
                                                                            Toast.makeText(getApplicationContext(), "Error : " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }

                                                                    ;
                                                                });
                                                            }
                                                        });

                                                        mProgressBar.setVisibility(View.GONE);

                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();

                                                    } else {

                                                        mProgressBar.setVisibility(View.GONE);


                                                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            });

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mProgressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }

    private boolean validateImage() {
        if (pickedImageUrl == null) {
            Toast.makeText(this, "Select a profile picture", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

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



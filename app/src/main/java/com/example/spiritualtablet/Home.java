package com.example.spiritualtablet;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.spiritualtablet.fragments.ProfileFragment;
import com.example.spiritualtablet.fragments.SettingsFragment;
import com.example.spiritualtablet.models.Post;
import com.example.spiritualtablet.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    Dialog popAddPost;
    ImageView popupUserImage,popupPostImage,popupClose;
    Button popupAddButton;
    TextView popupDescription;

    static int REQUEST_CODE = 1;
    private Uri pickedImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        intPopup();
        setupPopupImageClick();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        updateNavHeader();

        getSupportFragmentManager().beginTransaction().replace(R.id.container_view, new com.example.spiritualtablet.fragments.HomeFragment()).commit();

    }

    private void setupPopupImageClick() {

        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestForPermissions();
            }
        });
    }

    private void intPopup() {

        popAddPost = new Dialog(this);
        popAddPost.setCancelable(false);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;


        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupClose = popAddPost.findViewById(R.id.popup_close);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddButton = popAddPost.findViewById(R.id.popup_add);


        popupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.dismiss();
            }
        });

        popupDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                popupAddButton.setEnabled(!popupDescription.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        Glide.with(this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        popupAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!popupDescription.getText().toString().isEmpty()
                && pickedImageUrl != null){

                    //set progress dialog
                    final ProgressDialog progressDialog = new ProgressDialog(Home.this);

                    //set title
                    progressDialog.setTitle("Uploading...");

                    //set style
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                    //set progress
                    progressDialog.setProgress(0);

                    //set cancelable
                    progressDialog.setCancelable(false);

                    //show dialog
                    progressDialog.show();
                    final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(Objects.requireNonNull(pickedImageUrl.getLastPathSegment()));
                    imageFilePath.putFile(pickedImageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageDownloadedLink = uri.toString();

                                    Post post = new Post(popupDescription.getText().toString(),
                                            imageDownloadedLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString(),
                                            currentUser.getDisplayName());

                                    progressDialog.dismiss();
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage(e.getMessage());
                                    progressDialog.dismiss();
                                }
                            });;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            showMessage(e.getMessage());
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            int progress = (int)((taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()) * 100);
                            progressDialog.setProgress(progress);

                        }
                    });
                }
                else if (!popupDescription.getText().toString().isEmpty()
                        && pickedImageUrl == null){

                    Post post = new Post(popupDescription.getText().toString(),
                            "",
                            currentUser.getUid(),
                            currentUser.getPhotoUrl().toString(),
                            currentUser.getDisplayName());

                    addPost(post);

                }
                else if (popupDescription.getText().toString().isEmpty()
                && pickedImageUrl != null){

                    //set progress dialog
                    final ProgressDialog progressDialog = new ProgressDialog(Home.this);

                    //set title
                    progressDialog.setTitle("Uploading...");

                    //set style
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

                    //set progress
                    progressDialog.setProgress(0);

                    //set cancelable
                    progressDialog.setCancelable(false);

                    //show dialog
                    progressDialog.show();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(Objects.requireNonNull(pickedImageUrl.getLastPathSegment()));
                    imageFilePath.putFile(pickedImageUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageDownloadedLink = uri.toString();

                                    Post post = new Post("",
                                            imageDownloadedLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString(),
                                            currentUser.getDisplayName());

                                    progressDialog.dismiss();
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage(e.getMessage());
                                    progressDialog.dismiss();
                                }
                            });;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            showMessage(e.getMessage());
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                            int progress = (int)((taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()) * 100);
                            progressDialog.setProgress(progress);

                        }
                    });
                }else {
                    showMessage("Select a image or describe the post");
                }
            }
        });

    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        String key = myRef.getKey();
        post.setPostKey(key);

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added Successfully");
                popupPostImage.setImageURI(null);
                popupDescription.setText("");
                popAddPost.dismiss();
            }
        });
    }

    private void showMessage(String message) {

        Toast.makeText(this,message, Toast.LENGTH_LONG).show();
    }

    private void checkAndRequestForPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "Please accept required permissions", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            openGallery();
        }
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {

            pickedImageUrl = data.getData();
            popupPostImage.setImageURI(pickedImageUrl);
            popupAddButton.setEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.navigation_home) {

            Objects.requireNonNull(getSupportActionBar()).setTitle("Timeline");
            Intent i = new Intent(getApplicationContext(),Home.class);
            startActivity(i);
            finish();
           // getSupportFragmentManager().beginTransaction().replace(R.id.container_view, new HomeFragment()).commit();

        } else if (id == R.id.navigation_profile) {

            Objects.requireNonNull(getSupportActionBar()).setTitle("Profile");
            getSupportFragmentManager().beginTransaction().replace(R.id.container_view, new ProfileFragment()).commit();

        } else if (id == R.id.navigation_settings) {

            Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");
            getSupportFragmentManager().beginTransaction().replace(R.id.container_view, new SettingsFragment()).commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.nav_username);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_email);

        navUserMail.setText(currentUser.getEmail());
        navUserName.setText(currentUser.getDisplayName());

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);
    }
}

package com.example.spiritualtablet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.spiritualtablet.adapters.CommentAdapter;
import com.example.spiritualtablet.models.Comment;
import com.example.spiritualtablet.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PostDetailActivity extends AppCompatActivity {

    ImageView imgPost, imgUserPost, imgCurrentUser;
    TextView txtPostDesc, txtPostDateName, txtPostUserName;
    EditText editTextComment;
    Button btnAddComment;
    String PostKey;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<com.example.spiritualtablet.models.Comment> listComment;
    static String COMMENT_KEY = "Comment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // let's set the statue bar to transparent

        RvComment = findViewById(R.id.rv_comment);
        imgPost = findViewById(R.id.post_detail_img);
        imgUserPost = findViewById(R.id.post_detail_user_img);
        imgCurrentUser = findViewById(R.id.post_detail_currentuser_img);

        txtPostUserName = findViewById(R.id.post_detail_user_name);
        txtPostDesc = findViewById(R.id.post_detail_desc);
        txtPostDateName = findViewById(R.id.post_detail_date_name);

        editTextComment = findViewById(R.id.post_detail_comment);
        btnAddComment = findViewById(R.id.post_detail_add_comment_btn);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // add Comment button click listener

        btnAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference commentReference = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey).push();
                String comment_content = editTextComment.getText().toString();
                if (comment_content.equals("")) {
                    showMessage("Comment should not be empty");
                    return;
                }
                String uid = firebaseUser.getUid();
                String uname = firebaseUser.getDisplayName();
                String uimg = Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString();
                com.example.spiritualtablet.models.Comment comment;
                comment = new Comment(comment_content, uid, uimg, uname, PostKey);
                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        showMessage("comment added");
                        editTextComment.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("fail to add comment : " + e.getMessage());
                    }
                });


            }
        });


        // now we need to bind all data into those views
        // first we need to get post data
        // we need to send post detail data to this activity first ...
        // now we can get post data

        String postImage = getIntent().getExtras().getString("postImage");
        if (postImage != null)
            Glide.with(this).load(postImage).into(imgPost);
        else
            imgPost.setVisibility(View.GONE);

        txtPostUserName.setText(getIntent().getExtras().getString("userName"));

        String userPostImage = getIntent().getExtras().getString("userPhoto");
        Glide.with(this).load(userPostImage).into(imgUserPost);

        String postDescription = getIntent().getExtras().getString("description");
        assert postDescription != null;
        if (!postDescription.equals(""))
            txtPostDesc.setText(postDescription);
        else
            txtPostDesc.setVisibility(View.GONE);

        // set comment user image

        Glide.with(this).load(firebaseUser.getPhotoUrl()).into(imgCurrentUser);
        // get post id
        PostKey = getIntent().getExtras().getString("postKey");

        String date = timestampToString(getIntent().getExtras().getLong("postDate"));
        txtPostDateName.setText(date);

        // ini Recyclerview Comment
        iniRvComment();

    }

    private void iniRvComment() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        RvComment.setLayoutManager(linearLayoutManager);

        DatabaseReference commentRef = firebaseDatabase.getReference(COMMENT_KEY).child(PostKey);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {

                    com.example.spiritualtablet.models.Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment);

                }

                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                RvComment.setAdapter(commentAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void showMessage(String message) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }


    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String value = DateFormat.format("dd-MM-yyyy", calendar).toString();
        return value;


    }
}

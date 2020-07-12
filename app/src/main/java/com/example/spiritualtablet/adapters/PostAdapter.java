package com.example.spiritualtablet.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spiritualtablet.PostDetailActivity;
import com.example.spiritualtablet.R;
import com.example.spiritualtablet.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {


    private Context mContext;
    private List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_item, parent, false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (mData.get(position).getUserId().equals(userId)){
            holder.delete.setVisibility(View.VISIBLE);
        }else
            holder.delete.setVisibility(View.GONE);

        if (!mData.get(position).getDescription().equals(""))
            holder.description.setText(mData.get(position).getDescription());
        else
            holder.description.setVisibility(View.GONE);
        holder.userName.setText(mData.get(position).getUserName());
        long time = (long) mData.get(position).getTimeStamp();
        holder.postUploadedTime.setText(timestampToString(time));
        if (!mData.get(position).getPicture().equals("")) {
            Glide.with(mContext).load(mData.get(position).getPicture()).into(holder.imgPost);

            holder.imgPost.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, mContext.getResources().getDisplayMetrics());
            ;
        } else
            holder.imgPost.setVisibility(View.GONE);
        if (mData.get(position).getPicture() != null)
            Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);
        else
            Glide.with(mContext).load(R.drawable.userphoto).into(holder.imgPostProfile);


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView description, userName, postUploadedTime;
        ImageView imgPost, imgPostProfile, delete;

        MyViewHolder(View itemView) {

            super(itemView);

            description = itemView.findViewById(R.id.row_post_description);
            userName = itemView.findViewById(R.id.row_post_user_name);
            postUploadedTime = itemView.findViewById(R.id.row_post_uploaded_time);
            imgPost = itemView.findViewById(R.id.row_post_img);
            imgPostProfile = itemView.findViewById(R.id.row_post_profile_img);
            delete = itemView.findViewById(R.id.row_post_delete);

            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("postImage", mData.get(position).getPicture());
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey", mData.get(position).getPostKey());
                    postDetailActivity.putExtra("userPhoto", mData.get(position).getUserPhoto());
                    postDetailActivity.putExtra("userName", mData.get(position).getUserName());
                    long timeStamp = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate", timeStamp);
                    mContext.startActivity(postDetailActivity);

                }
            });
            imgPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent postDetailActivity = new Intent(mContext, PostDetailActivity.class);
                    int position = getAdapterPosition();

                    postDetailActivity.putExtra("postImage", mData.get(position).getPicture());
                    postDetailActivity.putExtra("description", mData.get(position).getDescription());
                    postDetailActivity.putExtra("postKey", mData.get(position).getPostKey());
                    postDetailActivity.putExtra("userPhoto", mData.get(position).getUserPhoto());
                    postDetailActivity.putExtra("userName", mData.get(position).getUserName());
                    long timeStamp = (long) mData.get(position).getTimeStamp();
                    postDetailActivity.putExtra("postDate", timeStamp);
                    mContext.startActivity(postDetailActivity);

                }
            });
          delete.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  LayoutInflater inflater = LayoutInflater.from(mContext);
                  View view = inflater.inflate(R.layout.delete_dialog, null);

                  Button delete = view.findViewById(R.id.alert_delete_button);
                  Button cancel = view.findViewById(R.id.alert_delete_cancel);

                  final AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                          .setView(view)
                          .setCancelable(false)
                          .create();

                  alertDialog.show();

                  delete.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          DatabaseReference post = FirebaseDatabase.getInstance().getReference("Posts").child(mData.get(getAdapterPosition()).getPostKey());
                          post.removeValue();
                          DatabaseReference comment = FirebaseDatabase.getInstance().getReference("Comment").child(mData.get(getAdapterPosition()).getPostKey());
                          comment.removeValue();
                          alertDialog.dismiss();
                      }
                  });

                  cancel.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {

                          alertDialog.dismiss();
                      }
                  });
              }
          });
        }
    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        return DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();


    }

}

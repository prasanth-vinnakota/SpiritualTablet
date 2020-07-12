package com.example.spiritualtablet.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spiritualtablet.R;
import com.example.spiritualtablet.models.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private static List<Comment> mData;


    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        if (holder.img_user == null || holder.tv_name == null || holder.tv_content == null || holder.tv_date == null)
            return;

        Glide.with(mContext).load(mData.get(position).getUimg()).into(holder.img_user);
        holder.tv_name.setText(mData.get(position).getUname());
        holder.tv_content.setText(mData.get(position).getContent());
        holder.tv_date.setText(timestampToString((Long) mData.get(position).getTimestamp()));
        if (mData.get(position).getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
            holder.delete.setVisibility(View.VISIBLE);
        else
            holder.delete.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView img_user, delete;
        TextView tv_name, tv_content, tv_date;

        public CommentViewHolder(View itemView) {
            super(itemView);


            img_user = itemView.findViewById(R.id.comment_user_img);
            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_date = itemView.findViewById(R.id.comment_date);
            delete = itemView.findViewById(R.id.row_comment_delete);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference comment = FirebaseDatabase.getInstance().getReference("Comment").child(mData.get(getAdapterPosition()).getPostKey());
                    comment.removeValue();
                }
            });
        }
    }


    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String val = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        return val;


    }

}

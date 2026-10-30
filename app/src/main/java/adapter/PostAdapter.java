package com.sportmatch.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sportmatch.app.R;
import com.sportmatch.app.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

    public PostAdapter(List<Post> postList, OnItemClickListener listener) {
        this.postList = postList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // Gán tên người đăng động cho từng card
        holder.tvAuthorName.setText(post.getAuthorName());
        holder.tvSportTag.setText(post.getSport());
        holder.tvDescription.setText(post.getDescription());
        holder.tvPostLocation.setText("📍 Địa điểm: " + post.getLocation());
        holder.tvPostTime.setText("⏰ Thời gian: " + post.getTime());
        holder.tvPostSlots.setText("👥 Cần tuyển: " + post.getCurrentAccepted() + "/" + post.getMaxPlayers() + " người");

        holder.itemView.setOnClickListener(v -> listener.onItemClick(post));
        holder.btnJoinFeed.setOnClickListener(v -> listener.onItemClick(post));
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorName, tvSportTag, tvDescription, tvPostLocation, tvPostTime, tvPostSlots;
        Button btnJoinFeed;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.tvAuthorName);
            tvSportTag = itemView.findViewById(R.id.tvSportTag);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPostLocation = itemView.findViewById(R.id.tvPostLocation);
            tvPostTime = itemView.findViewById(R.id.tvPostTime);
            tvPostSlots = itemView.findViewById(R.id.tvPostSlots);
            btnJoinFeed = itemView.findViewById(R.id.btnJoinFeed);
        }
    }
}
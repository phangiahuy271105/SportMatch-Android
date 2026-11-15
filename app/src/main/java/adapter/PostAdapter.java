package com.sportmatch.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sportmatch.app.LoginActivity;
import com.sportmatch.app.R;
import com.sportmatch.app.data.MatchStore;
import com.sportmatch.app.model.Post;
import com.sportmatch.app.model.User;
import com.sportmatch.app.utils.SharedPrefManager;

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
        holder.tvPostSlots.setText("👥 Đã có: " + post.getCurrentAccepted() + "/" + post.getMaxPlayers() + " người");
        boolean full = post.getCurrentAccepted() >= post.getMaxPlayers();
        holder.btnJoinFeed.setText(MatchStore.CLOSED.equals(post.getStatus()) || full
                ? "Đã đóng" : "Xem chi tiết / gửi yêu cầu");

        // [THÊM MỚI] Bắt buộc đăng nhập trước khi xem chi tiết hoặc gửi yêu cầu
        View.OnClickListener checkLoginAndOpen = v -> {
            Context context = v.getContext();
            User currentUser = SharedPrefManager.getInstance(context).getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(context, "Vui lòng đăng nhập hoặc đăng ký để xem chi tiết!", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, LoginActivity.class));
                return;
            }
            listener.onItemClick(post);
        };

        holder.itemView.setOnClickListener(checkLoginAndOpen);
        holder.btnJoinFeed.setOnClickListener(checkLoginAndOpen);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setPosts(List<Post> posts) {
        postList = posts;
        notifyDataSetChanged();
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
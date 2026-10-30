package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sportmatch.app.adapter.PostAdapter;
import com.sportmatch.app.model.Post;

import java.util.ArrayList;
import java.util.List;

public class TimNguoiFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;
    private FloatingActionButton fabAddPost;
    private TextView tvQuickCreatePost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tim_nguoi, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPosts);
        fabAddPost = view.findViewById(R.id.fabAddPost);
        tvQuickCreatePost = view.findViewById(R.id.tvQuickCreatePost);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Danh sách 3 bài đăng mẫu với tên Nguyễn Thắng, Nguyễn Anh Trọng, Lý Văn Phúc
        postList = new ArrayList<>();
        postList.add(new Post("p1", "Nguyễn Thắng", "Bóng đá mini", "Sân Bình Thạnh", "19:00 - 21:00", 8, 10, "Cần tìm thêm 2 chân đá giao hữu vui vẻ!", "OPEN"));
        postList.add(new Post("p2", "Nguyễn Anh Trọng", "Cầu lông", "Sân Linh Trung", "18:00 - 20:00", 3, 4, "Tuyển 1 bạn trình độ trung bình đánh đôi.", "OPEN"));
        postList.add(new Post("p3", "Lý Văn Phúc", "Tennis", "Sân An Phú Đông", "07:00 - 09:00", 2, 2, "Giao lưu đơn nam buổi sáng.", "CLOSED"));

        adapter = new PostAdapter(postList, post -> {
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra("authorName", post.getAuthorName());
            intent.putExtra("sport", post.getSport());
            intent.putExtra("location", post.getLocation());
            intent.putExtra("time", post.getTime());
            intent.putExtra("current", post.getCurrentAccepted());
            intent.putExtra("max", post.getMaxPlayers());
            intent.putExtra("description", post.getDescription());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        if (tvQuickCreatePost != null) {
            tvQuickCreatePost.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                startActivity(intent);
            });
        }

        if (fabAddPost != null) {
            fabAddPost.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }
}
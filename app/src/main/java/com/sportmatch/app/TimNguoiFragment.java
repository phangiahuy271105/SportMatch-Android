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
import com.sportmatch.app.data.MatchStore;
import com.sportmatch.app.model.Post;

import java.util.List;

public class TimNguoiFragment extends Fragment {

    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private List<Post> postList;
    private FloatingActionButton fabAddPost;
    private TextView tvQuickCreatePost;
    private MatchStore matchStore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tim_nguoi, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPosts);
        fabAddPost = view.findViewById(R.id.fabAddPost);
        tvQuickCreatePost = view.findViewById(R.id.tvQuickCreatePost);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        matchStore = new MatchStore(requireContext());
        postList = matchStore.getPosts();

        adapter = new PostAdapter(postList, post -> {
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            intent.putExtra("POST_ID", post.getId());
            startActivity(intent);
        });

        adapter.setPosts(postList);
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

    @Override
    public void onResume() {
        super.onResume();
        if (matchStore != null && adapter != null) adapter.setPosts(matchStore.getPosts());
    }
}

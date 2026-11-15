package com.sportmatch.app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportmatch.app.adapter.RequestAdapter;
import com.sportmatch.app.data.MatchStore;
import com.sportmatch.app.model.JoinRequest;
import com.sportmatch.app.model.Post;
import com.sportmatch.app.model.User;
import com.sportmatch.app.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

/** Chỉ chủ bài mới xem và xử lý được danh sách xin tham gia. */
public class ManageRequestsActivity extends AppCompatActivity {
    private MatchStore matchStore;
    private User currentUser;
    private Post post;
    private List<JoinRequest> requestList;
    private RequestAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);
        matchStore = new MatchStore(this);
        currentUser = SharedPrefManager.getInstance(this).getCurrentUser();
        String postId = getIntent().getStringExtra("POST_ID");
        post = matchStore.getPost(postId);
        if (post == null || currentUser == null || !currentUser.getId().equals(post.getOwnerId())) {
            Toast.makeText(this, "Chỉ chủ bài mới được quản lý yêu cầu.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ((TextView) findViewById(R.id.tv_manage_post_title)).setText(
                post.getSport() + " • " + post.getLocation());
        emptyView = findViewById(R.id.tv_no_requests);
        RecyclerView list = findViewById(R.id.rvRequests);
        list.setLayoutManager(new LinearLayoutManager(this));
        requestList = new ArrayList<>();
        adapter = new RequestAdapter(requestList, new RequestAdapter.OnRequestActionListener() {
            @Override
            public void onAccept(JoinRequest request, int position) {
                String error = matchStore.acceptRequest(post.getId(), request.getRequestId(), currentUser.getId());
                if (error == null) showMessage("Đã duyệt người chơi.");
                else showMessage(error);
                refreshRequests();
            }

            @Override
            public void onReject(JoinRequest request, int position) {
                String error = matchStore.rejectRequest(post.getId(), request.getRequestId(), currentUser.getId());
                if (error == null) showMessage("Đã từ chối yêu cầu.");
                else showMessage(error);
                refreshRequests();
            }
        }, canAcceptMorePlayers());
        list.setAdapter(adapter);
        refreshRequests();
    }

    private void refreshRequests() {
        requestList.clear();
        requestList.addAll(matchStore.getRequests(post.getId()));
        adapter.notifyDataSetChanged();
        post = matchStore.getPost(post.getId());
        adapter.setCanAccept(canAcceptMorePlayers());
        emptyView.setVisibility(requestList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private boolean canAcceptMorePlayers() {
        return post != null && MatchStore.OPEN.equals(post.getStatus())
                && post.getCurrentAccepted() < post.getMaxPlayers();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

package com.sportmatch.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sportmatch.app.data.MatchStore;
import com.sportmatch.app.model.JoinRequest;
import com.sportmatch.app.model.Post;
import com.sportmatch.app.model.User;
import com.sportmatch.app.utils.SharedPrefManager;

public class PostDetailActivity extends AppCompatActivity {
    private MatchStore matchStore;
    private Post post;
    private User currentUser;
    private Button btnCallNow, btnSendRequest, btnEdit, btnManage, btnClose;
    private TextView tvSport, tvLocation, tvTime, tvSlots, tvDescription, tvAuthor, tvPhone, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ((ImageView) findViewById(R.id.imgBack)).setOnClickListener(v -> finish());

        matchStore = new MatchStore(this);
        currentUser = SharedPrefManager.getInstance(this).getCurrentUser();
        tvSport = findViewById(R.id.tvDetailSport);
        tvLocation = findViewById(R.id.tvDetailLocation);
        tvTime = findViewById(R.id.tvDetailTime);
        tvSlots = findViewById(R.id.tvDetailSlots);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvAuthor = findViewById(R.id.tvAuthorNameDetail);
        tvPhone = findViewById(R.id.tvPhoneDetail);
        tvStatus = findViewById(R.id.tvDetailPostStatus);
        btnCallNow = findViewById(R.id.btnCallNow);
        btnSendRequest = findViewById(R.id.btnSendRequest);
        btnEdit = findViewById(R.id.btn_edit_post);
        btnManage = findViewById(R.id.btn_manage_requests);
        btnClose = findViewById(R.id.btn_close_post);

        String postId = getIntent().getStringExtra("POST_ID");
        post = matchStore.getPost(postId);
        if (post == null) {
            Toast.makeText(this, "Không tìm thấy bài đăng.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnCallNow.setOnClickListener(v -> dialPhone());
        btnSendRequest.setOnClickListener(v -> sendOrWithdrawRequest());
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreatePostActivity.class);
            intent.putExtra("POST_ID", post.getId());
            startActivity(intent);
        });
        btnManage.setOnClickListener(v -> {
            Intent intent = new Intent(this, ManageRequestsActivity.class);
            intent.putExtra("POST_ID", post.getId());
            startActivity(intent);
        });
        btnClose.setOnClickListener(v -> confirmClosePost());
        showPost();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (matchStore == null || post == null) return;
        currentUser = SharedPrefManager.getInstance(this).getCurrentUser();
        post = matchStore.getPost(post.getId());
        if (post != null) showPost();
    }

    private void showPost() {
        tvSport.setText(post.getSport());
        tvLocation.setText("📍 Địa điểm: " + post.getLocation());
        tvTime.setText("⏰ Thời gian: " + post.getTime());
        tvSlots.setText("👥 Số lượng: " + post.getCurrentAccepted() + "/"
                + post.getMaxPlayers() + " người");
        tvDescription.setText(post.getDescription().isEmpty()
                ? "Chủ bài chưa thêm mô tả." : "Ghi chú: " + post.getDescription());
        tvAuthor.setText("Người đăng: " + post.getAuthorName());
        tvPhone.setText(post.getPhoneNumber().isEmpty()
                ? "Chủ bài chưa thêm số điện thoại." : "📞 Số điện thoại / Zalo: " + post.getPhoneNumber());

        boolean owner = currentUser != null && currentUser.getId().equals(post.getOwnerId());
        findViewById(R.id.layout_owner_actions).setVisibility(owner ? View.VISIBLE : View.GONE);
        btnCallNow.setVisibility(post.getPhoneNumber().isEmpty() || owner ? View.GONE : View.VISIBLE);
        btnClose.setVisibility(MatchStore.CLOSED.equals(post.getStatus()) ? View.GONE : View.VISIBLE);
        if (owner) {
            btnSendRequest.setVisibility(View.GONE);
            tvStatus.setText(MatchStore.CLOSED.equals(post.getStatus()) ? "Bài đã đóng" : "Bài của bạn");
            return;
        }

        JoinRequest request = currentUser == null ? null
                : matchStore.getRequestForUser(post.getId(), currentUser.getId());
        btnSendRequest.setVisibility(View.VISIBLE);
        if (request != null && MatchStore.ACCEPTED.equals(request.getStatus())) {
            tvStatus.setText("Bạn đã được duyệt tham gia");
            btnSendRequest.setText("Đã được duyệt");
            btnSendRequest.setEnabled(false);
            return;
        }
        if (request != null && MatchStore.PENDING.equals(request.getStatus())) {
            tvStatus.setText(MatchStore.CLOSED.equals(post.getStatus())
                    ? "Bài đã đóng; yêu cầu đang chờ xử lý" : "Yêu cầu đang chờ duyệt");
            btnSendRequest.setText("Rút yêu cầu");
            btnSendRequest.setEnabled(true);
            return;
        }
        if (MatchStore.CLOSED.equals(post.getStatus())
                || post.getCurrentAccepted() >= post.getMaxPlayers()) {
            tvStatus.setText("Bài đã đóng hoặc đã đủ người");
            btnSendRequest.setText(MatchStore.CLOSED.equals(post.getStatus())
                    ? "Bài đã đóng" : "Đã đủ người");
            btnSendRequest.setEnabled(false);
            return;
        }

        tvStatus.setText("Đang tuyển");
        btnSendRequest.setEnabled(true);
        if (request == null) {
            btnSendRequest.setText(currentUser == null ? "Đăng nhập" : "Gửi yêu cầu");
        } else {
            tvStatus.setText(MatchStore.REJECTED.equals(request.getStatus())
                    ? "Yêu cầu trước đã bị từ chối" : "Bạn đã rút yêu cầu trước đó");
            btnSendRequest.setText("Gửi lại yêu cầu");
        }
    }

    private void dialPhone() {
        if (post.getPhoneNumber().isEmpty()) return;
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + post.getPhoneNumber()));
        startActivity(callIntent);
    }

    private void sendOrWithdrawRequest() {
        if (currentUser == null) {
            Toast.makeText(this, "Đăng nhập trước khi gửi yêu cầu.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        JoinRequest request = matchStore.getRequestForUser(post.getId(), currentUser.getId());
        String error;
        if (request != null && MatchStore.PENDING.equals(request.getStatus())) {
            boolean withdrawn = matchStore.withdrawRequest(post.getId(), currentUser.getId());
            error = withdrawn ? null : "Chưa rút được yêu cầu. Thử lại nhé.";
        } else if (request == null || MatchStore.REJECTED.equals(request.getStatus())
                || MatchStore.WITHDRAWN.equals(request.getStatus())) {
            error = matchStore.requestToJoin(post.getId(), currentUser);
        } else {
            error = "Yêu cầu của bạn đã được xử lý.";
        }
        boolean resending = request != null && (MatchStore.REJECTED.equals(request.getStatus())
                || MatchStore.WITHDRAWN.equals(request.getStatus()));
        Toast.makeText(this, error == null
                ? (request == null || resending ? "Đã gửi yêu cầu." : "Đã rút yêu cầu.") : error,
                Toast.LENGTH_SHORT).show();
        post = matchStore.getPost(post.getId());
        showPost();
    }

    private void confirmClosePost() {
        new AlertDialog.Builder(this)
                .setTitle("Đóng bài tìm người?")
                .setMessage("Người khác sẽ không gửi yêu cầu mới được nữa.")
                .setNegativeButton("Để sau", null)
                .setPositiveButton("Đóng bài", (dialog, which) -> {
                    boolean closed = matchStore.closePost(post.getId(), currentUser.getId());
                    Toast.makeText(this, closed ? "Đã đóng bài." : "Không đóng được bài.",
                            Toast.LENGTH_SHORT).show();
                    post = matchStore.getPost(post.getId());
                    showPost();
                }).show();
    }
}

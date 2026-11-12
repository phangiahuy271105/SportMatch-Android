package com.sportmatch.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sportmatch.app.data.MatchStore;
import com.sportmatch.app.model.Post;
import com.sportmatch.app.model.User;
import com.sportmatch.app.utils.SharedPrefManager;
import com.sportmatch.app.utils.ValidationUtils;

/** Tạo bài tìm người mới hoặc sửa bài của chính mình. */
public class CreatePostActivity extends AppCompatActivity {
    private EditText edtSport, edtLocation, edtTime, edtMaxPlayers, edtPhone, edtDescription;
    private Button btnSubmitPost;
    private String postId;
    private User currentUser;
    private MatchStore matchStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentUser = SharedPrefManager.getInstance(this).getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Đăng nhập trước khi đăng bài nhé.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_create_post);
        matchStore = new MatchStore(this);
        ImageView imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(v -> finish());
        edtSport = findViewById(R.id.edtSport);
        edtLocation = findViewById(R.id.edtLocation);
        edtTime = findViewById(R.id.edtTime);
        edtMaxPlayers = findViewById(R.id.edtMaxPlayers);
        edtPhone = findViewById(R.id.edtPhone);
        edtDescription = findViewById(R.id.edtDescription);
        btnSubmitPost = findViewById(R.id.btnSubmitPost);

        postId = getIntent().getStringExtra("POST_ID");
        TextView title = findViewById(R.id.tv_create_post_title);
        if (postId != null) {
            Post post = matchStore.getPost(postId);
            if (post == null || !currentUser.getId().equals(post.getOwnerId())) {
                Toast.makeText(this, "Chỉ chủ bài mới được sửa bài.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            title.setText("Sửa bài tìm người");
            btnSubmitPost.setText("Lưu thay đổi");
            edtSport.setText(post.getSport());
            edtLocation.setText(post.getLocation());
            edtTime.setText(post.getTime());
            edtMaxPlayers.setText(String.valueOf(post.getMaxPlayers()));
            edtPhone.setText(post.getPhoneNumber());
            edtDescription.setText(post.getDescription());
        }
        btnSubmitPost.setOnClickListener(v -> savePost());
    }

    private void savePost() {
        String sport = edtSport.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String time = edtTime.getText().toString().trim();
        String maxPlayersText = edtMaxPlayers.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(sport) || TextUtils.isEmpty(location)
                || TextUtils.isEmpty(time) || TextUtils.isEmpty(maxPlayersText)
                || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Nhập môn, địa điểm, giờ, số người và số điện thoại nhé.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ValidationUtils.isValidVietnamesePhone(phone)) {
            edtPhone.setError("Số điện thoại cần có 10 số hợp lệ.");
            return;
        }

        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(maxPlayersText);
        } catch (NumberFormatException e) {
            edtMaxPlayers.setError("Nhập số người bằng số nguyên nhé.");
            return;
        }
        if (maxPlayers < 2) {
            edtMaxPlayers.setError("Tổng số người cần từ 2 trở lên.");
            return;
        }

        Post oldPost = postId == null ? null : matchStore.getPost(postId);
        if (postId != null && (oldPost == null
                || !currentUser.getId().equals(oldPost.getOwnerId()))) {
            Toast.makeText(this, "Chỉ chủ bài mới được sửa bài này.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (oldPost != null && maxPlayers < oldPost.getCurrentAccepted()) {
            edtMaxPlayers.setError("Số người tối đa không thể thấp hơn số đã được duyệt.");
            return;
        }
        int currentAccepted = oldPost == null ? 1 : oldPost.getCurrentAccepted();
        String status = oldPost == null ? MatchStore.OPEN : oldPost.getStatus();
        Post post = new Post(postId == null ? "post_" + System.currentTimeMillis() : postId,
                currentUser.getFullName(), sport, location, time, currentAccepted, maxPlayers,
                description, status, currentUser.getId(), phone);
        if (!matchStore.savePost(post)) {
            Toast.makeText(this, "Chưa lưu được bài. Thử lại nhé.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, oldPost == null ? "Đã đăng bài tìm người." : "Đã cập nhật bài.",
                Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}

package com.sportmatch.app.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.sportmatch.app.model.JoinRequest;
import com.sportmatch.app.model.Post;
import com.sportmatch.app.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/** Lưu bài tìm người và yêu cầu tham gia mẫu trên thiết bị. */
public class MatchStore {
    public static final String OPEN = "OPEN";
    public static final String CLOSED = "CLOSED";
    public static final String PENDING = "PENDING";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String REJECTED = "REJECTED";
    public static final String WITHDRAWN = "WITHDRAWN";

    private static final String PREFS = "sportmatch_matches";
    private static final String POSTS = "posts";
    private static final String REQUESTS = "requests";
    private final SharedPreferences preferences;

    public MatchStore(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        addSamplePostsIfNeeded();
    }

    public List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        JSONArray values = readArray(POSTS);
        for (int i = 0; i < values.length(); i++) {
            JSONObject item = values.optJSONObject(i);
            if (item == null) continue;
            posts.add(new Post(item.optString("id"), item.optString("authorName"),
                    item.optString("sport"), item.optString("location"), item.optString("time"),
                    item.optInt("currentAccepted"), item.optInt("maxPlayers"),
                    item.optString("description"), item.optString("status"),
                    item.optString("ownerId"), item.optString("phoneNumber")));
        }
        return posts;
    }

    public Post getPost(String postId) {
        for (Post post : getPosts()) {
            if (post.getId().equals(postId)) return post;
        }
        return null;
    }

    public boolean savePost(Post post) {
        JSONArray oldPosts = readArray(POSTS);
        JSONArray newPosts = new JSONArray();
        boolean replaced = false;
        for (int i = 0; i < oldPosts.length(); i++) {
            JSONObject item = oldPosts.optJSONObject(i);
            if (item == null) continue;
            if (post.getId().equals(item.optString("id"))) {
                newPosts.put(toJson(post));
                replaced = true;
            } else {
                newPosts.put(item);
            }
        }
        if (!replaced) newPosts.put(toJson(post));
        return preferences.edit().putString(POSTS, newPosts.toString()).commit();
    }

    public List<JoinRequest> getRequests(String postId) {
        List<JoinRequest> requests = new ArrayList<>();
        JSONArray values = readArray(REQUESTS);
        for (int i = 0; i < values.length(); i++) {
            JSONObject item = values.optJSONObject(i);
            if (item == null || !postId.equals(item.optString("postId"))) continue;
            requests.add(new JoinRequest(item.optString("id"), postId,
                    item.optString("userId"), item.optString("userName"),
                    item.optString("status")));
        }
        return requests;
    }

    public JoinRequest getRequestForUser(String postId, String userId) {
        List<JoinRequest> requests = getRequests(postId);
        // Lấy yêu cầu mới nhất để người chơi vẫn thấy trạng thái bị từ chối/rút.
        for (int i = requests.size() - 1; i >= 0; i--) {
            JoinRequest request = requests.get(i);
            if (request.getUserId().equals(userId)) return request;
        }
        return null;
    }

    public String requestToJoin(String postId, User user) {
        if (user == null) return "Đăng nhập để gửi yêu cầu tham gia.";
        Post post = getPost(postId);
        if (post == null) return "Không tìm thấy bài đăng.";
        if (post.getOwnerId().equals(user.getId())) return "Bạn không thể xin vào bài của mình.";
        if (!OPEN.equals(post.getStatus()) || post.getCurrentAccepted() >= post.getMaxPlayers()) {
            return "Bài đăng đã đóng hoặc đã đủ người.";
        }
        JoinRequest oldRequest = getRequestForUser(postId, user.getId());
        if (oldRequest != null && (PENDING.equals(oldRequest.getStatus())
                || ACCEPTED.equals(oldRequest.getStatus()))) {
            return "Bạn đã gửi yêu cầu cho bài này rồi.";
        }

        JSONArray values = readArray(REQUESTS);
        JSONObject item = new JSONObject();
        try {
            item.put("id", "request_" + System.currentTimeMillis());
            item.put("postId", postId);
            item.put("userId", user.getId());
            item.put("userName", user.getFullName());
            item.put("status", PENDING);
        } catch (JSONException e) {
            return "Không lưu được yêu cầu. Thử lại nhé.";
        }
        values.put(item);
        return preferences.edit().putString(REQUESTS, values.toString()).commit()
                ? null : "Không lưu được yêu cầu. Thử lại nhé.";
    }

    public boolean withdrawRequest(String postId, String userId) {
        return updateRequestStatus(postId, userId, PENDING, WITHDRAWN);
    }

    public String acceptRequest(String postId, String requestId, String ownerId) {
        Post post = getPost(postId);
        if (post == null || !ownerId.equals(post.getOwnerId())) return "Chỉ chủ bài được duyệt.";
        if (!OPEN.equals(post.getStatus()) || post.getCurrentAccepted() >= post.getMaxPlayers()) {
            return "Bài đã đóng hoặc đã đủ người.";
        }
        JoinRequest request = getRequest(postId, requestId);
        if (request == null || !PENDING.equals(request.getStatus())) return "Yêu cầu này không còn chờ duyệt.";

        post.setCurrentAccepted(post.getCurrentAccepted() + 1);
        if (post.getCurrentAccepted() >= post.getMaxPlayers()) post.setStatus(CLOSED);
        if (!savePost(post)) return "Không lưu được số người mới.";
        return updateRequestStatus(postId, request.getUserId(), PENDING, ACCEPTED)
                ? null : "Không cập nhật được yêu cầu.";
    }

    public String rejectRequest(String postId, String requestId, String ownerId) {
        Post post = getPost(postId);
        if (post == null || !ownerId.equals(post.getOwnerId())) return "Chỉ chủ bài được từ chối.";
        JoinRequest request = getRequest(postId, requestId);
        if (request == null || !PENDING.equals(request.getStatus())) return "Yêu cầu này không còn chờ duyệt.";
        return updateRequestStatus(postId, request.getUserId(), PENDING, REJECTED)
                ? null : "Không cập nhật được yêu cầu.";
    }

    public boolean closePost(String postId, String ownerId) {
        Post post = getPost(postId);
        if (post == null || !ownerId.equals(post.getOwnerId())) return false;
        post.setStatus(CLOSED);
        return savePost(post);
    }

    private JoinRequest getRequest(String postId, String requestId) {
        for (JoinRequest request : getRequests(postId)) {
            if (request.getRequestId().equals(requestId)) return request;
        }
        return null;
    }

    private boolean updateRequestStatus(String postId, String userId, String oldStatus, String newStatus) {
        JSONArray values = readArray(REQUESTS);
        boolean changed = false;
        for (int i = 0; i < values.length(); i++) {
            JSONObject item = values.optJSONObject(i);
            if (item == null || !postId.equals(item.optString("postId"))
                    || !userId.equals(item.optString("userId"))
                    || !oldStatus.equals(item.optString("status"))) continue;
            try {
                item.put("status", newStatus);
                changed = true;
            } catch (JSONException e) {
                return false;
            }
        }
        return changed && preferences.edit().putString(REQUESTS, values.toString()).commit();
    }

    private JSONObject toJson(Post post) {
        JSONObject item = new JSONObject();
        try {
            item.put("id", post.getId());
            item.put("ownerId", post.getOwnerId());
            item.put("authorName", post.getAuthorName());
            item.put("sport", post.getSport());
            item.put("location", post.getLocation());
            item.put("time", post.getTime());
            item.put("currentAccepted", post.getCurrentAccepted());
            item.put("maxPlayers", post.getMaxPlayers());
            item.put("description", post.getDescription());
            item.put("phoneNumber", post.getPhoneNumber());
            item.put("status", post.getStatus());
        } catch (JSONException ignored) { }
        return item;
    }

    private JSONArray readArray(String key) {
        String json = preferences.getString(key, "[]");
        try {
            return new JSONArray(json);
        } catch (JSONException e) {
            return new JSONArray();
        }
    }

    private void addSamplePostsIfNeeded() {
        if (preferences.contains(POSTS)) return;
        List<Post> samples = new ArrayList<>();
        samples.add(new Post("p1", "Nguyễn Thắng", "Bóng đá mini", "Sân Bình Thạnh",
                "19:00 - 21:00", 8, 10, "Cần tìm thêm 2 chân đá giao hữu vui vẻ!",
                OPEN, "sample_owner_1", "0987654321"));
        samples.add(new Post("p2", "Nguyễn Anh Trọng", "Cầu lông", "Sân Linh Trung",
                "18:00 - 20:00", 3, 4, "Tuyển 1 bạn trình độ trung bình đánh đôi.",
                OPEN, "sample_owner_2", "0987654322"));
        samples.add(new Post("p3", "Lý Văn Phúc", "Cầu lông", "Sân Tân Bình",
                "07:00 - 09:00", 2, 2, "Giao lưu đơn nam buổi sáng.",
                CLOSED, "sample_owner_3", "0987654323"));
        JSONArray values = new JSONArray();
        for (Post post : samples) values.put(toJson(post));
        preferences.edit().putString(POSTS, values.toString()).commit();
    }
}

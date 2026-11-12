package com.sportmatch.app.model;

public class JoinRequest {
    private String requestId;
    private String postId;
    private String userId;
    private String userName;
    private String status;

    public JoinRequest(String requestId, String postId, String userId, String status) {
        this(requestId, postId, userId, userId, status);
    }

    public JoinRequest(String requestId, String postId, String userId, String userName, String status) {
        this.requestId = requestId;
        this.postId = postId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
    }

    public String getRequestId() { return requestId; }
    public String getPostId() { return postId; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getStatus() { return status; }
}

package com.sportmatch.app.model;

public class JoinRequest {
    private String requestId;
    private String postId;
    private String userId;
    private String status;

    public JoinRequest(String requestId, String postId, String userId, String status) {
        this.requestId = requestId;
        this.postId = postId;
        this.userId = userId;
        this.status = status;
    }

    public String getRequestId() { return requestId; }
    public String getPostId() { return postId; }
    public String getUserId() { return userId; }
    public String getStatus() { return status; }
}
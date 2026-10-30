package com.sportmatch.app;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportmatch.app.adapter.RequestAdapter;
import com.sportmatch.app.model.JoinRequest;

import java.util.ArrayList;
import java.util.List;

public class ManageRequestsActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    private RequestAdapter adapter;
    private List<JoinRequest> requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        rvRequests = findViewById(R.id.rvRequests);
        rvRequests.setLayoutManager(new LinearLayoutManager(this));

        // Dữ liệu mẫu danh sách người xin tham gia
        requestList = new ArrayList<>();
        requestList.add(new JoinRequest("r1", "p1", "NguyenVanA", "PENDING"));
        requestList.add(new JoinRequest("r2", "p1", "TranThiB", "PENDING"));

        adapter = new RequestAdapter(requestList, new RequestAdapter.OnRequestActionListener() {
            @Override
            public void onAccept(JoinRequest request, int position) {
                Toast.makeText(ManageRequestsActivity.this, "Đã duyệt: " + request.getUserId(), Toast.LENGTH_SHORT).show();
                requestList.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onReject(JoinRequest request, int position) {
                Toast.makeText(ManageRequestsActivity.this, "Đã từ chối: " + request.getUserId(), Toast.LENGTH_SHORT).show();
                requestList.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        rvRequests.setAdapter(adapter);
    }
}
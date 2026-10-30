package com.sportmatch.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sportmatch.app.R;
import com.sportmatch.app.model.JoinRequest;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<JoinRequest> requestList;
    private OnRequestActionListener listener;

    public interface OnRequestActionListener {
        void onAccept(JoinRequest request, int position);
        void onReject(JoinRequest request, int position);
    }

    public RequestAdapter(List<JoinRequest> requestList, OnRequestActionListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        JoinRequest request = requestList.get(position);
        holder.tvUserName.setText("Người chơi: " + request.getUserId());
        holder.tvRequestStatus.setText("Trạng thái: " + request.getStatus());

        holder.btnAccept.setOnClickListener(v -> listener.onAccept(request, position));
        holder.btnReject.setOnClickListener(v -> listener.onReject(request, position));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvRequestStatus;
        Button btnAccept, btnReject;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvRequestStatus = itemView.findViewById(R.id.tvRequestStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
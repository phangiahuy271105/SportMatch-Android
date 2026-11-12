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
import com.sportmatch.app.data.MatchStore;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private List<JoinRequest> requestList;
    private OnRequestActionListener listener;
    private boolean canAccept;

    public interface OnRequestActionListener {
        void onAccept(JoinRequest request, int position);
        void onReject(JoinRequest request, int position);
    }

    public RequestAdapter(List<JoinRequest> requestList, OnRequestActionListener listener,
                          boolean canAccept) {
        this.requestList = requestList;
        this.listener = listener;
        this.canAccept = canAccept;
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
        holder.tvUserName.setText("Người chơi: " + request.getUserName());
        boolean pending = MatchStore.PENDING.equals(request.getStatus());
        String status = request.getStatus();
        if (MatchStore.PENDING.equals(status)) status = "Đang chờ duyệt";
        else if (MatchStore.ACCEPTED.equals(status)) status = "Đã duyệt";
        else if (MatchStore.REJECTED.equals(status)) status = "Đã từ chối";
        else status = "Đã rút yêu cầu";
        holder.tvRequestStatus.setText("Trạng thái: " + status);
        holder.btnAccept.setVisibility(pending && canAccept ? View.VISIBLE : View.GONE);
        holder.btnReject.setVisibility(pending ? View.VISIBLE : View.GONE);

        holder.btnAccept.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onAccept(request, currentPosition);
            }
        });
        holder.btnReject.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                listener.onReject(request, currentPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void setCanAccept(boolean canAccept) {
        this.canAccept = canAccept;
        notifyDataSetChanged();
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

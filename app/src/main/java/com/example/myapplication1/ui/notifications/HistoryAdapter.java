package com.example.myapplication1.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Dao.Converters;
import com.example.myapplication1.Dao.DLog;
import com.example.myapplication1.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{
    private List<DLog> LogList;


    public HistoryAdapter(List<DLog> logList) {
        LogList = logList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        DLog dLog = LogList.get(position);
        String time = Converters.DateToString(dLog.getTime());//获取时间
        holder.time.setText("危险记录"+ position + "： " + time);
        holder.detail.setText("温度是：" + dLog.getTemperature());

    }

    @Override
    public int getItemCount() {
        return LogList.size();
    }

    //内部类
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        TextView detail;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.log_time);
            detail = (TextView) itemView.findViewById(R.id.log_detail);

        }
    }
}

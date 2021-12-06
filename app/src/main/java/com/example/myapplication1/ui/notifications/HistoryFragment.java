package com.example.myapplication1.ui.notifications;
/*
 * @Author Lxf
 * @Date 2021/9/1 16:44
 * @Description 用于显示历史记录的Fragment
 * @Since version-1.0
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication1.Dao.DLog;
import com.example.myapplication1.Dao.LogDataBase;
import com.example.myapplication1.MyApplication;
import com.example.myapplication1.R;
import com.example.myapplication1.ThreadPool;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {
    @Nullable
    @org.jetbrains.annotations.Nullable

    private List<DLog> myList = new ArrayList<>();


    private  Handler handler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what > 0){
                RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.h_recyclerView);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getContext());
                Log.d("HistoryFragment","my list in UI is " + myList.size());
                recyclerView.setLayoutManager(layoutManager);
                HistoryAdapter adapter = new HistoryAdapter(myList);
                recyclerView.setAdapter(adapter);
            }
            else {
                Toast.makeText(MyApplication.getContext(),"暂时没有危险记录",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history,container,false);

        ThreadPool.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                myList = LogDataBase.getInstance(MyApplication.getContext()).logDao().selectAll();
                Log.d("History","myList is " + myList.size());

                Message message = new Message();
                message.what = myList.size();
                handler.sendMessage(message);
            }
        });

        return view;
    }
}

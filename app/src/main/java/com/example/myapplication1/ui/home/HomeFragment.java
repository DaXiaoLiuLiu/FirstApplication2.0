package com.example.myapplication1.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication1.MyApplication;
import com.example.myapplication1.R;
import com.example.myapplication1.Repository;
import com.example.myapplication1.ThreadPool;
import com.example.myapplication1.network.home.ValueHelper;
import com.example.myapplication1.network.home.ValueResponse;

public class HomeFragment extends Fragment {

    private ValueHelper valueHelper;
    private HomeViewModel homeViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout constraintLayout;

    private ImageView imageView1;//温度图标
    private ImageView imageView2;//烟雾图标
    private ProgressBar progressBar;//进度条

    private TextView title_View;
    private Toolbar toolbar;

    volatile String freshMessage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        title_View = getActivity().findViewById(R.id.toolbar_title);
        title_View.setText("");
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_223F51B5));

        progressBar = root.findViewById(R.id.progressBar);

        //初始化各图标的图片
        imageView1 = root.findViewById(R.id.imageView1);
        imageView2 = root.findViewById(R.id.imageView2);

        imageView1.setImageResource(R.drawable.tem);
        imageView2.setImageResource(R.drawable.smoke);


        TextView textView = root.findViewById(R.id.textView);
        valueHelper = new ValueHelper();
        valueHelper.setValueActivity(getActivity());


        constraintLayout = root.findViewById(R.id.Constraint);
        constraintLayout.setVisibility(View.GONE);//先隐藏布局
        if(progressBar.getVisibility() == View.GONE){
            progressBar.setVisibility(View.VISIBLE);
        }
        
        homeViewModel.getValue().observe(getViewLifecycleOwner(), new Observer<ValueResponse>() {
            @Override
            public void onChanged(ValueResponse valueResponse) {
                //更新UI
                valueHelper.UiChange(valueResponse);
                if(progressBar.getVisibility() == View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.VISIBLE);
                }

            }
        });


        //下拉刷新功能，更新ui数据的实现
        swipeRefreshLayout = (SwipeRefreshLayout)root.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.black);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ThreadPool.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if(Repository.setValue()){
                            freshMessage = "数据刷新成功";
                        }
                        else {
                            freshMessage = "网络连接错误";
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                swipeRefreshLayout.setRefreshing(false);
                                Toast.makeText(MyApplication.getContext(),freshMessage,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


            }
        });

        return root;
    }


}
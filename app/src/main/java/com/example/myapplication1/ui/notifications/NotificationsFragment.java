package com.example.myapplication1.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication1.R;
import com.example.myapplication1.ui.GlideHelper;
import com.example.myapplication1.ui.notifications.User.LoginActivity;
import com.example.myapplication1.ui.notifications.User.UserActivity;
import com.tencent.mmkv.MMKV;

public class NotificationsFragment extends Fragment {


    private NotificationsViewModel notificationsViewModel;
    private TextView h_textView;//跳转历史页面
    private TextView info_textView;//跳转个人信息页面
    private TextView l_textView; //跳转登录页面
    private TextView title_View;
    private Toolbar toolbar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        title_View = getActivity().findViewById(R.id.toolbar_title);//初始化标题栏
        title_View.setText("用户设置");
        title_View.setTextColor(getResources().getColor(R.color.black));
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_223F51B5));

        MMKV.initialize(getContext());

        ImageView imageView = (ImageView) root.findViewById(R.id.imageView);
        TextView textView_Nick = root.findViewById(R.id.textView4);
        /*notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = MMKV.defaultMMKV().decodeString("name","");
                if(userId.length() == 0){//登录页面
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                else {//个人信息页面
                    Intent intent = new Intent(getActivity(), UserActivity.class);
                    startActivity(intent);
                }

            }
        });

        h_textView = (TextView) root.findViewById(R.id.textView1);
        h_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换Activity
                Intent intent = new Intent(getActivity(),HistoryActivity.class);
                startActivity(intent);

            }
        });

        info_textView = (TextView) root.findViewById(R.id.textView3);
        info_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),UserActivity.class);
                startActivity(intent);
            }
        });

        l_textView = (TextView) root.findViewById(R.id.textView5);
        l_textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("NotificationFragment","onStart 执行");
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView);
        TextView textView_Nick =getActivity().findViewById(R.id.textView4);

        String userId = MMKV.defaultMMKV().decodeString("name","");
        if(userId.length() == 0){
            imageView.setImageResource(R.drawable.load);
            Log.d("NotificationFragment","用户id为0，设置默认图片");
        }
        else {
            String imageUrl = MMKV.defaultMMKV().decodeString("Url","");
            Log.d("NotificationFragment","url is " + imageUrl);
            if(imageUrl.length() == 0){
                imageView.setImageResource(R.drawable.load);
                Log.d("NotificationFragment","图片url为0");
            }
            else {
                //昵称初始化失败的话，默认使用账户id代替
                textView_Nick.setText(MMKV.defaultMMKV().decodeString("NickName",userId));
                GlideHelper helper = new GlideHelper(getContext());
                helper.setGilde(imageUrl,imageView);
            }
        }
    }
}
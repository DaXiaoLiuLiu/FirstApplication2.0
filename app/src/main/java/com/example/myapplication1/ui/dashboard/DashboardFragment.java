package com.example.myapplication1.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication1.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextView title_View;
    private Toolbar toolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
     /*   final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        title_View = getActivity().findViewById(R.id.toolbar_title);
        title_View.setText("视频连接");
        title_View.setTextColor(getResources().getColor(R.color.black));
        toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_223F51B5));


        Button button = (Button) root.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("请求连接");
                dialog.setMessage("是否连接视频：");
                dialog.setCancelable(false);
                dialog.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), MyActivity.class);
                        startActivity(intent);
                    }
                });
                dialog.setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        return root;
    }
}

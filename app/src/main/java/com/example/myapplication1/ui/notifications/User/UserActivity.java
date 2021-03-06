package com.example.myapplication1.ui.notifications.User;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/*import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;*/
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.myapplication1.MainActivity;
import com.example.myapplication1.R;
import com.example.myapplication1.ThreadPool;
import com.example.myapplication1.network.Info.InfoNetWork;
import com.example.myapplication1.network.Info.entity.UInfo;
import com.example.myapplication1.network.Info.entity.UserInfoResponse;
import com.example.myapplication1.ui.GlideHelper;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.tencent.mmkv.MMKV;

public class UserActivity extends AppCompatActivity {
    private ImageView userAvatar;

    private Toolbar detailToolbar;

    private UInfo userInfo;

    private Button button;

    private GlideHelper helper;
    private static CityPickerView mPicker;

    private LinearLayout layout_avatar, layout_nickname, layout_sex, layout_local, layout_signature;

    private TextView showNickName, showSex, showLocation, showSignature;

    private ProgressBar progressBar;
    public static final int CHOOSE_USER_PROFILE = 12;

/*    private ActivityResultLauncher requestDataLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            userAvatar = findViewById(R.id.user_avatar);
                            GlideHelper helper = new GlideHelper(getApplicationContext());

                            String uri_s = uri.toString();
                            helper.setGilde(uri_s, userAvatar);
                            Log.d("UserActivity", "url is " + uri_s);
                            MMKV.defaultMMKV().encode("Url", uri_s);
                        } else {
                            Toast.makeText(getApplicationContext(), "??????????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        mPicker = new CityPickerView();
        mPicker.init(this);//????????????????????????
        MMKV.initialize(this);//?????????mmkv

        detailToolbar = findViewById(R.id.userData_toolbar);
        detailToolbar.setTitle("????????????");
        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_return_left);
        }
        layout_avatar = findViewById(R.id.lay_avatar);
        layout_nickname = findViewById(R.id.lay_nickname);

        layout_sex = findViewById(R.id.lay_sex);
        //layout_birth = findViewById(R.id.lay_birthday);
        layout_local = findViewById(R.id.lay_birthday);
        layout_signature = findViewById(R.id.lay_signature);

        userAvatar = findViewById(R.id.user_avatar);

        showNickName = findViewById(R.id.show_name);
        showSex = findViewById(R.id.show_sex);

        showLocation = findViewById(R.id.show_birthday);
        showSignature = findViewById(R.id.show_sign);

        button = findViewById(R.id.infoBtn);
        button.setText("??????");//???????????????????????????

        progressBar = findViewById(R.id.progress);

        String userId = MMKV.defaultMMKV().decodeString("name","");
        initData(userId);//?????????????????????


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);
                //???????????????????????????
                userInfo = new UInfo();//??????userInfo
                userInfo.setName(MMKV.defaultMMKV().decodeString("name",""));
                userInfo.setuDetail(new UInfo.UDetail());
                userInfo.getuDetail().setNickName(MMKV.defaultMMKV().decodeString("NickName",""));
                userInfo.getuDetail().setUserSex(MMKV.defaultMMKV().decodeString("Sex",""));
                userInfo.getuDetail().setUserLocation(MMKV.defaultMMKV().decodeString("Location",""));
                userInfo.getuDetail().setUserSignature(MMKV.defaultMMKV().decodeString("Signature",""));
                userInfo.getuDetail().setImageUrl(MMKV.defaultMMKV().decodeString("Url",""));

                ThreadPool.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        Looper myLooper = Looper.myLooper();
                        if (myLooper == null) {//??????Loop??????????????????????????????Toast
                            Looper.prepare();

                            myLooper = Looper.myLooper();
                        }

                        UserInfoResponse response = InfoNetWork.setInfo(userInfo);
                        if(response.getStatus()){//????????????
                            Log.d("UserDetailActivity"," success in create: " + response.getMessage());
                            Toast.makeText(UserActivity.this,"????????????",Toast.LENGTH_LONG).show();
                        }
                        else{//????????????
                            Log.d("UserDetailActivity"," failed in create: " + response.getMessage());
                            Toast.makeText(UserActivity.this,"????????????",Toast.LENGTH_LONG).show();
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(progressBar.getVisibility() == View.VISIBLE)
                                    progressBar.setVisibility(View.GONE);
                            }
                        });

                        if (myLooper != null) {//???????????????????????????Looper
                            Looper.loop();
                            myLooper.quit();
                        }

                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        layout_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //????????????
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                //requestDataLauncher.launch(intent);
                startActivityForResult(intent,CHOOSE_USER_PROFILE);

            }
        });
        layout_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(UserActivity.this)
                        .title("????????????")
                        .inputRangeRes(2, 8, R.color.colorBlack)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("???????????????????????????", MMKV.defaultMMKV().decodeString("NickName","?????????"), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // CharSequence?????????????????????????????????String????????????????????????
                                //Toast.makeText(UserDetailActivity.this, input, Toast.LENGTH_SHORT).show();

                                System.out.println(input.toString());
                                // ???????????????????????????????????????????????????????????????
                                MMKV.defaultMMKV().encode("NickName",input.toString());
                                showNickName.setText(input.toString());
                            }
                        })
                        .positiveText("??????")
                        .show();

            }
        });
        layout_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] contentSex = new String[]{"???", "???"};
                new MaterialDialog.Builder(UserActivity.this)
                        .title("????????????")
                        .items(contentSex)
                        .itemsCallbackSingleChoice(MMKV.defaultMMKV().decodeString("Sex","?????????").equals("???") ? 1 : 0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                System.out.println("???????????????" + which);
                                System.out.println("??????????????????" + text);

                                MMKV.defaultMMKV().encode("Sex",text.toString());
                                showSex.setText(text.toString());
                                return true;
                            }
                        })
                        .show();
            }
        });

        layout_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityConfig cityConfig = new CityConfig.Builder()
                        .setCityWheelType(CityConfig.WheelType.PRO_CITY)//??????????????????
                        .build();
                mPicker.setConfig(cityConfig);
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                       String location = city.getName();
                       showLocation.setText(location);
                        MMKV.defaultMMKV().encode("Location",location);
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(getApplicationContext(),"?????????");
                    }
                });
                mPicker.showCityPicker();
 /*               new MaterialDialog.Builder(UserActivity.this)
                        .title("????????????")
                        .inputRangeRes(1, 38, R.color.colorBlack)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("?????????????????????????????????", MMKV.defaultMMKV().decodeString("Location","?????????"), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                MMKV.defaultMMKV().encode("Location",input.toString());
                                showLocation.setText(input.toString());
                            }
                        })
                        .positiveText("??????")
                        .show();*/
            }
        });

        layout_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(UserActivity.this)
                        .title("??????????????????")
                        .inputRangeRes(1, 38, R.color.colorBlack)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("?????????????????????????????????", MMKV.defaultMMKV().decodeString("Signature","?????????"), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                                System.out.println(input.toString());

                                MMKV.defaultMMKV().encode("Signature",input.toString());
                                showSignature.setText(input.toString());
                            }
                        })
                        .positiveText("??????")
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://???????????????????????????????????????????????????
                // ?????????????????????,???????????????????????????

                if(progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);

                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                userInfo = new UInfo();//??????userInfo
                userInfo.setName(MMKV.defaultMMKV().decodeString("name",""));
                userInfo.setuDetail(new UInfo.UDetail());
                userInfo.getuDetail().setNickName(MMKV.defaultMMKV().decodeString("NickName",""));
                userInfo.getuDetail().setUserSex(MMKV.defaultMMKV().decodeString("Sex",""));
                userInfo.getuDetail().setUserLocation(MMKV.defaultMMKV().decodeString("Location",""));
                userInfo.getuDetail().setUserSignature(MMKV.defaultMMKV().decodeString("Signature",""));
                userInfo.getuDetail().setImageUrl(MMKV.defaultMMKV().decodeString("Url",""));

                ThreadPool.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        UserInfoResponse response = InfoNetWork.setInfo(userInfo);
                        if(response.getStatus()){//????????????
                            Log.d("UserDetailActivity"," success in create: " + response.getMessage());
                        }
                        else{//????????????
                            Log.d("UserDetailActivity"," fail in create: " + response.getMessage());
                        }

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(progressBar.getVisibility() == View.VISIBLE) {
                                    progressBar.setVisibility(View.GONE);
                                    startActivity(intent);
                                    UserActivity.this.finish();
                                }
                            }
                        });

                    }
                });

                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_USER_PROFILE:
                if(resultCode == Activity.RESULT_OK && data != null){
                    Uri uri = data.getData();
                    if(uri != null){
                        userAvatar = findViewById(R.id.user_avatar);
                        GlideHelper helper = new GlideHelper(getApplicationContext());

                        String uri_s = uri.toString();
                        helper.setGilde(uri_s,userAvatar);
                        Log.d("UserActivity","url is " + uri_s);
                        MMKV.defaultMMKV().encode("Url",uri_s);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"??????????????????",Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    // ???????????????
    private void initData(String userId) {

        ThreadPool.getExecutor().execute (new Runnable() {
            @Override
            public void run() {

                UserInfoResponse response = InfoNetWork.getInfo(userId);
                if(response.getStatus()){//????????????
                    userInfo = response.getData();
                    MMKV.defaultMMKV().encode("Sex",userInfo.getuDetail().getUserSex());
                    //??????
                    MMKV.defaultMMKV().encode("Location",userInfo.getuDetail().getUserLocation());
                    //????????????
                    MMKV.defaultMMKV().encode("Signature",userInfo.getuDetail().getUserSignature());
                    //??????
                    MMKV.defaultMMKV().encode("NickName",userInfo.getuDetail().getnickname());
                    //????????????
                    MMKV.defaultMMKV().encode("Url",userInfo.getuDetail().getImageUrl());
                    Log.d("UserDetailActivity",response.getMessage());
                }
                else {//????????????
                    Log.d("UserDetailActivity",response.getMessage());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showNickName.setText(MMKV.defaultMMKV().decodeString("NickName","?????????"));
                        showSex.setText(MMKV.defaultMMKV().decodeString("Sex","?????????"));
                        showLocation.setText(MMKV.defaultMMKV().decodeString("Location","?????????"));
                        showSignature.setText(MMKV.defaultMMKV().decodeString("Signature","?????????"));
                        String curImagePath = MMKV.defaultMMKV().decodeString("Url","");
                        helper = new GlideHelper(getApplicationContext());
                        helper.setGilde(curImagePath,userAvatar);
                        if(progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });




    }


}
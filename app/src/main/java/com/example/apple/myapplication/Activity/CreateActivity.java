package com.example.apple.myapplication.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.myapplication.Application.Information;
import com.example.apple.myapplication.Bean.Login;
import com.example.apple.myapplication.R;
import com.example.apple.myapplication.tools.DoubleClickJuage;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by apple on 15/9/22.
 */
public class CreateActivity extends Activity implements View.OnClickListener {

    private EditText editRoom, editPassword, editNickname;
    private TextView textLocation;
    private Button buttonCreate;
    private Double lat,lon;
    private String roomId, password, nickname;
    private Switch locationSwitch;
    private Information information;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //初始化 Bmob SDK
        Bmob.initialize(this, "ca1ecf8f5059480dac61d011da44f5db");
        // 初始化控件
        init();
        //设置监听
        buttonCreate.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void init() {
        editNickname = (EditText) findViewById(R.id.edit_nickname);
        editRoom = (EditText) findViewById(R.id.edit_room_id);
        editPassword = (EditText) findViewById(R.id.edit_password);
        buttonCreate = (Button) findViewById(R.id.button_create);
        textLocation= (TextView) findViewById(R.id.text_location);
        locationSwitch = (Switch) findViewById(R.id.switch_location);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    initGPS();
                } else {

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_create:

                if (DoubleClickJuage.isFastDoubleClick()) {
                    return;
                }
                nickname = editNickname.getText().toString();
                roomId = editRoom.getText().toString();
                password = editPassword.getText().toString();

                //判断是否为空值
                if (roomId.equals("")) {
                    Toast.makeText(CreateActivity.this, "请输入你的房间号", Toast.LENGTH_SHORT).show();
                } else if (nickname.equals("")) {
                    Toast.makeText(CreateActivity.this, "请输入你的昵称", Toast.LENGTH_SHORT).show();
                } else {

                    BmobQuery<Login> query = new BmobQuery<Login>();
                    //查询room_id叫“room_id(代表的String)”的数据
                    query.addWhereEqualTo("room_id", roomId);
                    //返回10条数据，如果不加上这条语句，默认返回10条数据
                    query.setLimit(10);
                    //执行查询方法
                    query.findObjects(this, new FindListener<Login>() {
                        @Override
                        public void onSuccess(List<Login> object) {
                            // TODO Auto-generated method stub
                            if (object.size() == 0) {
                                //实例化
                                Login login = new Login();
                                //判端是否要加密码
                                if (!password.equals("")) {
                                    //传入密码
                                    login.setPassword(password);
                                }
                                if (locationSwitch.isChecked()){
                                    login.setLon(lon+"");
                                    login.setLat(lat+"");
                                }
                                //设置要传入的内容
                                login.setNickname(nickname);
                                login.setRoom_id(roomId);
                                //开始传入
                                login.save(CreateActivity.this, new SaveListener() {

                                    @Override
                                    public void onSuccess() {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(CreateActivity.this, "成功", Toast.LENGTH_SHORT).show();

                                        Toast.makeText(CreateActivity.this, "成功登录", Toast.LENGTH_SHORT).show();
                                        //把昵称和房间号保存到Application
                                        information = (Information) getApplication();
                                        information.setRoomId(roomId);
                                        information.setNickname(nickname);
                                        Intent intentToChat = new Intent(CreateActivity.this, ChatActivity.class);
                                        startActivity(intentToChat);


                                    }


                                    @Override
                                    public void onFailure(int code, String arg0) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(CreateActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(CreateActivity.this, "此房间以存在，请更换房间名", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(int code, String msg) {
                            // TODO Auto-generated method stub
                            Toast.makeText(CreateActivity.this, "系统错误，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
        }
    }

    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(CreateActivity.this, "请打开GPS",
                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                            locationSwitch.setChecked(false);
                        }
                    });
            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
        } else {
            getGps();
        }
    }

    private void getGps() {
        LocationManager manager;
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //检查权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //如果要用GPS就把下面的NETWORK_PROVIDER改成GPS_PROVIDER,但是GPS不稳定
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, locationLinstener);
    }

    LocationListener locationLinstener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            String currentPosition=+location.getLatitude()+"    "+location.getLongitude();
            textLocation.setText(currentPosition);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Create Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

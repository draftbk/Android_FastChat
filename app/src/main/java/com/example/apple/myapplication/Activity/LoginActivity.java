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
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.myapplication.Application.Information;
import com.example.apple.myapplication.Bean.Login;
import com.example.apple.myapplication.R;
import com.example.apple.myapplication.tools.DoubleClickJuage;
import com.example.apple.myapplication.tools.LocationTools;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by apple on 15/9/20.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText editRoom,editNickname;
    private Button buttonLogin,buttonCreate;
    private String roomId,nickname,password,roomlon,roomlat;
    private Double lat,lon;
    private Information information;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //初始化 Bmob SDK
        Bmob.initialize(this, "ca1ecf8f5059480dac61d011da44f5db");
        // 初始化控件
        init();
        //设置监听
        buttonLogin.setOnClickListener(this);
        buttonCreate.setOnClickListener(this);

    }

    private void init() {
        editRoom=(EditText)findViewById(R.id.edit_room_id);
        editNickname=(EditText)findViewById(R.id.edit_nickname);
        buttonLogin=(Button)findViewById(R.id.button_login);
        buttonCreate=(Button)findViewById(R.id.button_create);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                //不能连续按两下
                if (DoubleClickJuage.isFastDoubleClick()) {
                    return;
                }
                //获取房间号和昵称
                roomId=editRoom.getText().toString();
                nickname=editNickname.getText().toString();
                //检查房间是否满员
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
                        if (object.size() <6&&object.size()>=1) {
                            //判断是否有密码
                            Boolean judge=false;
                            Boolean judgeName=false;
                            Boolean judgeLocation=false;
                            Log.d("test","password之前"+"     "+password);
                            Log.d("test","object"+"     "+object);
                            for (Login login : object) {
                                password=login.getPassword();
                                roomlon=login.getLon();
                                roomlat=login.getLat();
                                Log.e("test11","roomlon"+"     "+roomlon);
                                Log.d("test","login.getCreatedAt()"+"     "+login.getCreatedAt());
                                if (login.getNickname().equals(nickname)){
                                    judgeName=true;

                                }
                                if (!(password==null)){
                                    judge=true;
                                }
                                if (!(roomlon==null)){
                                    judgeLocation=true;
                                }

                            }
                            if (judgeName){
                                Toast.makeText(LoginActivity.this, "用户名已存在，请重新输入", Toast.LENGTH_SHORT).show();
                            }else {

                                if (!judge) {
                                    //实例化
                                    Login login = new Login();
                                    //设置要传入的内容
                                    login.setNickname(nickname);
                                    login.setRoom_id(roomId);
                                    //开始传入
                                    login.save(LoginActivity.this, new SaveListener() {

                                        @Override
                                        public void onSuccess() {
                                            // TODO Auto-generated method stub
                                            Toast.makeText(LoginActivity.this, "成功登录", Toast.LENGTH_SHORT).show();
                                            //把昵称和房间号保存到Application
                                            information = (Information) getApplication();
                                            information.setRoomId(roomId);
                                            information.setNickname(nickname);
                                            Intent intentToChat = new Intent(LoginActivity.this, ChatActivity.class);
                                            startActivity(intentToChat);

                                        }


                                        @Override
                                        public void onFailure(int code, String arg0) {
                                            // TODO Auto-generated method stub
                                            Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    final boolean finalJudgeLoaction=judgeLocation;
                                    final EditText editPassword=new EditText(LoginActivity.this);
                                    new AlertDialog.Builder(LoginActivity.this).setTitle("请输入密码").setIcon(
                                            android.R.drawable.ic_dialog_info).setView(
                                            editPassword).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(editPassword.getText().toString().equals(password)){
                                                if (finalJudgeLoaction){
                                                    initGPS();
                                                }else {
                                                    InTheRoom();
                                                }
                                            }else {
                                                Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }).setNegativeButton("取消", null).show();
                                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else if (object.size()==6){
                            Toast.makeText(LoginActivity.this, "此房间以满员", Toast.LENGTH_SHORT).show();
                        }else if (object.size()==0){
                            Toast.makeText(LoginActivity.this, "此房间不存在", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(int code, String msg) {
                        // TODO Auto-generated method stub
                        Toast.makeText(LoginActivity.this, "系统错误，请重试", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.button_create:
                //不能连续按两下
                if (DoubleClickJuage.isFastDoubleClick()) {
                    return;
                }

                Intent intent=new Intent(LoginActivity.this,CreateActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void InTheRoom() {
        //实例化
        Login login = new Login();
        //设置要传入的内容
        login.setNickname(nickname);
        login.setRoom_id(roomId);
        login.setPassword(password);
        //开始传入
        login.save(LoginActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "成功登录", Toast.LENGTH_SHORT).show();
                //把昵称和房间号保存到Application
                information = (Information) getApplication();
                information.setRoomId(roomId);
                information.setNickname(nickname);
                Intent intentToChat = new Intent(LoginActivity.this, ChatActivity.class);
                startActivity(intentToChat);

            }


            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InTheGPSRoom() {
        //实例化
        Login login = new Login();
        //设置要传入的内容
        login.setNickname(nickname);
        login.setRoom_id(roomId);
        login.setPassword(password);
        login.setLat(lat+"");
        login.setLon(lon+"");
        //开始传入
        login.save(LoginActivity.this, new SaveListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "成功登录", Toast.LENGTH_SHORT).show();
                //把昵称和房间号保存到Application
                information = (Information) getApplication();
                information.setRoomId(roomId);
                information.setNickname(nickname);
                Intent intentToChat = new Intent(LoginActivity.this, ChatActivity.class);
                startActivity(intentToChat);

            }


            @Override
            public void onFailure(int code, String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(LoginActivity.this, "请打开GPS",
                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("该房间要求打开GPS");
            dialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面
                            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
                            dialog.setMessage("获取经纬度");
                            dialog.setPositiveButton("获取",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            getGps();
                                        }
                                    });
                            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    arg0.dismiss();
                                }
                            });
                            dialog.show();
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
        Toast.makeText(LoginActivity.this, "请等待定位", Toast.LENGTH_SHORT).show();
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(LoginActivity.this);
            dialog.setMessage(currentPosition);
            dialog.setPositiveButton("进入",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Log.e("test11",roomlat);
                            Double otherLat= Double.valueOf(roomlat);
                            Double otherLon= Double.valueOf(roomlon);
//                        得到距离
                            Double dis= LocationTools.getDistance(lon,lat,otherLon,otherLat);
                            if (dis<2000){
                                InTheGPSRoom();
                            }else {
                                Toast.makeText(LoginActivity.this, "距离太远无法加入", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
            dialog.setNeutralButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });
            dialog.show();
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

}
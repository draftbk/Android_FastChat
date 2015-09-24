package com.example.apple.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.myapplication.Application.Information;
import com.example.apple.myapplication.Bean.Login;
import com.example.apple.myapplication.R;
import com.example.apple.myapplication.tools.DoubleClickJuage;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;


/**
 * Created by apple on 15/9/22.
 */
public class CreateActivity extends Activity implements View.OnClickListener {

    private EditText editRoom,editPassword,editNickname;
    private Button buttonCreate;
    private String roomId,password,nickname;
    private Information information;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //初始化 Bmob SDK
        Bmob.initialize(this, "ca1ecf8f5059480dac61d011da44f5db");
        // 初始化控件
        init();
        //设置监听
        buttonCreate.setOnClickListener(this);
    }

    private void init() {
        editNickname=(EditText)findViewById(R.id.edit_nickname);
        editRoom=(EditText)findViewById(R.id.edit_room_id);
        editPassword=(EditText)findViewById(R.id.edit_password);
        buttonCreate=(Button)findViewById(R.id.button_create);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_create:

                if (DoubleClickJuage.isFastDoubleClick()) {
                    return;
                }
                nickname=editNickname.getText().toString();
                roomId=editRoom.getText().toString();
                password=editPassword.getText().toString();

                //判断是否为空值
                if(roomId.equals("")){
                    Toast.makeText(CreateActivity.this, "请输入你的房间号", Toast.LENGTH_SHORT).show();
                }else if(nickname.equals("")){
                    Toast.makeText(CreateActivity.this, "请输入你的昵称", Toast.LENGTH_SHORT).show();
                }else {

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
                                Login login=new Login();
                                //判端是否要加密码
                                if (!password.equals("")) {
                                    //传入密码
                                    login.setPassword(password);
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
                            }else {
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
}

package com.example.apple.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.myapplication.Bean.Login;
import com.example.apple.myapplication.R;
import com.example.apple.myapplication.tools.DoubleClickJuage;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by apple on 15/9/20.
 */
public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText editRoom,editNickname;
    private Button buttonLogin,buttonCreate;
    private String roomId,nickname,password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                            Log.d("test","password之前"+"     "+password);
                            Log.d("test","object"+"     "+object);
                            for (Login login : object) {
                                password=login.getPassword();
                                Log.d("test","password"+"     "+password);
                                Log.d("test","login.getCreatedAt()"+"     "+login.getCreatedAt());
                                if (!(password==null)){
                                    judge=true;
                                }
                            }
                            if (!judge){
                                //实例化
                                Login login=new Login();
                                //设置要传入的内容
                                login.setNickname(nickname);
                                login.setRoom_id(roomId);
                                //开始传入
                                login.save(LoginActivity.this, new SaveListener() {

                                    @Override
                                    public void onSuccess() {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(LoginActivity.this, "成功登录", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onFailure(int code, String arg0) {
                                        // TODO Auto-generated method stub
                                        Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else {
                                Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
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

}
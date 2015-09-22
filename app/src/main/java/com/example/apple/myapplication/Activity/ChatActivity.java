package com.example.apple.myapplication.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apple.myapplication.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by apple on 15/9/20.
 */
public class ChatActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Bmob.initialize(this, "ca1ecf8f5059480dac61d011da44f5db");
//        final Person p2 = new Person();
//        p2.setName("lucky");
//        p2.setAddress("北京海淀");
//        p2.save(this, new SaveListener() {
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "添加数据成功，返回objectId为：" + p2.getObjectId(),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "创建数据失败：" + msg,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });

//        //更新Person表里面id为6b6c11c537的数据，address内容更新为“北京朝阳”
//        p2.setAddress("北京朝阳");
//        p2.update(this, "f3d4208630", new UpdateListener() {
//            @Override
//            public void onSuccess() {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "更新成功：" + p2.getUpdatedAt(),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                // TODO Auto-generated method stub
//                Toast.makeText(getApplicationContext(), "更新失败：" + msg,
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

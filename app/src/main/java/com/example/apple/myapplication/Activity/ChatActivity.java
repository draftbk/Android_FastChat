package com.example.apple.myapplication.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.apple.myapplication.Application.Info;
import com.example.apple.myapplication.R;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by apple on 15/9/20.
 */
public class ChatActivity extends Activity {

    private Info info;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //获取Application
        info= (Info) getApplication();


    }
}

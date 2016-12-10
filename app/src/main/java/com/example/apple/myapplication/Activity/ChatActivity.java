package com.example.apple.myapplication.Activity;

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.myapplication.Adapter.ChatMessageAdapter;
import com.example.apple.myapplication.Application.Information;
import com.example.apple.myapplication.Bean.Content;
import com.example.apple.myapplication.Bean.Login;
import com.example.apple.myapplication.R;
import com.example.apple.myapplication.tools.GetNumberFromString;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by apple on 15/9/20.
 */
public class ChatActivity extends Activity implements View.OnClickListener {

    private Information information;
    private ListView mMsgs;
    private ChatMessageAdapter mAdapter;
    private List<Content> mDatas;
    private EditText mInputMsg;
    private Button mSendMsg,mDis;
    private ChatMessageAdapter adapter;
    private String latestTime,objectId;
    private TextView chatTitle;
    private String lastContent="";
    private String lastCreatedAt = "2016-12-10 11:07:37";



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        lastCreatedAt=df.format(new Date());// new Date()为获取当前系统时间
        lastCreatedAt=lastCreatedAt+" 00:00:00";
        //初始化 Bmob SDK
        Bmob.initialize(this, "ca1ecf8f5059480dac61d011da44f5db");

        //获取Application
        information = (Information) getApplication();
        //实例化数组
        mDatas= new ArrayList<Content>();
        //实例化控件
        initView();
        //设置房间名
        chatTitle.setText(information.getRoomId());
        //设置监听
        mSendMsg.setOnClickListener(this);


        Thread thread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //实时监听
                adapter=new ChatMessageAdapter(ChatActivity.this,mDatas,information.getNickname());
                mMsgs.setAdapter(adapter);
                while (true) {
                    try {
                        //判断是否有新消息定义一个Boolean
                        CheckMessage();
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread.start();

    }

    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //删除账号
        BmobQuery<Login> query = new BmobQuery<Login>();
        query.addWhereEqualTo("room_id",information.getRoomId());
        query.addWhereEqualTo("nickname",information.getNickname());
        query.setLimit(10);
        Log.d("test", "有到这里吗" + objectId);
        //执行查询方法
        query.findObjects(this, new FindListener<Login>() {
            @Override
            public void onSuccess(List<Login> object) {
                // TODO Auto-generated method stub
                for (Login login : object) {
                    objectId=login.getObjectId();
                }
            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
            }
        });
        Login login = new Login();
        login.setObjectId(objectId);
        Log.d("test", "objectId" + objectId);
        login.delete(this, new DeleteListener() {

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(ChatActivity.this, "你的号码已被删除", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(ChatActivity.this, "系统错误", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void CheckMessage() {

        BmobQuery<Content> query = new BmobQuery<Content>();
        //查询playerName叫“比目”的数据
        query.addWhereEqualTo("room_id", information.getRoomId());
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(10);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date lastDate = sdf.parse(lastCreatedAt);
            query.addWhereGreaterThan("createdAt",new BmobDate(lastDate));
            query.order("createdAt");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //执行查询方法
        query.findObjects(this, new FindListener<Content>() {
            @Override
            public void onSuccess(List<Content> object) {
                // TODO Auto-generated method stub
                for (Content content : object) {
                    //获取上一次最近时间

                    Content content1=new Content();
                    content1.setNickname(content.getNickname());
                    content1.setRoom_id(content.getRoom_id());
                    content1.setContent(content.getContent());
                    content1.setTime(content.getCreatedAt());
                    //更新最近的接受到的聊天消息的时间
                    if (lastContent.equals(content.getContent())&&
                            lastCreatedAt.equals(content.getCreatedAt())){

                    }else if (GetNumberFromString.change(lastCreatedAt)>=GetNumberFromString.change(content.getCreatedAt())){

                    } else {
                        //保存最近的时间
                        lastCreatedAt=content.getCreatedAt();
                        mDatas.add(content1);
                        lastContent=content1.getContent();
                    }


                }

                adapter.notifyDataSetChanged();

            }
            @Override
            public void onError(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(ChatActivity.this, "获取消息失败", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void initView()
        {
            chatTitle=(TextView)findViewById(R.id.chat_title);
            mMsgs = (ListView) findViewById(R.id.id_listview_msgs);
            mInputMsg = (EditText) findViewById(R.id.id_input_msg);
            mSendMsg = (Button) findViewById(R.id.id_send_msg);
            mDis= (Button) findViewById(R.id.send_yuyin);
            mDis.setOnClickListener(this);
        }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_yuyin:
                Log.e("test","11111");
                String mess1="我离房主的距离："+information.getDis()+"m";
                if (mess1.equals("")){
                    Toast.makeText(ChatActivity.this, "发送消息不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("test","11111");
                    String nickname=information.getNickname();
                    String roomId=information.getRoomId();
                    //上传消息
                    Content content = new Content();
                    Log.e("test","11111");
                    content.setRoom_id(roomId);
                    content.setNickname(nickname);
                    content.setContent(mess1);
                    content.save(ChatActivity.this, new SaveListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
//                            mInputMsg.setText("");
                            //滚动到底部
//                            try {
//                                Thread.sleep(1000);
//                                mMsgs.setSelection(mMsgs.getBottom());
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int code, String arg0) {
                            // TODO Auto-generated method stub
                            // 添加失败
                        }
                    });
                }
                break;
            case R.id.id_send_msg:
                String mess=mInputMsg.getText().toString();
                if (mess.equals("")){
                    Toast.makeText(ChatActivity.this, "发送消息不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    String nickname=information.getNickname();
                    String roomId=information.getRoomId();
                    //上传消息
                    Content content = new Content();
                    content.setRoom_id(roomId);
                    content.setNickname(nickname);
                    content.setContent(mess);
                    content.save(ChatActivity.this, new SaveListener() {

                        @Override
                        public void onSuccess() {
                            // TODO Auto-generated method stub
                            mInputMsg.setText("");
//                            //滚动到底部
//                            try {
//                                Thread.sleep(1000);
//                                mMsgs.setSelection(mMsgs.getBottom());
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int code, String arg0) {
                            // TODO Auto-generated method stub
                            // 添加失败
                        }
                    });
                }
                
                break;
        }
    }
}

package com.example.apple.myapplication.Application;

import android.app.Application;

/**
 * Created by apple on 15/9/23.
 */
public class Information extends Application {
    private String Nickname;
    private String RoomId;


    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public void onCreate(){
        super.onCreate();
        setRoomId("未能获取");
        setNickname("未能获取");

    }

}

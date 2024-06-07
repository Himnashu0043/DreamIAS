package com.example.dream_ias.activity.socket;

public interface SocketCallbacks {
    void onConnect(Object... args);

    void onDisconnect(Object... args);

    void onConnectError(Object... args);

    void onRoomJoin(Object... args);

    void onMessage(Object... args);

    void onRoomLeave(Object... args);

    void onStartResults(Object... args);
}

package com.example.dream_ias.activity.socket;

import static com.example.dream_ias.activity.socket.SocketConstants.EVENT_ROOM_JOIN;
import static com.example.dream_ias.activity.socket.SocketConstants.EVENT_ROOM_LEAVE;
import static com.example.dream_ias.activity.socket.SocketConstants.SOCKET_URI3;
import static com.example.dream_ias.activity.socket.SocketConstants.START_CONTEST_RESULTS;
import static io.socket.engineio.client.Socket.EVENT_MESSAGE;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class SocketConnection {
    private static Socket mSocket;
    private static SocketCallbacks mListener;


    public static Socket connectSocket(SocketCallbacks listener) throws URISyntaxException {
        mListener = listener;
        mSocket = IO.socket(SOCKET_URI3);
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        //mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(START_CONTEST_RESULTS, onStartResults);
        mSocket.on(EVENT_ROOM_JOIN, onRoomJoin);
        mSocket.on(EVENT_MESSAGE, onMessage);
        mSocket.on(EVENT_ROOM_LEAVE, onLeaveRoom);
        mSocket.connect();
        Log.d("CheckingSocketsHere", "connected ad sockets are on");
        return mSocket;
    }

    public static void disconnectSocket() {
        if (mSocket != null) {
            mSocket.off(Socket.EVENT_CONNECT);
            mSocket.off(Socket.EVENT_DISCONNECT);
            mSocket.off(Socket.EVENT_CONNECT_ERROR);
           // mSocket.off(Socket.EVENT_CONNECT_TIMEOUT);
            mSocket.off(EVENT_ROOM_JOIN);
            mSocket.off(EVENT_MESSAGE);
            mSocket.off(EVENT_ROOM_LEAVE);
            mSocket.disconnect();
            mSocket = null;

        }
    }


    private static Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if (mListener != null)
                mListener.onConnect(args);
        }
    };

    private static Emitter.Listener onLeaveRoom = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if (mListener != null)
                mListener.onRoomLeave(args);
        }
    };

    private static Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if (mListener != null)
                mListener.onDisconnect(args);
        }
    };

    private static Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if (mListener != null)
                mListener.onConnectError(args);
        }
    };

    private static Emitter.Listener onRoomJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            if (mListener != null)
                mListener.onRoomJoin(args);
        }
    };

    private static final Emitter.Listener onStartResults = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mListener != null)
                mListener.onStartResults(args);
        }
    };

    private static Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if (mListener != null)
                mListener.onMessage(args);
        }
    };
}

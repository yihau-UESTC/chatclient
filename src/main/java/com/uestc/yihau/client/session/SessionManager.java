package com.uestc.yihau.client.session;


import com.google.protobuf.GeneratedMessageV3;
import com.uestc.yihau.client.model.Response;
import com.uestc.yihau.client.serial.Serializer;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 会话管理者
 */
public class SessionManager {

    private static final ConcurrentHashMap<Long, Session> onlineSessions = new ConcurrentHashMap<Long, Session>();

    public static boolean putSession(long playerId, Session session){
        if (!onlineSessions.containsKey(playerId)){
            boolean success = onlineSessions.putIfAbsent(playerId, session) == null ? true : false;
            return success;
        }
        return false;
    }

    public static Session removeSession(long playerId){
        return onlineSessions.remove(playerId);
    }

    public static <T extends GeneratedMessageV3> void sendMessage(long playerId, short module, short cmd, T message){
        Session session = onlineSessions.get(playerId);
        if (session != null && session.isConnected()){
            Response response = new Response(module, cmd, message.toByteArray());
            session.write(response);
        }
    }

    public static boolean isOnlinePlayer(long playerId){
        return onlineSessions.containsKey(playerId);
    }

    public static Set<Long> getOnlinePlayers(){
        return Collections.unmodifiableSet(onlineSessions.keySet());
    }

}

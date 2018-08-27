package com.uestc.yihau.client.handler;


import com.uestc.yihau.client.annotion.SocketCommand;
import com.uestc.yihau.client.annotion.SocketMoudule;
import com.uestc.yihau.client.module.ModuleId;
import com.uestc.yihau.client.module.chat.ChatCmd;

@SocketMoudule(module = ModuleId.CHAT)
public interface ClientMessageHandler {
    @SocketCommand(cmd = ChatCmd.PUBLIC_CHAT)
    public void publicMessage(int resultCode, byte[] data);

    @SocketCommand(cmd = ChatCmd.PUSH_CHAT)
    public void receiveMessage(int resultCode, byte[] data);
}

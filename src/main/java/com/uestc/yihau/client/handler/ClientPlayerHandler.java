package com.uestc.yihau.client.handler;


import com.uestc.yihau.client.annotion.SocketCommand;
import com.uestc.yihau.client.annotion.SocketMoudule;
import com.uestc.yihau.client.module.ModuleId;
import com.uestc.yihau.client.module.player.PlayerCmd;

@SocketMoudule(module = ModuleId.PLAYER)
public interface ClientPlayerHandler {

    @SocketCommand(cmd = PlayerCmd.REGISTER_AND_LOGIN)
    public void registerAndLogin(int resultCode, byte[] data);

    @SocketCommand(cmd = PlayerCmd.LOGIN)
    public void login(int resultCode, byte[] data);
}

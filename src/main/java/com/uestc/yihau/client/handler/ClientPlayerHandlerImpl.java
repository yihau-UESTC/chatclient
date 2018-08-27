package com.uestc.yihau.client.handler;



import com.google.protobuf.InvalidProtocolBufferException;
import com.uestc.yihau.client.ResultCodeTip;
import com.uestc.yihau.client.model.ResultCode;
import com.uestc.yihau.client.module.player.PlayerModule;
import org.springframework.stereotype.Component;

@Component
public class ClientPlayerHandlerImpl implements ClientPlayerHandler {


    @Override
    public void registerAndLogin(int resultCode, byte[] data) {

        PlayerModule.PlayerResponse playerResponse = null;
        try {
            playerResponse = PlayerModule.PlayerResponse.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.println(ResultCodeTip.getTipContent(resultCode));
        if (resultCode == ResultCode.SUCCESS){
            System.out.println(playerResponse.toString());
        }
    }

    @Override
    public void login(int resultCode, byte[] data) {
        PlayerModule.PlayerResponse playerResponse = null;
        try {
            playerResponse = PlayerModule.PlayerResponse.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        System.out.println(ResultCodeTip.getTipContent(resultCode));
        if (resultCode == ResultCode.SUCCESS){
            System.out.println(playerResponse.toString());
        }
    }
}

package com.uestc.yihau.client.handler;



import com.google.protobuf.InvalidProtocolBufferException;
import com.uestc.yihau.client.ResultCodeTip;
import com.uestc.yihau.client.model.ResultCode;
import com.uestc.yihau.client.module.chat.ChatModule;
import org.springframework.stereotype.Component;

@Component
public class ClientMessageHandlerImpl implements ClientMessageHandler {

    @Override
    public void publicMessage(int resultCode, byte[] data) {
        if (resultCode == ResultCode.SUCCESS){
            System.out.println("发送成功");
        }else {
            System.out.println(ResultCodeTip.getTipContent(resultCode));
        }
    }

    @Override
    public void receiveMessage(int resultCode, byte[] data) {
        if (resultCode == ResultCode.SUCCESS){
            ChatModule.MessageResponse messageResponse = null;
            try {
                messageResponse = ChatModule.MessageResponse.parseFrom(data);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            System.out.println(messageResponse.getPlayerName() + "说：" + messageResponse.getContent());
        }else {
            System.out.println("未知错误");
        }
    }
}

package com.uestc.yihau.client.model;

public class Response {
    private short module;
    private short cmd;
    private int stateCode = ResultCode.SUCCESS;
    private byte[] data;

    public Response() {
    }

    public Response(short module, short cmd) {
        this.module = module;
        this.cmd = cmd;
    }

    public Response(short module, short cmd, byte[] data) {
        this.module = module;
        this.cmd = cmd;
        this.data = data;
    }

    public short getModule() {
        return module;
    }

    public void setModule(short module) {
        this.module = module;
    }

    public short getCmd() {
        return cmd;
    }

    public void setCmd(short cmd) {
        this.cmd = cmd;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getDataLen(){
        if (data == null)return 0;
        return data.length;
    }
}

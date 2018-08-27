package com.uestc.yihau.client.model;


import com.google.protobuf.GeneratedMessageV3;
import com.uestc.yihau.client.serial.Serializer;

/**
 * 包装结果
 * @param <T>
 */
public class Result<T extends GeneratedMessageV3> {
    private int resultCode;
    private T content;

    public static <T extends GeneratedMessageV3> Result<T> SUCCESS(T content){
        Result<T> result = new Result<T>();
        result.resultCode = ResultCode.SUCCESS;
        result.content = content;
        return result;
    }

    public static <T extends GeneratedMessageV3> Result<T> SUCCESS(){
        Result<T> result = new Result<T>();
        result.resultCode = ResultCode.SUCCESS;
        return result;
    }

    public static <T extends GeneratedMessageV3> Result<T> ERROR(int resultCode){
        Result<T> result = new Result<T>();
        result.resultCode = resultCode;
        return result;
    }

    public static <T extends GeneratedMessageV3> Result<T> valueOf(int resultCode, T content){
        Result<T> result = new Result<T>();
        result.resultCode = resultCode;
        result.content = content;
        return result;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public boolean isSuccess(){
        return this.resultCode == ResultCode.SUCCESS;
    }
}

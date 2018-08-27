package com.uestc.yihau.client.serial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSerializer extends Serializer {

    private int id;
    private String name;
    private List<Integer> numList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getNumList() {
        return numList;
    }

    public void setNumList(List<Integer> numList) {
        this.numList = numList;
    }

    @Override
    protected void read() {
        this.id = readInt();
        this.name = readString();
        this.numList = readList(Integer.class);
    }

    @Override
    protected void write() {
        this.writeInt(id);
        this.writeString(name);
        this.writeList(numList);
    }

    public static void main(String[] args) {
        TestSerializer t1 = new TestSerializer();
        t1.setId(1000);
        t1.setName("yihau");
        List<Integer> list = new ArrayList<Integer>();
        list.add(123);
        list.add(456);
        t1.setNumList(list);
        byte[] bytes = t1.getBytes();
        System.out.println(Arrays.toString(bytes));
        System.out.println("==================================");
        TestSerializer t2 = new TestSerializer();
        t2.readFromBytes(bytes);
        System.out.println(t2.getId());
        System.out.println(t2.getName());
        System.out.println(t2.getNumList().toString());
    }
}

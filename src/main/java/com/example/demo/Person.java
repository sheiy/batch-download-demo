package com.example.demo;

public class Person {

    private String name;
    private String id;
    private String type;
    private String mobile;
    private String nationId;
    private String bankCard;

    public Person(String name, String id, String type, String mobile, String nationId, String bankCard) {
        this.name = name;
        this.id = id;
        this.type = type;
        this.mobile = mobile;
        this.nationId = nationId;
        this.bankCard = bankCard;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNationId() {
        return nationId;
    }

    public void setNationId(String nationId) {
        this.nationId = nationId;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }
}

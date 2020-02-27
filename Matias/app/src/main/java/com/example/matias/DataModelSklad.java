package com.example.matias;

public class DataModelSklad {


    String pin;
    String num;
    String data;
    String numbank;

    public DataModelSklad(String pin, String num, String data, String numbank) {
        this.pin = pin;
        this.num = num;
        this.numbank = numbank;
        this.data = data;

    }

    public String getpin() {
        return pin;
    }

    public String getnum() {
        return num;
    }

    public String getdata() {
        return data;
    }

    public String getnumbank() {
        return numbank;
    }

}

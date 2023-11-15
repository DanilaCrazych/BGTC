package com.example.bgtc;

public class Orders {
    int id;
    String fio;
    String adressot;
    String adressto;
    String phonenum;
    String status;

    public void setId(int id) {
        this.id = id;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public void setAdressot(String adressot) {
        this.adressot = adressot;
    }

    public void setAdressto(String adressto) {
        this.adressto = adressto;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getFio() {
        return fio;
    }

    public String getAdressot() {
        return adressot;
    }

    public String getAdressto() {
        return adressto;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getStatus() {
        return status;
    }

    public Orders(int id, String fio, String adressot, String adressto, String phonenum, String status) {
        this.id = id;
        this.fio = fio;
        this.adressot = adressot;
        this.adressto = adressto;
        this.phonenum = phonenum;
        this.status = status;
    }
}

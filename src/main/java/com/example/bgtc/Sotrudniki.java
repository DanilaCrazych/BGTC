package com.example.bgtc;

public class Sotrudniki {
    int id;
    String fio;
    String auto;
    String grz;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getGrz() {
        return grz;
    }

    public void setGrz(String grz) {
        this.grz = grz;
    }

    public Sotrudniki(int id, String fio, String auto, String grz) {
        this.id = id;
        this.fio = fio;
        this.auto = auto;
        this.grz = grz;
    }
}

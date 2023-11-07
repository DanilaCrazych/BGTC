package com.example.bgtc;

public class AutoPark {
    int id;
    String auto;
    String grz;

    public void setId(int id) {
        this.id = id;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public void setGrz(String grz) {
        this.grz = grz;
    }

    public int getId() {
        return id;
    }

    public String getAuto() {
        return auto;
    }

    public String getGrz() {
        return grz;
    }

    public AutoPark(int id, String auto, String grz) {
        this.id = id;
        this.auto = auto;
        this.grz = grz;
    }
}

package com.byulvi.shushasocial.ui;

public class testclass {

    public String grade,type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public  int index;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public testclass(String grade, int index,String type) {
        this.grade = grade;
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

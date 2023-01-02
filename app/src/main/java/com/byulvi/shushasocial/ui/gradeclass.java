package com.byulvi.shushasocial.ui;

public class gradeclass {
    public String grade,type;
            int usage;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getUsage() {
        return usage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public gradeclass(String grade, int usage,String type) {
        this.grade = grade;
        this.usage = usage;
        this.type = type;
    }
}

package com.example.stockinginventory;

public class Note {
    private String departmentInfo;
    private String departmentId;

    public Note(){
         // public no-arg constructor needed
    }


    public Note(String departmentInfo, String departmentId){
         this.departmentInfo = departmentInfo;
         this.departmentId = departmentId;
    }

    public String getDepartmentId() {
        return departmentId;
    }
    public String getDepartmentInfo() {
        return departmentInfo;
    }

}

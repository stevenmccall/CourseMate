/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate.meetingshared;

/**
 *
 * @author Harrison
 */
public class GroupCalendar{// extends IndividualSchedule {
    public IndividualSchedule IndSched;
    //initialize map here
    
    public void GroupCalendar() {
        //new instance of map here
    }
    
    public void addNewSched(String netID){
        System.out.println("inside GroupCalendar addNewSched");
        IndSched = new IndividualSchedule(netID);
    }
}

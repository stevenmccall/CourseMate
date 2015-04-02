/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate.meetingshared;

import java.util.LinkedList;
/**
 *
 * @author Harrison
 */
public class IndividualSchedule{// extends GroupCalendar{
    private LinkedList<Time> weekSched;
    String netID;
    
    public IndividualSchedule(String netID){
        weekSched = new LinkedList<>();
        this.netID=netID;
    }

    public void addTime(Time t){
        weekSched.add(t);
    }
    
    public String showSched(){
        String schedule = "";
        
        for(Time day: weekSched)
        {
            schedule += day.toString()+" ";
        }
        return schedule;
    }
    
    public String getNetID(){
        return netID;
    } 
}


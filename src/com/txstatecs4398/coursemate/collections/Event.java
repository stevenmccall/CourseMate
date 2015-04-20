/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate.collections;

/**
 *
 * @author Harrison
 */
public class Event {
    String day;
    int startTime;
    int endTime;
    String event;

    /**
     *
     * @param day
     * @param startTime
     * @param endTime
     * @param event
     */
        public Event(String day, int startTime, int endTime, String event) {
        this.day=day;
        this.startTime=startTime;
        this.endTime=endTime;
        this.event=event;
    }
    
    //getters
    protected String getDay() {
        return day;
    }
    protected int getStartTime() {
        return startTime;
    }
    protected int getEndTime() {
        return endTime;
    }
    protected String getEvent() {
        return event;
    }
    
    //setters
    protected void setDay(String day) {
        this.day=day;
    }
    protected void setStartTime(int startTime) {
        this.startTime=startTime;
    }
    protected void setEndTime(int endTime) {
        this.endTime=endTime;
    }
    protected void setEvent(String event) {
        this.event=event;
    }
    
    /**
     * Print the attributes of the Event, in a formatted fashion.
     */
    //@Override
    public void print() {
        //System.out.format("| %12s | %12s | %8s | %8s | %6d | %9d | %10.2f | %7.2flb %5.2fft | %n", 
        //        "Truck", vin, make, model, year, mileage, price, maxLoadWeight, length);
    }

    @Override
    public String toString() {
        return "Day{ "+day+" Start: "+Integer.toString(startTime)+" End: "
                +Integer.toString(endTime)+" } ";
    }
}


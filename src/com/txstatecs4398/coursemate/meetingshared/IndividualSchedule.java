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
        //System.out.println("inside MeetingSchedule constructor");
        weekSched = new LinkedList<>();
        this.netID=netID;
    }
    
    //functions
    public void addTime(Time t){//when this is called: example: dealership.getVehInv().addVehicle(new PassengerCar(vin, make, model, year, mileage, purchasePrice, bodyStyle));
        System.out.println("inside addTime");
        weekSched.add(t);
    }
    
    public void removeTime(String netID){
        System.out.println("inside removeTime");
    }
    
    public void showSched(){
        System.out.println("inside showSched");
    }
    
    public boolean timeConflict(){
        System.out.println("inside timeConflict");
        return true;
    }
    
    //getters
    public String getNetID(){
        return netID;
    }
    //setters
    public void setNetID(String netID){
        this.netID=netID;
    }
    //print, toString
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void showAll(){
        System.out.println("inside showAll");
    }
    
    private void addVehicle(){
        System.out.println("inside addVehicle");
    }
    
    private void removeVehicle(){
        System.out.println("inside removeVehicle");
    }
    
    private void searchVehicleVin(String vin){
        System.out.println("inside searchVehicleVin");
    }
    
    private void searchVehicleRange(String max, String min){
        System.out.println("inside searchVehicleRange");
    }
    
}


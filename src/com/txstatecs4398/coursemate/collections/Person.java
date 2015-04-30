/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate.collections;

import java.util.LinkedList;

/**
 *
 * @author Harrison
 */
public class Person {// extends GroupCalendar{

    private LinkedList<Event> weekSched;
    String schedule = "";
    String netID;

    public Person(String netID) {
        weekSched = new LinkedList<>();
        this.netID = netID;
        schedule = "";
    }

    public void addEvent(Event t) {
        weekSched.add(t);
    }

    /**
 *
 * @author Steven McCall
     * @param nfcSource showSchedule passed through NFC communication.
 */
    public void nfcParse(String nfcSource) {
        String day = "";
        int start = 0;

        String[] tempOut = nfcSource.replaceAll("Day", "").replaceAll("Start", "").replaceAll("End", "").split("\\W");
        for (String eventFragment : tempOut) {
            if (!eventFragment.equals("")) {
                if (day.equals("")) 
                {
                    day = eventFragment;
                } else if (start == 0) {
                    start = Integer.parseInt(eventFragment);
                } else {
                    addEvent(new Event(day, start, Integer.parseInt(eventFragment), null));

                    day = "";
                    start = 0;
                }
            }
        }
    }

    // Aurash - this makes my life easier.
    public LinkedList<Event> getEvents(){
        return weekSched;
    }
    
    
    public String showSched() {
        schedule = "";
        for (Event day : weekSched) {
            schedule += day.toString() + " ";
        }
        return schedule;
    }

    public String getNetID() {
        return netID;
    }
}

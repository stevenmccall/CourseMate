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
public class IndividualSchedule {// extends GroupCalendar{

    private LinkedList<Time> weekSched;
    String schedule = "";
    String netID;

    public IndividualSchedule(String netID) {
        weekSched = new LinkedList<>();
        this.netID = netID;
        schedule = "";
    }

    public void addTime(Time t) {
        weekSched.add(t);
    }

    /**
 *
 * @author Steven McCall
 */
    public void nfcParse(String nfcSource) {
        String day = "";
        int start = 0;

        String[] tempOut = nfcSource.replaceAll("Day", "").replaceAll("Start", "").replaceAll("End", "").split("\\W");
        for (String time : tempOut) {
            if (!time.equals("")) {
                if (day.equals("")) {
                    if (time.equals("M")) {
                        day = "Monday";
                    } else if (time.equals("T")) {
                        day = "Tuesday";
                    } else if (time.equals("W")) {
                        day = "Wednesday";
                    } else if (time.equals("R")) {
                        day = "Thursday";
                    } else {
                        day = "Friday";
                    }
                } else if (start == 0) {
                    start = Integer.parseInt(time);
                } else {
                    addTime(new Time(day, start, Integer.parseInt(time), null));

                    day = "";
                    start = 0;
                }
            }
        }
    }

    public String showSched() {
        schedule = "";
        for (Time day : weekSched) {
            schedule += day.toString() + " ";
        }
        return schedule;
    }

    public String getNetID() {
        return netID;
    }
}

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
public class MeetingShared {

 //public IndividualSchedule IndSched;//?
    
    public GroupCalendar GrpCal;
    public UserConsole UseCon;
    public ParseBuff parse;
    
    public MeetingShared() {
        System.out.println("inside SharedMeeting constructor");
        //IndSched = new IndividualSchedule("");//?
        GrpCal = new GroupCalendar();
        parse = new ParseBuff(this);
        UseCon = new UserConsole(this);
    }
    
    // public IndividualSchedule getIndSched() {
    //    return IndSched;
    //}
    public GroupCalendar getGrpCal() {
        return GrpCal;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("inside main");
        MeetingShared d = new MeetingShared();

    }
}

    


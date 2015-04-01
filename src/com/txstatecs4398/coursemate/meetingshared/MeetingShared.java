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
    
    public MeetingShared(String HTMLStream) {
        System.out.println("inside SharedMeeting constructor");
        //IndSched = new IndividualSchedule("");//?
        GrpCal = new GroupCalendar();
        parse = new ParseBuff(this, HTMLStream);
    }
    
    // public IndividualSchedule getIndSched() {
    //    return IndSched;
    //}
    public GroupCalendar getGrpCal() {
        return GrpCal;
    }
}

    


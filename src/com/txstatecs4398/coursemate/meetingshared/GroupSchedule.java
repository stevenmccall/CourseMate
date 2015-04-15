/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.txstatecs4398.coursemate.meetingshared;

import java.util.*;
/**
 *
 * @author Harrison
 */
public class GroupSchedule{// extends IndividualSchedule {
    private LinkedList<IndividualSchedule> groupWeek;
    //private List<IndividualSchedule> groupWeek;
    int numOfMembers;

    public GroupSchedule(){
        groupWeek = new LinkedList<>();
        //groupWeek = new ArrayList<>();
        this.numOfMembers = 0;
    }
    
    public void AddPerson(IndividualSchedule IndSched){
                groupWeek.add(IndSched);
                numOfMembers++;
    }
    
    public void RemovePerson(String NetID){
        for (IndividualSchedule IndSched : groupWeek) {
            if (IndSched.getNetID().equalsIgnoreCase(NetID)){
                groupWeek.remove(IndSched);
                numOfMembers--;
                break;
            } 
        }
    }
    
    public LinkedList<IndividualSchedule> returnStorage(){
        return groupWeek;
    }
}

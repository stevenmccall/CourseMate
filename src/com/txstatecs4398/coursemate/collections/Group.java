/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.txstatecs4398.coursemate.collections;

import java.util.*;
/**
 *
 * @author Harrison
 */
public class Group{// extends Person {
    private LinkedList<Person> groupWeek;
    //private List<IndividualSchedule> groupWeek;
    int numOfMembers;

    public Group(){
        groupWeek = new LinkedList<>();
        //groupWeek = new ArrayList<>();
        this.numOfMembers = 0;
    }
    
    public void AddPerson(Person IndSched){
                groupWeek.add(IndSched);
                numOfMembers++;
    }
    
    public void RemovePerson(String NetID){
        for (Person IndSched : groupWeek) {
            if (IndSched.getNetID().equalsIgnoreCase(NetID)){
                groupWeek.remove(IndSched);
                numOfMembers--;
                break;
            } 
        }
    }
    
    public LinkedList<Person> returnStorage(){
        return groupWeek;
    }
}

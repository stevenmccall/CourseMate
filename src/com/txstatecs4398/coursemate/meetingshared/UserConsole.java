/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate.meetingshared;

import java.util.Scanner;

/**
 *
 * @author Harrison
 */
public class UserConsole {
    protected static MeetingShared dealership;
    
    public UserConsole(MeetingShared d){
        UserConsole.dealership = d;
        //VehicleDealership dealership = new VehicleDealership();
        //dealership = d;
        choice();  
    }
    
    private void choice(){
        while(true){
            try{
            System.out.println(listChoice());
            System.out.println("select action");
            Scanner input = new Scanner(System.in);
            int select = Integer.parseInt(input.nextLine());
            switch(select){
                case 1: System.out.println("case 1");
                        dealership.getGrpCal().addNewSched("hk1066");
                        //dealership.getGrpCal().IndSched.addTime(new Time("monday", 1200, 1300, "null"));
                        break;
                case 2: System.out.println("case 2");
                        dealership.getGrpCal().IndSched.addTime(new Time("monday", 1200, 1300, "null"));
                        System.out.println("done test");
                        break;
                case 3: System.out.println("case 3");
                        System.out.println("test parse");
                        break;
                case 4: System.out.println("case 4");
                        break;
                case 5: System.out.println("case 5");
                        break;
                case 6: System.out.println("case 6");
                        System.exit(0);
                default: System.out.println("default");
                         break;
            }
            }catch(Exception ex){
                System.out.println("please enter valid input: 1,2,3,4,5");
            }
        }
    }
    
    private String listChoice(){
        return "\n---------------------------\n"
                + "1. Show all vehicles\n"
                + "2. add vehicle\n"
                + "3. remove vehicle\n"
                + "4. search vehicle by vin\n"
                + "5. search vehicle by range\n"
                + "6. exit program\n";
    }
}


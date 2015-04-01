/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate.meetingshared;

import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author Jack
 */
public class ParseBuff {
        static byte[] buffer =  new byte[(int) new File("ScheduleText.txt").length()];
	static BufferedInputStream f = null;
	static String strbuf;
	static int lastIndex, lastIndex2 = 0;
	static ArrayList<String> classSched = new ArrayList<String>();
	static String findstr = "pm";
	static String start= null, finish = null, day = null;
	static int startime, endtime;
	static Time tt;
    //------------------------------------------------------------------------------ hk added   
        protected static MeetingShared dealership;
    
        public ParseBuff(MeetingShared d){
            System.out.println("inside ParseBuff constructor");
            ParseBuff.dealership = d;
            //dealership.getGrpCal().addNewSched("hk1066");
            dealership.GrpCal.addNewSched("hk1066");//temp fix(netID will come from login)------------------------
            main();//this is temp untill steven adds his athetication program+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        }
    //----------------------------------------------------------------------------- hk added end    
	public static void main() throws NullPointerException {
                System.out.println("inside ParseBuff main1");//-----------------------------------------------------------
		int pmcount, colcount = 0;
		//
		// TODO Auto-generated method stub
		// Read into string from schedule.txt
		//
		try{
			//System.out.println("makeing ScheduleText.txt");
			//PrintWriter writer = new PrintWriter("ScheduleText.txt");
                        //writer.println("hellow world");
                        //writer.close();
                        
                        f = new BufferedInputStream(new FileInputStream("ScheduleText.txt"));//inside C:\Documents and Settings\Harrison\My Documents\NetBeansProjects\VehicleDealership\src\vehicledealership
			//System.out.println("inside ParseBuff main1: strbuff" + f);//-----------------------
                        f.read(buffer);
			strbuf = new String(buffer);
                        //System.out.println("inside ParseBuff main1: strbuff" + strbuf);//-----------------------
			try{
				f.close();
			}catch (IOException ignored){
				
			}
		}catch (IOException ignored){
			System.out.println("File not found.");
		}
                System.out.println("inside ParseBuff main2");//-----------------------------------------------------
// find netID add IndividualSchedule(String netID) via addNewSched(String netID) HERE
                //
		//search string for times... which are when in class
		//am</TD> pm</TD>
		while(lastIndex != -1 || lastIndex2 != -1){
                    System.out.println("inside ParseBuff main3");//-------------------------------------------------
                    //System.out.println("inside ParseBuff main3: strbuf = " + strbuf);//-------------------------------------------------                    
			lastIndex = strbuf.indexOf("am</TD>", lastIndex);
			lastIndex2 = strbuf.indexOf("pm</TD>", lastIndex2);
                        System.out.println("inside ParseBuff main3: lastIndex = " + lastIndex);//-------------------------------------------------
                        System.out.println("inside ParseBuff main3: lastIndex2 = " + lastIndex2);//-------------------------------------------------
			if (lastIndex != -1){
                                System.out.println("inside ParseBuff main3: if found1");//-------------------------------------------------
				int temp = lastIndex - 17;
				String times = strbuf.substring(temp, lastIndex+38);
				//System.out.println(times);
				classSched.add(times);
				lastIndex += 7;
			}else if(lastIndex2 != -1){
                                System.out.println("inside ParseBuff main3: if found2");//-------------------------------------------------
				int temp = lastIndex2 - 17;
				String times = strbuf.substring(temp, lastIndex2+38);
				//System.out.println(times);
				classSched.add(times);
				lastIndex2 += 7;
			}
		}
                System.out.println("inside ParseBuff main4");//-------------------------------------------------
		String findstr = "pm";
		String start= null, finish = null, day = null;
		
		for(String s: classSched){
                    //System.out.println("inside ParseBuff main5: " + s);//--------------------------
			lastIndex = 0;
			pmcount = 0;
			while(lastIndex != -1){
				lastIndex = s.indexOf(findstr, lastIndex);
				if(lastIndex != -1){
					pmcount ++;
					lastIndex += findstr.length();
				}
			}
			colcount = 0;
			lastIndex = 0;
			while(lastIndex != -1){
				lastIndex = s.indexOf(":", lastIndex);
				if(lastIndex != -1){
					colcount++;
					if(colcount == 1){
						start = s.substring(lastIndex - 2, lastIndex + 3);
						lastIndex ++;
					}else if (colcount == 2){
						finish = s.substring(lastIndex-2, lastIndex + 3);
						lastIndex ++;
						break;
					}
				}
			}
			
			lastIndex = 0;
			lastIndex = s.indexOf(">MW<", lastIndex);
			if(lastIndex != -1){
				day = "MW";
			}
			
			lastIndex = 0;
			lastIndex = s.indexOf(">TR<", lastIndex);
			if(lastIndex != -1){
				day = "TR";
			}
			
			lastIndex = 0;
			lastIndex = s.indexOf(">MWF<", lastIndex);
			if(lastIndex != -1){
				day = "MWF";
			}
			
			lastIndex = 0;
			lastIndex = s.indexOf(">M<", lastIndex);
			if(lastIndex != -1){
				day = "M";
			}

			lastIndex = 0;
			lastIndex = s.indexOf(">T<", lastIndex);
			if(lastIndex != -1){
				day = "T";
			}
			
			lastIndex = 0;
			lastIndex = s.indexOf(">W<", lastIndex);
			if(lastIndex != -1){
				day = "W";
			}
			
			lastIndex = 0;
			lastIndex = s.indexOf(">R<", lastIndex);
			if(lastIndex != -1){
				day = "R";
			}
			
			lastIndex = 0;
			lastIndex = s.indexOf(">F<", lastIndex);
			if(lastIndex != -1){
				day = "F";
			}
			
			System.out.println(day);
			start = start.replaceAll("[^0-9]+", "");
			finish = finish.replaceAll("[^0-9]+", "");
			startime = Integer.parseInt(start);
			endtime = Integer.parseInt(finish);
			if (pmcount == 1){
				if(endtime > 100 && endtime < 1200){
					endtime += 1200;
				}
			}else if(pmcount == 2){
				if(startime > 100 && startime < 1200){
					startime += 1200;
				}
				endtime += 1200;
			}
			System.out.println(startime);
			System.out.println(endtime + "   ");
			
 //before this loop is entered netID must be found and the constructor {IndividualSchedule(String netID)} via addNewSched(String netID) must be called
                        tt = new Time(day, startime, endtime, "null");
			dealership.GrpCal.IndSched.addTime(tt);//-------------------------hk changed
			System.out.println("inside ParseBuff main6");//-------------------------------------------------
		}
	}
}

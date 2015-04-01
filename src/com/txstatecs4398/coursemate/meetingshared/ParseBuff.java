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
	static ArrayList<String> classSched = new ArrayList<>();
	static String findstr = "pm";
	static String start= null, finish = null, day = null;
	static int startime, endtime;
	static Time tt;
    //------------------------------------------------------------------------------ hk added   
        protected static MeetingShared dealership;
    
        public ParseBuff(MeetingShared d, String HTMLStream){
            System.out.println("inside ParseBuff constructor");
            ParseBuff.dealership = d;
            //dealership.getGrpCal().addNewSched("hk1066");
            dealership.GrpCal.addNewSched("hk1066");//temp fix(netID will come from login)------------------------
            parse(HTMLStream);//this is temp untill steven adds his athetication program+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        }
    //----------------------------------------------------------------------------- hk added end    
	public void parse(String HTMLStream) throws NullPointerException {
                System.out.println("inside ParseBuff main1");
		int pmcount, colcount = 0;

                strbuf = HTMLStream;

		while(lastIndex != -1 || lastIndex2 != -1){                  
			lastIndex = strbuf.indexOf("am</TD>", lastIndex);
			lastIndex2 = strbuf.indexOf("pm</TD>", lastIndex2);
                        
			if (lastIndex != -1){
				int temp = lastIndex - 17;
				String times = strbuf.substring(temp, lastIndex+38);
				classSched.add(times);
				lastIndex += 7;
			}else if(lastIndex2 != -1){
				int temp = lastIndex2 - 17;
				String times = strbuf.substring(temp, lastIndex2+38);
				classSched.add(times);
				lastIndex2 += 7;
			}
		}
		String findstr = "pm";
		String start= null, finish = null, day = null;
		
		for(String s: classSched){
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
			
 //before this loop is entered netID must be found and the constructor {IndividualSchedule(String netID)} via addNewSched(String netID) must be called
                        tt = new Time(day, startime, endtime, "null");
			dealership.GrpCal.IndSched.addTime(tt);//-------------------------hk changed
		}
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate.meetingshared;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 *
 * @author Jack
 */
public class ParseBuff {
        static String strbuf;
	static int lastIndex, lastIndex2 = 0;
	static ArrayList<String> classSched = new ArrayList<>();
	static String findstr = "pm";
	static String start= null, finish = null, day = null;
	static int startime, endtime;
	static Time tt;
        public IndividualSchedule IndSched;
        
        static byte[] buffer =  new byte[(int) new File("schedule.txt").length()];
	static BufferedInputStream f = null;
	static ArrayList<String> schedStep = new ArrayList<>();
	static ArrayList<String> dayStep = new ArrayList<>();
	static ArrayList<Integer> startStep = new ArrayList<>();
	static ArrayList<Integer> finishStep = new ArrayList<>();
    
        public ParseBuff(String netID){
            IndSched = new IndividualSchedule(netID);
            //this is temp untill steven adds his athetication program+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++       
        }
    //----------------------------------------------------------------------------- hk added end    
	public IndividualSchedule parse(String HTMLStream) throws NullPointerException {
                int pmcount = 0, colcount = 0;
                
		strbuf = HTMLStream;
		//
		//search string for times... which are when in class
		//am</TD> pm</TD>
		
		int foundIndex = 0;
		String step = null;
		foundIndex = strbuf.indexOf("This layout table", foundIndex);
		if(foundIndex != -1){
			step = strbuf.substring(foundIndex);
		}
		while(lastIndex != -1 || lastIndex2 != -1){
			lastIndex = step.indexOf("<tr>", lastIndex);
			lastIndex2 = step.indexOf("</tr>", lastIndex2);
			if (lastIndex != -1 && lastIndex2 != -1){
				String times = step.substring(lastIndex, lastIndex2+ 5);
				classSched.add(times);
				//System.out.println(times);
				lastIndex2 +=5;
				lastIndex = lastIndex2;
			}
		}
		
		int count = 0;
		foundIndex = 0;
		String findstr = "pm";
		String start= null, finish = null, day = null;
		String timestep = null;
		
		for(String s: classSched){
			lastIndex = 0;
			lastIndex2 = 0; 
			count = 0;
			day = null;
			while(lastIndex != -1 || lastIndex2 != -1){
				lastIndex = s.indexOf("<td", lastIndex);
				lastIndex2 = s.indexOf("</td>", lastIndex2);
				if (lastIndex != -1 && lastIndex2 != -1){
					count ++;
					timestep = s.substring(lastIndex, lastIndex2+ 5);
					foundIndex = timestep.indexOf("prod/bwsk", foundIndex);
					if(foundIndex != -1){
						schedStep.add(timestep);
						if(count == 1){
							if(day == null){
								day = "M";
							}
						}else if(count == 2){
							if(day == null){
								day = "T";
							}
						}else if(count == 3){
							if(day == null){
								day = "W";
							}else{
								day += "W";
							}
						}else if(count == 4){
							if(day == null){
								day = "R";
							}else{
								day += "R";
							}
						}else if(count == 5){
							if(day == null){
								day = "F";
							}else{
								day += "F";
							}
						}
					}
					lastIndex2 += 5;
					lastIndex = lastIndex2;
				}
			}
			if(day != null){
				dayStep.add(day);
			}
		}
		int i = 1;
		for(String st: schedStep){
			
			lastIndex = 0;
			pmcount = 0;
			while(lastIndex != -1){
				lastIndex = st.indexOf(findstr, lastIndex);
				if(lastIndex != -1){
					pmcount ++;
					lastIndex += findstr.length();
				}
			}
			
			colcount = 0;
			lastIndex = 0;
			boolean found = false;
			
			while(lastIndex != -1){
				lastIndex = st.indexOf(":", lastIndex);
				
				if(lastIndex != -1){
					colcount++;
					if(colcount == 1){
						start = st.substring(lastIndex - 2, lastIndex + 3);
						start = start.replaceAll("[^0-9]+", "");
						startime = Integer.parseInt(start);
						if(pmcount == 2){
							if(startime > 100 && startime < 1200){
								startime += 1200;
							}
						}
						
						for(int ii: startStep){
							if(ii == startime){
								found = true;
							}
						}
						if(found == false){
							startStep.add(startime);
						}
						lastIndex ++;
					}else if (colcount == 2){
						finish = st.substring(lastIndex-2, lastIndex + 3);
						finish = finish.replaceAll("[^0-9]+", "");
						endtime = Integer.parseInt(finish);
						if(pmcount > 0){
							if(endtime > 100 && endtime < 1200){
								endtime += 1200;
							}
						}
						
						for(Integer ii: finishStep){
							if(ii == endtime){
								found = true;
							}
						}
						if(found == false){
							finishStep.add(endtime);
						}
						lastIndex ++;
						break;
					}
				}
				i++;
			}
		}
			
		for(i = 1; i < dayStep.size(); i++){
			System.out.println(dayStep.get(i));
			System.out.println(startStep.get(i-1));
			System.out.println(finishStep.get(i-1));	
			tt = new Time(dayStep.get(i), startStep.get(i-1), finishStep.get(i-1), null);
			IndSched.addTime(tt);
		}
            return IndSched;
	}
}

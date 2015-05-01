/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.txstatecs4398.coursemate.collections;

import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Jack
 */
public class HTMLParser {

    static String strbuf;
    static int lastIndex, lastIndex2;
    static ArrayList<String> classSched;
    static String findstr = "pm";
    static String start, finish, day;
    static int startime, endtime;
    static Event tt;
    public Person IndSched;

    static byte[] buffer;
    static BufferedInputStream f;
    static ArrayList<String> schedStep;
    static ArrayList<String> dayStep;
    static ArrayList<Integer> startStep;
    static ArrayList<Integer> finishStep;

    public HTMLParser(String netID) {
        IndSched = new Person(netID); 
    }

    public Person parse(String HTMLStream) throws NullPointerException {
        int pmcount = 0, colcount = 0;

        strbuf = HTMLStream;
        //-------reseting variables-------
        lastIndex2 = 0;
        classSched = new ArrayList<>();
        start = null;
        finish = null;
        day = null;
        buffer = new byte[(int) new File("schedule.txt").length()];
        f = null;
        schedStep = new ArrayList<>();
        dayStep  = new ArrayList<>();        
        startStep = new ArrayList<>();
        finishStep = new ArrayList<>();
        //--------------------------------

        int foundIndex = 0;
        String step = null;
        foundIndex = strbuf.indexOf("This layout table", foundIndex);
        if (foundIndex != -1) {
            step = strbuf.substring(foundIndex);
        }
        while (lastIndex != -1 || lastIndex2 != -1) {
            lastIndex = step.indexOf("<tr>", lastIndex);
            lastIndex2 = step.indexOf("</tr>", lastIndex2);
            if (lastIndex != -1 && lastIndex2 != -1) {
                String times = step.substring(lastIndex, lastIndex2 + 5);
                classSched.add(times);
                //System.out.println(times);
                lastIndex2 += 5;
                lastIndex = lastIndex2;
            }
        }

        int count = 0;
        foundIndex = 0;
        String findstr = "pm";
        String start, finish, day, timestep;

        for (String s : classSched) {
            lastIndex = 0;
            lastIndex2 = 0;
            count = 0;
            day = null;
            while (lastIndex != -1 || lastIndex2 != -1) {
                lastIndex = s.indexOf("<td", lastIndex);
                lastIndex2 = s.indexOf("</td>", lastIndex2);
                if (lastIndex != -1 && lastIndex2 != -1) {
                    count++;
                    timestep = s.substring(lastIndex, lastIndex2 + 5);
                    foundIndex = timestep.indexOf("prod/bwsk", foundIndex);
                    if (foundIndex != -1) {
                        schedStep.add(timestep);
                        if (count == 1) {
                            day = "M";
                        } else if (count == 2) {
                            day = "T";
                        } else if (count == 3) {
                            day = "W";
                        } else if (count == 4) {
                            day = "R";
                        } else if (count == 5) {
                            day = "F";
                        }
                        if (day != null) {
                            dayStep.add(day);
                        }
                    }
                    lastIndex2 += 5;
                    lastIndex = lastIndex2;
                }
            }
        }
        int i = 1;
        for (String st : schedStep) {

            lastIndex = 0;
            pmcount = 0;
            while (lastIndex != -1) {
                lastIndex = st.indexOf(findstr, lastIndex);
                if (lastIndex != -1) {
                    pmcount++;
                    lastIndex += findstr.length();
                }
            }

            colcount = 0;
            lastIndex = 0;
            boolean found = false;

            while (lastIndex != -1) {
                lastIndex = st.indexOf(":", lastIndex);

                if (lastIndex != -1) {
                    colcount++;
                    if (colcount == 1) {
                        start = st.substring(lastIndex - 2, lastIndex + 3);
                        start = start.replaceAll("[^0-9]+", "");
                        startime = Integer.parseInt(start);
                        if (pmcount == 2) {
                            if (startime > 100 && startime < 1200) {
                                startime += 1200;
                            }
                        }

                        startStep.add(startime);

                        lastIndex++;
                    } else if (colcount == 2) {
                        finish = st.substring(lastIndex - 2, lastIndex + 3);
                        finish = finish.replaceAll("[^0-9]+", "");
                        endtime = Integer.parseInt(finish);
                        if (pmcount > 0) {
                            if (endtime > 100 && endtime < 1200) {
                                endtime += 1200;
                            }
                        }

                        finishStep.add(endtime);

                        lastIndex++;
                        break;
                    }
                }
                i++;
            }
        }
                //parser fix - duplicate every wednesday that is not a lab.

        int startemp = 0;
        int finishtemp = 0;
        int total = 0;
        int foundtwo = 0;
        int j = 0;
        for (i = 2; i < dayStep.size(); i++) {
            if (dayStep.get(i) == "W") {
                startemp = startStep.get(i - 2);
                finishtemp = finishStep.get(i - 2);
                total = finishtemp - startemp;
                if (total <= 120) {
                    foundtwo = 0;
                    for (j = 2; j < dayStep.size(); j++) {
                        if (dayStep.get(j) == "M" && startStep.get(j - 2) == startemp && finishStep.get(j - 2) == finishtemp) {
                            foundtwo = 1;
                        }
                    }
                    if (foundtwo == 0) {
                        dayStep.add("M");
                        startStep.add(startemp);
                        finishStep.add(finishtemp);
                    }
                }
            }
        }

        for (i = 2; i < dayStep.size(); i++) {
            System.out.println(dayStep.get(i));
            System.out.println(startStep.get(i - 2));
            System.out.println(finishStep.get(i - 2));
            tt = new Event(dayStep.get(i), startStep.get(i - 2), finishStep.get(i - 2), null);
            IndSched.addEvent(tt);
        }
        return IndSched;
    }
}

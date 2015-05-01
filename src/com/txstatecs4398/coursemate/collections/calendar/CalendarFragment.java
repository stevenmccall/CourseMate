package com.txstatecs4398.coursemate.collections.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.txstatecs4398.coursemate.R;
import com.txstatecs4398.coursemate.collections.Event;
import com.txstatecs4398.coursemate.collections.Group;
import com.txstatecs4398.coursemate.collections.Person;
import java.util.Collections;
import java.util.List;

public class CalendarFragment extends Fragment {

    // *TextView
    private TextView textViewServiceApp;
    private TextView textViewWeekCalender;
    private TextView textViewPrevDate;
    private TextView textViewDate;
    private TextView textViewNextDate;
    private TextView textViewSun;
    private TextView textViewMon;
    private TextView textViewTue;
    private TextView textViewWed;
    private TextView textViewThu;
    private TextView textViewFri;
    private TextView textViewSat;
    private TextView textView12am;
    private TextView textView1am;
    private TextView textView2am;
    private TextView textView3am;
    private TextView textView4am;
    private TextView textView5am;
    private TextView textView6am;
    private TextView textView7am;
    private TextView textView8am;
    private TextView textView9am;
    private TextView textView10am;
    private TextView textView11am;
    private TextView textView12pm;
    private TextView textView1pm;
    private TextView textView2pm;
    private TextView textView3pm;
    private TextView textView4pm;
    private TextView textView5pm;
    private TextView textView6pm;
    private TextView textView7pm;
    private TextView textView8pm;
    private TextView textView9pm;
    private TextView textView10pm;
    private TextView textView11pm;

    // * Views
    private RelativeLayout relativeLayoutSunday;
    private RelativeLayout relativeLayoutMonDay;
    private RelativeLayout relativeLayoutTueDay;
    private RelativeLayout relativeLayoutWedDay;
    private RelativeLayout relativeLayoutThuDay;
    private RelativeLayout relativeLayoutFriDay;
    private RelativeLayout relativeLayoutSatDay;

    // * Images
    private ImageView buttonBack;
    private ImageView buttonHome;

    private Typeface typface;

    public String dateFormat, logInID;
    public String[] weekDays;
    public String[] NextPreWeekday;
    public String dateFormate;
    public String firstDayOfWeek;
    public String lastDayOfWeek;

    public static ArrayList<Entity> arrayListEntity = new ArrayList<>();
    public static ArrayList<Entity> arrayListEButtonId = new ArrayList<>();

    // Figure I'll support of to 10 users on a calendar with unique colors
    public static final String[] colorWheel = {"#f0aa7b", "#e4d9f1", "#00ffb8", "#00dfff", "#41c4dc", "#990000", "#8cc63f", "#008fc5", "#c7d6eb", "#f0d17b"};

    public int weekDaysCount = 0;
    public ArrayList<WeekSets> weekDatas;
    String tapMargin;
    String buttonHeight;

    public static Group temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calendar_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View v = getView();

        textViewPrevDate = (TextView) v.findViewById(R.id.textViewPrevDate);
        textViewDate = (TextView) v.findViewById(R.id.textViewDate);
        textViewNextDate = (TextView) v.findViewById(R.id.textViewNextDate);
        textViewSun = (TextView) v.findViewById(R.id.textViewSun);
        textViewMon = (TextView) v.findViewById(R.id.textViewMon);
        textViewTue = (TextView) v.findViewById(R.id.textViewTue);
        textViewWed = (TextView) v.findViewById(R.id.textViewWed);
        textViewThu = (TextView) v.findViewById(R.id.textViewThu);
        textViewFri = (TextView) v.findViewById(R.id.textViewFri);
        textViewSat = (TextView) v.findViewById(R.id.textViewSat);
        textView12am = (TextView) v.findViewById(R.id.textView12am);
        textView1am = (TextView) v.findViewById(R.id.textView1am);
        textView2am = (TextView) v.findViewById(R.id.textView2am);
        textView3am = (TextView) v.findViewById(R.id.textView3am);
        textView4am = (TextView) v.findViewById(R.id.textView4am);
        textView5am = (TextView) v.findViewById(R.id.textView5am);
        textView6am = (TextView) v.findViewById(R.id.textView6am);
        textView7am = (TextView) v.findViewById(R.id.textView7am);
        textView8am = (TextView) v.findViewById(R.id.textView8am);
        textView9am = (TextView) v.findViewById(R.id.textView9am);
        textView10am = (TextView) v.findViewById(R.id.textView10am);
        textView11am = (TextView) v.findViewById(R.id.textView11am);
        textView12pm = (TextView) v.findViewById(R.id.textView12pm);
        textView1pm = (TextView) v.findViewById(R.id.textView1pm);
        textView2pm = (TextView) v.findViewById(R.id.textView2pm);
        textView3pm = (TextView) v.findViewById(R.id.textView3pm);
        textView4pm = (TextView) v.findViewById(R.id.textView4pm);
        textView5pm = (TextView) v.findViewById(R.id.textView5pm);
        textView6pm = (TextView) v.findViewById(R.id.textView6pm);
        textView7pm = (TextView) v.findViewById(R.id.textView7pm);
        textView8pm = (TextView) v.findViewById(R.id.textView8pm);
        textView9pm = (TextView) v.findViewById(R.id.textView9pm);
        textView10pm = (TextView) v.findViewById(R.id.textView10pm);
        textView11pm = (TextView) v.findViewById(R.id.textView11pm);

        relativeLayoutSunday = (RelativeLayout) v.findViewById(R.id.relativeLayoutSunday);
        relativeLayoutMonDay = (RelativeLayout) v.findViewById(R.id.relativeLayoutMonDay);
        relativeLayoutTueDay = (RelativeLayout) v.findViewById(R.id.relativeLayoutTueDay);
        relativeLayoutWedDay = (RelativeLayout) v.findViewById(R.id.relativeLayoutWedDay);
        relativeLayoutThuDay = (RelativeLayout) v.findViewById(R.id.relativeLayoutThuDay);
        relativeLayoutFriDay = (RelativeLayout) v.findViewById(R.id.relativeLayoutFriDay);
        relativeLayoutSatDay = (RelativeLayout) v.findViewById(R.id.relativeLayoutSatDay);

        NextPreWeekday = getWeekDay();
        firstDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[0]);
        lastDayOfWeek = CommonMethod.convertWeekDays(NextPreWeekday[6]);
        textViewDate.setText(firstDayOfWeek + "-" + lastDayOfWeek + " "
                + CommonMethod.convertWeekDaysMouth(NextPreWeekday[6]));

        textViewSun.setText(CommonMethod.convertWeekDays(NextPreWeekday[0])
                + "\nSun");
        textViewMon.setText(CommonMethod.convertWeekDays(NextPreWeekday[1])
                + "\nMon");
        textViewTue.setText(CommonMethod.convertWeekDays(NextPreWeekday[2])
                + "\nTue");
        textViewWed.setText(CommonMethod.convertWeekDays(NextPreWeekday[3])
                + "\nWed");
        textViewThu.setText(CommonMethod.convertWeekDays(NextPreWeekday[4])
                + "\nThu");
        textViewFri.setText(CommonMethod.convertWeekDays(NextPreWeekday[5])
                + "\nFri");
        textViewSat.setText(CommonMethod.convertWeekDays(NextPreWeekday[6])
                + "\nSat");

        update();
    }

    private void update() {
        List<String> records = new ArrayList<>();
        String record = "";
        for (Person p : temp.returnStorage()) {
            record += p.getNetID().substring(0, 2) + " " + colorWheel[temp.returnStorage().indexOf(p) % 10] + " "; 
            for (Event e : p.getEvents()) {
                switch (e.getDay().toUpperCase()) { 
                    case "M":
                        record += "1 ";
                        break;
                    case "T":
                        record += "2 ";
                        break;
                    case "W":
                        record += "3 ";
                        break;
                    case "R":
                        record += "4 ";
                        break;
                    case "F":
                        record += "5 ";
                        break;
                    case "S":
                        record += "6 ";
                        break;
                    case "U":
                        record += "0 ";
                        break;
                    default:
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                                "DATE PARSING ERROR: Malformed date code detected. Received=" + e.getDay(), Toast.LENGTH_LONG);
                        toast.show();
                }
                    // set button start time location
                Integer start = e.getStartTime();   // recordField[%3]
                Integer offset = 0;//235;
                Double scaleCorrectionFactor = (80.0 / 30.0);//40/30
                Integer mins;
                String startProc = start.toString();
                if (startProc.length() == 3) {
                    startProc = "0" + startProc;
                }
                mins = Integer.parseInt(startProc.subSequence(2, 4).toString());
                mins += (((Integer.parseInt(startProc.subSequence(0, 2).toString())) * 60) + offset); 
                mins = (int) ((int) mins * scaleCorrectionFactor);
                record += mins + " ";
                
                Integer end = e.getEndTime();
                String endProc = end.toString();
                if (endProc.length() == 3) {
                    endProc = "0" + endProc;
                }
                mins = Integer.parseInt(endProc.subSequence(2, 4).toString());
                mins += (((Integer.parseInt(endProc.subSequence(0, 2).toString())) * 60) + offset);
                mins = (int) ((int) mins * scaleCorrectionFactor);
                record += mins + " ";
            }
            records.add(record); // done for each user after all events have been parsed
            record = "";
        }
        doCalendarWeekBuild(records);
    }

    void doCalendarWeekBuild(List<String> params) {
//        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "DEBUG doCalendarWeekBuild(): "+params.toString(), Toast.LENGTH_LONG);
//            toast.show();
        if (!"".equals(params.get(0))) {
            weekDatas = new ArrayList<>();

        // List<String> process = new ArrayList<String>();
                    //Collections.addAll(process,params);
            //process = params;
            for (String record : params) {
                List<String> recordchunk = new ArrayList<>();
                String[] recordFields = record.split(" ");
                Collections.addAll(recordchunk, recordFields);
                String netID = recordchunk.get(0);
                String colorCode = recordchunk.get(1);
                recordchunk.remove(netID);
                recordchunk.remove(colorCode);

                for (int i = 0; i < ((recordchunk.size() / 3)); i++) {
                    String startRef = recordchunk.get((i * 3) + 1);
                    String endRef = recordchunk.get((i * 3) + 2);
                    String dayCode = recordchunk.get(i * 3);
                    //String netIDwTime = netID+"\n"+(startRef)+" - "+(endRef);
                    netID = netID.trim();
                    tapMargin = startRef;//getWidthAndHeightToButton(Integer.parseInt(startRef));
                    buttonHeight = Integer.toString(Integer.parseInt(endRef) - Integer.parseInt(startRef));//getHeightOfButton(Integer.parseInt(startRef), 
                    //Integer.parseInt(endRef));
                    weekDatas.add(getWeekValues(dayCode, "12", netID, colorCode, // netID and color respectively
                            tapMargin, buttonHeight));
                }
            }
            doCalendarWeekDisplay();
        }
    }

    void doCalendarWeekDisplay() {
//         Toast toast = Toast.makeText(getActivity().getApplicationContext(), "DEBUG doCalendarWeekDisplay(): weekDatas.size()="+String.valueOf(weekDatas.size()), Toast.LENGTH_LONG);
//            toast.show();
        try {

            WeekSets weekToDay;
            int length = weekDatas.size();
            Log.i("length===>", String.valueOf(length));

            if (length != 0) {
                for (WeekSets weekData : weekDatas) {
                    weekToDay = weekData;
                    int day = Integer.parseInt(weekToDay.day);
                    switch (day) {
                        case 0:

                            int sunday = 100;
                            relativeLayoutSunday
                                    .addView(getButtonToLayout(
                                            Integer.parseInt(weekToDay.buttonHeight),
                                            Integer.parseInt(weekToDay.tapMargin),
                                            weekToDay.jobRefID,
                                            weekToDay.jobID, weekToDay.colorCode, sunday));
                            arrayListEButtonId.add(getButton(0, sunday));
                            sunday++;
                            break;

                        case 1:
                            int MonDay = 200;
                            relativeLayoutMonDay
                                    .addView(getButtonToLayout(
                                            Integer.parseInt(weekToDay.buttonHeight),
                                            Integer.parseInt(weekToDay.tapMargin),
                                            weekToDay.jobRefID,
                                            weekToDay.jobID, weekToDay.colorCode, MonDay));
                            arrayListEButtonId.add(getButton(1, MonDay));
                            MonDay++;
                            break;
                        case 2:
                            int TueDay = 200;
                            relativeLayoutTueDay
                                    .addView(getButtonToLayout(
                                            Integer.parseInt(weekToDay.buttonHeight),
                                            Integer.parseInt(weekToDay.tapMargin),
                                            weekToDay.jobRefID,
                                            weekToDay.jobID, weekToDay.colorCode, TueDay));
                            arrayListEButtonId.add(getButton(2, TueDay));
                            TueDay++;
                            break;
                        case 3:
                            int WedDay = 200;
                            relativeLayoutWedDay
                                    .addView(getButtonToLayout(
                                            Integer.parseInt(weekToDay.buttonHeight),
                                            Integer.parseInt(weekToDay.tapMargin),
                                            weekToDay.jobRefID,
                                            weekToDay.jobID, weekToDay.colorCode, WedDay));
                            arrayListEButtonId.add(getButton(3, WedDay));
                            WedDay++;
                            break;
                        case 4:
                            int ThuDay = 200;
                            relativeLayoutThuDay
                                    .addView(getButtonToLayout(
                                            Integer.parseInt(weekToDay.buttonHeight),
                                            Integer.parseInt(weekToDay.tapMargin),
                                            weekToDay.jobRefID,
                                            weekToDay.jobID, weekToDay.colorCode, ThuDay));
                            arrayListEButtonId.add(getButton(4, ThuDay));
                            ThuDay++;
                            break;
                        case 5:
                            int FriDay = 200;
                            relativeLayoutFriDay
                                    .addView(getButtonToLayout(
                                            Integer.parseInt(weekToDay.buttonHeight),
                                            Integer.parseInt(weekToDay.tapMargin),
                                            weekToDay.jobRefID,
                                            weekToDay.jobID, weekToDay.colorCode, FriDay));
                            arrayListEButtonId.add(getButton(5, FriDay));
                            FriDay++;
                            break;
                        case 6:
                            int SatDay = 200;
                            relativeLayoutSatDay
                                    .addView(getButtonToLayout(
                                            Integer.parseInt(weekToDay.buttonHeight),
                                            Integer.parseInt(weekToDay.tapMargin),
                                            weekToDay.jobRefID,
                                            weekToDay.jobID, weekToDay.colorCode, SatDay));
                            arrayListEButtonId.add(getButton(6, SatDay));
                            SatDay++;
                            break;

                        default:
                            break;
                    }
                }
            }
        } catch (NumberFormatException e) { Log.getStackTraceString(e); }
    }

    public String[] getWeekDay() {

        Calendar now = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }

    @SuppressLint("SimpleDateFormat")
    public String[] getWeekDayNext() {

        weekDaysCount++;
        Calendar now1 = Calendar.getInstance();
        Calendar now = (Calendar) now1.clone();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;
    }

    @SuppressLint("SimpleDateFormat")
    public String[] getWeekDayPrev() {

        weekDaysCount--;
        Calendar now1 = Calendar.getInstance();
        Calendar now = (Calendar) now1.clone();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] days = new String[7];
        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
        now.add(Calendar.DAY_OF_MONTH, delta);
        for (int i = 0; i < 7; i++) {
            days[i] = format.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
        }

        return days;

    }

    public Button getButtonToLayout(int height, int marginTop,
            String buttonText, String jobID, int buttonID) {

        @SuppressWarnings("deprecation")
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, height);
        Button button = new Button(getActivity());
        button.setLayoutParams(params);
        button.setBackgroundColor(Color.parseColor("#9ACC61"));
        button.setText(buttonText);
        button.setOnClickListener(buttonOnclickForAllWeekButton);
        button.setTextSize(9);
        button.setId(buttonID);
        params.setMargins(0, marginTop, 0, 0);
        arrayListEntity.add(getEntity(jobID, buttonText));

        return button;

    }

    public Button getButtonToLayout(int height, int marginTop,
            String buttonText, String jobID, String color, int buttonID) {

        // In case the color code sent is malformed
        if (color == null || !color.startsWith("#")) {
            color = "#9ACC61";
        }

        @SuppressWarnings("deprecation")
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, height);
        Button button = new Button(getActivity());
        button.setLayoutParams(params);
        button.setBackgroundColor(Color.parseColor(color));
        button.setText(buttonText);
        button.setOnClickListener(buttonOnclickForAllWeekButton);
        button.setTextSize(9);
        button.setId(buttonID);
        params.setMargins(0, marginTop, 0, 0);
        arrayListEntity.add(getEntity(jobID, buttonText));

        return button;

    }

    public OnClickListener buttonOnclickForAllWeekButton = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Button b = (Button) v;

            String buttonText = b.getText().toString();
            int position = 0;

            for (int j = 0; j < arrayListEntity.size(); j++) {
                Entity itemOne = arrayListEntity.get(j);

                String arryJobRefID = itemOne.JobRefID;
                if (arryJobRefID.equals(buttonText)) {
                    position = j;
                    break;
                }
            }

            Entity itemOne1 = arrayListEntity.get(position);
            Toast.makeText(getActivity(), "  " + itemOne1.JobRefID, Toast.LENGTH_SHORT).show();

        }
    };

    public static Entity getEntity(String jobID, String jobRefID) {
        Entity E = new Entity();
        E.JobIDForButton = jobID;
        E.JobRefID = jobRefID;
        return E;

    }

    public static Entity getButton(int layoutView, int buttonTag) {
        Entity E = new Entity();
        E.layoutView = layoutView;
        E.buttonTag = buttonTag;
        return E;

    }

    public static WeekSets getWeekValues(String weekDay, String jobId,
            String jobRefId, String tapMarginA, String buttonHeightA) {
        WeekSets W = new WeekSets();
        W.day = weekDay;
        W.jobID = jobId;
        W.jobRefID = jobRefId;
        W.tapMargin = tapMarginA;
        W.buttonHeight = buttonHeightA;
        W.colorCode = "#9ACC61"; // default.
        return W;
    }

    public static WeekSets getWeekValues(String weekDay, String jobId,
            String jobRefId, String colorCode, String tapMarginA, String buttonHeightA) {
        WeekSets W = new WeekSets();
        W.day = weekDay;
        W.jobID = jobId;
        W.jobRefID = jobRefId;
        W.tapMargin = tapMarginA;
        W.buttonHeight = buttonHeightA;
        W.colorCode = colorCode;
        return W;
    }

    public String getWidthAndHeightToButton(int startTime) {

        int time;
        String size = "0";
        try {
            time = startTime;

            switch (time) {
                case 0:
                    size = "0";
                    break;
                case 1:
                    size = "30";
                    break;
                case 2:
                    size = "60";
                    break;
                case 3:
                    size = "90";
                    break;
                case 4:
                    size = "120";
                    break;
                case 5:
                    size = "150";
                    break;
                case 6:
                    size = "180";
                    break;
                case 7:
                    size = "210";
                    break;
                case 8:
                    size = "240";
                    break;
                case 9:
                    size = "270";
                    break;
                case 10:
                    size = "300";
                    break;
                case 11:
                    size = "330";
                    break;
                case 12:
                    size = "360";
                    break;
                case 13:
                    size = "390";
                    break;
                case 14:
                    size = "420";
                    break;
                case 15:
                    size = "450";
                    break;
                case 16:
                    size = "480";
                    break;
                case 17:
                    size = "510";
                    break;
                case 18:
                    size = "540";
                    break;
                case 19:
                    size = "570";
                    break;
                case 20:
                    size = "600";
                    break;
                case 21:
                    size = "630";
                    break;
                case 22:
                    size = "660";
                    break;
                case 23:
                    size = "690";
                    break;
                case 24:
                    size = "720";
                    break;
                case 25:
                    size = "750";
                    break;
                case 26:
                    size = "780";
                    break;
                case 27:
                    size = "810";
                    break;
                case 28:
                    size = "840";
                    break;
                case 29:
                    size = "870";
                    break;
                case 30:
                    size = "900";
                    break;
                case 31:
                    size = "930";
                    break;
                case 32:
                    size = "960";
                    break;
                case 33:
                    size = "990";
                    break;
                case 34:
                    size = "1020";
                    break;
                case 35:
                    size = "1050";
                    break;
                case 36:
                    size = "1080";
                    break;
                case 37:
                    size = "1110";
                    break;
                case 38:
                    size = "1140";
                    break;
                case 39:
                    size = "1170";
                    break;
                case 40:
                    size = "1200";
                    break;
                case 41:
                    size = "1230";
                    break;
                case 42:
                    size = "1260";
                    break;
                case 43:
                    size = "1290";
                    break;
                case 44:
                    size = "1320";
                    break;
                case 45:
                    size = "1350";
                    break;
                case 46:
                    size = "1380";
                    break;
                case 47:
                    size = "1410";
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.getStackTraceString(e);
        }
        return size;
    }

    public String getHeightOfButton(int startTime, int endTime) {
        String height = "0";
        try {
            int subHeight = endTime - startTime;

            height = String.valueOf(subHeight * 60);

        } catch (Exception e) {
            Log.getStackTraceString(e);
        }

        return height;
    }

    public class LoadViewsInToWeekView extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                weekDatas = new ArrayList<>();

                if ("".equals(params[0])) {
                    //** for sun day
                    tapMargin = getWidthAndHeightToButton(4);
                    buttonHeight = getHeightOfButton(4, 9);
                    weekDatas.add(getWeekValues(String.valueOf(0), "12", "ref",
                            tapMargin, buttonHeight));

                    //** for tue day
                    tapMargin = getWidthAndHeightToButton(8);
                    buttonHeight = getHeightOfButton(8, 14);
                    weekDatas.add(getWeekValues(String.valueOf(2), "12", "ref",
                            tapMargin, buttonHeight));

                    //** for tue day
                    tapMargin = getWidthAndHeightToButton(15);
                    buttonHeight = getHeightOfButton(15, 19);
                    weekDatas.add(getWeekValues(String.valueOf(2), "12", "ref",
                            tapMargin, buttonHeight));

                    //** for fr day
                    tapMargin = getWidthAndHeightToButton(2);
                    buttonHeight = getHeightOfButton(2, 10);
                    weekDatas.add(getWeekValues(String.valueOf(5), "12", "ref",
                            tapMargin, buttonHeight));
                } else {
                    List<String> process = new ArrayList<>();
                    List<String> recordchunk = new ArrayList<>();
                    Collections.addAll(process, params);
                    for (String record : process) {
                        String[] recordFields = record.split(" ");
                        Collections.addAll(recordchunk, recordFields);
                        String netID = recordchunk.get(0);
                        String colorCode = recordchunk.get(1);
                        recordchunk.remove(0);
                        recordchunk.remove(1);
                        for (int i = 0; i < (recordchunk.size() / 3); i++) {
                            tapMargin = getWidthAndHeightToButton(Integer.parseInt(recordchunk.get(1 + i * 3)));
                            buttonHeight = getHeightOfButton(Integer.parseInt(recordchunk.get(1 + i * 3)), Integer.parseInt(recordchunk.get(2 + i * 3)));
                            weekDatas.add(getWeekValues(recordchunk.get(i * 1), "12", netID, colorCode, // netID and color respectively
                                    tapMargin, buttonHeight));
                        }
                    }
                }
            } catch (NumberFormatException e) { Log.getStackTraceString(e); }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {

            try {

                WeekSets weekToDay;
                int length = weekDatas.size();
                Log.i("length===>", String.valueOf(length));

                if (length != 0) {
                    for (WeekSets weekData : weekDatas) {
                        weekToDay = weekData;
                        int day = Integer.parseInt(weekToDay.day);
                        switch (day) {
                            case 0:

                                int sunday = 100;
                                relativeLayoutSunday
                                        .addView(getButtonToLayout(
                                                        Integer.parseInt(weekToDay.buttonHeight),
                                                        Integer.parseInt(weekToDay.tapMargin),
                                                        weekToDay.jobRefID,
                                                        weekToDay.jobID, sunday));
                                arrayListEButtonId.add(getButton(0, sunday));
                                sunday++;
                                break;

                            case 1:
                                int MonDay = 200;
                                relativeLayoutMonDay
                                        .addView(getButtonToLayout(
                                                        Integer.parseInt(weekToDay.buttonHeight),
                                                        Integer.parseInt(weekToDay.tapMargin),
                                                        weekToDay.jobRefID,
                                                        weekToDay.jobID, MonDay));
                                arrayListEButtonId.add(getButton(1, MonDay));
                                MonDay++;
                                break;
                            case 2:
                                int TueDay = 200;
                                relativeLayoutTueDay
                                        .addView(getButtonToLayout(
                                                        Integer.parseInt(weekToDay.buttonHeight),
                                                        Integer.parseInt(weekToDay.tapMargin),
                                                        weekToDay.jobRefID,
                                                        weekToDay.jobID, TueDay));
                                arrayListEButtonId.add(getButton(2, TueDay));
                                TueDay++;
                                break;
                            case 3:
                                int WedDay = 200;
                                relativeLayoutWedDay
                                        .addView(getButtonToLayout(
                                                        Integer.parseInt(weekToDay.buttonHeight),
                                                        Integer.parseInt(weekToDay.tapMargin),
                                                        weekToDay.jobRefID,
                                                        weekToDay.jobID, WedDay));
                                arrayListEButtonId.add(getButton(3, WedDay));
                                WedDay++;
                                break;
                            case 4:
                                int ThuDay = 200;
                                relativeLayoutThuDay
                                        .addView(getButtonToLayout(
                                                        Integer.parseInt(weekToDay.buttonHeight),
                                                        Integer.parseInt(weekToDay.tapMargin),
                                                        weekToDay.jobRefID,
                                                        weekToDay.jobID, ThuDay));
                                arrayListEButtonId.add(getButton(4, ThuDay));
                                ThuDay++;
                                break;
                            case 5:
                                int FriDay = 200;
                                relativeLayoutFriDay.addView
                                (
                                    getButtonToLayout(Integer.parseInt(weekToDay.buttonHeight),
                                    Integer.parseInt(weekToDay.tapMargin),
                                    weekToDay.jobRefID,
                                    weekToDay.jobID, FriDay)
                                );
                                arrayListEButtonId.add(getButton(5, FriDay));
                                FriDay++;
                                break;
                            case 6:
                                int SatDay = 200;
                                relativeLayoutSatDay.addView
                                (
                                    getButtonToLayout(Integer.parseInt(weekToDay.buttonHeight),
                                    Integer.parseInt(weekToDay.tapMargin),
                                    weekToDay.jobRefID,
                                    weekToDay.jobID, SatDay)
                                );
                                arrayListEButtonId.add(getButton(6, SatDay));
                                SatDay++;
                                break;

                            default:
                                break;
                        }
                    }
                }
            } catch (NumberFormatException e) { Log.getStackTraceString(e); }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            try {
                dialog = ProgressDialog.show(getActivity(), null, null, true, false);
                dialog.setContentView(R.layout.progress_layout);
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }
        }
    }
}

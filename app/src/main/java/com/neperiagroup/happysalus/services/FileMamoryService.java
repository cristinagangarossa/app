package com.neperiagroup.happysalus.services;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.neperiagroup.happysalus.bean.BalanceDayData;
import com.neperiagroup.happysalus.bean.BloodPressureData;
import com.neperiagroup.happysalus.bean.BreathDayData;
import com.neperiagroup.happysalus.bean.HrDayData;
import com.neperiagroup.happysalus.bean.OxygenDayData;
import com.neperiagroup.happysalus.bean.PresDayData;
import com.neperiagroup.happysalus.bean.PressSaveDataBean;
import com.neperiagroup.happysalus.bean.SaveDataBean;
import com.neperiagroup.happysalus.bean.ScaleData;
import com.neperiagroup.happysalus.bean.SleepDayData;
import com.neperiagroup.happysalus.bean.SleepSaveDataBean;
import com.neperiagroup.happysalus.bean.StepDayData;
import com.neperiagroup.happysalus.bean.WeekData;
import com.neperiagroup.happysalus.utility.StoreInMemory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class FileMamoryService {

    private StoreInMemory storeInMemory;
    private Context context;

    public static final String currentMonth="current_month";
    public static final String preMonth="pre_month";

    private final String FILE_HR = "fileHr";
    private final String FILE_SCALE="scale";
    private final String FILE_BLOOD_PRESSURE_DATA="pressureDataSphygmo";
    private final String FILE_BLOOD_PRESSURE = "fileBloodPressureTot";
    private final String FILE_OXYGEN = "fileOxygen";
    private final String FILE_STEP = "file_step";
    private final String FILE_CALORIE="file_calorie";
    private final String FILE_DISTANZE="file_distance";
    private final String FILE_SLEEP= "file_sleep";
    private final String FILE_BREATH_REATE = "fileBreath";



    public FileMamoryService(Context context){
        this.storeInMemory=new StoreInMemory(context);
        this.context=context;
    }

    public void addToFile(String fileName,String text) throws IOException {
        String filePath = context.getFilesDir() + "/" + fileName;
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
        out.println(text);
        out.close();

    }

    public void writeFile(String fileName,String text) throws IOException {
        String filePath = context.getFilesDir() + "/"+currentMonth+"/" + fileName;
        File today=new File(filePath);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath,false)));
        out.println(text);
        out.close();
    }


    public String readFromFile(String filePath) throws IOException {
        //String filePath = context.getFilesDir() + "/" + fileName;
        File file = new File(filePath);
        if(file.length()==0){
            return"";
        }
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }


    }

    private File openFile(String fileName) throws IOException {
        String filePath = context.getFilesDir() + "/" + fileName;
        File file = new File(filePath );
        return file;

    }

    public List<String> getAllFileNameContain(String substring, String path){
        //String filePath = context.getFilesDir()+"";

        File folder = new File(path);

        File[] listOfFiles = folder.listFiles();
        List<String> files=new ArrayList<String>();
        if(listOfFiles==null){
            return files;
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                if(listOfFiles[i].getName().contains(substring)){
                    files.add(listOfFiles[i].getAbsolutePath());
                }
            } else if (listOfFiles[i].isDirectory()) {
                //TODO mast be recursiv??
            }
        }
        return files;
    }

    public  List<SaveDataBean> getHrDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_HR+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        json = readFromFile(path);
        List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
        if(dayData==null){
            return new ArrayList<SaveDataBean>();
        }
        return dayData;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeekData> getAllWeekHrData() throws IOException {
        List<WeekData> weekDataList=new ArrayList<WeekData>();
        String pathCurrent=context.getFilesDir()+"/"+currentMonth;
        String pathPre=context.getFilesDir()+"/"+preMonth;
        List<String> filesName=getAllFileNameContain("fileHr",pathCurrent);
        filesName.addAll(getAllFileNameContain("fileHr",pathPre));
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        List<HrDayData> days=new ArrayList<HrDayData>();

        for(String file :filesName){
            json = readFromFile(file);
            List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
            if(dayData.size()>0) {
                HrDayData day = new HrDayData();
                day.setDate(dayData.get(0).getDate());
                float min=Float.parseFloat(dayData.get(0).getValue());
                float max=Float.parseFloat(dayData.get(0).getValue());
                float value=0;
                for(SaveDataBean data:dayData){
                    float current=Float.parseFloat(data.getValue());
                    if(current<min){
                        min=current;
                    }
                    if(current>max){
                        max=current;
                    }
                    value+=current;
                }
                value=value/dayData.size();
                day.setData(String.valueOf(Math.round(value)));
                day.setMin(String.valueOf(Math.round(min)));
                day.setMax(String.valueOf(Math.round(max)));
                days.add(day);
            }

        }
        Map<Integer,List<HrDayData>> map=new HashMap<Integer,List<HrDayData>>();
        for(HrDayData day:days){
            Calendar c= Calendar.getInstance();
            c.setTime(day.getDate());
            int week=c.get(Calendar.WEEK_OF_YEAR);
            if(map.containsKey(week)){
                map.get(week).add(0,day);
            }else{
                List<HrDayData> weekElement=new ArrayList<>();
                weekElement.add(0,day);
                map.put(week,weekElement);
            }
        }
        Set<Integer> keys=map.keySet();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM");
        for(Integer i :keys){
            List<HrDayData> week=map.get(i);
            WeekData<HrDayData> weekData=new WeekData<HrDayData>();
            Calendar dayOfWeek=Calendar.getInstance();
            dayOfWeek.setTime(week.get(0).getDate());
            weekData.setDays(week);
            if(isCurrentWeek(week.get(0).getDate())){
                weekData.setWeek("Questa settimana");
            }else{
                Calendar start=Calendar.getInstance();
                int year=dayOfWeek.get(Calendar.YEAR);
                int weekNumber=dayOfWeek.get(Calendar.WEEK_OF_YEAR);
                start.setWeekDate(year,weekNumber,Calendar.MONDAY);
                Date startDate=start.getTime();
                Calendar end=Calendar.getInstance();
                end.setWeekDate(year,weekNumber,Calendar.SUNDAY);
                Date endDate=end.getTime();
                String dataToWeek= String.format(format1.format(startDate) + " " + format1.format(endDate));
                weekData.setWeek(dataToWeek);
            }
            float media=0;
            for(HrDayData day: week){
                media+=Float.valueOf(day.getData());
            }
            media=media/week.size();
            media=Math.round(media);
            int mediaInt = (int) media;
            weekData.setValue(String.valueOf(mediaInt));
            weekDataList.add(0,weekData);

        }



        return weekDataList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeekData> getAllWeekOxygenData() throws IOException {
        List<WeekData> weekDataList=new ArrayList<WeekData>();
        String pathCurrent=context.getFilesDir()+"/"+currentMonth;
        String pathPre=context.getFilesDir()+"/"+preMonth;
        List<String> filesName=getAllFileNameContain("fileOxygen",pathCurrent);
        filesName.addAll(getAllFileNameContain("fileOxygen",pathPre));
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        List<OxygenDayData> days=new ArrayList<OxygenDayData>();

        for(String file :filesName){
            json = readFromFile(file);
            List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
            if(dayData.size()>0) {
                OxygenDayData day = new OxygenDayData();
                day.setDate(dayData.get(0).getDate());
                float min=Float.parseFloat(dayData.get(0).getValue());
                float max=Float.parseFloat(dayData.get(0).getValue());
                float value=0;
                for(SaveDataBean data:dayData){
                    float current=Float.parseFloat(data.getValue());
                    if(current<min){
                        min=current;
                    }
                    if(current>max){
                        max=current;
                    }
                    value+=current;
                }
                value=value/dayData.size();
                day.setData(String.valueOf(Math.round(value)));
                //day.setMin(String.valueOf(min));
                //day.setMax(String.valueOf(max));
                days.add(day);
            }

        }
        Map<Integer,List<OxygenDayData>> map=new HashMap<Integer,List<OxygenDayData>>();
        for(OxygenDayData day:days){
            Calendar c= Calendar.getInstance();
            c.setTime(day.getDate());
            int week=c.get(Calendar.WEEK_OF_YEAR);
            if(map.containsKey(week)){
                map.get(week).add(0,day);
            }else{
                List<OxygenDayData> weekElement=new ArrayList<>();
                weekElement.add(0,day);
                map.put(week,weekElement);
            }
        }
        Set<Integer> keys=map.keySet();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM");
        for(Integer i :keys){
            List<OxygenDayData> week=map.get(i);
            WeekData<OxygenDayData> weekData=new WeekData<OxygenDayData>();
            Calendar dayOfWeek=Calendar.getInstance();
            dayOfWeek.setTime(week.get(0).getDate());
            weekData.setDays(week);
            if(isCurrentWeek(week.get(0).getDate())){
                weekData.setWeek("Questa settimana");
            }else{
                Calendar start=Calendar.getInstance();
                int year=dayOfWeek.get(Calendar.YEAR);
                int weekNumber=dayOfWeek.get(Calendar.WEEK_OF_YEAR);
                start.setWeekDate(year,weekNumber,Calendar.MONDAY);
                Date startDate=start.getTime();
                Calendar end=Calendar.getInstance();
                end.setWeekDate(year,weekNumber,Calendar.SUNDAY);
                Date endDate=end.getTime();
                String dataToWeek= String.format(format1.format(startDate) + " " + format1.format(endDate));
                weekData.setWeek(dataToWeek);
            }
            float media=0;
            for(OxygenDayData day: week){
                media+=Float.valueOf(day.getData());
            }
            media=media/week.size();
            weekData.setValue("Media: "+Math.round(media));
            weekDataList.add(0,weekData);

        }



        return weekDataList;
    }
    public  List<SaveDataBean> getOxygenDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_OXYGEN+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        json = readFromFile(path);
        List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
        if(dayData==null){
            return new ArrayList<SaveDataBean>();
        }
        return dayData;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeekData> getAllWeekBrData() throws IOException {
        List<WeekData> weekDataList=new ArrayList<WeekData>();
        String pathCurrent=context.getFilesDir()+"/"+currentMonth;
        String pathPre=context.getFilesDir()+"/"+preMonth;
        List<String> filesName=getAllFileNameContain("fileBreath",pathCurrent);
        filesName.addAll(getAllFileNameContain("fileBreath",pathPre));
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        List<BreathDayData> days=new ArrayList<BreathDayData>();

        for(String file :filesName){
            json = readFromFile(file);
            List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
            if(dayData.size()>0) {
                BreathDayData day = new BreathDayData();
                day.setDate(dayData.get(0).getDate());
                float min=Float.parseFloat(dayData.get(0).getValue());
                float max=Float.parseFloat(dayData.get(0).getValue());
                float value=0;
                for(SaveDataBean data:dayData){
                    float current=Float.parseFloat(data.getValue());
                    if(current<min){
                        min=current;
                    }
                    if(current>max){
                        max=current;
                    }
                    value+=current;
                }
                value=value/dayData.size();
                day.setData(String.valueOf(Math.round(value)));
                day.setMin(String.valueOf(Math.round(min)));
                day.setMax(String.valueOf(Math.round(max)));
                days.add(day);
            }

        }
        Map<Integer,List<BreathDayData>> map=new HashMap<Integer,List<BreathDayData>>();
        for(BreathDayData day:days){
            Calendar c= Calendar.getInstance();
            c.setTime(day.getDate());
            int week=c.get(Calendar.WEEK_OF_YEAR);
            if(map.containsKey(week)){
                map.get(week).add(0,day);
            }else{
                List<BreathDayData> weekElement=new ArrayList<>();
                weekElement.add(0,day);
                map.put(week,weekElement);
            }
        }
        Set<Integer> keys=map.keySet();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM");
        for(Integer i :keys){
            List<BreathDayData> week=map.get(i);
            WeekData<BreathDayData> weekData=new WeekData<BreathDayData>();
            Calendar dayOfWeek=Calendar.getInstance();
            dayOfWeek.setTime(week.get(0).getDate());
            weekData.setDays(week);
            if(isCurrentWeek(week.get(0).getDate())){
                weekData.setWeek("Questa settimana");
            }else{
                Calendar start=Calendar.getInstance();
                int year=dayOfWeek.get(Calendar.YEAR);
                int weekNumber=dayOfWeek.get(Calendar.WEEK_OF_YEAR);
                start.setWeekDate(year,weekNumber,Calendar.MONDAY);
                Date startDate=start.getTime();
                Calendar end=Calendar.getInstance();
                end.setWeekDate(year,weekNumber,Calendar.SUNDAY);
                Date endDate=end.getTime();
                String dataToWeek= String.format(format1.format(startDate) + " " + format1.format(endDate));
                weekData.setWeek(dataToWeek);
            }
            float media=0;
            for(BreathDayData day: week){
                media+=Float.valueOf(day.getData());
            }
            media=media/week.size();
            weekData.setValue("Media: "+Math.round(media));
            weekDataList.add(0,weekData);

        }



        return weekDataList;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeekData> getAllWeekPressData() throws IOException {
        List<WeekData> weekDataList=new ArrayList<WeekData>();
        String pathCurrent=context.getFilesDir()+"/"+currentMonth;
        String pathPre=context.getFilesDir()+"/"+preMonth;
        List<String> filesName=getAllFileNameContain("fileBloodPressureTot",pathCurrent);
        filesName.addAll(getAllFileNameContain("fileBloodPressureTot",pathPre));
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<PressSaveDataBean>>() {
        }.getType();
        List<PresDayData> days=new ArrayList<PresDayData>();

        for(String file :filesName){
            json = readFromFile(file);
            List<PressSaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
            if(dayData.size()>0) {
                PresDayData day = new PresDayData();
                day.setDate(dayData.get(0).getDate());
                //float min=Float.parseFloat(dayData.get(0).getValue());
                //float max=Float.parseFloat(dayData.get(0).getValue());
                float valueMin=0;
                float valueMax=0;
                for(PressSaveDataBean data:dayData){
                   valueMax+=data.getPressData().getMax();
                   valueMin+=data.getPressData().getMin();
                }
                valueMin=valueMin/dayData.size();
                valueMax=valueMax/dayData.size();
                day.setPresMax(""+Math.round(valueMax));
                day.setPressMin(""+Math.round(valueMin));
                //day.setMin(String.valueOf(min));
                //day.setMax(String.valueOf(max));
                days.add(day);
            }

        }
        Map<Integer,List<PresDayData>> map=new HashMap<Integer,List<PresDayData>>();
        for(PresDayData day:days){
            Calendar c= Calendar.getInstance();
            c.setTime(day.getDate());
            int week=c.get(Calendar.WEEK_OF_YEAR);
            if(map.containsKey(week)){
                map.get(week).add(0,day);
            }else{
                List<PresDayData> weekElement=new ArrayList<>();
                weekElement.add(0,day);
                map.put(week,weekElement);
            }
        }
        Set<Integer> keys=map.keySet();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM");
        for(Integer i :keys){
            List<PresDayData> week=map.get(i);
            WeekData<PresDayData> weekData=new WeekData<PresDayData>();
            Calendar dayOfWeek=Calendar.getInstance();
            dayOfWeek.setTime(week.get(0).getDate());
            weekData.setDays(week);
            if(isCurrentWeek(week.get(0).getDate())){
                weekData.setWeek("Questa settimana");
            }else{
                Calendar start=Calendar.getInstance();
                int year=dayOfWeek.get(Calendar.YEAR);
                int weekNumber=dayOfWeek.get(Calendar.WEEK_OF_YEAR);
                start.setWeekDate(year,weekNumber,Calendar.MONDAY);
                Date startDate=start.getTime();
                Calendar end=Calendar.getInstance();
                end.setWeekDate(year,weekNumber,Calendar.SUNDAY);
                Date endDate=end.getTime();
                String dataToWeek= String.format(format1.format(startDate) + " " + format1.format(endDate));
                weekData.setWeek(dataToWeek);
            }
            float mediaMax=0;
            float mediaMin=0;
            for(PresDayData day: week){
                mediaMin+=Float.valueOf(day.getPressMin());
                mediaMax+=Float.valueOf(day.getPresMax());
            }
            mediaMin=mediaMin/week.size();
            mediaMax=mediaMax/week.size();
            weekData.setValue("Media "+Math.round(mediaMax)+" - "+Math.round(mediaMin)+ " mmHg");
            weekDataList.add(0,weekData);

        }



        return weekDataList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeekData> getAllWeekPressDataSphygmo() throws IOException {

        List<WeekData> weekDataList=new ArrayList<WeekData>();
        String pathCurrent=context.getFilesDir()+"/"+currentMonth;
        String pathPre=context.getFilesDir()+"/"+preMonth;
        List<String> filesName=getAllFileNameContain(FILE_BLOOD_PRESSURE_DATA,pathCurrent);
        filesName.addAll(getAllFileNameContain(FILE_BLOOD_PRESSURE_DATA,pathPre));
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<BloodPressureData>>() {
        }.getType();
        List<PresDayData> days=new ArrayList<PresDayData>();

        for(String file :filesName){
            json = readFromFile(file);
            List<BloodPressureData> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
            if(dayData.size()>0) {
                PresDayData day = new PresDayData();
                day.setDate(dayData.get(0).getTimestamp());
                //float min=Float.parseFloat(dayData.get(0).getValue());
                //float max=Float.parseFloat(dayData.get(0).getValue());
                float valueMin=0;
                float valueMax=0;
                for(BloodPressureData data:dayData){
                    valueMax+=data.getSystolic();
                    valueMin+=data.getDiastolic();
                }
                valueMin=valueMin/dayData.size();
                valueMax=valueMax/dayData.size();
                day.setPresMax(""+Math.round(valueMax));
                day.setPressMin(""+Math.round(valueMin));
                //day.setMin(String.valueOf(min));
                //day.setMax(String.valueOf(max));
                days.add(day);
            }

        }
        Map<Integer,List<PresDayData>> map=new HashMap<Integer,List<PresDayData>>();
        for(PresDayData day:days){
            Calendar c= Calendar.getInstance();
            c.setTime(day.getDate());
            int week=c.get(Calendar.WEEK_OF_YEAR);
            if(map.containsKey(week)){
                map.get(week).add(0,day);
            }else{
                List<PresDayData> weekElement=new ArrayList<>();
                weekElement.add(0,day);
                map.put(week,weekElement);
            }
        }
        Set<Integer> keys=map.keySet();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM");
        for(Integer i :keys){
            List<PresDayData> week=map.get(i);
            WeekData<PresDayData> weekData=new WeekData<PresDayData>();
            Calendar dayOfWeek=Calendar.getInstance();
            dayOfWeek.setTime(week.get(0).getDate());
            weekData.setDays(week);
            if(isCurrentWeek(week.get(0).getDate())){
                weekData.setWeek("Questa settimana");
            }else{
                Calendar start=Calendar.getInstance();
                int year=dayOfWeek.get(Calendar.YEAR);
                int weekNumber=dayOfWeek.get(Calendar.WEEK_OF_YEAR);
                start.setWeekDate(year,weekNumber,Calendar.MONDAY);
                Date startDate=start.getTime();
                Calendar end=Calendar.getInstance();
                end.setWeekDate(year,weekNumber,Calendar.SUNDAY);
                Date endDate=end.getTime();
                String dataToWeek= String.format(format1.format(startDate) + " " + format1.format(endDate));
                weekData.setWeek(dataToWeek);
            }
            float mediaMax=0;
            float mediaMin=0;
            for(PresDayData day: week){
                mediaMin+=Float.valueOf(day.getPressMin());
                mediaMax+=Float.valueOf(day.getPresMax());
            }
            mediaMin=mediaMin/week.size();
            mediaMax=mediaMax/week.size();
            weekData.setValue("Media "+Math.round(mediaMax)+" - "+Math.round(mediaMin)+ " mmHg");
            weekDataList.add(0,weekData);

        }



        return weekDataList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeekData> getAllWeekScaleData() throws IOException {
        List<WeekData> weekDataList=new ArrayList<WeekData>();
        String pathCurrent=context.getFilesDir()+"/"+currentMonth;
        String pathPre=context.getFilesDir()+"/"+preMonth;
        List<String> filesName=getAllFileNameContain(FILE_SCALE,pathCurrent);
        filesName.addAll(getAllFileNameContain("fileBloodPressureTot",pathPre));
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<ScaleData>>() {
        }.getType();
        List<BalanceDayData> days=new ArrayList<BalanceDayData>();

        for(String file :filesName){
            json = readFromFile(file);
            List<ScaleData> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
            if(dayData.size()>0) {
                BalanceDayData day = new BalanceDayData();
                day.setDate(dayData.get(0).getTimestamp());

                double weight=0;
                for(ScaleData data:dayData){
                   weight+=data.getWeight();
                }
                if(dayData.size()!=0){
                    weight=weight/dayData.size();
                }

                day.setData(String.valueOf(weight));
                //day.setMin(String.valueOf(min));
                //day.setMax(String.valueOf(max));
                days.add(day);
            }

        }
        Map<Integer,List<BalanceDayData>> map=new HashMap<Integer,List<BalanceDayData>>();
        for(BalanceDayData day:days){
            Calendar c= Calendar.getInstance();
            c.setTime(day.getDate());
            int week=c.get(Calendar.WEEK_OF_YEAR);
            if(map.containsKey(week)){
                map.get(week).add(0,day);
            }else{
                List<BalanceDayData> weekElement=new ArrayList<>();
                weekElement.add(0,day);
                map.put(week,weekElement);
            }
        }
        Set<Integer> keys=map.keySet();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM");
        for(Integer i :keys){
            List<BalanceDayData> week=map.get(i);
            WeekData<BalanceDayData> weekData=new WeekData<BalanceDayData>();
            Calendar dayOfWeek=Calendar.getInstance();
            dayOfWeek.setTime(week.get(0).getDate());
            weekData.setDays(week);
            if(isCurrentWeek(week.get(0).getDate())){
                weekData.setWeek("Questa settimana");
            }else{
                Calendar start=Calendar.getInstance();
                int year=dayOfWeek.get(Calendar.YEAR);
                int weekNumber=dayOfWeek.get(Calendar.WEEK_OF_YEAR);
                start.setWeekDate(year,weekNumber,Calendar.MONDAY);
                Date startDate=start.getTime();
                Calendar end=Calendar.getInstance();
                end.setWeekDate(year,weekNumber,Calendar.SUNDAY);
                Date endDate=end.getTime();
                String dataToWeek= String.format(format1.format(startDate) + " " + format1.format(endDate));
                weekData.setWeek(dataToWeek);
            }
            double media=0;
            for(BalanceDayData day: week){
               media+=Double.valueOf(day.getData());
            }
            media=media/week.size();

            weekData.setValue("Media: "+Math.round(media));
            weekDataList.add(0,weekData);

        }



        return weekDataList;
    }
    public  List<ScaleData> getScaleDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_SCALE+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<ScaleData>>() {
        }.getType();
        json = readFromFile(path);
        List<ScaleData> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
        if(dayData==null){
            return new ArrayList<ScaleData>();
        }
        return dayData;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeekData> getAllWeekStepData() throws IOException {
        List<WeekData> weekDataList=new ArrayList<WeekData>();
        String pathCurrent=context.getFilesDir()+"/"+currentMonth;
        String pathPre=context.getFilesDir()+"/"+preMonth;
        List<String> filesName=getAllFileNameContain(FILE_STEP,pathCurrent);
        filesName.addAll(getAllFileNameContain(FILE_STEP,pathPre));
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        List<StepDayData> days=new ArrayList<StepDayData>();

        for(String file :filesName){
            json = readFromFile(file);
            List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
            if(dayData.size()>0) {
                StepDayData day = new StepDayData();
                day.setDate(dayData.get(0).getDate());
                //float min=Float.parseFloat(dayData.get(0).getValue());
                //float max=Float.parseFloat(dayData.get(0).getValue());
                float value=0;
                for(SaveDataBean data:dayData){
                    float current=Float.parseFloat(data.getValue());
                    if(current>value) {
                        value = current;
                    }
                }

                day.setData(String.valueOf(Math.round(value)));
                //day.setMin(String.valueOf(min));
                //day.setMax(String.valueOf(max));
                days.add(day);
            }

        }
        Map<Integer,List<StepDayData>> map=new HashMap<Integer,List<StepDayData>>();
        for(StepDayData day:days){
            Calendar c= Calendar.getInstance();
            c.setTime(day.getDate());
            int week=c.get(Calendar.WEEK_OF_YEAR);
            if(map.containsKey(week)){
                map.get(week).add(0,day);
            }else{
                List<StepDayData> weekElement=new ArrayList<>();
                weekElement.add(0,day);
                map.put(week,weekElement);
            }
        }
        Set<Integer> keys=map.keySet();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM");
        for(Integer i :keys){
            List<StepDayData> week=map.get(i);
            WeekData<StepDayData> weekData=new WeekData<StepDayData>();
            Calendar dayOfWeek=Calendar.getInstance();
            dayOfWeek.setTime(week.get(0).getDate());
            weekData.setDays(week);
            if(isCurrentWeek(week.get(0).getDate())){
                weekData.setWeek("Questa settimana");
            }else{
                Calendar start=Calendar.getInstance();
                int year=dayOfWeek.get(Calendar.YEAR);
                int weekNumber=dayOfWeek.get(Calendar.WEEK_OF_YEAR);
                start.setWeekDate(year,weekNumber,Calendar.MONDAY);
                Date startDate=start.getTime();
                Calendar end=Calendar.getInstance();
                end.setWeekDate(year,weekNumber,Calendar.SUNDAY);
                Date endDate=end.getTime();
                String dataToWeek= String.format(format1.format(startDate) + " " + format1.format(endDate));
                weekData.setWeek(dataToWeek);
            }
            float media=0;
            for(StepDayData day: week){
                media+=Float.valueOf(day.getData());
            }
            media=media/week.size();
            weekData.setValue("Media: "+Math.round(media));
            weekDataList.add(0,weekData);

        }



        return weekDataList;
    }
    public  List<SaveDataBean> getStepDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_STEP+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        json = readFromFile(path);
        List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
        if(dayData==null){
            return new ArrayList<SaveDataBean>();
        }
        return dayData;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<WeekData> getAllWeekSleepData() throws IOException {
        List<WeekData> weekDataList=new ArrayList<WeekData>();
        String pathCurrent=context.getFilesDir()+"/"+currentMonth;
        String pathPre=context.getFilesDir()+"/"+preMonth;
        List<String> filesName=getAllFileNameContain(FILE_SLEEP,pathCurrent);
        filesName.addAll(getAllFileNameContain(FILE_SLEEP,pathPre));
        String json="";
        Type TypeDaySleepBean = new TypeToken<SleepSaveDataBean>() {
        }.getType();
        List<SleepDayData> days=new ArrayList<SleepDayData>();

        for(String file :filesName){
            json = readFromFile(file);
            SleepSaveDataBean dayData=new Gson().fromJson(json, TypeDaySleepBean);
            if(dayData!=null) {
                SleepDayData day = new SleepDayData();
                day.setDate(dayData.getDate());
                //float min=Float.parseFloat(dayData.get(0).getValue());
                //float max=Float.parseFloat(dayData.get(0).getValue());
                day.setHardSleep(dayData.getHardSleep());
                day.setLightSleep(dayData.getLightSleep());
                int totSleep=dayData.getHardSleep()+dayData.getLightSleep();
                day.setData(""+totSleep);

                days.add(day);
            }

        }
        Map<Integer,List<SleepDayData>> map=new HashMap<Integer,List<SleepDayData>>();
        for(SleepDayData day:days){
            Calendar c= Calendar.getInstance();
            c.setTime(day.getDate());
            int week=c.get(Calendar.WEEK_OF_YEAR);
            if(map.containsKey(week)){
                map.get(week).add(0,day);
            }else{
                List<SleepDayData> weekElement=new ArrayList<>();
                weekElement.add(0,day);
                map.put(week,weekElement);
            }
        }
        Set<Integer> keys=map.keySet();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MMM");
        for(Integer i :keys){
            List<SleepDayData> week=map.get(i);
            WeekData<SleepDayData> weekData=new WeekData<SleepDayData>();
            Calendar dayOfWeek=Calendar.getInstance();
            dayOfWeek.setTime(week.get(0).getDate());
            weekData.setDays(week);
            if(isCurrentWeek(week.get(0).getDate())){
                weekData.setWeek("Questa settimana");
            }else{
                Calendar start=Calendar.getInstance();
                int year=dayOfWeek.get(Calendar.YEAR);
                int weekNumber=dayOfWeek.get(Calendar.WEEK_OF_YEAR);
                start.setWeekDate(year,weekNumber,Calendar.MONDAY);
                Date startDate=start.getTime();
                Calendar end=Calendar.getInstance();
                end.setWeekDate(year,weekNumber,Calendar.SUNDAY);
                Date endDate=end.getTime();
                String dataToWeek= String.format(format1.format(startDate) + " " + format1.format(endDate));
                weekData.setWeek(dataToWeek);
            }
            float media=0;
            for(SleepDayData day: week){
                media+=Float.valueOf(day.getData());
            }
            media=media/week.size();
            int h=(int)(media/60/60);
            int m=(int)(media/60%60);
            weekData.setValue("Media: "+h+" ore e "+m+" min");
            weekDataList.add(0,weekData);

        }



        return weekDataList;
    }

    public  List<SaveDataBean> getCalorieDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_CALORIE+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        json = readFromFile(path);
        List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
        if(dayData==null){
            return new ArrayList<SaveDataBean>();
        }
        return dayData;

    }

    public  List<SaveDataBean> getDistanceDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_DISTANZE+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        json = readFromFile(path);
        List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
        if(dayData==null){
            return new ArrayList<SaveDataBean>();
        }
        return dayData;

    }

    public  SleepSaveDataBean getSleepDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_SLEEP+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type typeDaySleepDataBean = new TypeToken<SleepSaveDataBean>() {
        }.getType();
        json = readFromFile(path);
        SleepSaveDataBean dayData=new Gson().fromJson(json, typeDaySleepDataBean);
        if(dayData==null){
            return new SleepSaveDataBean();
        }
        return dayData;

    }
    public  List<SaveDataBean> getBreathDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_BREATH_REATE+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<SaveDataBean>>() {
        }.getType();
        json = readFromFile(path);
        List<SaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
        if(dayData==null){
            return new ArrayList<SaveDataBean>();
        }
        return dayData;

    }

    public  List<PressSaveDataBean> getPressDayData(Date date) throws IOException {
        Calendar day=Calendar.getInstance();
        day.setTime(date);
        Calendar today=Calendar.getInstance();
        String month="";
        if(day.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
            month=currentMonth;
        }else if(day.get(Calendar.MONTH)==(today.get(Calendar.MONTH)-1)){
            month=preMonth;
        }else{
            //TODO case month on server
            //get from server

            return null;

        }
        String path=context.getFilesDir()+"/"+month+"/"+FILE_BLOOD_PRESSURE+"_"+day.get(Calendar.DAY_OF_MONTH)+day.get(Calendar.MONTH)+".txt";
        String json="";
        Type listTypeDaySaveDataBean = new TypeToken<ArrayList<PressSaveDataBean>>() {
        }.getType();
        json = readFromFile(path);
        List<PressSaveDataBean> dayData=new Gson().fromJson(json, listTypeDaySaveDataBean);
        if(dayData==null){
            return new ArrayList<PressSaveDataBean>();
        }
        return dayData;

    }


    public void switchMonth(){

        String currentMonthPath=context.getFilesDir()+"/"+currentMonth;
        File currentMonthFolder = new File(currentMonthPath);
        String preMonthPath=context.getFilesDir()+"/"+preMonth;
        File preMonthFolder=new File(preMonthPath);
        preMonthFolder.delete();
        preMonthFolder=new File(preMonthPath);
        currentMonthFolder.renameTo(preMonthFolder);
        currentMonthFolder=new File(currentMonthPath);
        currentMonthFolder.mkdir();

    }


    private boolean isCurrentWeek(Date d){
        Calendar today=Calendar.getInstance();
        Calendar day=Calendar.getInstance();
        day.setTime(d);
        return day.get(Calendar.WEEK_OF_YEAR)==today.get(Calendar.WEEK_OF_YEAR);
    }



}

package konkuk.scheduledeca;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.SharedPreferences;
import android.hardware.SensorEventListener;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import processing.core.PApplet;


/**
 * Created by hjh on 2016-06-20.
 */
public class DrawSchedule extends PApplet {
    int k=60*1;
    int t=60*1;
    String today;
    int tableY;
    int drag=0; // 1 -> table, 2-> todo
    int v;
    int a = -3;
    boolean slideMode = false;
    boolean buttonClicked=false;
    boolean timerClicked=false;
    boolean timerOn=false;
    boolean memoClicked=false;
    //schedule[] today_works = new schedule[0];
    int t_index=-1;
    int now;
    boolean noWork=false;

    float longHand = 100;
    float shortHand = 600;
    float newLongHand;
    float newShortHand;
    int timerStart = -1;
    Calendar cal = Calendar.getInstance();
    Bundle bundle;

    listManagement listManagement;
    String tableName = "activityList";
    String tag = "SQLite";
    int dbVersion = 1;
    String dbName = "activityList.db";
    Cursor cursor, cursor2;
    SQLiteDatabase sqlDB;
    dbQuery query = new dbQuery();

    String[] suggest = new String[5];


    ArrayList<schedule> today_works = new ArrayList<>();
    ArrayList<ArrayList<String>> list = new ArrayList<>();
    ArrayList<ArrayList<String>> suggesList = new ArrayList<>();

    @Override
    public void settings(){
        size(1080, 1656);
    }

    @Override
    public void setup(){
        final Context context = getActivity();
        int nWeek = cal.get(Calendar.DAY_OF_WEEK);

        orientation(PORTRAIT);
        colorMode(RGB);
        if(month() < 10){
            if(day() <10) {
                today = year()+".0"+month()+".0"+day();
            }else{
                today = year()+".0"+month()+"."+day();
            }
        }else{
            today = year()+"."+month()+"."+day();
        }
        //현재 시간에 해당하는 시간표의 위치를 나타내 주는 변수!
        tableY = 120*3 - (120*hour()+120*minute()/60);

        listManagement = new listManagement(context, dbName, null, dbVersion);
        sqlDB = listManagement.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT activity, startTime, hour, optimization, day FROM " + tableName, null);


        if(cursor != null){
            while(cursor.moveToNext()){
                if(cursor.getString(4).charAt(nWeek+1) == '1'){
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(cursor.getString(0));//activity
                    temp.add(cursor.getString(1));//startTime
                    temp.add(cursor.getString(2));//hour
                    temp.add(cursor.getString(3));//optimization
                    list.add(temp);
                }
            }
        }

        Iterator<ArrayList<String>> itr = list.iterator();
        while(itr.hasNext()){
            ArrayList<String> temp = itr.next();
            today_works.add(new schedule(temp.get(0), Integer.parseInt(temp.get(1)), Integer.parseInt(temp.get(2)), Integer.parseInt(temp.get(3))));
        }


        cursor2 = sqlDB.rawQuery("SELECT activity, hour FROM " + tableName + " WHERE suggestion = 1 and startTime = 0", null);
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                //String suggeslist = cursor2.getString();
                ArrayList<String>temp = new ArrayList<>();
                temp.add(cursor2.getString(0));
                temp.add(cursor2.getString(1));
                suggesList.add(temp);
            }
        }



    }

    @Override
    public void draw(){
        if(tableY>= 120*3)
            tableY = 120*3;
        else if(120*24+tableY <= 120*3)
            tableY = -(120*21);
        if(!(mouseX >= 860 && mouseX <= 1060 && mouseY >= 1430 && mouseY <= 1630)){
            buttonClicked = false;
        }
        if(!(mouseX >= 860 && mouseX <= 1060 && mouseY >= 1230 && mouseY <= 1430)){
            memoClicked = false;
        }
        if(timerOn == true && !(mouseX >= 25 && mouseX <= 85 && mouseY >= 145-120 && mouseY <= 205-120)){
            timerOn = false;
        }
        background(255);

        noStroke();
        textSize(20);

        Iterator<schedule> iter = today_works.iterator();
        while(iter.hasNext()) {
            schedule temp = iter.next();
            fill(temp.R, temp.G, temp.B, 127);
            rect(110, 250 + 2 * temp.start + tableY, width - 210, 2 * temp.time);
            fill(temp.R, temp.G, temp.B, 127);
            rect(110, 250 + 2 * temp.start + tableY, width - 210, 2 * temp.a_time);
            fill(0);
            text(temp.task, 130, 273 + 120 * temp.start + tableY);

        }

        for(int i=-4;i<29;i++){
            fill(255);
            strokeWeight(1);
            stroke(213,213,213);
            line(110,250+120*i+tableY,width-100,250+120*i+tableY);
            textSize(20);
            fill(0);
            if(i==0)
                text("AM 12:00",10,258+120*i+tableY);
            else if(i<12 && i>0)
                text("AM "+i+":00",10,258+120*i+tableY);
            else if(i==12)
                text("PM 12:00",10,258+120*i+tableY);
            else if(i<24 && i>0)
                text("PM "+(i-12)+":00",10,258+120*i+tableY);
        }

        fill(255);
        noStroke();
        rect(0,0,width,100);

        fill(0);
        textSize(20);
        text(today,width-200,250 -40-120);

        strokeWeight(5);
        stroke(213, 213, 213);
        line(110, 160 - 120, 110, 250 - 120 + 120 * 9 - 30);
        line(20, 250 - 30 - 120,  width - 100, 250 - 120 - 30);
        stroke(255, 0, 0);
        line( 110, 250 + 120 * 4 - 120,  130, 250 - 120 + 120 * 4);

        noStroke(); // top rect
        fill(53, 53, 53);
        rect(0, 250 - 120 + 120 * 9 - 30, width, 70);
        fill(255);
        rect(0, 250 + 120 * 8 - 50, width, 20);
        rect(0, 250 + 120 * 8 - 30 + 70, width, height - (250 + 120 * 9 - 30 + 70) + 120);



        textSize(35);
        text("Suggestion!", 40, 250 - 120 + 120 * 9 - 30 + 50);

        stroke(213, 213, 213);
        strokeWeight(1);
        line(0, 250 - 120 + 120 * 9 + 40 + (1776 - (250 + 120 * 9 - 30 + 70)) / 3, width, 250 + 120 * 9 - 120 + 40 + (1776 - (250 + 120 * 9 - 30 + 70)) / 3);
        line(0, 250 - 120 + 120 * 9 + 40 + (1776 - (250 + 120 * 9 - 30 + 70)) / 3 * 2, width, 250 + 120 * 9 - 120 + 40 + (1776 - (250 + 120 * 9 - 30 + 70)) / 3 * 2);

        fill(0);
        textSize(50);
        Iterator<ArrayList<String>> itr_sugges = suggesList.iterator();
        Iterator<schedule> itr = today_works.iterator();
        int i =0;
        while(itr_sugges.hasNext()){

            int t = 60;
            ArrayList<String> temp1 = itr_sugges.next();
            while(itr.hasNext()){
                schedule temp = itr.next();
                if(temp.start > (hour()*60+minute())){
                    t = temp.start - (hour()*60+minute());
                    break;
                }else if(temp.start < (hour()*60+minute()) &&
                        temp.start + temp.time > (hour()*60+minute())){
                    t = 0;
                    break;
                }
            }
            if(Integer.parseInt(temp1.get(1)) < t){
                text(temp1.get(0)+"    활동시간 : " + temp1.get(1),10,1350+130*i);
                i++;

            }
        }

        //button
        if(buttonClicked == false){
            stroke(242, 203,97);
            strokeWeight(6);
            fill(255);
            ellipse(960,1536,156,156);

            stroke(116,116,116);
            strokeWeight(8);
            line(917,1536,1003,1536);
            line(960,1493,960,1579);
        }else if(buttonClicked == true){
            stroke(242,150,97);
            strokeWeight(6);
            fill(53,53,53);
            ellipse(960,1536,156,156);

            stroke(255);
            strokeWeight(8);
            line(917,1536,1003,1536);
            line(960,1493,960,1579);
        }
        if(memoClicked == false){
            stroke(242, 203, 97);
            strokeWeight(6);
            fill(255);
            ellipse(960, 1336, 156, 156);

            fill(116, 116, 116);
            textSize(80);
            text("M", 930, 1366);

        }else if(memoClicked == true){
            stroke(242, 150, 97);
            strokeWeight(6);
            fill(53, 53, 53);
            ellipse(960, 1336, 156, 156);

            fill(255);
            textSize(80);
            text("M", 930, 1366);
        }

        //timer
        fill(255);
        stroke(116,116,116);
        ellipse(55,50,70,70);
        line(55,50,55+25*cos(longHand),50+25*sin(longHand));
        line(55,50,55+15*cos(shortHand),50+15*sin(shortHand));
        if(timerStart == 1){
            longHand += PI/100;
            shortHand += PI/600;
        }
        else if(timerStart == -1){
            newLongHand = longHand;
            newShortHand = shortHand;
        }

        if(slideMode && tableY<120*4 && tableY>-(120*20)){
            tableY += v;
            if(v>0){
                v += a;
                if(v<=0)
                    slideMode = false;
            }else if(v<0){
                v -= a;
                if(v>=0)
                    slideMode = false;
            }
        }
        if(t<60*1){
            fill(0);
            textSize(40);
            text("-There isn't any work-",300,200-120);
            t++;
        }
        if (k < 60 * 1){
            fill(0);
            textSize(40);
            text("Count Time: " + today_works.get(t_index).task, 300, 200 - 120);
            k++;
        }
    }

    public void mousePressed(){
        if(mouseX >= 110 && mouseX <= width-100 && mouseY >=250-30-120 && mouseY <= 250+120*9-30-120){
            drag=1;
            v=0;
        }
        //button click
        else if(mouseX >= 860 && mouseX <= 1060 && mouseY >= 1430 && mouseY <= 1630){
            buttonClicked = true;
        }
        //timer click
        else if(mouseX >= 25 && mouseX <= 85 && mouseY >= 145-120 && mouseY <= 205-120) {
            timerOn = true;
        }
        else if(mouseX >= 860 && mouseX <= 1060 && mouseY >= 1230 && mouseY <= 1430){
            memoClicked = true;
        }
    }

    public void mouseDragged(){
        if(drag == 1){
            tableY += mouseY - pmouseY;
            v = mouseY - pmouseY;
        }
    }

    public void mouseReleased() {
        if (drag == 1)
            slideMode = true;
        drag = 0;
        if(buttonClicked == true && mouseX >= 860 && mouseX <= 1060 && mouseY >= 1430 && mouseY <= 1630) {
            buttonClicked = false;
            //buttonAction
            Intent intent = new Intent(getActivity(), InsertActivity.class);
            startActivity(intent);
        }
        if(memoClicked == true && mouseX >= 860 && mouseX <= 1060 && mouseY >= 1230 && mouseY <= 1430){
            memoClicked = false;
            //memoAction
            Intent intent = new Intent(getActivity(), memoActivity.class);
            startActivity(intent);
        }
        if (timerOn == true && mouseX >=  25 && mouseX <=  85 && mouseY >= 145 - 120 && mouseY <= 205 - 120) {
            //timerAction
            timerClicked = !timerClicked;
            timerOn = false;
            //t_index = -1;

            if (timerClicked == true) {
                for (int i = 0; i < today_works.size(); i++) {
                    if (hour() * 60 + minute() <= today_works.get(i).start + today_works.get(i).time && hour() * 60 + minute() >= today_works.get(i).start) {
                        t_index = i;
                    }
                }
                if (t_index != -1) {
                    now = hour() * 60 + minute();
                    k = 0;
                    timerStart = timerStart * -1;
                } else if (t_index == -1) {
                    timerClicked = false;
                    t = 0;
                }
            } else {
                int r_time = hour() * 60 + minute() - now;
                timerStart = timerStart * -1;
                //t_index에 현재 today_works의 index 값 집어넣기!!
                if (today_works.get(t_index).a_time == 0) {
                    today_works.get(t_index).a_time = (int) (Math.random()*10+45);
                    //((0.2 * today_works.get(t_index).time) + (0.8 * r_time));
                    query.update(listManagement, today_works.get(t_index).task, Integer.toString(today_works.get(t_index).a_time));
                } else {
                    int a = (int)(0.2*(Math.random()*50));//+45
                    int b = (int)(0.8*today_works.get(t_index).a_time);
                    today_works.get(t_index).a_time = a+b;
                    //today_works.get(t_index).a_time)
                    query.update(listManagement, today_works.get(t_index).task, Integer.toString(today_works.get(t_index).a_time));
                }
            }
        }
    }
}

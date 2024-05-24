/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tarea09;

/**
 *
 * @author Ignacio
 */
public class Time {
    private int hour;
    private int minute;
    private int second;
    
    
    public Time(int hour,int minute,int second){
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    public Time(String currentTime){
        String[] time = currentTime.split(":");
        hour = Integer.parseInt(time[0]);
        minute = Integer.parseInt(time[1]);
        second = Integer.parseInt(time[2]);
    }
    
    public String getCurrentTime(){
        return hour+":"+minute+":"+second;
    }
    public void oneSecondPassed(){
        second++;
        if(second == 60){
            minute++;
            second=0;
            if(minute==60){
                hour++;
                minute=0;
            }
            if(hour==24){
                hour=0;
                System.out.println("Próximo día");
            }
        }
    }
}

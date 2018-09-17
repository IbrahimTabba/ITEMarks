package com.example.cec.test;

import android.util.Log;

import com.loopj.android.http.LogHandler;

import java.util.ArrayList;

/**
 * Created by CEC on 21-Aug-18.
 */

public class Data {
    public static ArrayList <Elemnt> Arr = new ArrayList<>();
    public static ArrayList<Elemnt> F1 = new ArrayList<>() , F2 = new ArrayList<>() , Tak = new ArrayList<>();
    public static Listener Listener = null , f1Listener = null , Chosing = null , f2Listener = null , tackListener = null ;
    public static String ApplicationPath ;
    public static void Add(ArrayList<Elemnt> A)
    {
        Arr.addAll(0,A);
        for(int i =  A.size()-1 ; i >=0 ; i--)
        {
            String Year = A.get(i).Turm;
            Year = Year.replace(" ","");
            Log.e("Kaz",Year);
            if(Year.equals("الفصلالثاني"))
            {
                F2.add(0,A.get(i));
                if(f2Listener!=null)
                    f2Listener.emmit_elemnt();
            }
            else if (Year.equals("الفصلالأول"))
            {
                F1.add(0,A.get(i));
                if(f1Listener!=null)
                    f1Listener.emmit_elemnt();
            }
            else
            {
                Tak.add(0,A.get(i));
            }
        }
        if(Listener!=null)
            Listener.emmit_elemnt();
    }
    public static void Progress(int type)
    {
        if(f1Listener!=null && type==1)
            f1Listener.emmit_progress();
        if(f2Listener!=null && type==2)
            f2Listener.emmit_progress();
        if(tackListener!=null && type==3)
            tackListener.emmit_progress();
    }
}

package com.example.cec.test;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by CEC on 22-Aug-18.
 */

public class BackGroundService extends Service {
    private Thread thread;
    public static ArrayList<Elemnt> Arr = new ArrayList<>();
    private static ArrayList<String> Names = new ArrayList<>();
    SharedPreferences SP;
    SharedPreferences.Editor ED;
    private static SyncHttpClient client;
    private static boolean FirstTime = false;
    int lastCol , lastidx ;
    int lastE;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Service Created","LOL");
        client = new SyncHttpClient();
        Arr = new ArrayList<>();
        Names = new ArrayList<>();
        SP = getSharedPreferences("Ibrahim",MODE_PRIVATE);
        ED = SP.edit();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.e("Survice Started"," S");
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean TT = true;
                    while (TT)
                    {
                        try {
                            Thread.sleep(3600000);
                            Calendar c= Calendar.getInstance();
                            SimpleDateFormat SDF = new SimpleDateFormat("hh", Locale.US);
                            int Houre = Integer.parseInt(SDF.format(c.getTime()));
                            SDF = new SimpleDateFormat("a", Locale.US);
                            boolean Morning = SDF.format(c.getTime()).equals("AM");
                            Log.e("Houres Are",Houre+""+Morning);
                            if(!ValidDate(Houre , Morning))
                            {
                                Log.e("Time is ","In Vallid");
                                continue;
                            }
                            else
                            {
                                Log.e("Time is "," Vallid");
                            }
                            if(checkNetworkStatus(BackGroundService.this).equals("noNetwork"))
                            {
                                Log.e("NotNetwork Available","Continued");
                                continue;
                            }
                            Arr.clear();
                            lastidx = SP.getInt("lastidx",-1);
                            lastCol = SP.getInt("lastCol",-1);
                            if(lastCol!=-1)
                            {
                                Log.e("Not","First Time");
                                FirstTime = false;
                                MakeRequest(lastCol-1,"");
                            }
                            else
                            {
                                Log.e("First","Time");
                                FirstTime = true;
                                MakeRequest(0,"");
                            }
                        }
                        catch (InterruptedException e)
                        {
                            Log.e("Threas ","Interrrupted");
                            break;
                        }
                    }
                }
            });
        try
        {
            if(!thread.isInterrupted())
                thread.interrupt();
        }catch (Exception e)
        {

        }
        try
        {
            thread.start();
        }
        catch (Exception e)
        {

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static void newMarks(ArrayList<Elemnt> el)
    {
        Arr.addAll(0,el);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try
        {
            if(!thread.isInterrupted())
                thread.interrupt();
        }
        catch (Exception e)
        {

        }
        ED.putInt("service",0);
        ED.commit();
        Log.e("Service "," Had Destroied From the Insicde");
    }

    public void MakeRequest(final int Col , final String Par)
    {
        client.setTimeout(300000);
        client.setConnectTimeout(300000);
        client.get("http://damasuniv.edu.sy/ite/index.php?End=171&Coll="+Col+"&Start=0&set=14&lang=1&func=2&Ser=6"+Par, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                String S = new String(responseBody);
                ArrayList <Elemnt> E =  Getelemts(S);
                if(E.size()==0)
                {
                    Log.e("L , Ar , Fi , LE " , lastidx + " " + Arr.size() + " " + FirstTime + " " + lastE);
                    if(Arr.size() > lastidx && !FirstTime)
                    {
                        Names.clear();
                        for(int i = Arr.size() - lastidx -1  ; i >=0 ; i--)
                        {
                            Names.add(Arr.get(i).Name);
                        }
                        if(Names.size()>0)
                        {
                            Intent I = new Intent(BackGroundService.this , B_Reciver.class);
                            I.putExtra("Names",Names);
                            Log.e("Going To Send","BreoadCast");
                            sendBroadcast(I);
                        }
                    }
                    lastCol = Col;
                    lastidx = lastE;
                    Log.e("LastCol , Lastidx" , lastCol + " " + lastidx);
                    ED.putInt("lastCol",lastCol);
                    ED.putInt("lastidx",lastidx);
                    Log.e("new : L , Ar , Fi , LE " , lastidx + " " + Arr.size() + " " + FirstTime + " " + lastE);
                    ED.commit();
                    return;
                }
                Collections.reverse(E);
                lastE = E.size();
                Arr.addAll(0,E);
                MakeRequest(Col+1,Par);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Fail","Faileeeee");
                if(!checkNetworkStatus(BackGroundService.this).equals("noNetwork"))
                    MakeRequest(Col,Par);
            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {

            }
        });
    }
    public static ArrayList<Elemnt> Getelemts(String HTMLString)
    {
        ArrayList<Elemnt> Arr = new ArrayList<>();
        int idx = HTMLString.indexOf("المدرس");
        if(idx==-1)
            return  Arr;
        String[] Temp = new String[7];
        while (HTMLString.indexOf("<span class ='font'>",idx)!=-1)
        {
            for(int i = 0 ; i < 7 ; i++)
            {
                if(i==6)
                {
                    int idx2 = HTMLString.indexOf("downloads",idx);
                    int idx3 = HTMLString.indexOf(".pdf",idx2)+4;
                    String Val = HTMLString.substring(idx2,idx3);
                    Temp[i] = Val;
                    idx = idx3;
                    continue;
                }
                int idx2 = HTMLString.indexOf("<span class ='font'>",idx);
                if(idx2==-1)
                    break;
                idx2+=19;
                int idx3 = HTMLString.indexOf("</span>",idx2);
                String Val = HTMLString.substring(idx2+1,idx3-1);
                Temp[i] = Val;
                idx = idx3;
            }
            Elemnt El = new Elemnt(Temp[0],Temp[1],Temp[2],Temp[3],Temp[4],Temp[5],Temp[6]);
            Log.e("El",El.toString());
            Arr.add(El);
        }
        return  Arr;
    }
    private String checkNetworkStatus(Context context) {

        String networkStatus ="";
        final ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //Check Wifi
        final android.net.NetworkInfo wifi = manager.getActiveNetworkInfo();
        //Check for mobile data
        final android.net.NetworkInfo mobile = manager.getActiveNetworkInfo();

        if( wifi!=null && wifi.getType() == ConnectivityManager.TYPE_WIFI) {
            networkStatus = "wifi";
        }else if(mobile !=null && mobile.getType() == ConnectivityManager.TYPE_MOBILE){
            networkStatus = "mobileData";
        }else{
            networkStatus="noNetwork";
        }
        return networkStatus;
    }
    private boolean ValidDate(int Houre , boolean AM)
    {
        if(AM && (Houre==10 || Houre == 11 ) )
            return true;
        else if(!AM)
        {
            if(Houre==12||Houre==1||Houre==2||Houre==3||Houre==4||Houre==5
                    || Houre==13||Houre==14||Houre==15||Houre==16||Houre==17)
            {
                return  true;
            }
        }
        return  false;
    }
}

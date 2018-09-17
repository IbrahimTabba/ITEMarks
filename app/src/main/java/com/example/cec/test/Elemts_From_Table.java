package com.example.cec.test;

import android.util.Log;

import java.util.ArrayList;

import javax.xml.transform.Templates;

/**
 * Created by CEC on 20-Aug-18.
 */

public class Elemts_From_Table {
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
}

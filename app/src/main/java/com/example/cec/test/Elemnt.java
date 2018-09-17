package com.example.cec.test;

import java.io.File;
import java.util.Collections;

/**
 * Created by CEC on 20-Aug-18.
 */

public class Elemnt {
    public String Src , Name , Date , StudyYear , Specialist , Turm , Teacher , FileName ;
    public boolean Downloaded , Downloading ;
    public int Progrees;
    public Elemnt(String Name , String Specialist , String StudyYear , String Date , String Turm , String Teacher , String Src )
    {
        this.Name = Name ; this.Specialist = Specialist ; this.StudyYear = StudyYear ; this.Date = Date ; this.Turm = Turm ;
        this.Src = "http://damasuniv.edu.sy/ite/"+Src ; this.Teacher = Teacher;
        this.Downloaded = Downloading = false;
        this.Progrees = 0;
        this.FileName = fileName(this.Src);
        File F  = new File(Data.ApplicationPath+'/'+FileName);
        if(F.exists())
        {
            Downloading = false;
            Downloaded = true;
        }
    }
    public Elemnt()
    {

    }

    @Override
    public String toString() {
        return Name + ' ' + Src + ' ' + Date + ' ' + StudyYear + ' ' + Specialist + ' ' + Teacher + ' ' + Turm ;
    }
    public static String  fileName(String Src)
    {
        int i = Src.length()-1;
        int j=0 ;
        String Res = "";
        while (Src.charAt(i)!='/')
        {
            j = i;
            i--;
        }
        while (j<Src.length())
        {
            Res+=Src.charAt(j);
            j++;
        }
        return Res;
    }
}

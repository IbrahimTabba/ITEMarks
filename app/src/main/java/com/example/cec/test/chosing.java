package com.example.cec.test;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class chosing extends AppCompatActivity implements Listener {
    Spinner Forth , Fifth ;
    Button B ;
    static String Params = "" ,  Year = "كل السنوات";
    ProgressDialog PG;
    SharedPreferences SP;
    SharedPreferences.Editor ED;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosing);
        SP = getSharedPreferences("Ibrahim",MODE_PRIVATE);
        ED = SP.edit();
        Data.Chosing = this;
        B = (Button) findViewById(R.id.Search);
        PG = new ProgressDialog(this);
        Data.ApplicationPath = Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + this.getApplicationContext().getPackageName()
                + "/Downloads";
        PG.setTitle("Connecting to damasuniv.edu.sy");
        PG.setMessage("Please Wait");
        B.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.F1.clear();
                Data.F2.clear();
                Data.Tak.clear();
                Data.Arr.clear();
                Http.MakeRequest(0,Params);
                PG.show();
            }
        });
        Forth = (Spinner) findViewById(R.id.S4);
        Fifth = (Spinner) findViewById(R.id.S5);
        Forth.setEnabled(false);
        Fifth.setEnabled(false);
        Forth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(((RadioButton)findViewById(R.id.c4)).isChecked()){
                if(Forth.getItemAtPosition(i).toString().equals("هندسة البرمجيات"))
                {
                    Params = "&department_id=2&StadyYear=4";
                    Year = "السنة الربعة هندسة البرمجيات";
                }
                else if(Forth.getItemAtPosition(i).toString().equals("الذكاء الصنعي واللغات الطبيعية"))
                {
                    Params = "&department_id=1&StadyYear=16";
                    Year = "السنة الربعة الذكاء الصنعي واللغات الطبيعية";
                }
                else
                {
                    Params = "&department_id=5&StadyYear=14";
                    Year = "السنة الربعة الذكاء النظم والشبكات الحاسوبية";
                }}
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Fifth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(((RadioButton)findViewById(R.id.c5)).isChecked()){
                if(Fifth.getItemAtPosition(i).toString().equals("هندسة البرمجيات"))
                {
                    Params = "&department_id=2&StadyYear=5";
                    Year = "السنة الخامسة هندسة البرمجيات";
                }
                else if(Fifth.getItemAtPosition(i).toString().equals("الذكاء الصنعي واللغات الطبيعية"))
                {
                    Params = "&department_id=1&StadyYear=9";
                    Year = "السنة الخامسة الذكاء الصنعي واللغات الطبيعية";
                }
                else
                {
                    Params = "&department_id=5&StadyYear=15";
                    Year = "السنة الخامسة الذكاء النظم والشبكات الحاسوبية";
                }}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        int val = SP.getInt("service",-1);
        if(val==-1)
            AlertNew();
        if(!checkServiceRunning() && ((val==-1)||(val==1)))
        {
            ED.putInt("service",1);
            ED.commit();
            Log.e("Service Started" , "LOL");
            startService(new Intent(chosing.this,BackGroundService.class));
        }
        else if(checkServiceRunning())
        {
            Log.e("Service"," Was Allready Stared");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Data.Chosing = null;
    }

    @Override
    public void emmit_elemnt() {

    }

    @Override
    public void emmit_progress() {

    }

    @Override
    public void emmit_connnected() {
        PG.dismiss();
        Intent I = new Intent(chosing.this,terms.class);
        I.putExtra("Year",Year);
        startActivity(I);
    }

    public void chose(View view) {
        if(view.getId()==R.id.c1)
        {
            Forth.setEnabled(false);
            Fifth.setEnabled(false);
            Params = "&department_id=3&StadyYear=7";
            Year = "السنة الأولى";
        }
        else if(view.getId()==R.id.c2)
        {
            Forth.setEnabled(false);
            Fifth.setEnabled(false);
            Params = "&department_id=3&StadyYear=8";
            Year = "السنة الثانية";
        }
        else if(view.getId()==R.id.c3)
        {
            Forth.setEnabled(false);
            Fifth.setEnabled(false);
            Params = "&department_id=3&StadyYear=13";
            Year = "السنة الثالثة";
        }
        else if(view.getId()==R.id.c4)
        {
            String S = Forth.getSelectedItem().toString();
            if(S.equals("هندسة البرمجيات"))
            {
                Params = "&department_id=2&StadyYear=4";
                Year = "السنة الربعة هندسة البرمجيات";
            }
            else if(S.equals("الذكاء الصنعي واللغات الطبيعية"))
            {
                Params = "&department_id=1&StadyYear=16";
                Year = "السنة الربعة الذكاء الصنعي واللغات الطبيعية";
            }
            else
            {
                Params = "&department_id=5&StadyYear=14";
                Year = "السنة الربعة الذكاء النظم والشبكات الحاسوبية";
            }
            Forth.setEnabled(true);
            Fifth.setEnabled(false);
        }
        else if(view.getId()==R.id.c5)
        {
            String S = Fifth.getSelectedItem().toString();
            if(S.equals("هندسة البرمجيات"))
            {
                Params = "&department_id=2&StadyYear=5";
                Year = "السنة الخامسة هندسة البرمجيات";
            }
            else if(S.equals("الذكاء الصنعي واللغات الطبيعية"))
            {
                Params = "&department_id=1&StadyYear=9";
                Year = "السنة الخامسة الذكاء الصنعي واللغات الطبيعية";
            }
            else
            {
                Params = "&department_id=5&StadyYear=15";
                Year = "السنة الخامسة الذكاء النظم والشبكات الحاسوبية";
            }
            Fifth.setEnabled(true);
            Forth.setEnabled(false);
        }
        else if(view.getId()==R.id.c6)
        {
            Forth.setEnabled(false);
            Fifth.setEnabled(false);
            Params = "";
            Year = "كل السنوات";
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean Actvated = false;
        int val = SP.getInt("service",-1);
        if(val==1)
            Actvated = true;
        menu.add("الإشعار بصدور نتائج جديدة").setActionView(R.layout.checkbox).setCheckable(true).setChecked(Actvated).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.isChecked())
                {
                    ED.putInt("service",0);
                    ED.commit();
                    stopService(new Intent(chosing.this,BackGroundService.class));
                }
                else
                {
                    ED.putInt("service",1);
                    ED.commit();
                    if(!checkServiceRunning())
                    {
                        Log.e("Service Started" , "LOL");
                        startService(new Intent(chosing.this,BackGroundService.class));
                    }
                }
                menuItem.setChecked(!menuItem.isChecked());
                return false;
            }
        });
        menu.add("حول").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Alert();
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    public boolean checkServiceRunning(){
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
        {
            if ("com.example.cec.test.BackGroundService"
                    .equals(service.service.getClassName()))
            {
                return true;
            }
        }
        return false;
    }
    public void Alert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(chosing.this);
        builder.setMessage(R.string.about)
                .setTitle("Info");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void AlertNew()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(chosing.this);
        builder.setMessage(R.string.neww)
                .setTitle("What's New !!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

package com.example.cec.test;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import com.loopj.android.http.*;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpTrace;
import cz.msebera.android.httpclient.client.utils.DateUtils;
import cz.msebera.android.httpclient.protocol.HTTP;
public class Http
{
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void MakeRequest(final int Col , final String Par)
    {
        client.setTimeout(300000);
        client.get("http://damasuniv.edu.sy/ite/index.php?End=6000&Coll="+Col+"&Start=0&set=14&lang=1&func=2&Ser=6"+Par, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
            {
                String S = new String(responseBody);
                ArrayList <Elemnt> E =  Elemts_From_Table.Getelemts(S);
                if(E.size()==0)
                {
                    if(Data.Chosing!=null)
                        Data.Chosing.emmit_connnected();
                    return;
                }
                Collections.reverse(E);
                Data.Add(E);
                MakeRequest(Col+1,Par);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("Fail","Faileeeee");
                MakeRequest(Col,Par);
            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
            }
        });
    }
    public static void DownloadPdf(final Elemnt elm , final int type) {
        RequestParams Parms = new RequestParams();
        client.setTimeout(300000);
        client.setConnectTimeout(300000);
        /*client.get(elm.Src, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String Path = File_Name(elm.FileName);
                elm.Progrees = 100;
                elm.Downloaded = true;
                elm.Downloading = false;
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(Path);
                    out.write(responseBody);
                    out.close();
                } catch (FileNotFoundException e) {
                    Log.e("FNO", e.toString());
                    //e.printStackTrace();
                } catch (IOException e) {
                    Log.e("IO", e.toString());
                    //e.printStackTrace();
                }
                Data.Progress(type);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //Log.e("Fail", "Failed To Downliad " + Src );
                elm.Progrees = 0;
                elm.Downloaded = elm.Downloading = false;
                Data.Progress(type);
                // Do Somthing
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                //super.onProgress(bytesWritten, totalSize);
                int P  = (int)((bytesWritten*100)/totalSize);
                if(P > elm.Progrees + 10 )
                {
                    elm.Progrees = P;
                    Data.Progress(type);
                }
            }
        });*/
        String Path = File_Name(elm.FileName);
        FileAsyncHttpResponseHandler FAHP = new FileAsyncHttpResponseHandler(new File(Path)) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                elm.Progrees = 0;
                elm.Downloaded = elm.Downloading = false;
                Data.Progress(type);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                elm.Progrees = 100;
                elm.Downloaded = true;
                elm.Downloading = false;
                Data.Progress(type);
            }
            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                int P  = (int)((bytesWritten*100)/totalSize);
                if(P > elm.Progrees + 10 )
                {
                    elm.Progrees = P;
                    Data.Progress(type);
                }
            }
        };
        try {
            client.get(elm.Src,FAHP);
        }catch (Exception e)
        {

        }
    }
    public static String File_Name(String Filename)
    {
        File mediaStorageDir = new File(Data.ApplicationPath);
        if (! mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        }
        String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ Filename);;
        return uriString;
    }
}

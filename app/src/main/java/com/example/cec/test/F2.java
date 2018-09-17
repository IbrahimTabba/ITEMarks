package com.example.cec.test;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by CEC on 22-Aug-18.
 */

public class F2 extends Fragment implements Listener {
    ListView list ;
    View reqview;
    Elemnt reqE;
    private static final int REQUEST_ID_READ_PERMISSION = 100 , REQUEST_ID_WRITE_PERMISSION = 200 , REQUEST_ID_CAMERA = 300;
    F2.ListResources listResources ;
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState){
        return inflater.inflate(R.layout.f2, viewGroup, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Data.f2Listener = this;
        list = (ListView) view.findViewById(R.id.listf2);
        listResources = new F2.ListResources(getContext());
        list.setAdapter(listResources);
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Data.F2.get(i).Downloaded)
                {
                    share(File_Name(Data.F2.get(i).FileName));
                }
                return true;
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Elemnt temp = Data.F2.get(i);
                //view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                if(!temp.Downloaded && !temp.Downloading)
                {
                    reqview =  view.findViewById(R.id.DownloadBtn);
                    reqE = temp;
                    askPermission(REQUEST_ID_WRITE_PERMISSION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    askPermission(REQUEST_ID_READ_PERMISSION,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                else if(temp.Downloaded)
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File url = null;
                    if(Build.VERSION.SDK_INT>=24)
                    {
                        try {
                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                            m.invoke(null);
                        }
                        catch (Exception e)
                        {
                            Log.e("Cant","Disable The Fucking Shotu");
                        }
                    }
                    url = new File(Data.ApplicationPath+'/'+temp.FileName);
                    Uri uri = Uri.fromFile(url);
                    intent.setDataAndType(uri, "application/pdf");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(Intent.createChooser(intent, "Open with"));
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Data.f2Listener = null;
    }

    @Override
    public void emmit_elemnt() {
        listResources.notifyDataSetChanged();
    }

    @Override
    public void emmit_progress() {
        listResources.notifyDataSetChanged();
    }

    @Override
    public void emmit_connnected() {
        listResources.notifyDataSetChanged();
    }

    class ListResources extends BaseAdapter
    {
        Context context;
        ListResources(Context context) {
            this.context = context;
        }
        @Override
        public int getCount() {
            return Data.F2.size();
        }
        @Override
        public Object getItem(int position) {
            return Data.F2.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            Elemnt temp = Data.F2.get(position);
            View row2 = inflater.inflate(R.layout.item, parent , false);
            if(position%2==0)
                row2.setBackgroundColor( Color.argb(13,100,24,50));
            TextView Name = (TextView) row2.findViewById(R.id.Name);
            TextView Year = (TextView) row2.findViewById(R.id.Year);
            ImageView Download = (ImageView) row2.findViewById(R.id.DownloadBtn);
            ProgressBar Pb = (ProgressBar) row2.findViewById(R.id.progressBar);
            Pb.getProgressDrawable().setColorFilter(
                    Color.rgb(100,24,50), android.graphics.PorterDuff.Mode.SRC_IN);
            Name.setText(temp.Name);
            Year.setText(temp.Date);
            if(temp.Downloaded)
            {
                Download.setImageResource(R.drawable.downloaded);
                Download.setAlpha(1.0f);
            }
            else
            {
                Download.setImageResource(R.drawable.downloads);
                if(temp.Downloading)
                    Download.setAlpha(0.5f);
            }
            if(temp.Downloading)
                Pb.setVisibility(View.VISIBLE);
            else
                Pb.setVisibility(View.GONE);
            Pb.setProgress(temp.Progrees);
            return row2;
        }
    }
    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(getContext(), permissionName);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        reqview.setAlpha(0.5f);
        reqE.Downloading = true;
        Http.DownloadPdf(reqE,2);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED || (
                grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ))
        {
            reqview.setAlpha(0.5f);
            reqE.Downloading = true;
            Http.DownloadPdf(reqE,2);
        }
    }
    public static String File_Name(String Filename)
    {

        File mediaStorageDir = new File(Data.ApplicationPath);
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
        }
        String uriString = (mediaStorageDir.getAbsolutePath() + "/"+ Filename);;
        return uriString;
    }
    public void share(String Path)
    {
        try {

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(Path);

            if (fileWithinMyDir.exists()) {
                intentShareFile.setType("application/pdf");
                Uri U = Uri.fromFile(fileWithinMyDir);
                intentShareFile.putExtra(Intent.EXTRA_STREAM, U);
                intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                        "Sharing File...");
                intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");
                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }
        }
        catch (Exception e)
        {

        }
    }
}

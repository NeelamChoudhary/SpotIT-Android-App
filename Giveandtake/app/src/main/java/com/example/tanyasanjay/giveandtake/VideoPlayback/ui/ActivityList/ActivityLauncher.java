/*===============================================================================
Copyright (c) 2016 PTC Inc. All Rights Reserved.

Copyright (c) 2012-2015 Qualcomm Connected Experiences, Inc. All Rights Reserved.

Vuforia is a trademark of PTC Inc., registered in the United States and other 
countries.
===============================================================================*/


package com.example.tanyasanjay.giveandtake.VideoPlayback.ui.ActivityList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.tanyasanjay.giveandtake.R;



// This activity starts activities which demonstrate the Vuforia features
public class ActivityLauncher extends ListActivity
{
    String URL_LINK;
    private String mActivities[] = { "Video on Image Targets", "User Defined Targets"};
    
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            R.layout.activities_list_text_view, mActivities);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activities_list);
        setListAdapter(adapter);
        //neelam
        Intent intent = getIntent();
        URL_LINK = intent.getStringExtra("URL_LINK");
        Log.d("neelam0",URL_LINK);

    }
    
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Log.d("neelam00",URL_LINK);
        Intent intent = new Intent(this, AboutScreen.class);
        intent.putExtra("ABOUT_TEXT_TITLE", mActivities[position]);

        switch (position)
        {
            case 0:
                intent.putExtra("ACTIVITY_TO_LAUNCH", "VideoPlayback.app.VideoPlayback.VideoPlayback"); //vuforia.samples.VideoPlayback.app.VideoPlayback.VideoPlayback
                intent.putExtra("URL_LINK",URL_LINK);
                intent.putExtra("ABOUT_TEXT", "assets/VideoPlayback/VP_about.html");
                break;
            case 1:
                intent.putExtra("ACTIVITY_TO_LAUNCH", "UserDefinedTargets.UserDefinedTargets");
                intent.putExtra("ABOUT_TEXT", "assets/UserDefinedTargets/UD_about.html");
                break;

//            case 1:
//                intent.putExtra("ACTIVITY_TO_LAUNCH",
//                    "app.CylinderTargets.CylinderTargets");
//                intent.putExtra("ABOUT_TEXT", "CylinderTargets/CY_about.html");
//                break;
//            case 2:
//                intent.putExtra("ACTIVITY_TO_LAUNCH",
//                    "app.MultiTargets.MultiTargets");
//                intent.putExtra("ABOUT_TEXT", "MultiTargets/MT_about.html");
//                break;
//            case 4:
//                intent.putExtra("ACTIVITY_TO_LAUNCH",
//                    "app.ObjectRecognition.ObjectTargets");
//                intent.putExtra("ABOUT_TEXT", "ObjectRecognition/OR_about.html");
//                break;
//            case 5:
//                intent.putExtra("ACTIVITY_TO_LAUNCH",
//                    "app.CloudRecognition.CloudReco");
//                intent.putExtra("ABOUT_TEXT", "CloudReco/CR_about.html");
//                break;
//            case 6:
//                intent.putExtra("ACTIVITY_TO_LAUNCH",
//                    "app.TextRecognition.TextReco");
//                intent.putExtra("ABOUT_TEXT", "TextReco/TR_about.html");
//                break;
//            case 7:
//                intent.putExtra("ACTIVITY_TO_LAUNCH",
//                    "app.FrameMarkers.FrameMarkers");
//                intent.putExtra("ABOUT_TEXT", "FrameMarkers/FM_about.html");
//                break;
//            case 8:
//                intent.putExtra("ACTIVITY_TO_LAUNCH",
//                    "app.VirtualButtons.VirtualButtons");
//                intent.putExtra("ABOUT_TEXT", "VirtualButtons/VB_about.html");
//                break;
        }

        startActivity(intent);

    }
}

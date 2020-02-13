package com.scrnz.shelllibraryexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.multidex.MultiDex;

import com.google.gson.Gson;
import com.screenz.shell_library.ShellLibraryBuilder;
import com.screenz.shell_library.config.ConfigManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SplitActivity extends FragmentActivity {
    private static final String CONFIG_FILE_NAME = "config.json";
    private static SplitActivity INSTANCE;
    private LocalConfig mLocalConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.split_activity);

        final String path = "android.resource://" + getPackageName() + "/raw/video";

        Uri uri = Uri.parse(path);
        MediaController mediaController = new MediaController(this);

        VideoView simpleVideoView = findViewById(R.id.split_video_view);
        simpleVideoView.setMediaController(mediaController);
        simpleVideoView.setVideoURI(uri);
        simpleVideoView.start();

        loadLocalConfig();

        ConfigManager configManager = ConfigManager.getInstance();
        if (mLocalConfig.core != null) {
            configManager.setCoreData(mLocalConfig.core);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.split_framework_view, ShellLibraryBuilder.create(this)).commit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.split_framework_view);
        if (currentFragment != null){
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private BroadcastReceiver dataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("data");
            Log.d("DATA RECEIVER",data);
            if(data.equalsIgnoreCase("sdk-exit-new")){
                finish();
            }
        }
    };

    public void finish() {
        this.unregisterReceiver(dataReceiver);
        super.finish();
    };

    private void loadLocalConfig() {
        BufferedReader in = null;
        try {
            InputStream json = getAssets().open(CONFIG_FILE_NAME);
            in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;
            StringBuilder buf = new StringBuilder();
            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            mLocalConfig = new Gson().fromJson(buf.toString(), LocalConfig.class);
        } catch (Exception e) {
            //Do nothing
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    //Do nothing
                }
            }
        }
    }
}

package com.scrnz.shelllibraryexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.google.gson.Gson;
import com.screenz.shell_library.ShellLibraryBuilder;
import com.screenz.shell_library.config.ConfigManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by MC.
 */
public class TestActivity extends FragmentActivity {

    private static final String CONFIG_FILE_NAME = "config.json";
    private static TestActivity INSTANCE;
    private LocalConfig mLocalConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        /**
         ******** Config *******
         */

        loadLocalConfig();

        ConfigManager configManager = ConfigManager.getInstance();
        if (mLocalConfig.core != null) {
            configManager.setCoreData(mLocalConfig.core);
        }
        if (mLocalConfig.facebook != null) {
            configManager.setFacebookData(mLocalConfig.facebook);
        }
        if (mLocalConfig.google != null) {
            configManager.setGooglePlusData(mLocalConfig.google);
        }
        if (mLocalConfig.twitter != null) {
            configManager.setTwitterData(mLocalConfig.twitter);
        }
//        configManager.setExtraData(this,"#data_to_store"); //In case you want to provide data to the sdk
//        configManager.setLaunchPageID(this,"#PAGEID"); //In case you want to set the pageid to be opened on launch
//        configManager.setPid(this,#PID); //In case you want to set the pageid to be opened on launch

        /**
         ******** Config *******
         */
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ShellLibraryBuilder.create(this)).commit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment != null){
            currentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

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

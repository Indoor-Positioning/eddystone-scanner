package com.mooo.sestus.eddystonescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static java.security.AccessController.getContext;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchScanBeaconsActivity(View v) {
        startActivity(ScanBeaconsActivity.getCallingIntent(this));
    }
}

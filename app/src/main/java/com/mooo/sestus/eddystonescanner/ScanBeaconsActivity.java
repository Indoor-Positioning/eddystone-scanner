package com.mooo.sestus.eddystonescanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ScanBeaconsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_beacons);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ScanBeaconsActivity.class);
    }
}

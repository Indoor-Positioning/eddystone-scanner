package com.mooo.sestus.eddystonescanner;

import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

public class ScanBeaconsActivity extends ListActivity {

    private DrawerLayout drawerLayout;
    private static final String TAG = "ScanBeaconResults : ";
    private static ScanCallback cb;
    private LeDeviceListAdapter mLeDeviceListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_beacons);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_scan_beacons);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
            addDrawerContent(navigationView);
        cb = createScanCallback();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        PermissionManager.enableLocation(this);
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        setListAdapter(mLeDeviceListAdapter);

        if (PermissionManager.hasPermission(this)) {
            scanLe();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
            if (!PermissionManager.hasPermission(this))
                finish();
            scanLe();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopScan();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stopScan() {
        BluetoothManager blManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter blAdapter = blManager.getAdapter();
        BluetoothLeScanner leScanner = blAdapter.getBluetoothLeScanner();
        if (!blAdapter.isEnabled())
            return;
        Log.v(TAG, "Stoping scan");
        leScanner.stopScan(cb);
    }

    private void scanLe() {
        BluetoothManager blManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter blAdapter = blManager.getAdapter();
        if (blAdapter != null) {
            if (!blAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
            else {
                BluetoothLeScanner leScanner = blAdapter.getBluetoothLeScanner();
                Log.v(TAG, "Starting scan");
                ScanSettings settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                leScanner.startScan(null, settings, cb);
            }
        }
    }

    private void addDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    default:
                        break;
                }
                item.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ScanBeaconsActivity.class);
    }


    @NonNull
    private ScanCallback createScanCallback() {
        return new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                Log.v(TAG, "OnScanResult : " +  String.valueOf(callbackType) + "  " + result);
                mLeDeviceListAdapter.addScanResult(result);
                mLeDeviceListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                for (ScanResult result: results) {
                    mLeDeviceListAdapter.addScanResult(result);
                    Log.v(TAG, "onBatchScanResults : " + result.toString());
                }
                mLeDeviceListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.v(TAG, "Scan failed with errorCode: " + String.valueOf(errorCode));
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        scanLe();
    }
}

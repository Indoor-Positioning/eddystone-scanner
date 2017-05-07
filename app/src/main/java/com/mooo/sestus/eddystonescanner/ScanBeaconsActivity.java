package com.mooo.sestus.eddystonescanner;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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


    @Override
    protected void onResume() {
        super.onResume();

        // Use this check to determine whether BLE is supported on the device. Then
        // you can selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);

        if (hasPermission()) {
            scanLe();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
            if (!hasPermission())
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
            if (blAdapter.isEnabled()) {
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

    private boolean hasPermission() {
        return(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION));
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

    protected void addFragment(int containerViewId, Fragment fragment) {
        final FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<ScanResult> scanResults;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            scanResults = new ArrayList<>();
            mInflator = ScanBeaconsActivity.this.getLayoutInflater();
        }

        public void addScanResult(ScanResult scanResult) {
            if(!scanResults.contains(scanResult)) {
                scanResults.add(scanResult);
            }
        }

        public ScanResult getDevice(int position) {
            return scanResults.get(position);
        }

        public void clear() {
            scanResults.clear();
        }

        @Override
        public int getCount() {
            return scanResults.size();
        }

        @Override
        public Object getItem(int i) {
            return scanResults.get(i);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.rssi = (TextView) view.findViewById(R.id.rssi);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            ScanResult scanResult = scanResults.get(i);
            final String deviceName = scanResult.getDevice().getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(scanResult.getDevice().getAddress());
            viewHolder.rssi.setText(String.valueOf(scanResult.getRssi()));

            return view;
        }
    }


    private static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView rssi;
    }

}

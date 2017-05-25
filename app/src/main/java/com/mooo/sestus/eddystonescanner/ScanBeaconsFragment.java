package com.mooo.sestus.eddystonescanner;

import android.Manifest;
import android.app.ListFragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ScanBeaconsFragment extends ListFragment {

    private static final String TAG = "ScanBeaconResults : ";
    private static ScanCallback cb;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private SensorManager sensorManager;
    private Sensor mSensor;
    private SensorEventListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan_beacons, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);


        cb = createScanCallback();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();

        if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        PermissionManager.enableLocation(getActivity());
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter(this);
        setListAdapter(mLeDeviceListAdapter);

        if (PermissionManager.hasPermission(getActivity())) {
            scanLe();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
            if (!PermissionManager.hasPermission(getActivity()))
                getActivity().finish();
            scanLe();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopScan();
    }

    private static List<ScanFilter> buildScanFilters() {
        List<ScanFilter> scanFilters = new ArrayList<>();
        scanFilters.add(BeaconUtils.EDDYSTONE_SCAN_FILTER);
        return scanFilters;
    }

    private void stopScan() {
        BluetoothManager blManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter blAdapter = blManager.getAdapter();
        BluetoothLeScanner leScanner = blAdapter.getBluetoothLeScanner();
        if (!blAdapter.isEnabled())
            return;
        Log.v(TAG, "Stopping scan");
        leScanner.stopScan(cb);
        sensorManager.unregisterListener(listener);
    }

    private void scanLe() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.v(TAG, String.format("Magnetic (x,y,z) : (%f, %f, %f", event.values[0], event.values[1], event.values[2]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(listener,
                mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);


        BluetoothManager blManager = (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
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
                leScanner.startScan(buildScanFilters(), settings, cb);
            }
        }
    }


    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ScanBeaconsFragment.class);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        scanLe();
    }
}

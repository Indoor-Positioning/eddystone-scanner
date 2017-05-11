package com.mooo.sestus.eddystonescanner;

import android.bluetooth.le.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mmilonakis on 8/5/2017.
 */ // Adapter for holding devices found through scanning.
public class LeDeviceListAdapter extends BaseAdapter {
    private ScanBeaconsFragment scanBeaconsActivity;
    private ArrayList<ScanResult> scanResults;
    private HashMap<String, Integer> addressToIndexMap;
    private LayoutInflater mInflator;

    public LeDeviceListAdapter(ScanBeaconsFragment scanBeaconsActivity) {
        super();
        this.scanBeaconsActivity = scanBeaconsActivity;
        scanResults = new ArrayList<>();
        addressToIndexMap = new HashMap<>();
        mInflator = scanBeaconsActivity.getActivity().getLayoutInflater();
    }

    public void addScanResult(ScanResult scanResult) {
        String address = scanResult.getDevice().getAddress();
        if (addressToIndexMap.containsKey(address)) {
            scanResults.set(addressToIndexMap.get(address), scanResult);
        } else {
            scanResults.add(scanResult);
            addressToIndexMap.put(address, scanResults.size() - 1);
        }
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

    private static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView rssi;
    }
}



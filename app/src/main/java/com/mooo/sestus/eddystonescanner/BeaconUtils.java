package com.mooo.sestus.eddystonescanner;

import android.bluetooth.le.ScanFilter;
import android.os.ParcelUuid;

public class BeaconUtils {
    public static final ParcelUuid EDDYSTONE_SERVICE_UUID =
            ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");
    static final byte[] NAMESPACE_FILTER = {
            0x00, //Frame type
            0x00, //TX power
            (byte)0x01, (byte)0x23, (byte)0x45, (byte)0x67, (byte)0x89,
            (byte)0xab, (byte)0xcd, (byte)0xef, (byte)0x01, (byte)0x23,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };
    static final byte[] NAMESPACE_FILTER_MASK = {
            (byte)0xFF,
            0x00,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00
    };

    static final ScanFilter EDDYSTONE_SCAN_FILTER = new ScanFilter.Builder()
            .setServiceUuid(EDDYSTONE_SERVICE_UUID)
            .setServiceData(EDDYSTONE_SERVICE_UUID, NAMESPACE_FILTER, NAMESPACE_FILTER_MASK)
            .build();
}

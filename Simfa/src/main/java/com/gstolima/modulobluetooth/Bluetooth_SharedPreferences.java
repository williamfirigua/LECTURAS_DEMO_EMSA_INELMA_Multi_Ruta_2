package com.gstolima.modulobluetooth;

import android.content.Context;

public final class Bluetooth_SharedPreferences {

    private static final String PREF = "printer_pref";
    private static final String KEY = "printer_mac";

    private Bluetooth_SharedPreferences() {
    }

    public static String get(Context c) {
        return c.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY, null);
    }

    public static void save(Context c, String mac) {
        c.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY, mac)
                .apply();
    }

    public static void clear(Context c) {
        c.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .remove(KEY)
                .apply();
    }
}

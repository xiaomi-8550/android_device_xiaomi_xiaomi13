/*
 * Copyright (C) 2023 Paranoid Android
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.xiaomi.settings.display;

import static android.provider.Settings.System.DC_DIMMING_STATE;
import static com.xiaomi.settings.display.DfWrapper.DfParams;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;

public class DcDimmingService extends Service {

    private static final String TAG = "XiaomiPartsDcDimmingService";
    private static final boolean DEBUG = Log.isLoggable(TAG, Log.DEBUG);

    private boolean mIsDcDimmingEnabled;
    private boolean mIsScreenOn;

    private Handler mHandler = new Handler();

    private final ContentObserver mSettingObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            if (DEBUG) Log.d(TAG, "SettingObserver: onChange");
            updateDcDimming();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) Log.d(TAG, "Creating service");
        getContentResolver().registerContentObserver(Settings.System.getUriFor(DC_DIMMING_STATE),
                    false, mSettingObserver, UserHandle.USER_CURRENT);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.d(TAG, "Starting service");
        updateDcDimming();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.d(TAG, "Destroying service");
        getContentResolver().unregisterContentObserver(mSettingObserver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void updateDcDimming() {
        final int enabled = Settings.System.getInt(getContentResolver(),
                Settings.System.DC_DIMMING_STATE, 0);
        if (DEBUG) Log.d(TAG, "updateDcDimming: enabled=" + enabled);
        try {
            DfWrapper.setDisplayFeature(
                    new DfWrapper.DfParams(/*DC_BACKLIGHT_STATE*/ 20, enabled, 0));
        } catch (Exception e) {
            Log.e(TAG, "updateDcDimming failed!", e);
        }
    }
}
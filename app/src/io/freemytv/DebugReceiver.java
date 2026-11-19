package io.freemytv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This fork's testing hook: adb sets the palette and the accent and the home screen redraws at once, without
 * driving the settings with key events (and it works from a script):
 *
 *   adb shell am broadcast -a io.freemytv.DEBUG_LOOK   --ei value 2
 *   adb shell am broadcast -a io.freemytv.DEBUG_ACCENT --ei value 1
 *
 * Runs in the :home process, so Prefs.setLook's version bump is the home screen's own and it repaints immediately.
 * Nothing else listens, and a value that makes no sense is clamped away.
 */
public class DebugReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context c, Intent i) {
        String action = i.getAction();
        int value = i.getIntExtra("value", -1);
        AppLog.w("debug receiver: " + action + " value=" + value);
        try {
            if ("io.freemytv.DEBUG_LOOK".equals(action)) {
                Prefs.setLook(Math.max(0, Math.min(Look.NAMES.length - 1, value)));
            } else if ("io.freemytv.DEBUG_ACCENT".equals(action)) {
                Prefs.setAccent(Math.max(0, Math.min(Look.ACCENTS.length - 1, value)));
            }
        } catch (RuntimeException e) {
            AppLog.w("debug receiver: " + e.getMessage());
        }
    }
}

package com.little.framework.utils;

import android.app.ActivityManager;
import android.content.Context;
import java.util.List;

/**
 *
 */
public class ProcessUtils {

    private static volatile String sProcessName;
    private final static Object sNameLock = new Object();

    private static volatile Boolean sMainProcess;
    private final static Object sMainLock = new Object();


    public static String myProcessName(Context context) {
        if (sProcessName != null) {
            return sProcessName;
        }
        synchronized (sNameLock) {
            if (sProcessName != null) {
                return sProcessName;
            }
            return sProcessName = obtainProcessName(context);
        }
    }

    public static boolean isMainProcess(Context context) {
        if (sMainProcess != null) {
            return sMainProcess;
        }
        synchronized (sMainLock) {
            if (sMainProcess != null) {
                return sMainProcess;
            }
            final String processName = myProcessName(context);
            if (processName == null) {
                return false;
            }
            sMainProcess = processName.equals(context.getApplicationInfo().processName);
            return sMainProcess;
        }
    }

    private static String obtainProcessName(Context context) {
        final int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> listTaskInfo = am.getRunningAppProcesses();
        if (listTaskInfo != null && listTaskInfo.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo proc : listTaskInfo) {
                if (proc != null && proc.pid == pid) {
                    return proc.processName;
                }
            }
        }
        return null;
    }

    private ProcessUtils() {
        // static usage.
    }
}

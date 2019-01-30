package com.gemini.kvod2;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.gemini.play.MGplayer;
import java.util.List;

public class StartupReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        MGplayer.MyPrintln("======onReceive=======");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            List<RunningTaskInfo> runningTasks = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
            Intent ootStartIntent;
            if (runningTasks.size() > 0) {
                ComponentName topActivity = ((RunningTaskInfo) runningTasks.get(0)).topActivity;
                if (!topActivity.getPackageName().equals("com.gemini.gp2p") || topActivity.getClassName().equals("com.gemini.gp2p.ui.activity.ValidateActivity")) {
                    ootStartIntent = new Intent(context, ValidateActivity.class);
                    ootStartIntent.addFlags(268435456);
                    context.startActivity(ootStartIntent);
                    return;
                }
                return;
            }
            ootStartIntent = new Intent(context, ValidateActivity.class);
            ootStartIntent.addFlags(268435456);
            context.startActivity(ootStartIntent);
        }
    }
}

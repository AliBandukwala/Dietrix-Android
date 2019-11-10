package dietrixapp.dietrix;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.ALARM_SERVICE;
import static dietrixapp.dietrix.MyDb.TblClients.CLDUR;
import static dietrixapp.dietrix.MyDb.TblClients.CLFEE;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,BackgroundService.class);
        context.startService(intent1);
    }
}

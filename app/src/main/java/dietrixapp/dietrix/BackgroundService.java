package dietrixapp.dietrix;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import static dietrixapp.dietrix.MyDb.TblClients.CLDUR;
import static dietrixapp.dietrix.MyDb.TblClients.CLFEE;

public class BackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    Cursor cursor;
    private static final String CHANNEL_ID = "ServiceChannel";

    //Some Other Method:
    public  Double getMonthlyIncome()
    {
        double sum = 0, value;
        cursor = MyDb.TblClients.getFeesData();
        if(cursor != null)
        {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext())
            {
                String dur = cursor.getString(cursor.getColumnIndex(CLDUR));
                Double fee = Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLFEE)));

                if(dur.equalsIgnoreCase("1 month"))
                {sum += fee;}
                else if(dur.equalsIgnoreCase("3 month"))
                {sum += (fee/3);}
                else if(dur.equalsIgnoreCase("6 month"))
                {sum += (fee/6);}
                else if(dur.equalsIgnoreCase("1 year"))
                {sum += (fee/12);}
            }
            cursor.close();
        }

        if((sum % 10) != 0)
        {
            DecimalFormat decf = new DecimalFormat(".##");
            value = Double.parseDouble(decf.format(sum));
            return value;
        }

        return  sum;
    }

    //Some Other Method:
    public int getActCltNum()
    {
        cursor = MyDb.TblClients.getActClients("Active");
        if (cursor != null) {
            return cursor.getCount();
        }

        return 0;
    }

    //Some Other Method:
    private long getInterval()
    {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH);
        currentMonth++;

        if(currentMonth > Calendar.DECEMBER)
        {
            currentMonth = Calendar.JANUARY;
            c.set(Calendar.YEAR, c.get(Calendar.YEAR)+1);
        }

        c.set(Calendar.MONTH,currentMonth);

        int maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        c.set(Calendar.DAY_OF_MONTH,maxDays);
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.SECOND,50);
        c.set(Calendar.MILLISECOND,0);

        return c.getTimeInMillis();
    }

    // Method to re-create next Alarm:
    private void Alarm()
    {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,
                new Intent(this,Receiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null)
        {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,getInterval(),pendingIntent);
        }
    }

    //Service-Handling Method:
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"serviceChannel",
                        NotificationManager.IMPORTANCE_DEFAULT);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }
            }

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Financial Report")
                .setContentText("Creating the new Financial Report")
                .setSmallIcon(R.drawable.ic_notif_finance);

        startForeground(1,notification.build());

        long inserted = MyDb.Tbl_Income_Act.insertIncData(String.valueOf(getMonthlyIncome()), String.valueOf(getActCltNum())
                , new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));

        if(inserted > 0)
        {
            PendingIntent pendingIntent = PendingIntent.getActivity(this,111,
                    new Intent(this,Payments.class),PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notification2 = new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setContentTitle("Financial Report")
                    .setContentText("New Financial Report has been created.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_notif_finance);
            if (notificationManager != null) {
                notificationManager.notify(2,notification2.build());
            }

            Alarm();
            stopSelf();
        }

        return START_NOT_STICKY;
    }
}

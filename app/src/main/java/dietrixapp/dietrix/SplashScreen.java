package dietrixapp.dietrix;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.database.Cursor;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static dietrixapp.dietrix.MyDb.TblBoot.USRFTNM;

public class SplashScreen extends AppCompatActivity implements PurchasesUpdatedListener {

    BillingClient billingClient;
    TextView SplashName;
    MyDb myDb;
    Button SplashSubsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        myDb = new MyDb(this);
        SplashName = findViewById(R.id.SplashNameTxt);
        SplashSubsBtn = findViewById(R.id.SplashSubsBtn);

        SplashSubsBtn.setVisibility(View.VISIBLE);

        boolean isFirstRun = getSharedPreferences("PREF",MODE_PRIVATE).getBoolean("isFirstRun",true);
        if(isFirstRun){
            startActivity(new Intent(this,IntroViewPager.class));
            getSharedPreferences("PREF",MODE_PRIVATE).edit().putBoolean("isFirstRun",false).apply();
        }
        else {
            View decorView = getWindow().getDecorView();
            int ui = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(ui);

            Cursor c = MyDb.TblBoot.getBootName();
            try{
                if(c!=null) {
                    c.moveToPosition(-1);
                    while (c.moveToNext()) {
                        SplashName.setText(c.getString(c.getColumnIndex(USRFTNM)));
                    }
                }
            }catch (Exception e){e.printStackTrace();}

            billingClient = BillingClient.newBuilder(this).setListener(this).build();
            billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(int responseCode) {
                    queryPurchase();
                }
                @Override
                public void onBillingServiceDisconnected() {
                    Toast.makeText(getBaseContext(),"Transaction cancelled!",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void SplashSubs(View view)
    {
        int i = billingClient.isFeatureSupported(BillingClient.FeatureType.SUBSCRIPTIONS);
        if(i != BillingClient.BillingResponse.FEATURE_NOT_SUPPORTED){
        BillingFlowParams params = new BillingFlowParams().newBuilder()
                .setType(BillingClient.SkuType.SUBS).setSku("dietrix2").build();
        billingClient.launchBillingFlow(this,params);
        }else {Toast.makeText(getBaseContext(),"Subscriptions are NOT supported on your Device!",Toast.LENGTH_SHORT).show();}
    }


    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if(responseCode == BillingClient.BillingResponse.OK){
            SplashSubsBtn.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 25) { createShortcuts(); }
            timer();
        }
        else if(responseCode == BillingClient.BillingResponse.USER_CANCELED){
            Toast.makeText(getBaseContext(),"Transaction cancelled!",Toast.LENGTH_SHORT).show();
            SplashSubsBtn.setVisibility(View.VISIBLE);
            removeShortcuts();
        }
        else if(responseCode == BillingClient.BillingResponse.ERROR){
            Toast.makeText(getBaseContext(),"Some Unknown error occurred",Toast.LENGTH_SHORT).show();
            SplashSubsBtn.setVisibility(View.VISIBLE);
            removeShortcuts();
        }
    }

    public void queryPurchase(){
        Purchase.PurchasesResult subsResult = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
        if(subsResult.getResponseCode() == BillingClient.BillingResponse.OK){
            subsResult.getPurchasesList();
            for(Purchase purchase : subsResult.getPurchasesList()){
                boolean subsState = purchase.isAutoRenewing();
                if(subsState)
                {
                    SplashSubsBtn.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= 25) { createShortcuts(); }
                    timer();
                }
                else {
                    SplashSubsBtn.setVisibility(View.VISIBLE);
                    removeShortcuts();
                }
            }
        }
    }

    public  void timer(){
        TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    finish();
                    Intent intent = new Intent().setClass(SplashScreen.this,MainActivity.class);
                    startActivity(intent);

                }
            };
            Timer timer = new Timer();
            timer.schedule(task,2250);
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    public void createShortcuts()
    {
        ShortcutManager sm = getSystemService(ShortcutManager.class);

        Intent intentAdd = new Intent(getApplicationContext(),AddClientAct.class);
        intentAdd.setAction(Intent.ACTION_VIEW);

        Intent intentPay = new Intent(getApplicationContext(),Payments.class);
        intentPay.setAction(Intent.ACTION_VIEW);

        Intent intentWeight = new Intent(getApplicationContext(), Weights.class);
        intentWeight.setAction(Intent.ACTION_VIEW);

        /*Intent intentChart = new Intent(getApplicationContext(), ChartsMain.class);
        intentChart.setAction(Intent.ACTION_VIEW);*/


        ShortcutInfo shortcut1 = new ShortcutInfo.Builder(this,"sc1")
                .setIntent(intentAdd)
                .setLongLabel(getString(R.string.shortcutLongLabel))
                .setDisabledMessage(getString(R.string.shortcutDisable))
                .setShortLabel(getString(R.string.shortcutShortLabel))
                .setIcon(Icon.createWithResource(this,R.drawable.ic_shortcut_addclt))
                .build();

        ShortcutInfo shortcut2 = new ShortcutInfo.Builder(this,"sc2")
                .setIntent(intentPay)
                .setLongLabel(getString(R.string.shortcutPayLong))
                .setDisabledMessage(getString(R.string.shortcutPayDisable))
                .setShortLabel(getString(R.string.shortcutPayShort))
                .setIcon(Icon.createWithResource(this,R.drawable.ic_shortcut_money))
                .build();

        ShortcutInfo shortcut3 = new ShortcutInfo.Builder(this,"sc3")
                .setIntent(intentWeight)
                .setDisabledMessage(getString(R.string.shortcutDisable))
                .setLongLabel(getString(R.string.shortcutWeightLong))
                .setShortLabel(getString(R.string.shortcutWeightShort))
                .setIcon(Icon.createWithResource(this,R.drawable.ic_shortcut_weights))
                .build();

        /*ShortcutInfo shortcut4 = new ShortcutInfo.Builder(this,"sc4")
                .setIntent(intentChart)
                .setDisabledMessage(getString(R.string.shortcutDisable))
                .setLongLabel(getString(R.string.shortcutChartLong))
                .setShortLabel(getString(R.string.shortcutChartShort))
                .setIcon(Icon.createWithResource(this,R.drawable.ic_shortcut_charts))
                .build();*/

        if (sm != null) {
            sm.setDynamicShortcuts(Arrays.asList(shortcut1,shortcut2,shortcut3));
        }
    }

    public void  removeShortcuts(){
        ShortcutManager sm;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N_MR1) {
            sm = getSystemService(ShortcutManager.class);
            if(sm != null)
            sm.removeAllDynamicShortcuts();
        }

    }
}

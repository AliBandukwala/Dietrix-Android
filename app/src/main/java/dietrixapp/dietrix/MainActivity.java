package dietrixapp.dietrix;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.DragEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import static dietrixapp.dietrix.MyDb.TblBoot.USRPAY;
import static dietrixapp.dietrix.MyDb.TblClients.CLACT;
import static dietrixapp.dietrix.MyDb.TblClients.CLDUE;
import static dietrixapp.dietrix.MyDb.TblClients.CLDUR;
import static dietrixapp.dietrix.MyDb.TblClients.CLFEE;
import static dietrixapp.dietrix.MyDb.TblClients.CLID;
import static dietrixapp.dietrix.MyDb.TblClients.CLMNO;
import static dietrixapp.dietrix.MyDb.TblClients.CLNM;
import static dietrixapp.dietrix.MyDb.TblClients.CLSPCS;
import static dietrixapp.dietrix.MyDb.Tbl_Income_Act.INCPREMNTH;

public class MainActivity extends AppCompatActivity{

    RecyclerView rv;
    RecyclerAdapter recyclerAdapter;
    MyDb myDb;
    ArrayList<RecClNames> recClNames;
    FloatingActionButton fab, fabChart, fabAddClt;
    boolean isFabOpen = false;
    TextView noCltMsg;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    SharedPreferences settingPref;
    private static final String CHANNEL_ID = "MainActChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDb = new MyDb(this);
        recClNames = new ArrayList<>();
        rv = findViewById(R.id.recyclerView);
        recyclerAdapter = new RecyclerAdapter(this, recClNames);
        rv.setAdapter(recyclerAdapter);
        noCltMsg = findViewById(R.id.NoCltMsgTxt);
        rv.setLayoutManager(new LinearLayoutManager(this));
        settingPref = getSharedPreferences("SettingsPref", Context.MODE_PRIVATE);
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {animateFab();}
        });
        fabChart = findViewById(R.id.fabChart);
        fabChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ChartsMain.class);
                startActivity(intent);
            }
        });
        fabAddClt = findViewById(R.id.fabAddCl);
        fabAddClt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddClientAct.class);
                startActivity(intent);
            }
        });

        retrieveAct();

        //Checking for INC_DATA Table:
        checkForIncData();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                if (direction == ItemTouchHelper.RIGHT) {

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String today = df.format(c.getTime());
                    TextView tvDue = viewHolder.itemView.findViewById(R.id.ClDueTxt);

                    TextView tvAct = viewHolder.itemView.findViewById(R.id.ClActTxt);
                    if (tvAct.getText().toString().equalsIgnoreCase("Inactive")) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(MainActivity.this);
                        alert1.setCancelable(false);
                        alert1.setTitle("Warning!");
                        final TextView tvName = viewHolder.itemView.findViewById(R.id.ClientNameText);
                        alert1.setMessage(tvName.getText().toString() + " is currently INACTIVE! Turn Active to continue.");

                        alert1.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        });
                        alert1.show();
                    }
                    else if (Long.parseLong(tvDue.getText().toString().replace("-", "")) <= Long.parseLong(today.replace("-", ""))) {
                        AlertDialog.Builder alert2 = new AlertDialog.Builder(MainActivity.this);
                        alert2.setCancelable(false);
                        alert2.setTitle("Service Term Expired!");
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = dateFormat.parse(tvDue.getText().toString());
                            dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                            String dDate = dateFormat.format(date);
                            TextView tvName = viewHolder.itemView.findViewById(R.id.ClientNameText);
                            alert2.setMessage("Payment of " + tvName.getText().toString() + " was due on " + dDate + " and his/her service period" +
                                    " has finished. Do you wish to continue?");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        alert2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getBaseContext(), ChartsIndv.class);
                                TextView tvName = viewHolder.itemView.findViewById(R.id.ClientNameText);
                                intent.putExtra("NaMe", tvName.getText().toString());
                                TextView tvPhone = viewHolder.itemView.findViewById(R.id.ClPhTxt);
                                intent.putExtra("PhOnE",tvPhone.getText().toString());
                                TextView tvSpCs = viewHolder.itemView.findViewById(R.id.ClSpCsTxt);
                                if (!tvSpCs.getText().toString().equalsIgnoreCase("")) {
                                    intent.putExtra("SpCaSe", tvSpCs.getText());
                                }
                                startActivity(intent);
                                recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                            }
                        });
                        alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        });
                        alert2.setNeutralButton("Remind", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cursor cursor;
                                try {
                                    String Reminder = "REMINDER: Your payment is pending. So please complete it by tomorrow to continue your services.";
                                    cursor = MyDb.TblBoot.getBootPayRmd();
                                    if(cursor != null){
                                        cursor.moveToPosition(-1);
                                        while (cursor.moveToNext()){
                                            Reminder = cursor.getString(cursor.getColumnIndex(USRPAY));
                                        }
                                    }
                                    Intent intent = new Intent("android.intent.action.MAIN");
                                    intent.setAction(Intent.ACTION_SEND);
                                    intent.setType("text/*");
                                    intent.setPackage("com.whatsapp");
                                    intent.putExtra(Intent.EXTRA_TEXT, Reminder);
                                    TextView tvPhone = viewHolder.itemView.findViewById(R.id.ClPhTxt);
                                    intent.putExtra("jid", tvPhone.getText().toString().replace("+","")+"@s.whatsapp.net");
                                    startActivity(intent);
                                    recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "WhatsApp is not Installed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        alert2.show();
                    }
                    else {
                        Intent intent = new Intent(getBaseContext(), ChartsIndv.class);
                        TextView tvName = viewHolder.itemView.findViewById(R.id.ClientNameText);
                        intent.putExtra("NaMe", tvName.getText().toString());
                        TextView tvPhone = viewHolder.itemView.findViewById(R.id.ClPhTxt);
                        intent.putExtra("PhOnE",tvPhone.getText().toString());
                        TextView tvSpCs = viewHolder.itemView.findViewById(R.id.ClSpCsTxt);
                        if (!tvSpCs.getText().toString().equalsIgnoreCase("")) {
                            intent.putExtra("SpCaSe", tvSpCs.getText());
                        }
                        startActivity(intent);
                        recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                }

                else
                    {
                        Intent intent = new Intent(getBaseContext(), WeightHistory.class);
                        TextView tvName = viewHolder.itemView.findViewById(R.id.ClientNameText);
                        intent.putExtra("WeightName", tvName.getText().toString());
                        startActivity(intent);
                        recyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View v = viewHolder.itemView;
                    Bitmap icon;
                    Paint paint = new Paint();

                    if (dX > 0) {
                        paint.setColor(getResources().getColor(R.color.chartsOrange, null));
                        c.drawRect((float) v.getLeft(), (float) v.getTop(), dX, (float) v.getBottom(), paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_swipe_chart);
                        c.drawBitmap(icon, (float) v.getLeft() + 80, (float) v.getTop() + 40, paint);
                    } else {
                        paint.setColor(getResources().getColor(R.color.darkBlue, null));
                        c.drawRect((float) v.getRight() + dX, (float) v.getTop(), (float) v.getRight(), (float) v.getBottom(), paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_swipe_weightrecord);
                        c.drawBitmap(icon, (float) v.getRight() - 180, (float) v.getTop() + 55, paint);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);

        try{
            toolbar.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            toolbar.setTitle("");
                            toolbar.setBackgroundColor(Color.WHITE);
                            TextView tvAct = rv.getFocusedChild().findViewById(R.id.ClActTxt);
                            if (tvAct.getText().toString().equalsIgnoreCase("Active")) {
                                toolbar.setSubtitle("TURN INACTIVE");
                                toolbar.setSubtitleTextColor(Color.RED);
                            } else if (tvAct.getText().toString().equalsIgnoreCase("Inactive")) {
                                toolbar.setSubtitle("TURN ACTIVE");
                                toolbar.setSubtitleTextColor(getResources().getColor(R.color.lightGreen,null));
                            }
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            toolbar.setSubtitleTextColor(Color.DKGRAY);
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            tvAct = rv.getFocusedChild().findViewById(R.id.ClActTxt);
                            if (tvAct.getText().toString().equalsIgnoreCase("Active"))
                                toolbar.setSubtitleTextColor(Color.RED);
                            else if (tvAct.getText().toString().equalsIgnoreCase("Inactive"))
                                toolbar.setSubtitleTextColor(getResources().getColor(R.color.lightGreen,null));
                            break;
                        case DragEvent.ACTION_DROP:
                            tvAct = rv.getFocusedChild().findViewById(R.id.ClActTxt);
                            TextView id = rv.getFocusedChild().findViewById(R.id.ClIdTxt);
                            TextView tvName = rv.getFocusedChild().findViewById(R.id.ClientNameText);
                            if (tvAct.getText().toString().equalsIgnoreCase("Active")) {
                                int pos = rv.getChildAdapterPosition(rv.getFocusedChild());
                                recClNames.remove(pos);
                                recyclerAdapter.notifyItemRemoved(pos);
                                recyclerAdapter.notifyItemRangeChanged(pos, recyclerAdapter.getItemCount());

                                MyDb.TblClients.TurnActInct(id.getText().toString(), "Inactive");
                                retrieveAct();
                                Toast.makeText(getBaseContext(),tvName.getText().toString()+" turned Inactive",Toast.LENGTH_SHORT).show();
                            }
                            else if (tvAct.getText().toString().equalsIgnoreCase("Inactive")) {
                                int pos = rv.getChildAdapterPosition(rv.getFocusedChild());
                                recClNames.remove(pos);
                                recyclerAdapter.notifyItemRemoved(pos);
                                recyclerAdapter.notifyItemRangeChanged(pos, recyclerAdapter.getItemCount());

                                MyDb.TblClients.TurnActInct(id.getText().toString(), "Active");
                                Toast.makeText(getBaseContext(),tvName.getText().toString()+" turned Active",Toast.LENGTH_SHORT).show();
                                retrieveInact();
                            }
                            break;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            toolbar.setTitle("Dietrix");
                            toolbar.setSubtitle("");
                            toolbar.setBackgroundColor(getResources().getColor(R.color.lightGreen, null));
                            break;
                    }
                    return true;
                }
            });
        }catch (Exception e){e.printStackTrace();}

        //Background broadcast-receiver to add data to INC_ACT table:
        Alarm();
    }

    private void Alarm()
    {
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,123,
                new Intent(this,Receiver.class), PendingIntent.FLAG_CANCEL_CURRENT);
        if (alarmManager != null)
        {
            Calendar c = Calendar.getInstance();
            c.set(Calendar.DAY_OF_MONTH,c.getActualMaximum(Calendar.DAY_OF_MONTH));
            c.set(Calendar.HOUR_OF_DAY,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,50);
            c.set(Calendar.MILLISECOND,0);
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),pendingIntent);
        }
    }

    public  Double getMonthlyIncome()
    {
        double sum = 0, value;
        Cursor cursor = MyDb.TblClients.getFeesData();
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

    private void checkForIncData()
    {
        Cursor c = MyDb.Tbl_Income_Act.getIncomeActData();
        Calendar cal = Calendar.getInstance(), cal2 = Calendar.getInstance();
        int currMonth = cal.get(Calendar.MONTH);

        if(c != null && c.getCount() > 0)
        {
            c.moveToLast();
            Date date = new Date();
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(c.getString(c.getColumnIndex(INCPREMNTH)));
            }
            catch (ParseException e) { e.printStackTrace(); }

            cal2.setTime(date);
            int  preMonth = cal2.get(Calendar.MONTH);

            if( (preMonth != (currMonth-1)) && (preMonth != currMonth))
            {
                cal.set(Calendar.MONTH,(currMonth-1));
                cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));

                long inserted = MyDb.Tbl_Income_Act.insertIncData(String.valueOf(getMonthlyIncome()),
                        String.valueOf(recyclerAdapter.getItemCount()), new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));

                if(inserted >0)
                {
                    NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"serviceChannel",
                                NotificationManager.IMPORTANCE_DEFAULT);

                        if (notificationManager != null) {
                            notificationManager.createNotificationChannel(notificationChannel);
                        }
                    }

                    NotificationCompat.Builder notification2 = new NotificationCompat.Builder(this,CHANNEL_ID)
                            .setContentTitle("Financial Report")
                            .setContentText("New Financial Report has been created.")
                            .setSmallIcon(R.drawable.ic_notif_finance);
                    if (notificationManager != null) {
                        notificationManager.notify(2,notification2.build());
                    }
                }
            }
            c.close();
        }
        else if(c != null && c.getCount() == 0)
        {
            cal.set(Calendar.MONTH,(currMonth-1));
            cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            if(currMonth == Calendar.JANUARY)
            {
                cal.set(Calendar.YEAR, Calendar.YEAR -1);
            }
            MyDb.Tbl_Income_Act.insertIncData("0.0", "0",
                    new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));

            c.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.SearchMI);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<RecClNames> FilterClList = new ArrayList<>();
                for(RecClNames recClNames1 : recClNames  )
                {
                    String name = recClNames1.getName().toLowerCase();
                    if(name.contains(newText))
                    {FilterClList.add(recClNames1);}
                }
                recyclerAdapter.setFilter(FilterClList);
                recyclerAdapter.notifyDataSetChanged();
                rv.setAdapter(recyclerAdapter);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.AllClientsMI) {
            retrieveAll();
            return true; }
        if (id == R.id.ActiveClientsMI) {
            retrieveAct();
            if(recyclerAdapter.getItemCount()!=0) {
                int actCount = recyclerAdapter.getItemCount();
                Toast.makeText(this,actCount+" Active clients ",Toast.LENGTH_SHORT).show();}
            else{rv.setAdapter(recyclerAdapter);
                Toast.makeText(this,"No Client is Currently Active",Toast.LENGTH_SHORT).show();}
            return true;
        }
        if (id == R.id.InactiveClientsMI) {
            retrieveInact();
            return true;
        }
        if(id == R.id.SettingsMI) {
           Intent intent = new Intent(this,Settings.class);
           startActivity(intent);
           return true;
        }
        if(id == R.id.PayDueMI){
            Intent intent = new Intent(this, Payments.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.RefreshMI){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        if(id == R.id.WeightMI)
        {
            Intent intent = new Intent(this, Weights.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void retrieveAct()
    {   recClNames.clear();
        Cursor cursor = MyDb.TblClients.getActClients("Active");
        if (cursor != null) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext())
            {
                try
                {
                String ClId = cursor.getString(cursor.getColumnIndex(CLID));
                String ClName = cursor.getString(cursor.getColumnIndex(CLNM));
                String ClPh = cursor.getString(cursor.getColumnIndex(CLMNO));
                String ClAct = cursor.getString(cursor.getColumnIndex(CLACT));
                String ClSpCs = cursor.getString(cursor.getColumnIndex(CLSPCS));
                String ClDue = cursor.getString(cursor.getColumnIndex(CLDUE));
                RecClNames rec = new RecClNames(ClId,ClName,ClPh,ClSpCs,ClDue,ClAct);
                recClNames.add(rec);
                rv.setAdapter(recyclerAdapter);
                }
                catch (Exception e) {e.printStackTrace();}
            }

            getAplhabeticalView();

            if(recyclerAdapter.getItemCount() == 0)
            { noCltMsg.setVisibility(View.VISIBLE); }
            else{ noCltMsg.setVisibility(View.GONE); }
        }
    }


    public void retrieveInact()
    {   recClNames.clear();
        Cursor cursor = MyDb.TblClients.getInActClients("Inactive");
        if (cursor != null) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext())
            {
                try
                {
                String ClId = cursor.getString(cursor.getColumnIndex(CLID));
                String ClName = cursor.getString(cursor.getColumnIndex(CLNM));
                String ClPh = cursor.getString(cursor.getColumnIndex(CLMNO));
                String ClAct = cursor.getString(cursor.getColumnIndex(CLACT));
                String ClSpCs = cursor.getString(cursor.getColumnIndex(CLSPCS));
                String ClDue = cursor.getString(cursor.getColumnIndex(CLDUE));
                RecClNames rec = new RecClNames(ClId,ClName,ClPh,ClSpCs,ClDue,ClAct);
                recClNames.add(rec);
                rv.setAdapter(recyclerAdapter);
                }
                catch (Exception e) {e.printStackTrace();}
            }

            getAplhabeticalView();

            if(recyclerAdapter.getItemCount() != 0)
            {
                noCltMsg.setVisibility(View.GONE);
                int InactCount = recyclerAdapter.getItemCount();
                Toast.makeText(this,InactCount+" Inactive clients ",Toast.LENGTH_SHORT).show();
            }
            else
                {
                noCltMsg.setVisibility(View.GONE);
                rv.setAdapter(recyclerAdapter);
                Toast.makeText(this,"No Client is Currently Inactive",Toast.LENGTH_SHORT).show();
                }
        }
    }

    public void retrieveAll()
    {   recClNames.clear();
        Cursor cursor = MyDb.TblClients.getAllClients();
        if (cursor != null) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext())
            {
                try
                {
                String ClId = cursor.getString(cursor.getColumnIndex(CLID));
                String ClName = cursor.getString(cursor.getColumnIndex(CLNM));
                String ClPh = cursor.getString(cursor.getColumnIndex(CLMNO));
                String ClAct = cursor.getString(cursor.getColumnIndex(CLACT));
                String ClSpCs = cursor.getString(cursor.getColumnIndex(CLSPCS));
                String ClDue = cursor.getString(cursor.getColumnIndex(CLDUE));
                RecClNames rec = new RecClNames(ClId,ClName,ClPh,ClSpCs,ClDue,ClAct);
                recClNames.add(rec);
                rv.setAdapter(recyclerAdapter);
                }
                catch (Exception e) {e.printStackTrace();}
            }

            getAplhabeticalView();

            if(recyclerAdapter.getItemCount() != 0)
            {
                noCltMsg.setVisibility(View.GONE);
                int TotalCount = recyclerAdapter.getItemCount();
                Toast.makeText(this,TotalCount+" Total clients ",Toast.LENGTH_SHORT).show();
            }
            else
                {
                noCltMsg.setVisibility(View.GONE);
                rv.setAdapter(recyclerAdapter);
                Toast.makeText(this,"No Client Available",Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void getAplhabeticalView()
    {
        if(Objects.requireNonNull(settingPref.getString("ViewType", "")).equalsIgnoreCase("Alphabetically"))
        {
            Collections.sort(recClNames, new Comparator<RecClNames>() {
                @Override
                public int compare(RecClNames o1, RecClNames o2) {
                    int sortName = o1.getName().compareTo(o2.getName());
                    recyclerAdapter.notifyDataSetChanged();
                    return sortName;
                }
            });
        }
    }

    public void animateFab(){
        if(isFabOpen){
           fab.startAnimation(rotate_backward);
           fabChart.startAnimation(fab_close);
           fabAddClt.startAnimation(fab_close);
           fabChart.setClickable(false);
           fabAddClt.setClickable(false);
           isFabOpen = false;
        }
        else {
            fab.startAnimation(rotate_forward);
            fabChart.startAnimation(fab_open);
            fabAddClt.startAnimation(fab_open);
            fabChart.setClickable(true);
            fabAddClt.setClickable(true);
            isFabOpen = true;
        }
    }
}

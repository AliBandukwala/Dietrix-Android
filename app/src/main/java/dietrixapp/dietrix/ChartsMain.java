package dietrixapp.dietrix;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static dietrixapp.dietrix.MyDb.TblBoot.USRTAG;
import static dietrixapp.dietrix.MyDb.TblClients.CLDUE;
import static dietrixapp.dietrix.MyDb.TblClients.CLMNO;
import static dietrixapp.dietrix.MyDb.TblClients.CLNM;
import static dietrixapp.dietrix.MyDb.TblClients.CLSPCS;

public class ChartsMain extends AppCompatActivity {

    TextView Warning, Greetings, ChartClDate;
    EditText Wake, Break, Lunch, Aft, Eve, Dine, Bed, Mid;
    Spinner ChartClNameSp;
    ImageButton ssBtn;
    ImageView chartImgView;
    List<String> ChartNamesList;
    ArrayAdapter<String> ListAdapter;
    Cursor cursor;
    String number, SpCs, due;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_main);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ChartClNameSp = findViewById(R.id.MChartNameSp);
        ChartClDate = findViewById(R.id.MChartDateET);
        chartImgView = findViewById(R.id.MChartImgView);
        Wake = findViewById(R.id.MWakeUpET);
        Break = findViewById(R.id.MBrkET);
        Lunch = findViewById(R.id.MLunchET);
        Aft = findViewById(R.id.MAftMET);
        Mid = findViewById(R.id.MMidET);
        Eve = findViewById(R.id.MEveMET);
        Dine = findViewById(R.id.MDinnerET);
        Bed = findViewById(R.id.MBedET);
        ssBtn = findViewById(R.id.MSSBtn);
        Warning = findViewById(R.id.MChartWrngTxt);
        Greetings = findViewById(R.id.MGreetings);

        try{
            cursor = MyDb.TblBoot.getBootTag();
            if(cursor!=null){
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()){
                Greetings.setText(cursor.getString(cursor.getColumnIndex(USRTAG)));
            }
                cursor.close();
            }else {
                Greetings.setText("");
                cursor.close();
            }
        }

        catch (Exception e){e.printStackTrace();}
        Greetings.setVisibility(View.INVISIBLE);

        ChartNamesList = new ArrayList<>();
        ChartNamesList.clear();

        cursor = MyDb.TblClients.getChartActNames("Active");
        if(cursor!=null){
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                try{
                    ChartNamesList.add(cursor.getString(cursor.getColumnIndex(CLNM)));
                }
                catch (Exception e){e.printStackTrace();}
            }
            cursor.close();
        }else {
            Toast.makeText(getBaseContext(),"No Client Available",Toast.LENGTH_SHORT).show();}

        ChartNamesList.add(0,"Select a Client");
        ListAdapter = new ArrayAdapter<>(this, R.layout.chart_name_spinner, ChartNamesList);
        ChartClNameSp.setAdapter(ListAdapter);

        ChartClNameSp.post(new Runnable() {
            @Override
            public void run() {
                ChartClNameSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Cursor cursorNum = MyDb.TblClients.getChartNum(ChartClNameSp.getSelectedItem().toString());
                        if(cursorNum!=null) {
                            cursorNum.moveToPosition(-1);
                            while(cursorNum.moveToNext()){
                                number = cursorNum.getString(cursorNum.getColumnIndex(CLMNO));
                                SpCs = cursorNum.getString(cursorNum.getColumnIndex(CLSPCS));
                                due = cursorNum.getString(cursorNum.getColumnIndex(CLDUE));
                            }
                            cursor.close();
                            if(SpCs != null && !SpCs.contentEquals("")){Warning.setText("Warning : "+SpCs);}
                            else {Warning.setText("");}

                            java.util.Calendar c = java.util.Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String today = df.format(c.getTime());
                            if(due != null && Long.parseLong(due.replace("-", "")) <=
                                    Long.parseLong(today.replace("-", "")))
                            {
                                AlertDialog.Builder alert = new AlertDialog.Builder(ChartsMain.this);
                                alert.setTitle("Warning: Service Term Expired!");
                                try {
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = dateFormat.parse(due);
                                    dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                    String dDate = dateFormat.format(date);
                                    alert.setMessage("Payment of " + ChartClNameSp.getSelectedItem().toString() +
                                            " is Pending since " + dDate + " and his/her service term has finished!");
                                } catch (Exception e) {e.printStackTrace();}
                                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                });
                                alert.show();
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
            }
        });

        Calendar  c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        if (c != null) {
            c.add(Calendar.DAY_OF_MONTH,1);
            String date = df.format(c.getTime());
            ChartClDate.setText(date);
        }

        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);

                ChartClDate.setText(new SimpleDateFormat("dd-MMM-yyyy").format(c.getTime()));
            }
        };

        ChartClDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(ChartsMain.this,dateSetListener,c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)+1).show();
            }
        });
    }

    public void SaveAndSEND(View view) {

        ssBtn.setVisibility(View.INVISIBLE);
        Warning.setVisibility(View.INVISIBLE);
        Greetings.setVisibility(View.VISIBLE);
        try{
            View decorView = getWindow().getDecorView();
            int ui = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(ui);

            ChartClDate.setCursorVisible(false);
            Wake.setCursorVisible(false);
            Break.setCursorVisible(false);
            Lunch.setCursorVisible(false);
            Aft.setCursorVisible(false);
            Mid.setCursorVisible(false);
            Eve.setCursorVisible(false);
            Dine.setCursorVisible(false);
            Bed.setCursorVisible(false);

            Bitmap bitmap = takeSsRootView(chartImgView);

            File path = new File(getFilesDir(),"ChartsMain");
            if(!path.exists()){path.mkdirs();}
            File img = new File(path, "ChartImg.jpeg");
            if(img.exists()){img.delete();}
            FileOutputStream stream = new FileOutputStream(img);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();

            Uri uri = FileProvider.getUriForFile(this,"dietrixapp.dietrix.fileprovider",img);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("image/*");
            intent.setPackage("com.whatsapp");
            intent.putExtra("jid", number.replace("+","") + "@s.whatsapp.net");
            intent.putExtra(Intent.EXTRA_STREAM,uri);
            startActivity(intent);

        }catch (Exception e){e.printStackTrace();}
    }

    public Bitmap takeSs(View v)
    {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);
        return  bitmap;
    }

    public Bitmap takeSsRootView(View view)
    {
        return takeSs(view.getRootView());
    }

    protected void onResume()
    {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int ui = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(ui);

        Warning.setVisibility(View.VISIBLE);
        ssBtn.setVisibility(View.VISIBLE);
        Greetings.setVisibility(View.INVISIBLE);
        Wake.setCursorVisible(true);
        Break.setCursorVisible(true);
        Lunch.setCursorVisible(true);
        Aft.setCursorVisible(true);
        Mid.setCursorVisible(true);
        Eve.setCursorVisible(true);
        Dine.setCursorVisible(true);
        Bed.setCursorVisible(true);
        ChartClDate.setCursorVisible(true);
    }
}
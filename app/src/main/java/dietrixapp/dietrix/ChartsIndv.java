package dietrixapp.dietrix;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;

import static dietrixapp.dietrix.MyDb.TblBoot.USRTAG;

public class ChartsIndv extends AppCompatActivity {

    TextView Warning, Greetings, ChartClDate;
    EditText ChartClNameSp, Wake, Break, Lunch, Aft, Eve, Dine, Bed, Mid;
    ImageButton ssBtn;
    ImageView chartImgView;
    String number = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts_indv);

        ChartClNameSp = findViewById(R.id.ChartNameSp);
        ChartClDate = findViewById(R.id.ChartDateET);
        chartImgView = findViewById(R.id.ChartImgView);
        Wake = findViewById(R.id.WakeUpET);
        Break = findViewById(R.id.BrkET);
        Lunch = findViewById(R.id.LunchET);
        Aft = findViewById(R.id.AftMET);
        Mid = findViewById(R.id.MidET);
        Eve = findViewById(R.id.EveMET);
        Dine = findViewById(R.id.DinnerET);
        Bed = findViewById(R.id.BedET);
        ssBtn = findViewById(R.id.SSBtn);
        Warning = findViewById(R.id.ChartWrngTxt);
        Greetings = findViewById(R.id.Greetings);

        Cursor cursor = MyDb.TblBoot.getBootTag();
        if(cursor!=null){
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()){
                Greetings.setText(cursor.getString(cursor.getColumnIndex(USRTAG)));
            }
            cursor.close();
        }else {Greetings.setText("");}
        Greetings.setVisibility(View.INVISIBLE);

        ChartClNameSp.setText(getIntent().getStringExtra("NaMe"));

        number = getIntent().getStringExtra("PhOnE");

        if(getIntent().getStringExtra("SpCaSe") != null){Warning.setText("Warning : " +
                getIntent().getStringExtra("SpCaSe"));}
        else{Warning.setText("");}

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
                new DatePickerDialog(ChartsIndv.this,dateSetListener,c.get(Calendar.YEAR),
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

            Wake.setCursorVisible(false);
            Break.setCursorVisible(false);
            Lunch.setCursorVisible(false);
            Aft.setCursorVisible(false);
            Mid.setCursorVisible(false);
            Eve.setCursorVisible(false);
            Dine.setCursorVisible(false);
            Bed.setCursorVisible(false);

            Bitmap bitmap = takeSsRootView(chartImgView);

            File path = new File(getFilesDir(),"ChartsIndv");
            if(!path.exists()){path.mkdirs();}
            File img = new File(path, "ChartImgs.jpeg");
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
    }
}
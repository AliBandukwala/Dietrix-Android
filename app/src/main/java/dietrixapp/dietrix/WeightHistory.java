package dietrixapp.dietrix;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static dietrixapp.dietrix.MyDb.TblWeights.CLCRBMI2;
import static dietrixapp.dietrix.MyDb.TblWeights.CLCWT2;
import static dietrixapp.dietrix.MyDb.TblWeights.CLNM2;
import static dietrixapp.dietrix.MyDb.TblWeights.CLWTDT2;

public class WeightHistory extends AppCompatActivity {

    RecyclerView recyclerWtHis;
    ArrayList<WeightHistoryInfo> weightHistoryInfo;
    WtHisRecyclerAdapter wtHisRecyclerAdapter;
    MyDb myDb;
    Date date;
    SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM, yyyy"),
            formatter2 = new SimpleDateFormat("dd-MMM"),
            formatter3 = new SimpleDateFormat("yyyy-MM-dd"),
            formatter4 = new SimpleDateFormat("dd MMM, yyyy");
    TextView WeightName;
    GraphView graphView;
    LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_history);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Weight Records </font>",0));
        }
        recyclerWtHis = findViewById(R.id.recyclerWeightHistory);
        recyclerWtHis.setLayoutManager(new LinearLayoutManager(this));
        weightHistoryInfo = new ArrayList<>();
        myDb = new MyDb(this);
        wtHisRecyclerAdapter = new WtHisRecyclerAdapter(this,weightHistoryInfo);
        recyclerWtHis.setAdapter(wtHisRecyclerAdapter);
        WeightName = findViewById(R.id.WeightHisName);
        graphView = findViewById(R.id.WeightGraph);
        series = new LineGraphSeries<>();

        WeightName.setText(getIntent().getStringExtra("WeightName"));

        retrieveWeightHistory();

        Cursor cursor = MyDb.TblWeights.getWeightData(getIntent().getStringExtra("WeightName"));
        cursor.moveToPosition(-1);
        while (cursor.moveToNext())
        {
            date = new Date();
            try
            {
                date = formatter1.parse(cursor.getString(cursor.getColumnIndex(CLWTDT2)));
                String str = formatter3.format(date);
                date  = formatter3.parse(str);

                series.appendData(new DataPoint(date,Double.parseDouble(cursor.getString(cursor.getColumnIndex(CLCWT2)))),
                        true,200);
            }
            catch (ParseException e) { e.printStackTrace(); }
        }

        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                  return formatter2.format(new Date((long)value));
                }
                else{ return (String.valueOf(value)+" kg"); }
            }
        });

        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                String st = formatter4.format(new Date((long)dataPoint.getX()));
                Toast.makeText(getBaseContext(),dataPoint.getY()+" kg on "+st,Toast.LENGTH_LONG).show();
            }
        });

        series.setDrawDataPoints(true);
        series.setThickness(8);
        series.setDataPointsRadius(10);
        series.setColor(Color.WHITE);
        graphView.addSeries(series);
        graphView.getGridLabelRenderer().setTextSize(34);
        graphView.getGridLabelRenderer().setGridColor(Color.DKGRAY);
        graphView.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        graphView.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
        graphView.getGridLabelRenderer().setNumVerticalLabels(5);
        graphView.getViewport().setBackgroundColor(getResources().getColor(R.color.lightBlue,null));
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setScalableY(true);
    }

    public void retrieveWeightHistory()
    {
        weightHistoryInfo.clear();
        Cursor cursor = MyDb.TblWeights.getWeightData(getIntent().getStringExtra("WeightName"));
        if(cursor != null){
            cursor.moveToLast();
            while(cursor.getPosition()!=-1){
                String crtWtName = cursor.getString(cursor.getColumnIndex(CLNM2));
                String crntWt = cursor.getString(cursor.getColumnIndex(CLCWT2));
                String crWtDt = cursor.getString(cursor.getColumnIndex(CLWTDT2));
                String crWtBMI = cursor.getString(cursor.getColumnIndex(CLCRBMI2));
                WeightHistoryInfo wtHis = new WeightHistoryInfo(crtWtName,crntWt,crWtDt,crWtBMI);
                weightHistoryInfo.add(wtHis);
                recyclerWtHis.setAdapter(wtHisRecyclerAdapter);
                cursor.moveToPrevious();
            }
            cursor.close();
        }
    }

}


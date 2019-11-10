package dietrixapp.dietrix;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static dietrixapp.dietrix.MyDb.Tbl_Income_Act.INCACTCNT;
import static dietrixapp.dietrix.MyDb.Tbl_Income_Act.INCAMT;
import static dietrixapp.dietrix.MyDb.Tbl_Income_Act.INCPREMNTH;

public class IncomeHistory extends AppCompatActivity {

    ArrayList<IncomeHisRecInfo> arrayList;
    RecyclerView incRecyclerView;
    IncomeHisRecAdapter adapter;
    TextView noDataMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_history);
        arrayList = new ArrayList<>();
        incRecyclerView = findViewById(R.id.IncomeHistoryRecycler);
        incRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IncomeHisRecAdapter(this,arrayList);
        incRecyclerView.setAdapter(adapter);
        noDataMsg = findViewById(R.id.IncHisNoDataMgsTxt);

        getIncHisData();
    }

    private void getIncHisData()
    {
        arrayList.clear();
        Cursor cursor = MyDb.Tbl_Income_Act.getIncomeActData();
        if(cursor != null && cursor.getCount() != 0)
        {
            cursor.moveToLast();
            while (cursor.getPosition() != -1)
            {
                IncomeHisRecInfo incomeHisRecInfo = new IncomeHisRecInfo(cursor.getString(cursor.getColumnIndex(INCAMT)),
                        cursor.getString(cursor.getColumnIndex(INCPREMNTH)),cursor.getString(cursor.getColumnIndex(INCACTCNT)));
                arrayList.add(incomeHisRecInfo);
                cursor.moveToPrevious();
            }
            cursor.close();
        }
        else
            {
                noDataMsg.setVisibility(View.VISIBLE);
            }
    }
}

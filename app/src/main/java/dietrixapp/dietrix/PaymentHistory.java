package dietrixapp.dietrix;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import static dietrixapp.dietrix.MyDb.TblPayHistory.PAYHISTAMT;
import static dietrixapp.dietrix.MyDb.TblPayHistory.PAYHISTDUE;
import static dietrixapp.dietrix.MyDb.TblPayHistory.PAYHISTMONTH;

public class PaymentHistory extends AppCompatActivity {
    TextView NoDataMsg;
    RecyclerView payHistRecView;
    PaymentHistRecAdapter adapter;
    ArrayList<PaymentHistRecInfo> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Payment Records </font>",0));
        }
        payHistRecView = findViewById(R.id.PayHistRecyclerView);
        adapter = new PaymentHistRecAdapter(this,arrayList);
        payHistRecView.setLayoutManager(new LinearLayoutManager(this));
        payHistRecView.setAdapter(adapter);
        NoDataMsg = findViewById(R.id.PayHistNoDataMsgTxt);

        getPayHistData(getIntent().getStringExtra("PayID"));
    }

    private void getPayHistData(String id)
    {
        Cursor cursor = MyDb.TblPayHistory.getPayHistData(id);
        if(cursor != null && cursor.getCount() > 0)
        {
            cursor.moveToLast();
            while (cursor.getPosition() != -1)
            {
                PaymentHistRecInfo paymentHistRecInfo = new PaymentHistRecInfo(cursor.getString(cursor.getColumnIndex(PAYHISTMONTH))
                        ,cursor.getString(cursor.getColumnIndex(PAYHISTAMT)),cursor.getString(cursor.getColumnIndex(PAYHISTDUE)));

                arrayList.add(paymentHistRecInfo);

                cursor.moveToPrevious();
            }
        }
        else
            NoDataMsg.setVisibility(View.VISIBLE);
    }
}

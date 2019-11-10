package dietrixapp.dietrix;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import static dietrixapp.dietrix.MyDb.TblClients.CLDUE;
import static dietrixapp.dietrix.MyDb.TblClients.CLDUR;
import static dietrixapp.dietrix.MyDb.TblClients.CLFEE;
import static dietrixapp.dietrix.MyDb.TblClients.CLID;
import static dietrixapp.dietrix.MyDb.TblClients.CLMNO;
import static dietrixapp.dietrix.MyDb.TblClients.CLNM;
import static dietrixapp.dietrix.MyDb.Tbl_Income_Act.INCAMT;
import static dietrixapp.dietrix.MyDb.Tbl_Income_Act.INCPREMNTH;

public class Payments extends AppCompatActivity {

    TextView payDueCount,activeCount,monthlyIncome,revenueViewDts,revenueMonth, incomePercent;
    GraphView incomeGraph;
    LineGraphSeries<DataPoint> lineGraphSeries;
    ArrayList<PayRecInfo> payRecInfoArrayList;
    RecyclerView rc;
    PayRecyclerAdapter payRecyclerAdapter;
    MyDb myDb;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        //Declarations:
        myDb = new MyDb(this);
        rc = findViewById(R.id.recyclerView2);
        rc.setLayoutManager(new LinearLayoutManager(this));
        payRecInfoArrayList = new ArrayList<>();
        payRecyclerAdapter = new PayRecyclerAdapter(this, payRecInfoArrayList);
        rc.setAdapter(payRecyclerAdapter);
        payDueCount = findViewById(R.id.PayDueCountTxt);
        monthlyIncome = findViewById(R.id.MonthlyIncomeTxt);
        revenueMonth = findViewById(R.id.RevenueMonthTxt);
        activeCount = findViewById(R.id.ActiveCountTxt);
        revenueViewDts = findViewById(R.id.RevenueViewDetailsTxt);
        incomePercent = findViewById(R.id.IncomePercentTxt);
        incomeGraph = findViewById(R.id.IncomeGraph);
        lineGraphSeries = new LineGraphSeries<>();

        // Active-count CardView methods:
        cursor = MyDb.TblClients.getActClients("Active");
        if(cursor != null){
            activeCount.setText(String.valueOf(cursor.getCount()));
            cursor.close();
        }

        //Income-Graph CardView methods:
        cursor = MyDb.Tbl_Income_Act.getIncomeActData();
        if(cursor != null)
        {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                Date dt = new Date();
                try {
                    dt = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex(INCPREMNTH)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                lineGraphSeries.appendData(new DataPoint(dt, Double.parseDouble(cursor.getString(cursor.getColumnIndex(INCAMT)))),
                        true, 100);
            }
            cursor.close();
        }

        cursor = MyDb.Tbl_Income_Act.getIncomeActData();
        if(cursor != null && cursor.getCount() > 1)
        {
            cursor.moveToLast();
            Double lastInc = Double.parseDouble(cursor.getString(cursor.getColumnIndex(INCAMT)));
            cursor.moveToPrevious();
            Double preInc = Double.parseDouble(cursor.getString(cursor.getColumnIndex(INCAMT)));
            Date  date = new Date();
            try{
                date = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex(INCPREMNTH)));
            } catch (Exception e){ e.printStackTrace(); }
            String preMonth = new SimpleDateFormat("MMM").format(date);

            DecimalFormat dec = new DecimalFormat(".#");

            double percent = 0.0;
            if(preInc > 0)
                percent = Double.parseDouble(dec.format((((lastInc - preInc)/preInc)*100)));
            else
                percent = lastInc;

            if(percent >= 0)
                incomePercent.setText("+"+percent+"% vs "+preMonth);
            else { incomePercent.setText(percent+"% vs "+preMonth); }

            cursor.close();
        }
        else if(cursor != null && cursor.getCount() <= 1) { incomePercent.setText(""); }

        incomeGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return new SimpleDateFormat("MMM").format(new Date((long)value));
                }
                else { return super.formatLabel(value, false); }
            }
        });

        lineGraphSeries.setColor(Color.WHITE);
        lineGraphSeries.setThickness(8);
        incomeGraph.addSeries(lineGraphSeries);
        incomeGraph.getGridLabelRenderer().setGridColor(Color.argb(100,255,255,255));
        incomeGraph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
        incomeGraph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
        incomeGraph.getGridLabelRenderer().setHumanRounding(false,true);
        incomeGraph.getGridLabelRenderer().setTextSize(34);
        incomeGraph.getGridLabelRenderer().setNumHorizontalLabels(4);
        incomeGraph.getGridLabelRenderer().setNumVerticalLabels(5);
        incomeGraph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        incomeGraph.getViewport().setBackgroundColor(getResources().getColor(R.color.lightGreen,null));
        incomeGraph.getViewport().setMinY(0);
        incomeGraph.getViewport().setScalable(true);
        incomeGraph.getViewport().setScalableY(true);

        revenueViewDts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), IncomeHistory.class);
                startActivity(intent);
            }
        });


        //Revenue CardView methods:
        cursor = MyDb.Tbl_Income_Act.getIncomeActData();
        if (cursor != null && cursor.getCount() != 0)
        {
            cursor.moveToLast();
            monthlyIncome.setText(cursor.getString(cursor.getColumnIndex(INCAMT)));
            Date Dt = null;
            try { Dt = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(cursor.getColumnIndex(INCPREMNTH))); }
            catch (ParseException e) { e.printStackTrace(); }
            revenueMonth.setText(new SimpleDateFormat("MMM yyyy").format(Dt));
        }
        else { monthlyIncome.setText("No Data");
                revenueMonth.setText("--"); }

        // Payment Due methods:
        retrievePayDueCl();

        int num = payRecyclerAdapter.getItemCount();
        payDueCount.setText(num+" clients");

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT ||  direction == ItemTouchHelper.LEFT){
                    rc.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(Payments.this);
                    alert1.setTitle("Are you sure?");
                    final TextView tvName = viewHolder.itemView.findViewById(R.id.PayNameTxt);
                    final String dt = new SimpleDateFormat("dd MMM, yyyy").format(Calendar.getInstance().getTime());
                    alert1.setMessage("Has the payment been done on "+dt+"? You can change the Date before" +
                            " proceeding, but not after saving the data. Also, "+tvName.getText().toString()+" will removed from this " +
                            "list until his/her next due date.");
                    alert1.setCancelable(false);
                    alert1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                Calendar c;
                                SimpleDateFormat df;
                                TextView tvDue = viewHolder.itemView.findViewById(R.id.PayDueDateTxt);
                                TextView tvDur = viewHolder.itemView.findViewById(R.id.PayDuration);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    df = new SimpleDateFormat("dd MMM, yyyy");
                                    Date date = df.parse(tvDue.getText().toString());
                                    c = Calendar.getInstance();
                                    c.setTime(date);

                                    if(tvDur.getText().toString().equalsIgnoreCase("1 month"))
                                        c.add(Calendar.MONTH,1);
                                    else if(tvDur.getText().toString().equalsIgnoreCase("3 month"))
                                        c.add(Calendar.MONTH,3);
                                    else if(tvDur.getText().toString().equalsIgnoreCase("6 month"))
                                        c.add(Calendar.MONTH,6);
                                    else if(tvDur.getText().toString().equalsIgnoreCase("1 year"))
                                        c.add(Calendar.YEAR,1);

                                    df = new SimpleDateFormat("yyyy-MM-dd");
                                    String newDueDate = df.format(c.getTime());

                                    TextView tvID = viewHolder.itemView.findViewById(R.id.PayId);
                                    TextView fee = viewHolder.itemView.findViewById(R.id.PayFeeTxt);
                                    boolean isPayed = MyDb.TblClients.UpdatePayed(tvID.getText().toString(),newDueDate);
                                    long inserted = MyDb.TblPayHistory.insertPayHistData(tvID.getText().toString(),dt,
                                            fee.getText().toString(),tvDue.getText().toString());

                                    if(isPayed && (inserted > 0) ) {
                                        payRecInfoArrayList.remove(viewHolder.getAdapterPosition());
                                        payRecyclerAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                        payRecyclerAdapter.notifyItemRangeChanged(viewHolder.getAdapterPosition()
                                                , payRecyclerAdapter.getItemCount());
                                        Toast.makeText(getBaseContext(),
                                                tvName.getText().toString()+"'s Account Updated Successfully",
                                                Toast.LENGTH_SHORT).show();
                                        payDueCount.setText(payRecyclerAdapter.getItemCount()+" clients");
                                    }
                                    else
                                        Toast.makeText(getBaseContext(),"Unable to Update Client's Account", Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e) {e.printStackTrace();}
                        }
                    });

                    alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            payRecyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    alert1.setNeutralButton("Change Date", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final Dialog dialog = new Dialog(Payments.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_payhist_date);
                            dialog.setCancelable(false);
                            final TextView dateText = dialog.findViewById(R.id.PayHistDialogDateTxt);

                            Button cancel = dialog.findViewById(R.id.PayHistDialogDateCancelBtn),
                                    Ok = dialog.findViewById(R.id.PayHistDialogDateOKtBtn);

                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    payRecyclerAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                    dialog.dismiss();
                                }
                            });
                            Ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    try{
                                        Calendar c;
                                        SimpleDateFormat df;
                                        TextView tvDue = viewHolder.itemView.findViewById(R.id.PayDueDateTxt);
                                        TextView tvDur = viewHolder.itemView.findViewById(R.id.PayDuration);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                            df = new SimpleDateFormat("dd MMM, yyyy");
                                            Date date = df.parse(tvDue.getText().toString());
                                            c = Calendar.getInstance();
                                            c.setTime(date);

                                            if(tvDur.getText().toString().equalsIgnoreCase("1 month"))
                                                c.add(Calendar.MONTH,1);
                                            else if(tvDur.getText().toString().equalsIgnoreCase("3 month"))
                                                c.add(Calendar.MONTH,3);
                                            else if(tvDur.getText().toString().equalsIgnoreCase("6 month"))
                                                c.add(Calendar.MONTH,6);
                                            else if(tvDur.getText().toString().equalsIgnoreCase("1 year"))
                                                c.add(Calendar.YEAR,1);

                                            df = new SimpleDateFormat("yyyy-MM-dd");
                                            String newDueDate = df.format(c.getTime());

                                            TextView tvID = viewHolder.itemView.findViewById(R.id.PayId);
                                            TextView fee = viewHolder.itemView.findViewById(R.id.PayFeeTxt);

                                            boolean isPayed = MyDb.TblClients.UpdatePayed(tvID.getText().toString(),newDueDate);

                                            long inserted = MyDb.TblPayHistory.insertPayHistData(tvID.getText().toString(),
                                                    dateText.getText().toString(), fee.getText().toString(),tvDue.getText().toString());

                                            if(isPayed && (inserted > 0) ) {
                                                dialog.dismiss();
                                                payRecInfoArrayList.remove(viewHolder.getAdapterPosition());
                                                payRecyclerAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                                                payRecyclerAdapter.notifyItemRangeChanged(viewHolder.getAdapterPosition()
                                                        , payRecyclerAdapter.getItemCount());
                                                Toast.makeText(getBaseContext(),
                                                        tvName.getText().toString()+"'s Account Updated Successfully",
                                                        Toast.LENGTH_SHORT).show();
                                                payDueCount.setText(payRecyclerAdapter.getItemCount()+" clients");
                                            }
                                            else
                                                Toast.makeText(getBaseContext(),"Unable to Update Client's Account", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e) {e.printStackTrace();}
                                }
                            });

                            dateText.setText(new SimpleDateFormat("dd MMM, yyyy").format(Calendar.getInstance().getTime()) );
                            final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    Calendar c = Calendar.getInstance();
                                    c.set(Calendar.YEAR,year);
                                    c.set(Calendar.MONTH,month);
                                    c.set(Calendar.DAY_OF_MONTH,day);

                                    dateText.setText(new SimpleDateFormat("dd MMM, yyyy").format(c.getTime()));
                                }
                            };

                            dateText.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Calendar c = Calendar.getInstance();
                                    new DatePickerDialog(Payments.this,dateSetListener,c.get(Calendar.YEAR),
                                            c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
                                }
                            });

                            dialog.show();
                        }
                    });

                    alert1.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View v = viewHolder.itemView;
                    Bitmap icon;
                    Paint paint = new Paint();

                    if (dX > 0) {
                        paint.setColor(getResources().getColor(R.color.lightRed, null));
                        c.drawRect((float) v.getLeft(), (float) v.getTop(), dX, (float) v.getBottom(), paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_swipe_pay_remove);
                        c.drawBitmap(icon, (float) v.getLeft() + 80, (float) v.getTop() + 65, paint);
                    }
                    else {
                        paint.setColor(getResources().getColor(R.color.lightRed, null));
                        c.drawRect((float) v.getRight() + dX, (float) v.getTop(), (float) v.getRight(), (float) v.getBottom(), paint);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_swipe_pay_remove);
                        c.drawBitmap(icon, (float) v.getRight() - 180, (float) v.getTop() + 65, paint);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rc);
    }

    protected void onResume() {
        super.onResume();
        retrievePayDueCl();
    }

    public void retrievePayDueCl() {
        payRecInfoArrayList.clear();
        cursor = MyDb.TblClients.getPayDueData("Active");
        if (cursor != null) {
            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                try {
                    String PayID = cursor.getString(cursor.getColumnIndex(CLID));
                    String PayName = cursor.getString(cursor.getColumnIndex(CLNM));
                    String PayDue = cursor.getString(cursor.getColumnIndex(CLDUE));
                    String PayFee = cursor.getString(cursor.getColumnIndex(CLFEE));
                    String PayPhone = cursor.getString(cursor.getColumnIndex(CLMNO));
                    String PayDur = cursor.getString(cursor.getColumnIndex(CLDUR));

                    PayRecInfo payRecInfo = new PayRecInfo(PayID, PayName, PayDue, PayFee,PayPhone,PayDur);
                    payRecInfoArrayList.add(payRecInfo);
                    rc.setAdapter(payRecyclerAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
           cursor.close();
        }
    }
}

package dietrixapp.dietrix;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

public class ClUpdate extends AppCompatActivity {

    EditText UpdName, UpdPhone, UpdHeight, UpdEmail, UpdAddress, UpdAge, UpdFee, UpdSpCase;
    TextView UpdId, UpdDate, UpdDue;
    Spinner UpdDurationList, UpdActive;
    MyDb myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cl_update);
        Toolbar toolbar3 = findViewById(R.id.toolbarupd);
        setSupportActionBar(toolbar3);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Edit Client </font>",0));
        }
        if(myDb==null)
            myDb = new MyDb(this);
        UpdName = findViewById(R.id.UpdNameET);
        UpdPhone = findViewById(R.id.UpdPhoneET);
        UpdActive = findViewById(R.id.UpdActSpET);
        UpdHeight = findViewById(R.id.UpdHeightET);
        UpdEmail = findViewById(R.id.UpdEmailET);
        UpdAddress = findViewById(R.id.UpdAddressET);
        UpdAge = findViewById(R.id.UpdAgeET);
        UpdFee = findViewById(R.id.UpdFeeET);
        UpdSpCase = findViewById(R.id.UpdSpCaseET);
        UpdDate = findViewById(R.id.UpdDateET);
        UpdDue = findViewById(R.id.UpdDueET);
        UpdDurationList = findViewById(R.id.UpdDurationSp);
        UpdId = findViewById(R.id.UpdIdTxt);

        UpdId.setText(getIntent().getStringExtra("id"));
        UpdName.setText(getIntent().getStringExtra("NAME"));
        String Act = getIntent().getStringExtra("ACTIVE");
        if(Act.equalsIgnoreCase("active")){UpdActive.setSelection(0);}
        else if(Act.equalsIgnoreCase("inactive")){UpdActive.setSelection(1);}
        UpdPhone.setText(getIntent().getStringExtra("PHONE"));
        UpdHeight.setText(getIntent().getStringExtra("HEIGHT"));
        UpdAge.setText(getIntent().getStringExtra("AGE"));
        UpdEmail.setText(getIntent().getStringExtra("EMAIL"));
        UpdAddress.setText(getIntent().getStringExtra("ADDRESS"));
        UpdFee.setText(getIntent().getStringExtra("FEE"));
        UpdSpCase.setText(getIntent().getStringExtra("SPCASE"));
        UpdDue.setText(getIntent().getStringExtra("DUE"));
        UpdDate.setText(getIntent().getStringExtra("JOINDATE"));
        String duration  = getIntent().getStringExtra("DURATION");
        if(duration.equalsIgnoreCase("1 month"))
            UpdDurationList.setSelection(0);
        else if(duration.equalsIgnoreCase("3 month"))
            UpdDurationList.setSelection(1);
        else if(duration.equalsIgnoreCase("6 month"))
            UpdDurationList.setSelection(2);
        else if(duration.equalsIgnoreCase("1 year"))
            UpdDurationList.setSelection(3);

        UpdDurationList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.performClick();
                UpdDurationList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getBaseContext(),"Please change the Due Date accordingly as well",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {}
                });
                return true;
            }
        });

        //***********************
        final DatePickerDialog.OnDateSetListener JnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);

                UpdDate.setText(new SimpleDateFormat("dd MMM, yyyy").format(c.getTime()));
            }
        };

        UpdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                Date dt = new Date();
                try {
                    dt = new SimpleDateFormat("dd MMM, yyyy").parse(UpdDate.getText().toString());
                } catch (ParseException e) { e.printStackTrace(); }
                c.setTime(dt);
                new DatePickerDialog(ClUpdate.this,JnDateSetListener,c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //************************
        final DatePickerDialog.OnDateSetListener DueDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR,year);
                c.set(Calendar.MONTH,month);
                c.set(Calendar.DAY_OF_MONTH,day);

                UpdDue.setText(new SimpleDateFormat("dd MMM, yyyy").format(c.getTime()));
            }
        };

        UpdDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                Date dt = new Date();
                try {
                    dt = new SimpleDateFormat("dd MMM, yyyy").parse(UpdDue.getText().toString());
                } catch (ParseException e) { e.printStackTrace(); }
                c.setTime(dt);
                new DatePickerDialog(ClUpdate.this,DueDateSetListener,c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.UpdateMI)
        {
            try{
                SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
                Date date = df.parse(UpdDue.getText().toString());
                df = new SimpleDateFormat("yyyy-MM-dd");
                String newDue = df.format(date);

                boolean isUpdated = MyDb.TblClients.UpdateData(UpdId.getText().toString(),UpdName.getText().toString(),
                        UpdPhone.getText().toString(),UpdHeight.getText().toString(), UpdAge.getText().toString(),
                        UpdEmail.getText().toString(), UpdAddress.getText().toString(), UpdFee.getText().toString(),
                        UpdDate.getText().toString(), newDue,UpdDurationList.getSelectedItem().toString(),
                        UpdActive.getSelectedItem().toString(), UpdSpCase.getText().toString());

                boolean isWtUpdated = MyDb.TblWeights.updateWtHisName(UpdName.getText().toString(),UpdPhone.getText().toString());

                if(isUpdated && isWtUpdated)
                {Toast.makeText(this,UpdName.getText().toString()+"'s Account Updated Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                else { Toast.makeText(this,"Unable to Update data",Toast.LENGTH_SHORT).show();}
            }
            catch (Exception e){e.printStackTrace();}
        }
       return true;
    }
}

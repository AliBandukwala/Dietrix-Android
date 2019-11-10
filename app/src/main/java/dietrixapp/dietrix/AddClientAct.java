package dietrixapp.dietrix;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.DecimalFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

import static dietrixapp.dietrix.MyDb.TblClients.CLNM;

public class AddClientAct extends AppCompatActivity {

    EditText ClName, ClPhone, ClHeight, ClJnWeight, ClEmail, ClAddress,ClAge, ClFee, ClSpCs, ClJnBMI;
    TextView ClDate, ClDue;
    Spinner ClDurationList;
    MyDb myDb=null;
    Calendar c;
    SimpleDateFormat df;
    Date dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clients);
        Toolbar toolbar2 = findViewById(R.id.toolbaradd);
        setSupportActionBar(toolbar2);

        if (myDb == null) myDb = new MyDb(this);
        ClName = findViewById(R.id.ClNameET);
        ClPhone = findViewById(R.id.ClPhoneET);
        ClHeight = findViewById(R.id.ClHeightET);
        ClJnWeight = findViewById(R.id.ClWeightET);
        ClEmail = findViewById(R.id.ClEmailET);
        ClAddress = findViewById(R.id.ClAddressET);
        ClAge = findViewById(R.id.ClAgeET);
        ClFee = findViewById(R.id.ClFeeET);
        ClDate = findViewById(R.id.ClDateET);
        ClDue = findViewById(R.id.ClDueET);
        ClSpCs = findViewById(R.id.ClSpCaseET);
        ClDurationList = findViewById(R.id.ClDurationSp);
        ClJnBMI = findViewById(R.id.ClBMIET);

        c = Calendar.getInstance();
        df = new SimpleDateFormat("dd MMM, yyyy");
        if (c != null) {
            ClDate.setText(df.format(c.getTime()));

            ClDurationList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    try {
                        dt = df.parse(ClDate.getText().toString());
                        c.setTime(dt);
                    } catch (ParseException e) {e.printStackTrace();}

                    if (ClDurationList.getSelectedItem().toString().equalsIgnoreCase("1 month"))
                        c.add(Calendar.MONTH, 1);
                    else if (ClDurationList.getSelectedItem().toString().equalsIgnoreCase("3 month"))
                        c.add(Calendar.MONTH, 3);
                    else if (ClDurationList.getSelectedItem().toString().equalsIgnoreCase("6 month"))
                        c.add(Calendar.MONTH, 6);
                    else if (ClDurationList.getSelectedItem().toString().equalsIgnoreCase("1 year"))
                        c.add(Calendar.YEAR, 1);

                    ClDue.setText(df.format(c.getTime()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });


            Uri uri = Uri.parse("content://contacts");
            Intent intent = new Intent(Intent.ACTION_PICK, uri);
            intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(intent, 1);

            final DatePickerDialog.OnDateSetListener JnDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,month);
                    c.set(Calendar.DAY_OF_MONTH,day);

                    ClDate.setText(new SimpleDateFormat("dd MMM, yyyy").format(c.getTime()));
                }
            };

            ClDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                    new DatePickerDialog(AddClientAct.this,JnDateSetListener,c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            final DatePickerDialog.OnDateSetListener DueDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR,year);
                    c.set(Calendar.MONTH,month);
                    c.set(Calendar.DAY_OF_MONTH,day);

                    ClDue.setText(new SimpleDateFormat("dd MMM, yyyy").format(c.getTime()));
                }
            };

            ClDue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        dt = new SimpleDateFormat("dd MMM, yyyy").parse(ClDue.getText().toString());
                    } catch (ParseException e) { e.printStackTrace(); }

                    Calendar c = Calendar.getInstance();
                    c.setTime(dt);
                    new DatePickerDialog(AddClientAct.this,DueDateSetListener,c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

                assert uri != null;
                Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();

                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    ClName.setText(name);
                    ClPhone.setText(number);

                    cursor.close();
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_ok_add, menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.AddOKBtn)
        {
            Cursor cursor = MyDb.TblClients.getAllClients();
            if(cursor != null && cursor.getCount() != 0)
            {
                cursor.moveToPosition(-1);
                while (cursor.moveToNext())
                {
                    if(ClName.getText().toString().equalsIgnoreCase(cursor.getString(cursor.getColumnIndex(CLNM))))
                    {
                        Toast.makeText(this,"This name already exists. Please give a different name.",Toast.LENGTH_LONG).show();
                        return false;
                    }
                }

            }
            if(ClName.getText().toString().matches("")){
                Toast.makeText(this,"Client's Name is not entered",Toast.LENGTH_SHORT).show();}
            else if(ClPhone.getText().toString().matches("")){
                Toast.makeText(this,"Client's Phone No. is not entered",Toast.LENGTH_SHORT).show();}
            else if(!ClPhone.getText().toString().startsWith("+")){
                Toast.makeText(this,"Put a proper Country Code with + for Client's Phone number",Toast.LENGTH_SHORT).show();}
            else if(ClJnWeight.getText().toString().matches("")){
                Toast.makeText(this,"Client's Weight is not entered",Toast.LENGTH_SHORT).show();}
            else if(ClFee.getText().toString().matches("")){
                Toast.makeText(this,"Client's Fee is not entered",Toast.LENGTH_SHORT).show();}
            else if(ClHeight.getText().toString().matches("")){
                Toast.makeText(this,"Client's Height is not entered",Toast.LENGTH_SHORT).show();}
            else if(ClHeight.getText().toString().substring(0,1).startsWith("0")){
                Toast.makeText(this,"Invalid initial '0' in Height(Feet)",Toast.LENGTH_LONG).show(); }
            else if(Integer.parseInt(ClHeight.getText().toString().substring(2)) > 11) {
                Toast.makeText(this,"Invalid number in height(inches).",Toast.LENGTH_LONG).show(); }
            else {
                double Weight = Double.parseDouble(ClJnWeight.getText().toString());
                String height1 = ClHeight.getText().toString().substring(0,1);
                String height2 = ClHeight.getText().toString().substring(2);
                double Height1 = Double.parseDouble(height1);
                double Height2 = Double.parseDouble(height2) * 0.083;
                double Height = Height1 + Height2;
                DecimalFormat decf = new DecimalFormat(".##");
                double bmi = (Weight/((Height*0.305)*(Height*0.305)));
                String BMI = String.valueOf(decf.format(bmi));
                ClJnBMI.setText(BMI);

                RecClNames recClNames = new RecClNames();
                recClNames.setActive("Active");
                recClNames.setName(ClName.getText().toString());
                recClNames.setPhone(ClPhone.getText().toString().trim().replace(" ",""));
                recClNames.setHeight(ClHeight.getText().toString());
                recClNames.setAge(ClAge.getText().toString());
                recClNames.setJnWeight(ClJnWeight.getText().toString());
                recClNames.setCrWeight(ClJnWeight.getText().toString());
                recClNames.setEmail(ClEmail.getText().toString());
                recClNames.setAddress(ClAddress.getText().toString());
                recClNames.setFee(ClFee.getText().toString());
                recClNames.setDate(ClDate.getText().toString());
                recClNames.setJnBmi(ClJnBMI.getText().toString());
                recClNames.setCrBmi(ClJnBMI.getText().toString());

                SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");
                Date dueDaTe = new Date();
                try {
                    dueDaTe = df.parse(ClDue.getText().toString());
                } catch (ParseException e) { e.printStackTrace(); }
                df = new SimpleDateFormat("yyyy-MM-dd");
                String newDate = df.format(dueDaTe);
                recClNames.setDue(newDate);

                recClNames.setDuration(ClDurationList.getSelectedItem().toString());
                recClNames.setSpCase(ClSpCs.getText().toString());

                long result = MyDb.TblClients.insertData(recClNames);

                long result2 = MyDb.TblWeights.insertWeightData(ClName.getText().toString(),ClPhone.getText().toString(),
                        ClJnWeight.getText().toString(), ClDate.getText().toString(),ClJnBMI.getText().toString(),
                        ClJnBMI.getText().toString());

                if(result>0 && result2>0){
                    Toast.makeText(this,ClName.getText().toString()+"'s Account Saved Successfully",Toast.LENGTH_SHORT).show();
                    Intent back = new Intent(this, MainActivity.class);
                    startActivity(back);
                }
                else {Toast.makeText(this," SORRY!! Unable to save data ",Toast.LENGTH_SHORT).show();}
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
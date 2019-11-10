package dietrixapp.dietrix;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

import static dietrixapp.dietrix.MyDb.TblClients.CLACT;
import static dietrixapp.dietrix.MyDb.TblClients.CLADD;
import static dietrixapp.dietrix.MyDb.TblClients.CLAGE;
import static dietrixapp.dietrix.MyDb.TblClients.CLCRBMI;
import static dietrixapp.dietrix.MyDb.TblClients.CLCWT;
import static dietrixapp.dietrix.MyDb.TblClients.CLDATE;
import static dietrixapp.dietrix.MyDb.TblClients.CLDUE;
import static dietrixapp.dietrix.MyDb.TblClients.CLDUR;
import static dietrixapp.dietrix.MyDb.TblClients.CLEML;
import static dietrixapp.dietrix.MyDb.TblClients.CLFEE;
import static dietrixapp.dietrix.MyDb.TblClients.CLHT;
import static dietrixapp.dietrix.MyDb.TblClients.CLJNBMI;
import static dietrixapp.dietrix.MyDb.TblClients.CLJWT;
import static dietrixapp.dietrix.MyDb.TblClients.CLMNO;
import static dietrixapp.dietrix.MyDb.TblClients.CLNM;
import static dietrixapp.dietrix.MyDb.TblClients.CLSPCS;

public class ClientDetails extends AppCompatActivity {

    TextView DtlId, DtlName, DtlPhone, DtlHt, DtlAge, DtlWt, DtlCrWt, DtlEmail, DtlAdd, DtlFee,
            DtlJoinDate, DtlDue, DtlDur, DtlAct, DtlSpCase, DtlJnBmi, DtlCrBmi;
    MyDb myDb;

    private  static final int CALL_REQUEST = 1;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Client Details </font>",0));
        }
        myDb = new MyDb(this);

        DtlId = findViewById(R.id.DetailIdTxt);
        DtlName = findViewById(R.id.DetailNameTxt);
        DtlPhone = findViewById(R.id.DetailPhoneTxt);
        DtlAct = findViewById(R.id.DetailActTxt);
        DtlHt = findViewById(R.id.DetailHeightTxt);
        DtlAge = findViewById(R.id.DetailAgeTxt);
        DtlWt = findViewById(R.id.DetailJnWeightTxt);
        DtlCrWt = findViewById(R.id.DetailCrWeightTxt);
        DtlEmail = findViewById(R.id.DetailEmailTxt);
        DtlAdd = findViewById(R.id.DetailAddTxt);
        DtlFee = findViewById(R.id.DetailFeeTxt);
        DtlJoinDate = findViewById(R.id.DetailJoinDateTxt);
        DtlDue = findViewById(R.id.DetailDueTxt);
        DtlDur = findViewById(R.id.DetailDurationTxt);
        DtlSpCase = findViewById(R.id.DetailSpCsTxt);
        DtlJnBmi = findViewById(R.id.DetailJnBMITxt);
        DtlCrBmi = findViewById(R.id.DetailCrBMITxt);

        try {
            Cursor cursor = MyDb.TblClients.getClientAllData(getIntent().getStringExtra("ID"));
            if(cursor != null){
                cursor.moveToPosition(-1);
                while (cursor.moveToNext()){
                    DtlId.setText(getIntent().getStringExtra("ID"));
                    DtlName.setText(cursor.getString(cursor.getColumnIndex(CLNM)));
                    DtlPhone.setText(cursor.getString(cursor.getColumnIndex(CLMNO)));

                    DtlAct.setText(cursor.getString(cursor.getColumnIndex(CLACT)));
                    if (DtlAct.getText().toString().equalsIgnoreCase("active")) {
                        DtlAct.setTextColor(getResources().getColor(R.color.lightGreen,null));}
                    else if (DtlAct.getText().toString().equalsIgnoreCase("inactive")) {
                        DtlAct.setTextColor(Color.RED);}

                    DtlHt.setText(cursor.getString(cursor.getColumnIndex(CLHT)));
                    DtlAge.setText(cursor.getString(cursor.getColumnIndex(CLAGE)));
                    DtlWt.setText(cursor.getString(cursor.getColumnIndex(CLJWT)));
                    DtlCrWt.setText(cursor.getString(cursor.getColumnIndex(CLCWT)));
                    DtlEmail.setText(cursor.getString(cursor.getColumnIndex(CLEML)));
                    DtlAdd.setText(cursor.getString(cursor.getColumnIndex(CLADD)));
                    DtlFee.setText(cursor.getString(cursor.getColumnIndex(CLFEE)));
                    DtlSpCase.setText(cursor.getString(cursor.getColumnIndex(CLSPCS)));
                    DtlJoinDate.setText(cursor.getString(cursor.getColumnIndex(CLDATE)));

                    String due = cursor.getString(cursor.getColumnIndex(CLDUE));
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = df.parse(due);
                    df = new SimpleDateFormat("dd MMM, yyyy");
                    String Due = df.format(date);
                    DtlDue.setText(Due);

                    DtlDur.setText(cursor.getString(cursor.getColumnIndex(CLDUR)));
                    DtlJnBmi.setText(cursor.getString(cursor.getColumnIndex(CLJNBMI)));
                    DtlCrBmi.setText(cursor.getString(cursor.getColumnIndex(CLCRBMI)));
                }
                cursor.close();
            }else {Toast.makeText(this,"Unable to get the Data!",Toast.LENGTH_SHORT).show();}

            DtlPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(ClientDetails.this);
                        alert1.setTitle("Make a Call?");
                        alert1.setMessage("Do you want to call "+DtlName.getText().toString()+"?");
                        alert1.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + DtlPhone.getText().toString()));

                                if (ActivityCompat.checkSelfPermission(ClientDetails.this, Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ClientDetails.this,
                                            new String[]{Manifest.permission.CALL_PHONE}, CALL_REQUEST);
                                    return;
                                }

                                startActivity(intent);
                            }
                        });
                        alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        alert1.show();

            }
        });

            DtlEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!DtlEmail.getText().toString().isEmpty()){
                        AlertDialog.Builder alert1 = new AlertDialog.Builder(ClientDetails.this);
                        alert1.setTitle("Send Email?");
                        alert1.setMessage("Do you want to send Email to "+DtlName.getText().toString()+"?");
                        alert1.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("message/rfc822");
                                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{DtlEmail.getText().toString()});
                                try{
                                    startActivity(Intent.createChooser(intent,"Send Email via..."));}
                                catch (Exception e){ e.printStackTrace(); }
                            }
                        });
                        alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        alert1.show();
                    }else {Toast.makeText(getBaseContext(),"Email address not Available",Toast.LENGTH_SHORT).show();}
                }
            });

        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CALL_REQUEST)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "Permission Granted.", Toast.LENGTH_SHORT).show();
            }

            else {
                Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_dlt_upd, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == R.id.UpdMI)
        {
            Intent intent = new Intent(this,ClUpdate.class);
            intent.putExtra("id",DtlId.getText().toString());
            intent.putExtra("NAME",DtlName.getText().toString());
            intent.putExtra("PHONE",DtlPhone.getText().toString());
            intent.putExtra("ACTIVE",DtlAct.getText().toString());
            intent.putExtra("HEIGHT",DtlHt.getText().toString());
            intent.putExtra("AGE",DtlAge.getText().toString());
            intent.putExtra("EMAIL",DtlEmail.getText().toString());
            intent.putExtra("ADDRESS",DtlAdd.getText().toString());
            intent.putExtra("FEE",DtlFee.getText().toString());
            intent.putExtra("SPCASE",DtlSpCase.getText().toString());
            intent.putExtra("JOINDATE",DtlJoinDate.getText().toString());
            intent.putExtra("DUE",DtlDue.getText().toString());
            intent.putExtra("DURATION",DtlDur.getText().toString());
            startActivity(intent);
        }

        if(id == R.id.DltMI)
        {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Warning!");
            alert.setMessage("Do you want to delete this client?");
            alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyDb.TblClients.DeleteData(DtlId.getText().toString());

                    MyDb.TblWeights.deleteWeightData(DtlName.getText().toString());

                    Toast.makeText(getBaseContext(),DtlName.getText().toString()+"'s Account Deleted Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void PaymentHistory(View view)
    {
        Intent intent = new Intent(this,PaymentHistory.class);
        intent.putExtra("PayID",DtlId.getText().toString());
        startActivity(intent);
    }
}

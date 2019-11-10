package dietrixapp.dietrix;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Objects;

import static dietrixapp.dietrix.MyDb.TblBoot.USRFTNM;
import static dietrixapp.dietrix.MyDb.TblBoot.USRPAY;
import static dietrixapp.dietrix.MyDb.TblBoot.USRSNNM;
import static dietrixapp.dietrix.MyDb.TblBoot.USRTAG;
import static dietrixapp.dietrix.MyDb.TblBoot.USRWT;

public class Settings extends AppCompatActivity {

    RadioGroup radioGroupSettingView;
    RadioButton newAddedRB, alphabetRB;
    SharedPreferences settingsViewSharedPref;
    EditText DialFName,DialSName,DialTag,DialPayRmd,DialWtRmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if(getSupportActionBar()!= null) {
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'> Settings</font>",0));
        }

        radioGroupSettingView = findViewById(R.id.SettingsRadioGroup);
        newAddedRB = findViewById(R.id.SettingsRadioDefault);
        alphabetRB = findViewById(R.id.SettingsRadioAlpha);
        settingsViewSharedPref = getSharedPreferences("SettingsPref", Context.MODE_PRIVATE);
        Button saveSharedPref = findViewById(R.id.SettingViewSaveBtn);
        Button settingSendEmail = findViewById(R.id.SettingEmailButton);

        if(Objects.requireNonNull(settingsViewSharedPref.getString("ViewType", "")).equalsIgnoreCase(alphabetRB.getText().toString()))
        {
            alphabetRB.setChecked(true);
        }
        else
            {
                newAddedRB.setChecked(true);
            }

        saveSharedPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioGroupSettingView.getCheckedRadioButtonId() == -1)
                {
                    Toast.makeText(getBaseContext(),"Please select one option above!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SharedPreferences.Editor editor = settingsViewSharedPref.edit();
                    RadioButton rb = findViewById(radioGroupSettingView.getCheckedRadioButtonId());
                    editor.putString("ViewType",rb.getText().toString());
                    boolean val = editor.commit();
                    if(val)
                        Toast.makeText(Settings.this,"Setting Saved!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        DialFName = findViewById(R.id.TaglineFN);
        DialSName = findViewById(R.id.TaglineLN);
        DialTag = findViewById(R.id.TaglineCharts);
        DialPayRmd = findViewById(R.id.TaglinePayments);
        DialWtRmd = findViewById(R.id.TaglineWeight);
        Button DialUpd = findViewById(R.id.SettingTaglineSaveBtn);

        Cursor c = MyDb.TblBoot.getAllBootData();
        if(c!=null){
            c.moveToPosition(-1);
            while (c.moveToNext()){
                DialFName.setText(c.getString(c.getColumnIndex(USRFTNM)));
                DialSName.setText(c.getString(c.getColumnIndex(USRSNNM)));
                DialTag.setText(c.getString(c.getColumnIndex(USRTAG)));
                DialPayRmd.setText(c.getString(c.getColumnIndex(USRPAY)));
                DialWtRmd.setText(c.getString(c.getColumnIndex(USRWT)));
            }
            c.close();
        }

        DialUpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DialFName.getText().toString().equalsIgnoreCase("") ||
                DialSName.getText().toString().equalsIgnoreCase("") ||
                DialTag.getText().toString().equalsIgnoreCase("") ||
                DialPayRmd.getText().toString().equalsIgnoreCase("") ||
                        DialWtRmd.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(getBaseContext(),"Cannot leave any field empty!",Toast.LENGTH_SHORT).show();
                }
                else
                    {
                        boolean updated = MyDb.TblBoot.bootUpdate(DialFName.getText().toString(),DialSName.getText().toString(),
                                DialTag.getText().toString(),DialPayRmd.getText().toString(),DialWtRmd.getText().toString());
                        if(updated)
                        {
                            Toast.makeText(getBaseContext(),"Data updated successfully!",Toast.LENGTH_SHORT).show();
                            DialFName.clearFocus();
                            DialSName.clearFocus();
                            DialTag.clearFocus();
                            DialPayRmd.clearFocus();
                            DialWtRmd.clearFocus();
                        }
                    }
            }
        });

        settingSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(Settings.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_settings_email);

                final RadioGroup radioGroupEmail = dialog.findViewById(R.id.SettingEmailTopicRadio);
                Button EmailProceed,EmailCancel;
                EmailProceed = dialog.findViewById(R.id.SettingsEmailProceed);
                EmailCancel = dialog.findViewById(R.id.SettingsEmailCancel);


                EmailProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"imdcorp.customercare@gmail.com"});
                        String subject = ((RadioButton)dialog.findViewById(radioGroupEmail.getCheckedRadioButtonId())).getText().toString();
                        intent.putExtra(Intent.EXTRA_SUBJECT,"'"+subject+"' by "+ DialFName.getText().toString()
                        +" "+DialSName.getText().toString());
                        try{
                            startActivity(Intent.createChooser(intent,"Choose your Email app..."));}
                        catch (Exception e){ e.printStackTrace(); }
                    }
                });

                EmailCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {dialog.dismiss();}
                });

                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

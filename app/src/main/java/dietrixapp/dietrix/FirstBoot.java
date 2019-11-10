package dietrixapp.dietrix;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstBoot extends AppCompatActivity {

    EditText BootFName,BootSName,BootTag,BootPayRmd,BootWtRmd;
    Button BootBtn;
    MyDb myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_boot);

        myDb = new MyDb(this);
        BootFName = findViewById(R.id.BootFirstTxt);
        BootSName = findViewById(R.id.BootSecondTxt);
        BootTag = findViewById(R.id.BootTaglineTxt);
        BootBtn = findViewById(R.id.BootBtn);
        BootPayRmd = findViewById(R.id.BootRmdPayTxt);
        BootWtRmd = findViewById(R.id.BootRmdWtTxt);

        BootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!BootFName.getText().toString().equals("") && !BootSName.getText().toString().equals("")
                        && !BootTag.getText().toString().equals("") && !BootPayRmd.getText().toString().equals("")
                        && !BootWtRmd.getText().toString().equals("")){
                long inserted = MyDb.TblBoot.insertBoot(BootFName.getText().toString(),BootSName.getText().toString(),BootTag.getText().toString(),
                        BootPayRmd.getText().toString(),BootWtRmd.getText().toString());
                if(inserted>0){
                Intent intent = new Intent(getBaseContext(),SplashScreen.class);
                startActivity(intent);}
                else { Toast.makeText(getBaseContext(),"Sorry Unable to save Data!",Toast.LENGTH_SHORT).show();}
                }
                else {
                    Toast.makeText(getBaseContext(),"Please enter all the above details!",Toast.LENGTH_SHORT).show();}
            }
        });
    }
}

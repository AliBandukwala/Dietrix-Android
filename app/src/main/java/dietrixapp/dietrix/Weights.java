package dietrixapp.dietrix;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;
import java.util.ArrayList;
import static dietrixapp.dietrix.MyDb.TblClients.CLCRBMI;
import static dietrixapp.dietrix.MyDb.TblClients.CLCWDT;
import static dietrixapp.dietrix.MyDb.TblClients.CLCWT;
import static dietrixapp.dietrix.MyDb.TblClients.CLHT;
import static dietrixapp.dietrix.MyDb.TblClients.CLID;
import static dietrixapp.dietrix.MyDb.TblClients.CLJNBMI;
import static dietrixapp.dietrix.MyDb.TblClients.CLMNO;
import static dietrixapp.dietrix.MyDb.TblClients.CLNM;

public class Weights extends AppCompatActivity {

    ArrayList<WeightRecInfo> weightRecInfo;
    RecyclerView recyclerView3;
    WeightRecyclerAdapter weightRecyclerAdapter;
    MyDb myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weights);
        myDb = new MyDb(this);
        weightRecInfo = new ArrayList<>();
        recyclerView3 = findViewById(R.id.recyclerWeight);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        weightRecyclerAdapter = new WeightRecyclerAdapter(this, weightRecInfo);
        recyclerView3.setAdapter(weightRecyclerAdapter);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        retrieveWeights();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_weights, menu);

        MenuItem searchItem = menu.findItem(R.id.SearchWeights);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<WeightRecInfo> FilterClList = new ArrayList<>();
                for(WeightRecInfo weightInfo : weightRecInfo  )
                {
                    String name = weightInfo.getNameW().toLowerCase();
                    if(name.contains(newText))
                    {FilterClList.add(weightInfo);}
                }
                weightRecyclerAdapter.setFilter(FilterClList);
                weightRecyclerAdapter.notifyDataSetChanged();
                recyclerView3.setAdapter(weightRecyclerAdapter);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void retrieveWeights()
    {
        weightRecInfo.clear();
        Cursor cursor = MyDb.TblClients.getWeights("Active");
        if(cursor!=null){
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                try{
                    String id = cursor.getString(cursor.getColumnIndex(CLID));
                    String name = cursor.getString(cursor.getColumnIndex(CLNM));
                    String ht = cursor.getString(cursor.getColumnIndex(CLHT));
                    String phone = cursor.getString(cursor.getColumnIndex(CLMNO));
                    String cwt = cursor.getString(cursor.getColumnIndex(CLCWT));
                    String cwtdt = cursor.getString(cursor.getColumnIndex(CLCWDT));
                    String cwtJnBmi = cursor.getString(cursor.getColumnIndex(CLJNBMI));
                    String cwtCrBmi = cursor.getString(cursor.getColumnIndex(CLCRBMI));
                    WeightRecInfo wtRec = new WeightRecInfo(id,name,ht,phone,cwt,cwtdt,cwtJnBmi,cwtCrBmi);
                    weightRecInfo.add(wtRec);
                    recyclerView3.setAdapter(weightRecyclerAdapter);
                }
                catch (Exception e){e.printStackTrace();}
            }
        }else {Toast.makeText(this,"No Active clients are currently available!",Toast.LENGTH_SHORT).show();}
    }
}

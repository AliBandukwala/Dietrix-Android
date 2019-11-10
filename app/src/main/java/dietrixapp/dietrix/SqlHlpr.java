package dietrixapp.dietrix;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqlHlpr extends SQLiteOpenHelper {
    SqlHlpr(Context context) {
        super(context, MyDb.DB_NAME, null,MyDb.DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CLIENTS_TABLE = "CREATE TABLE " + MyDb.TBL_CLIENTS + " (" +MyDb.TblClients.CLID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," + MyDb.TblClients.CLNM + " TEXT,"+MyDb.TblClients.CLMNO +" TEXT," +MyDb.TblClients.CLHT+ " TEXT,"+ MyDb.TblClients.CLAGE +" TEXT,"+ MyDb.TblClients.CLJWT +" TEXT,"+MyDb.TblClients.CLCWT+" TEXT,"+ MyDb.TblClients.CLEML +" TEXT,"+MyDb.TblClients.CLADD +" TEXT," +
                MyDb.TblClients.CLFEE+ " TEXT,"+ MyDb.TblClients.CLDATE +" TEXT,"+ MyDb.TblClients.CLDUR +" TEXT,"+MyDb.TblClients.CLACT +" TEXT," + MyDb.TblClients.CLSPCS + " TEXT,"+MyDb.TblClients.CLDUE +" TEXT,"+ MyDb.TblClients.CLCWDT +" TEXT," +MyDb.TblClients.CLJNBMI+ " TEXT," +MyDb.TblClients.CLCRBMI+ " TEXT)";
        db.execSQL(CREATE_CLIENTS_TABLE);

        String CREATE_WEIGHTS_TABLE = "CREATE TABLE " + MyDb.TBL_WEIGHTS + " (" +MyDb.TblWeights.CLNM2+ " TEXT," +MyDb.TblWeights.CLPHN2+ " TEXT," +MyDb.TblWeights.CLCWT2+ " TEXT," +MyDb.TblWeights.CLWTDT2+ " TEXT," +MyDb.TblWeights.CLJNBMI2+ " TEXT," +MyDb.TblWeights.CLCRBMI2+ " TEXT)";
        db.execSQL(CREATE_WEIGHTS_TABLE);

        String CREATE_BOOT_TABLE = "CREATE TABLE "+MyDb.TBL_BOOT+" ("+MyDb.TblBoot.USRFTNM+" TEXT,"+MyDb.TblBoot.USRSNNM+" TEXT,"+MyDb.TblBoot.USRTAG+" TEXT,"+MyDb.TblBoot.USRPAY+" TEXT,"+MyDb.TblBoot.USRWT+" TEXT)";
        db.execSQL(CREATE_BOOT_TABLE);

        String CREATE_INCOME_TABLE = "CREATE TABLE "+MyDb.TBL_INCOME_ACT+" ("+MyDb.Tbl_Income_Act.INCAMT+" TEXT,"+MyDb.Tbl_Income_Act.INCACTCNT+" TEXT,"+MyDb.Tbl_Income_Act.INCPREMNTH+" TEXT)";
        db.execSQL(CREATE_INCOME_TABLE);

        String CREATE_PAYHIST_TABLE = "CREATE TABLE "+MyDb.TBL_PAY_HIST+" ("+MyDb.TblPayHistory.PAYHISTID+" TEXT,"+MyDb.TblPayHistory.PAYHISTMONTH+" TEXT,"+MyDb.TblPayHistory.PAYHISTAMT+" TEXT,"+MyDb.TblPayHistory.PAYHISTDUE+" TEXT)";
        db.execSQL(CREATE_PAYHIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " +MyDb.TBL_CLIENTS);
        db.execSQL("DROP TABLE IF EXISTS " +MyDb.TBL_WEIGHTS);
        db.execSQL("DROP TABLE IF EXISTS "+MyDb.TBL_BOOT);
        db.execSQL("DROP TABLE IF EXISTS "+MyDb.TBL_INCOME_ACT);
        db.execSQL("DROP TABLE IF EXISTS "+MyDb.TBL_PAY_HIST);
        onCreate(db);*/

        String CREATE_INCOME_TABLE = "CREATE TABLE "+MyDb.TBL_INCOME_ACT+" ("+MyDb.Tbl_Income_Act.INCAMT+" TEXT,"+MyDb.Tbl_Income_Act.INCACTCNT+" TEXT,"+MyDb.Tbl_Income_Act.INCPREMNTH+" TEXT)";
        db.execSQL(CREATE_INCOME_TABLE);

        String CREATE_PAYHIST_TABLE = "CREATE TABLE "+MyDb.TBL_PAY_HIST+" ("+MyDb.TblPayHistory.PAYHISTID+" TEXT,"+MyDb.TblPayHistory.PAYHISTMONTH+" TEXT,"+MyDb.TblPayHistory.PAYHISTAMT+" TEXT,"+MyDb.TblPayHistory.PAYHISTDUE+" TEXT)";
        db.execSQL(CREATE_PAYHIST_TABLE);
    }

}

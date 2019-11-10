package dietrixapp.dietrix;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyDb {

        public static SqlHlpr sqh;
        public static final String  DB_NAME="MyDb";
        public static final String  TBL_CLIENTS="tbl_clients";
        public static final String  TBL_WEIGHTS="tbl_weights";
        public static final String TBL_BOOT="tbl_boot";
        public static final String TBL_INCOME_ACT="tbl_inc_act";
        public static final String TBL_PAY_HIST = "tbl_pay_hist";
        public static final int  DB_VER= 11;



    public MyDb(Context ct)
    {
        sqh = new SqlHlpr(ct);
    }

    //TABLE BOOT ***********************************************************************************
    public static final class TblBoot{
        public static final String USRFTNM="usrftnm";
        public static final String USRSNNM="usrsnnm";
        public static final String USRTAG="usrtag";
        public static final String USRPAY="usrpay";
        public static final String USRWT="usrwt";

        public static long insertBoot(String fName, String sName, String tag, String pay, String wt){
            try{
            SQLiteDatabase db =sqh.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(USRFTNM,fName);
            cv.put(USRSNNM,sName);
            cv.put(USRTAG,tag);
            cv.put(USRPAY,pay);
            cv.put(USRWT,wt);
            return db.insert(TBL_BOOT,null,cv);}
            catch (Exception e){e.printStackTrace();}
            return 0;
        }

        public static Cursor getAllBootData(){
            SQLiteDatabase db = sqh.getReadableDatabase();
            String bootData[] = {USRFTNM,USRSNNM,USRTAG,USRPAY,USRWT};
            return db.query(TBL_BOOT,bootData,null,null,null,null,null);
        }

        public static Cursor getBootName(){
            SQLiteDatabase db = sqh.getReadableDatabase();
            String bootData[] = {USRFTNM};
            return db.query(TBL_BOOT,bootData,null,null,null,null,null);
        }

        public static Cursor getBootTag(){
            SQLiteDatabase db = sqh.getReadableDatabase();
            String bootData[] = {USRTAG};
            return db.query(TBL_BOOT,bootData,null,null,null,null,null);
        }

        public static Cursor getBootPayRmd(){
            SQLiteDatabase db = sqh.getReadableDatabase();
            String bootData[] = {USRPAY};
            return db.query(TBL_BOOT,bootData,null,null,null,null,null);
        }

        public static Cursor getBootWtRmd(){
            SQLiteDatabase db = sqh.getReadableDatabase();
            String bootData[] = {USRWT};
            return db.query(TBL_BOOT,bootData,null,null,null,null,null);
        }

        public static boolean bootUpdate(String fName, String sName, String tag, String pay, String wt){
            SQLiteDatabase db = sqh.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(USRFTNM,fName);
            cv.put(USRSNNM,sName);
            cv.put(USRTAG,tag);
            cv.put(USRPAY,pay);
            cv.put(USRWT,wt);
            db.update(TBL_BOOT,cv,null,null);
            return true;
        }
    }

    //TABLE CLIENTS ********************************************************************************
    public static final class TblClients
        {
            public static final String  CLID="clid";
            public static final String  CLNM="clnm";
            public static final String  CLMNO="clmno";
            public static final String  CLHT="clht";
            public static final String  CLAGE="clage";
            public static final String  CLJWT="cljoinwt";
            public static final String  CLCWT="clcrntwt";
            public static final String  CLEML="cleml";
            public static final String  CLADD="cladd";
            public static final String  CLFEE="clfee";
            public static final String  CLDATE="cldate";
            public static final String  CLDUR="cldur";
            public static final String  CLACT="clact";
            public static final String  CLSPCS="clspcs";
            public static final String  CLDUE="cldue";
            public static final String  CLCWDT="clcwdt";
            public static final String  CLJNBMI="cljnbmi";
            public static final String  CLCRBMI="clcrbmi";


            public static long insertData(RecClNames recClNames)
            {
                try{
                    SQLiteDatabase db =sqh.getWritableDatabase();

                    ContentValues cv = new ContentValues();

                    cv.put(CLNM,recClNames.getName());
                    cv.put(CLMNO,recClNames.getPhone());
                    cv.put(CLHT,recClNames.getHeight());
                    cv.put(CLAGE,recClNames.getAge());
                    cv.put(CLJWT,recClNames.getJnWeight());
                    cv.put(CLCWT,recClNames.getCrWeight());
                    cv.put(CLEML,recClNames.getEmail());
                    cv.put(CLADD,recClNames.getAddress());
                    cv.put(CLFEE,recClNames.getFee());
                    cv.put(CLDATE,recClNames.getDate());
                    cv.put(CLDUE,recClNames.getDue());
                    cv.put(CLDUR,recClNames.getDuration());
                    cv.put(CLACT,recClNames.getActive());
                    cv.put(CLSPCS,recClNames.getSpCase());
                    cv.put(CLJNBMI,recClNames.getJnBmi());
                    cv.put(CLCRBMI,recClNames.getCrBmi());
                    return db.insert(TBL_CLIENTS,CLID,cv);
                }
            catch (Exception e){e.printStackTrace();}
                return 0;
            }


            public static Cursor getActClients(String act)
            {   try{
                act="Active";
                SQLiteDatabase db = sqh.getReadableDatabase();
                String[] NameLine = {CLID, CLNM, CLMNO, CLSPCS, CLDUE, CLACT};
                Cursor cursor = db.query(TBL_CLIENTS,NameLine," CLACT = ? ",new String[]{act},null,null,null);

                return cursor;}
            catch (Exception e){e.printStackTrace();}
                return null;
            }

            public static Cursor getInActClients(String inAct)
            {   try{
                inAct="Inactive";
                SQLiteDatabase db = sqh.getReadableDatabase();
                String[] NameLine = {CLID, CLNM, CLMNO, CLSPCS, CLDUE, CLACT};
                Cursor cursor = db.query(TBL_CLIENTS,NameLine," CLACT = ? ",new String[]{inAct},null,null,null);

                return cursor;}
            catch (Exception e){e.printStackTrace();}
                return null;
            }

            public static Cursor getAllClients(){
                try{
                    SQLiteDatabase db = sqh.getReadableDatabase();
                    String NameLine[] = {CLID, CLNM, CLMNO, CLSPCS, CLDUE, CLACT};
                    return db.query(TBL_CLIENTS,NameLine,null,null,null,null,null);
                }
                catch (Exception e){e.printStackTrace();}
                return null;
            }

            public static Cursor getClientAllData(String Id)
            {   try{
                SQLiteDatabase db = sqh.getReadableDatabase();
                String[] NameLine = {CLID, CLNM, CLMNO, CLACT, CLHT, CLAGE, CLJWT, CLCWT, CLEML, CLADD, CLFEE, CLDATE, CLDUE, CLDUR, CLSPCS, CLJNBMI, CLCRBMI};
                Cursor cursor = db.query(TBL_CLIENTS,NameLine," CLID = ? ",new String[]{Id},null,null,null);

                return cursor;}
            catch (Exception e){e.printStackTrace();}
                return null;
            }

            public static Cursor getChartActNames(String act){
                try{
                    act = "Active";
                    SQLiteDatabase db = sqh.getReadableDatabase();
                    String[] ChartNames = {CLNM};
                    return db.query(TBL_CLIENTS,ChartNames,"CLACT = ?",new String[]{act},null,null,null);
                }
                catch (Exception e){e.printStackTrace();}
                return null;
            }

            public static Cursor getChartNum(String name){
                try{
                    SQLiteDatabase db = sqh.getReadableDatabase();
                    String[] num = {CLMNO, CLSPCS, CLDUE};
                    return db.query(TBL_CLIENTS,num,"CLNM = ?",new String[]{name},null,null,null);
                }
                catch (Exception e){e.printStackTrace();}
                return null;
            }

            public static boolean UpdateData(String Id, String name, String phone, String height, String age,
                                             String email, String address, String fee, String date, String due,
                                             String duration, String active, String SplCase)
            {
                SQLiteDatabase db = sqh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(CLID,Id);
                cv.put(CLNM,name);
                cv.put(CLMNO,phone);
                cv.put(CLHT,height);
                cv.put(CLAGE,age);
                cv.put(CLEML,email);
                cv.put(CLADD,address);
                cv.put(CLFEE,fee);
                cv.put(CLDATE,date);
                cv.put(CLDUE,due);
                cv.put(CLDUR,duration);
                cv.put(CLACT,active);
                cv.put(CLSPCS,SplCase);
                db.update(TBL_CLIENTS,cv,"CLID = ?", new String[]{Id});
                return true;
            }
            public  static void DeleteData(String id)
            {
                SQLiteDatabase db = sqh.getWritableDatabase();
                db.delete(TBL_CLIENTS, " CLID = ?", new String[]{id});
            }

            public static boolean TurnActInct(String id, String act){
                SQLiteDatabase db = sqh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(CLACT,act);
                db.update(TBL_CLIENTS,cv," CLID = ?",new String[]{id});
                return true;
            }

            public static Cursor getPayDueData(String act)
            {   act = "Active";
                SQLiteDatabase db = sqh.getReadableDatabase();
                String[] payData = {CLID,CLNM,CLDUE,CLFEE,CLMNO,CLDUR};
                return  db.query(TBL_CLIENTS,payData,"date(CLDUE) <= date('now') AND CLACT = ? ",new String[]{act},null,null,null);
            }

            public static boolean UpdatePayed(String id, String due)
            {
                SQLiteDatabase db = sqh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(CLID,id);
                cv.put(CLDUE,due);
                db.update(TBL_CLIENTS,cv,"CLID = ?",new String[]{id});
                return true;
            }

            public static Cursor getWeights(String act)
            {
                act = "Active";
                SQLiteDatabase db = sqh.getReadableDatabase();
                String[] weightsData = {CLID,CLNM,CLMNO,CLCWT,CLCWDT,CLHT,CLJNBMI,CLCRBMI};
                return db.query(TBL_CLIENTS,weightsData,"CLACT = ?",new String[]{act},null,null,null);
            }

            public  static boolean updateWeight(String id, String CrntWt, String CrWtDt, String CrBmi)
            {
                SQLiteDatabase db = sqh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(CLCWT,CrntWt);
                cv.put(CLCWDT,CrWtDt);
                cv.put(CLCRBMI,CrBmi);
                db.update(TBL_CLIENTS,cv,"CLID = ?",new String[]{id});
                return true;
            }

            public static Cursor getFeesData()
            {   try{
                String act = "Active";
                SQLiteDatabase db = sqh.getReadableDatabase();
                String[] NameLine = {CLFEE,CLDUR};

                return db.query(TBL_CLIENTS,NameLine," CLACT = ? ",new String[]{act},null,null,null);}
            catch (Exception e){e.printStackTrace();}
                return null;
            }
        }

        //TABLE INCOME/ACT *************************************************************************
        public static final class Tbl_Income_Act
        {
            public static final String INCAMT = "IncAmt";
            public static final String INCACTCNT = "ActCnt";
            public static final String INCPREMNTH = "PreMnth";

            public static long insertIncData(String amt, String act, String month)
            {
                SQLiteDatabase db = sqh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(INCAMT,amt);
                cv.put(INCACTCNT,act);
                cv.put(INCPREMNTH,month);

                return db.insert(TBL_INCOME_ACT,null,cv);
            }

            public static Cursor getIncomeActData()
            {
                SQLiteDatabase db = sqh.getReadableDatabase();
                String[] incomeData = {INCAMT,INCACTCNT,INCPREMNTH};
                return db.query(TBL_INCOME_ACT,incomeData,null,null,null,null,null);
            }
        }

        //TABLE WEIGHTS ****************************************************************************
        public static final class TblWeights
        {
            public static final String CLNM2 = "clnm2";
            public static final String CLPHN2 = "clphn2";
            public static final String CLCWT2 = "clcwt2";
            public static final String CLWTDT2 = "clwtdt2";
            public static final String CLJNBMI2 = "cljnbmi2";
            public static final String CLCRBMI2 = "clcrbmi2";

            public static long insertWeightData(String name2, String phone2, String cWt2, String cWtDt2, String JnBmi2, String CrBmi2){
                SQLiteDatabase db = sqh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(CLNM2,name2);
                cv.put(CLPHN2,phone2);
                cv.put(CLCWT2,cWt2);
                cv.put(CLWTDT2,cWtDt2);
                cv.put(CLJNBMI2,JnBmi2);
                cv.put(CLCRBMI2,CrBmi2);
                return db.insert(TBL_WEIGHTS,null,cv);
            }

            public static Cursor getWeightData(String name2){
                SQLiteDatabase db = sqh.getReadableDatabase();
                String[] weightData = {CLNM2,CLCWT2,CLWTDT2,CLJNBMI2,CLCRBMI2};
                return db.query(TBL_WEIGHTS,weightData,"CLNM2 = ?",new String[]{name2},null,null,null);
            }

            public static boolean updateWtHisName(String name2, String phone2){
                SQLiteDatabase db = sqh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(CLNM2,name2);
                cv.put(CLPHN2,phone2);
                db.update(TBL_WEIGHTS,cv,"CLPHN2 = ?",new String[]{phone2});
                return true;
            }

            public static void deleteWeightData(String name2){
                SQLiteDatabase db = sqh.getWritableDatabase();
                db.delete(TBL_WEIGHTS, "CLNM2 = ?", new String[]{name2});
            }

            public static void deleteWtInfo(String name2, String WtDt2, String cWt2, String CrtDate2){
                SQLiteDatabase db = sqh.getWritableDatabase();
                db.delete(TBL_WEIGHTS, "CLNM2 = ? AND CLWTDT2 = ? AND CLCWT2 = ? AND CLWTDT2 = ?", new String[]{name2, WtDt2, cWt2, CrtDate2});
            }
        }

    //TABLE PAYMENT_HISTORY*************************************************************************
    public static final class TblPayHistory {
        public static final String PAYHISTID = "payHistID";
        public static final String PAYHISTMONTH = "payHistMonth";
        public static final String PAYHISTAMT = "payHistAMt";
        public static final String PAYHISTDUE = "payHistDue";

            public static long insertPayHistData(String id, String month, String amt, String due){
                SQLiteDatabase db = sqh.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put(PAYHISTID,id);
                cv.put(PAYHISTMONTH,month);
                cv.put(PAYHISTAMT,amt);
                cv.put(PAYHISTDUE,due);
                return db.insert(TBL_PAY_HIST,null,cv);
            }

        public static Cursor getPayHistData(String id){
            SQLiteDatabase db = sqh.getReadableDatabase();
            String[] payHistData = {PAYHISTMONTH,PAYHISTAMT,PAYHISTDUE};
            return db.query(TBL_PAY_HIST,payHistData,"PAYHISTID = ?",new String[]{id},null,null,null);
        }
    }
}

package dietrixapp.dietrix;

public class IncomeHisRecInfo
{
    private String Amt, Month, Act;

    IncomeHisRecInfo(String amt, String month, String act)
    {
        Amt = amt;
        Month = month;
        Act = act;
    }

    public String getAmt() {
        return Amt;
    }

    public void setAmt(String amt) {
        Amt = amt;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getAct() {
        return Act;
    }

    public void setAct(String act) {
        Act = act;
    }
}

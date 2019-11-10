package dietrixapp.dietrix;

public class PaymentHistRecInfo {
    private String PayHistMonth, PayHisAmt, PayHistDue;

    PaymentHistRecInfo(String payHisMonth, String payHistAmt, String payHistDue){
        this.PayHistMonth = payHisMonth;
        this.PayHisAmt = payHistAmt;
        this.PayHistDue = payHistDue;
    }

    public String getPayHistMonth() {
        return PayHistMonth;
    }

    public void setPayHistMonth(String payHistMonth) {
        PayHistMonth = payHistMonth;
    }

    public String getPayHisAmt() {
        return PayHisAmt;
    }

    public void setPayHisAmt(String payHisAmt) {
        PayHisAmt = payHisAmt;
    }

    public String getPayHistDue() {
        return PayHistDue;
    }

    public void setPayHistDue(String payHistDue) {
        PayHistDue = payHistDue;
    }
}

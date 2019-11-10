package dietrixapp.dietrix;

class WeightHistoryInfo {

    private String WtName,WT,wtDT, WtHisCrBMI;

    WeightHistoryInfo(String wtName, String wt, String wtDaTe, String wtHisCrBMI){
        WtName=wtName;
        WT=wt;
        wtDT=wtDaTe;
        WtHisCrBMI=wtHisCrBMI;
    }

    String getWtName() {return WtName;}


    String getWT() {return WT;}


    String getWtDT() {return wtDT;}


    String getWtHisCrBMI() {
        return WtHisCrBMI;
    }

}

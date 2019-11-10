package dietrixapp.dietrix;

class WeightRecInfo {

    private String IdW, NameW, HtW, CWeightW, PhoneW, CWtDtW, JnBmiW, CrBmiW ;

    WeightRecInfo(String idW, String nameW, String htW, String phoneW, String cWeightW, String crWtDtW,
                  String jnBmiW, String crBmiW)
    {
        IdW=idW;
        NameW=nameW;
        HtW=htW;
        CWeightW=cWeightW;
        PhoneW=phoneW;
        CWtDtW=crWtDtW;
        JnBmiW=jnBmiW;
        CrBmiW=crBmiW;
    }

    String getIdW() {
        return IdW;
    }


    String getNameW() {
        return NameW;
    }



    String getHtW() {
        return HtW;
    }


    String getCWeightW() {
        return CWeightW;
    }


    String getPhoneW() {
        return PhoneW;
    }


    String getCWtDtW() {
        return CWtDtW;
    }


    String getJnBmiW() {
        return JnBmiW;
    }


    String getCrBmiW() {
        return CrBmiW;
    }
}

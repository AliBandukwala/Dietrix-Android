package dietrixapp.dietrix;

class RecClNames {
    private String ID, Name, Phone, Height, Age, JnWeight, CrWeight, Email, Address, Fee, Date, Due, Duration, Active, SpCase, JnBmi, CrBmi;

    RecClNames(String Id, String name, String phone, String height, String age, String jnWeight, String crWeight,
               String email, String address, String fee, String date, String due, String duration, String active, String SplCase,
               String jnBmi, String crBmi) {
        Name = name;
        ID = Id;
        Phone = phone;
        Active = active;
        Height = height;
        Age = age;
        JnWeight = jnWeight;
        CrWeight=crWeight;
        Email = email;
        Address = address;
        Fee = fee;
        Date = date;
        Due = due;
        Duration = duration;
        SpCase = SplCase;
        JnBmi = jnBmi;
        CrBmi = crBmi;
    }

    RecClNames(String Id,String name, String phone, String splCase,String due, String active) {
        ID = Id;
        Name = name;
        Phone = phone;
        SpCase = splCase;
        Due = due;
        Active = active;
    }

    RecClNames(){}

    String getID() {return ID;}


    public String getName() {return Name;}

    public void setName(String name) {Name = name;}

    String getPhone() {return Phone;}

    void setPhone(String phone) {Phone = phone;}

    public String getHeight() {return Height;}

    public void setHeight(String height) {Height = height;}

    String getAge() {return Age;}

    void setAge(String age) {Age = age;}

    String getJnWeight() {return JnWeight;}

    void setJnWeight(String jnweight) {JnWeight = jnweight;}

    String getEmail() {return Email;}

    void setEmail(String email) {Email = email;}

    String getAddress() {return Address;}

    void setAddress(String address) {Address = address;}

    String getFee() {return Fee;}

    void setFee(String fee) {Fee = fee;}

    String getDate() {return Date;}

    void setDate(String date) {Date = date;}

    public String getDue() {
        return Due;
    }

    public void setDue(String due) {
        Due = due;
    }

    String getDuration() {return Duration;}

    void setDuration(String duration) {Duration = duration;}

    String getActive() {return Active;}

    void setActive(String active) {Active = active;}

    String getSpCase() {return SpCase;}

    void setSpCase(String spCase) {SpCase = spCase;}

    String getCrWeight() {return CrWeight;}

    void setCrWeight(String crWeight) {CrWeight = crWeight;}

    String getJnBmi() {return JnBmi;}

    void setJnBmi(String jnBmi) {JnBmi = jnBmi;}

    String getCrBmi() {return CrBmi;}

    void setCrBmi(String crBmi) {CrBmi = crBmi;}
}

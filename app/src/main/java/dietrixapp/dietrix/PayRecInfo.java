package dietrixapp.dietrix;

class PayRecInfo {
    private String Id2, Name2, Due2, Fee2, Phone2,Duration2;

   PayRecInfo(String id2, String name2, String due2, String fee2, String phone2,String duration2){
        Id2=id2;
        Name2=name2;
        Due2=due2;
        Fee2=fee2;
       Phone2=phone2;
       Duration2=duration2;
    }

    String getPhone2() {return Phone2;}


    String getId2() {return Id2;}


    String getName2() {
        return Name2;
    }


    String getDue2() {
        return Due2;
    }


    String getFee2() {
        return Fee2;
    }

    public String getDuration2() {return Duration2;}
}

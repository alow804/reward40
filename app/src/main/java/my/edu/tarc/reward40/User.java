package my.edu.tarc.reward40;

public class User {
    private String fullname;
    private String ic;
    private String email;
    private String contact;
    private int points;
    private String userid;
    private String registeredDate;
    private String address;


    public User(String fullname, String ic, String email, String contact, int points, String userid,String registeredDate,String address) {
        this.fullname = fullname;
        this.ic = ic;
        this.email = email;
        this.contact = contact;
        this.points = points;
        this.userid = userid;
        this.registeredDate = registeredDate;
        this.address = address;
    }

    public User() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(String registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

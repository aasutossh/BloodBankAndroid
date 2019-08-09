package np.com.aasutosh.bloodbankandroid;

public class Request {
    String name;
    String bloodGroup;
    int quantity;
    String phoneNum;
    //    Use maps format "Latitude, Longitude"
    String time;
    String date;
    String location;

    public Request(String name, String bloodGroup, int quantity, String phoneNum, String time, String date, String location) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.bloodGroup = bloodGroup;
        this.location = location;
        this.date = date;
        this.time = time;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getQuantity() {
        return quantity;
    }

    public Request() { }
}

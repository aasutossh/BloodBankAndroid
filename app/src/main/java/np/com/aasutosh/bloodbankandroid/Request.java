package np.com.aasutosh.bloodbankandroid;

public class Request {
    String name;
    String phoneNum;
    String bloodGroup;
//    Use maps format "Latitude, Longitude"
//    String location;
    String typeOfRequest; // Donate or request
//    Date date;
    int quantity;
    public Request(String name, String phoneNum, String bloodGroup, String typeOfRequest, int quantity) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.bloodGroup = bloodGroup;
//        this.location = location;
        this.typeOfRequest = typeOfRequest;
//        this.date = date;
        this.quantity = quantity;
    }

//    public Date getDate() {
//        return date;
//    }

    public int getQuantity() {
        return quantity;
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

//    public String getLocation() {
//        return location;
//    }

    public String getTypeOfRequest() {
        return typeOfRequest;
    }
    public Request() { }
}

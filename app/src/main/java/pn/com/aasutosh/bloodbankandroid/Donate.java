package pn.com.aasutosh.bloodbankandroid;

public class Donate {
    String name, phoneNum, bloodGroup, userId, key;
    double lat, lng;



    public Donate(String key, String name, String phoneNum, String bloodGroup, double lat, double lng, String userId) {
        this.key = key;
        this.name = name;
        this.phoneNum = phoneNum;
        this.bloodGroup = bloodGroup;
        this.lat = lat;
        this.lng = lng;
        this.userId = userId;
    }

    public Donate() {
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

    public String getUserId() {
        return userId;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
    public String getKey() {
        return key;
    }
}

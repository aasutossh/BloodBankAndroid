package pn.com.aasutosh.bloodbankandroid;

public class Profile {
    private String name, district, phoneNum, bloodGroup, lastDonated;

    public String getName() {
        return name;
    }

    public String getDistrict() {
        return district;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getLastDonated() {
        return lastDonated;
    }

    public Profile(String name, String phoneNum, String bloodGroup, String district, String lastDonated) {
        this.name = name;
        this.district = district;
        this.phoneNum = phoneNum;
        this.bloodGroup = bloodGroup;
        this.lastDonated = lastDonated;
    }
}

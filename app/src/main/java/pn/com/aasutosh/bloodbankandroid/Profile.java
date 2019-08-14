package pn.com.aasutosh.bloodbankandroid;

public class Profile {
    public void setName(String name) {
        this.name = name;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public void setLastDonated(String lastDonated) {
        this.lastDonated = lastDonated;
    }

    private String name, district, phoneNum, bloodGroup, lastDonated, userId;

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

    public Profile(String name, String phoneNum, String bloodGroup, String district, String lastDonated, String userId) {
        this.name = name;
        this.district = district;
        this.phoneNum = phoneNum;
        this.bloodGroup = bloodGroup;
        this.lastDonated = lastDonated;
        this.userId = userId;
    }
    public Profile() {}

    public String getUserId() {
        return userId;
    }
}

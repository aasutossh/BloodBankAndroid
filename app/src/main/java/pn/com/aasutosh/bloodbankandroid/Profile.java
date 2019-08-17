package pn.com.aasutosh.bloodbankandroid;

public class Profile {
    private String name, district, phoneNum, bloodGroup, lastDonated, district_bloodGroup, userId;

    public Profile(String name, String phoneNum, String bloodGroup, String district, String lastDonated, String district_bloodGroup, String userId) {
        this.name = name;
        this.district = district;
        this.phoneNum = phoneNum;
        this.bloodGroup = bloodGroup;
        this.lastDonated = lastDonated;
        this.userId = userId;
        this.district_bloodGroup = district_bloodGroup;
    }

    public Profile() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getLastDonated() {
        return lastDonated;
    }

    public void setLastDonated(String lastDonated) {
        this.lastDonated = lastDonated;
    }

    public String getDistrict_bloodGroup() {
        return district_bloodGroup;
    }

    public void setDistrict_bloodGroup(String district_bloodGroup) {
        this.district_bloodGroup = district_bloodGroup;
    }

    public String getUserId() {
        return userId;
    }
}

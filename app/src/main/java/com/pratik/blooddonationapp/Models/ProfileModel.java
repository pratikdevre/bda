package com.pratik.blooddonationapp.Models;

public class ProfileModel {
    String name, phone, email, location, bloodGroup, type, profilePic, search;

    public ProfileModel(String name, String phone, String email, String location, String bloodGroup, String type, String profilePic, String search) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.bloodGroup = bloodGroup;
        this.type = type;
        this.profilePic = profilePic;
        this.search = search;


    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public ProfileModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}

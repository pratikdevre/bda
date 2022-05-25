package com.pratik.blooddonationapp.Models;

public class DonorsModel {
    String name, phone, email, currentUserId, location, bloodGroup, type, search, profilePic;

    public DonorsModel(String name, String phone, String email, String currentUserId, String location, String bloodGroup, String type, String search, String profilePic) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.currentUserId = currentUserId;
        this.location = location;
        this.bloodGroup = bloodGroup;
        this.type = type;
        this.search = search;
        this.profilePic = profilePic;
    }

    public DonorsModel() {
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getName() {
        return name;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public DonorsModel(String search) {
        this.search = search;
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

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
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
}

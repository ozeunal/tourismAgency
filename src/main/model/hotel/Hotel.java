package main.model.hotel;

import java.util.List;

public class Hotel {
    private int id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private String star;
    private BoardingHouseType boardingHouseType;
    private List<FacilityFeatures> facilityFeatures;

    public Hotel(int id, String name, String address, String email, String phoneNumber, String star, BoardingHouseType boardingHouseType, List<FacilityFeatures> facilityFeatures) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.star = star;
        this.boardingHouseType = boardingHouseType;
        this.facilityFeatures = facilityFeatures;
    }

    public Hotel(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public BoardingHouseType getBoardingHouseType() {
        return boardingHouseType;
    }

    public void setBoardingHouseType(BoardingHouseType boardingHouseType) {
        this.boardingHouseType = boardingHouseType;
    }

    public List<FacilityFeatures> getFacilityFeatures() {
        return facilityFeatures;
    }

    public void setFacilityFeatures(List<FacilityFeatures> facilityFeatures) {
        this.facilityFeatures = facilityFeatures;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", star='" + star + '\'' +
                ", boardingHouseType=" + boardingHouseType +
                ", facilityFeatures=" + facilityFeatures +
                '}';
    }
}

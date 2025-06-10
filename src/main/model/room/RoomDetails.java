package main.model.room;

import java.util.List;

public class RoomDetails {
    private int roomId;
    private int hotelId;
    private String hotelName;
    private String roomType;
    private int bedCount;
    private int squareMeters;
    private int stock;
    private double adultPrice;
    private double childPrice;
    private List<String> roomFeatures;

    public RoomDetails() {
    }

    public int getRoomId() {
        return roomId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getHotelId() {
        return hotelId;
    }

    public void setHotelId(int hotelId) {
        this.hotelId = hotelId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getBedCount() {
        return bedCount;
    }

    public void setBedCount(int bedCount) {
        this.bedCount = bedCount;
    }

    public int getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(int squareMeters) {
        this.squareMeters = squareMeters;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getAdultPrice() {
        return adultPrice;
    }

    public void setAdultPrice(double adultPrice) {
        this.adultPrice = adultPrice;
    }

    public double getChildPrice() {
        return childPrice;
    }

    public void setChildPrice(double childPrice) {
        this.childPrice = childPrice;
    }

    public List<String> getRoomFeatures() {
        return roomFeatures;
    }

    public void setRoomFeatures(List<String> roomFeatures) {
        this.roomFeatures = roomFeatures;
    }

    @Override
    public String toString() {
        return "RoomDetails{" +
                "roomId=" + roomId +
                ", hotelId=" + hotelId +
                ", hotelName='" + hotelName + '\'' +
                ", roomType='" + roomType + '\'' +
                ", bedCount=" + bedCount +
                ", squareMeters=" + squareMeters +
                ", stock=" + stock +
                ", adultPrice=" + adultPrice +
                ", childPrice=" + childPrice +
                ", roomFeatures=" + roomFeatures +
                '}';
    }
}

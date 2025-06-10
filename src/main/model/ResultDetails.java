package main.model;

public class ResultDetails {
    private int roomId;
    private String roomType;
    private int bedCount;
    private int stock;
    private double adultPrice;
    private double childPrice;
    private String hotelName;
    private double squareMeters;

    public ResultDetails(int roomId, String roomType, int bedCount, int stock, double adultPrice, double childPrice, String hotelName, double squareMeters) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.bedCount = bedCount;
        this.stock = stock;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
        this.hotelName = hotelName;
        this.squareMeters = squareMeters;
    }

    public ResultDetails(){}

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public double getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(double squareMeters) {
        this.squareMeters = squareMeters;
    }

    @Override
    public String toString() {
        return "ResultDetails{" +
                "roomId=" + roomId +
                ", roomType='" + roomType + '\'' +
                ", bedCount=" + bedCount +
                ", stock=" + stock +
                ", adultPrice=" + adultPrice +
                ", childPrice=" + childPrice +
                ", hotelName='" + hotelName + '\'' +
                ", squareMeters=" + squareMeters +
                '}';
    }
}

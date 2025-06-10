package main.model.room;

public class Room {

    private int id;
    private int hotelId;
    private String roomType;
    private int bedCount;
    private int squareMeters;
    private int stock;

    public Room(int id, int hotelId, String roomType, int bedCount, int squareMeters, int stock) {
        this.id = id;
        this.hotelId = hotelId;
        this.roomType = roomType;
        this.bedCount = bedCount;
        this.squareMeters = squareMeters;
        this.stock = stock;
    }

    public Room(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", hotelId=" + hotelId +
                ", roomType='" + roomType + '\'' +
                ", bedCount=" + bedCount +
                ", squareMeters=" + squareMeters +
                ", stock=" + stock +
                '}';
    }
}

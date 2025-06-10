package main.model.room;

public class RoomPrice {
    private int id;
    private int roomId;
    private int seasonId;
    private double adultPrice;
    private double childPrice;

    public RoomPrice(int id, int roomId, int seasonId, double adultPrice, double childPrice) {
        this.id = id;
        this.roomId = roomId;
        this.seasonId = seasonId;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
    }

    public RoomPrice(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
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
}

package main.model.room;

import java.util.List;

public class RoomFeatures {
    private int id;
    private int roomId;
    private List<RoomFeaturesEnum> roomFeatures;
    private boolean isAvailable;

    public RoomFeatures(int id, int roomId, List<RoomFeaturesEnum> roomFeatures, boolean isAvailable) {
        this.id = id;
        this.roomId = roomId;
        this.roomFeatures = roomFeatures;
        this.isAvailable = isAvailable;
    }

    public RoomFeatures(){

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

    public List<RoomFeaturesEnum> getRoomFeatures() {
        return roomFeatures;
    }

    public void setRoomFeatures(List<RoomFeaturesEnum> roomFeatures) {
        this.roomFeatures = roomFeatures;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

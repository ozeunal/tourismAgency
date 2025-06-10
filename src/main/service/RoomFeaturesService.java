package main.service;

import main.helper.DatabaseConfig;
import main.model.room.RoomFeatures;
import main.model.room.RoomFeaturesEnum;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RoomFeaturesService {

    public static boolean add(RoomFeatures roomFeatures) {
        if (roomFeatures == null || roomFeatures.getRoomFeatures() == null) {
            System.out.println("Room features data is invalid.");
            return false;
        }
        System.out.println("Room ID: " + roomFeatures.getRoomId());

        String query = "INSERT INTO room_features (room_id, features) " +
                "SELECT r.id, ?::room_features_enum[] " +
                "FROM room r " +
                "WHERE r.id = ?";

        try (Connection connection = DatabaseConfig.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            List<RoomFeaturesEnum> roomFeaturesEnumList = roomFeatures.getRoomFeatures();
            if (roomFeaturesEnumList != null && !roomFeaturesEnumList.isEmpty()) {
                String[] featuresArray = roomFeaturesEnumList.stream()
                        .map(Enum::name)
                        .toArray(String[]::new);
                Array sqlArray = connection.createArrayOf("room_features_enum", featuresArray);
                preparedStatement.setArray(1, sqlArray);
            } else {
                preparedStatement.setArray(1, null);
            }

            preparedStatement.setInt(2, roomFeatures.getRoomId());

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
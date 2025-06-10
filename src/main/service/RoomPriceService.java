package main.service;

import main.helper.DatabaseConfig;
import main.helper.GUIHelper;
import main.model.room.RoomPrice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomPriceService {

    public static RoomPrice getRoomPriceById(int id){
        String query = "SELECT * FROM room_price WHERE id = ?";
        RoomPrice roomPrice = null;
        try(Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                roomPrice = new RoomPrice();
                roomPrice.setId(resultSet.getInt("id"));
                roomPrice.setRoomId(resultSet.getInt("room_id"));
                roomPrice.setSeasonId(resultSet.getInt("season_id"));
                roomPrice.setAdultPrice(resultSet.getDouble("adult_price"));
                roomPrice.setChildPrice(resultSet.getDouble("child_price"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return roomPrice;
    }

    public static boolean add(RoomPrice roomPrice, int roomId, String seasonName){
        String query = "INSERT INTO room_price (room_id, season_id, adult_price, child_price) " +
                "SELECT r.id, s.id, ?, ? " +
                "FROM room r, season s " +
                "WHERE r.id = ? AND s.season_name = ? ";
        RoomPrice findRoomPrice = getRoomPriceById(roomPrice.getId());
        if (findRoomPrice != null){
            GUIHelper.showMessage("This price already entered");
            return false;
        }

        try(Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, roomPrice.getAdultPrice());
            preparedStatement.setDouble(2, roomPrice.getChildPrice());
            preparedStatement.setInt(3, roomId);
            preparedStatement.setString(4, seasonName);
            return preparedStatement.executeUpdate() == 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
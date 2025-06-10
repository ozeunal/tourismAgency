package main.service;

import main.helper.DatabaseConfig;
import main.helper.GUIHelper;
import main.model.room.Room;
import main.model.room.RoomDetails;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RoomService {

    public static ArrayList<Room> listAll(){
        ArrayList<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM room";
        Room room;
        try(Connection connection = DatabaseConfig.connect()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                room = new Room();
                room.setId(resultSet.getInt("id"));
                room.setHotelId(resultSet.getInt("hotel_id"));
                room.setRoomType(resultSet.getString("room_type"));
                room.setBedCount(resultSet.getInt("bed_count"));
                room.setSquareMeters(resultSet.getInt("square_meters"));
                room.setStock(resultSet.getInt("stock"));
                rooms.add(room);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return rooms;
    }

    public static Room getRoomById(int id){
        String query = "SELECT * FROM room WHERE id = ?";
        Room room = null;
        try(Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                room = new Room();
                room.setId(resultSet.getInt("id"));
                room.setHotelId(resultSet.getInt("hotel_id"));
                room.setRoomType(resultSet.getString("room_type"));
                room.setBedCount(resultSet.getInt("bed_count"));
                room.setSquareMeters(resultSet.getInt("square_meters"));
                room.setStock(resultSet.getInt("stock"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return room;
    }

    public static Room getRoomByRoomTypeHotelId(String roomType, int hotelId){
        String query = "SELECT * FROM room WHERE room_type = ? AND hotel_id = ?";
        Room room = null;
        try(Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, roomType);
            preparedStatement.setInt(2, hotelId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                room = new Room();
                room.setId(resultSet.getInt("id"));
                room.setHotelId(resultSet.getInt("hotel_id"));
                room.setRoomType(resultSet.getString("room_type"));
                room.setBedCount(resultSet.getInt("bed_count"));
                room.setSquareMeters(resultSet.getInt("square_meters"));
                room.setStock(resultSet.getInt("stock"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return room;
    }

    public static boolean add(Room room){
        String query = "INSERT INTO room (hotel_id, room_type, bed_count, square_meters, stock)" +
                "VALUES (?, ?, ?, ?, ?) RETURNING id";
        Room findRoom = getRoomByRoomTypeHotelId(room.getRoomType(), room.getHotelId());
        if ((findRoom != null) && (findRoom.getRoomType().equals(room.getRoomType())) && (findRoom.getHotelId() == room.getHotelId())){
            GUIHelper.showMessage("This room already added");
            return false;
        }

        try(Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, room.getHotelId());
            preparedStatement.setString(2, room.getRoomType());
            preparedStatement.setInt(3, room.getBedCount());
            preparedStatement.setInt(4, room.getSquareMeters());
            preparedStatement.setInt(5, room.getStock());

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                int generatedId = rs.getInt("id"); // Dönen id sütunu alınır
                room.setId(generatedId); // Room nesnesine ID set edilir
                return true; // Ekleme işlemi başarılı
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(Room room){
        String query = "UPDATE room set hotel_id = ?, room_type = ?, bed_count = ?, square_meters = ?, stock = ? " +
                "WHERE id = ?";
        try(Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, room.getHotelId());
            preparedStatement.setString(2, room.getRoomType());
            preparedStatement.setInt(3, room.getBedCount());
            preparedStatement.setInt(4, room.getSquareMeters());
            preparedStatement.setInt(5, room.getStock());
            preparedStatement.setInt(6, room.getId());
            return preparedStatement.executeUpdate() == 1;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<RoomDetails> listAllDetails() {
        String query = "SELECT r.id AS room_id, r.hotel_id, r.room_type, r.bed_count, r.square_meters, " +
                "r.stock, rp.adult_price, rp.child_price, rf.features " +
                "FROM room r " +
                "LEFT JOIN room_price rp ON r.id = rp.room_id " +
                "LEFT JOIN room_features rf ON r.id = rf.room_id";

        ArrayList<RoomDetails> roomDetailsList = new ArrayList<>();
        try (Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                RoomDetails roomDetails = new RoomDetails();
                roomDetails.setRoomId(rs.getInt("room_id"));
                roomDetails.setHotelId(rs.getInt("hotel_id"));
                roomDetails.setRoomType(rs.getString("room_type"));
                roomDetails.setBedCount(rs.getInt("bed_count"));
                roomDetails.setSquareMeters(rs.getInt("square_meters"));
                roomDetails.setStock(rs.getInt("stock"));
                roomDetails.setAdultPrice(rs.getDouble("adult_price"));
                roomDetails.setChildPrice(rs.getDouble("child_price"));

                Array featuresArray = rs.getArray("features");
                if (featuresArray != null) {
                    String[] features = (String[]) featuresArray.getArray();
                    roomDetails.setRoomFeatures(Arrays.asList(features));
                }

                roomDetailsList.add(roomDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomDetailsList;
    }

    public static RoomDetails getRoomDetailsById(int roomId) {
        String query = "SELECT r.id AS room_id, r.hotel_id, r.room_type, r.bed_count, r.square_meters, " +
                "r.stock, rp.adult_price, rp.child_price, rf.features " +
                "FROM room r " +
                "LEFT JOIN room_price rp ON r.id = rp.room_id " +
                "LEFT JOIN room_features rf ON r.id = rf.room_id " +
                "WHERE r.id = ?";

        RoomDetails roomDetails = null;
        try (Connection connection = DatabaseConfig.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, roomId);

            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                roomDetails = new RoomDetails();
                roomDetails.setRoomId(rs.getInt("room_id"));
                roomDetails.setHotelId(rs.getInt("hotel_id"));
                roomDetails.setRoomType(rs.getString("room_type"));
                roomDetails.setBedCount(rs.getInt("bed_count"));
                roomDetails.setSquareMeters(rs.getInt("square_meters"));
                roomDetails.setStock(rs.getInt("stock"));
                roomDetails.setAdultPrice(rs.getDouble("adult_price"));
                roomDetails.setChildPrice(rs.getDouble("child_price"));

                Array featuresArray = rs.getArray("features");
                if (featuresArray != null) {
                    String[] features = (String[]) featuresArray.getArray();
                    roomDetails.setRoomFeatures(Arrays.asList(features));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roomDetails;
    }
}
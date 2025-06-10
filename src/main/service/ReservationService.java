package main.service;

import main.helper.DatabaseConfig;
import main.model.Reservation;
import main.model.ResultDetails;
import main.model.room.RoomDetails;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationService {

    public static ArrayList<Reservation> listAll(){
         ArrayList<Reservation> reservations = new ArrayList<>();
         String query = "SELECT * FROM reservation";
         Reservation reservation;
         try (Connection connection= DatabaseConfig.connect()){
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);
             while (resultSet.next()){
                 reservation = new Reservation();
                 reservation.setId(resultSet.getInt("id"));
                 reservation.setRoomId(resultSet.getInt("room_id"));
                 reservation.setCustomerName(resultSet.getString("customer_name"));
                 reservation.setCustomerSurname(resultSet.getString("customer_surname"));
                 reservation.setCheckinDate(resultSet.getDate("check_in"));
                 reservation.setCheckoutDate(resultSet.getDate("check_out"));
                 reservation.setTotalPrice(resultSet.getDouble("total_price"));
                 reservations.add(reservation);
             }
         }catch (SQLException e){
             e.printStackTrace();
         }
         return reservations;
    }

    public static ArrayList<ResultDetails> searchRooms(String city, String hotelName, LocalDate checkIn, LocalDate checkOut) {
        String query = "SELECT r.id AS room_id, r.room_type, r.bed_count, r.stock, rp.adult_price, rp.child_price, h.name AS hotel_name, r.square_meters "
                + "FROM room r "
                + "INNER JOIN hotel h ON r.hotel_id = h.id "
                + "LEFT JOIN room_price rp ON r.id = rp.room_id "
                + "WHERE r.stock > 0 "
                + "AND h.address LIKE ? "
                + "AND h.name LIKE ? "
                + "AND rp.season_id IN ("
                + "    SELECT id FROM season WHERE start_date <= ? AND end_date >= ? "
                + ");";

        ArrayList<ResultDetails> resultList = new ArrayList<>();

        try (Connection connection = DatabaseConfig.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + city + "%");
            preparedStatement.setString(2, "%" + hotelName + "%");
            preparedStatement.setDate(3, Date.valueOf(checkIn));
            preparedStatement.setDate(4, Date.valueOf(checkOut));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                resultList.add(new ResultDetails(
                        resultSet.getInt("room_id"),
                        resultSet.getString("room_type"),
                        resultSet.getInt("bed_count"),
                        resultSet.getInt("stock"),
                        resultSet.getDouble("adult_price"),
                        resultSet.getDouble("child_price"),
                        resultSet.getString("hotel_name"),
                        resultSet.getDouble("square_meters")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static boolean add(Reservation reservation) {
        String query = "INSERT INTO reservation (room_id, customer_name, customer_surname, customer_identity_number, check_in, check_out, total_price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection connection = DatabaseConfig.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, reservation.getRoomId());
            preparedStatement.setString(2, reservation.getCustomerName());
            preparedStatement.setString(3, reservation.getCustomerSurname());
            preparedStatement.setString(4, reservation.getCustomerIdentityNo());
            preparedStatement.setDate(5, new java.sql.Date(reservation.getCheckinDate().getTime()));
            preparedStatement.setDate(6, new java.sql.Date(reservation.getCheckoutDate().getTime()));
            preparedStatement.setDouble(7, reservation.getTotalPrice());

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                reservation.setId(rs.getInt("id"));
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteById(int id){
        String query = "DELETE FROM reservation WHERE id = ?";
        Reservation reservation = null;
        try (Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() == 1;
        }catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return false;
    }

    public static void decreaseRoomStock(int roomId) {
        String query = "UPDATE room SET stock = stock - 1 WHERE id = ?";

        try (Connection connection = DatabaseConfig.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, roomId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double calculatePrice(RoomDetails room, double adults, double children, double nights) {
        double adultPrice = room.getAdultPrice();
        double childPrice = room.getChildPrice();
        return (adults * adultPrice + children * childPrice) * nights;
    }

}
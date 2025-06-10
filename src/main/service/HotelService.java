package main.service;

import main.helper.DatabaseConfig;
import main.helper.GUIHelper;
import main.model.hotel.BoardingHouseType;
import main.model.hotel.FacilityFeatures;
import main.model.hotel.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HotelService {

    public static ArrayList<Hotel> listAll(){
        ArrayList<Hotel> hotels = new ArrayList<>();
        String query = "SELECT * FROM hotel";
        Hotel hotel;
        try(Connection connection = DatabaseConfig.connect()){
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                hotel = new Hotel();
                hotel.setId(resultSet.getInt("id"));
                hotel.setName(resultSet.getString("name"));
                hotel.setAddress(resultSet.getString("address"));
                hotel.setEmail(resultSet.getString("email"));
                hotel.setPhoneNumber(resultSet.getString("phone_number"));
                hotel.setStar(resultSet.getString("star"));
                String boardingTypeStr = resultSet.getString("boarding_house_type");
                if (boardingTypeStr != null){
                    hotel.setBoardingHouseType(BoardingHouseType.valueOf(boardingTypeStr));
                }

                Array sqlArray = resultSet.getArray("facility_features");
                if (sqlArray != null) {
                    String[] featuresArray = (String[]) sqlArray.getArray();
                    List<FacilityFeatures> featuresList = Arrays.stream(featuresArray)
                            .map(FacilityFeatures::valueOf)
                            .collect(Collectors.toList());
                    hotel.setFacilityFeatures(featuresList);
                }

                hotels.add(hotel);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return hotels;
    }

    public static Hotel getHotelByEmail(String email){
        String query = "SELECT * FROM hotel WHERE email = ?";
        Hotel hotel = null;
        try(Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hotel = new Hotel();
                hotel.setId(resultSet.getInt("id"));
                hotel.setName(resultSet.getString("name"));
                hotel.setAddress(resultSet.getString("address"));
                hotel.setEmail(resultSet.getString("email"));
                hotel.setPhoneNumber(resultSet.getString("phone_number"));
                hotel.setStar(resultSet.getString("star"));
                String boardingTypeStr = resultSet.getString("boarding_house_type");
                if (boardingTypeStr != null){
                    hotel.setBoardingHouseType(BoardingHouseType.valueOf(boardingTypeStr));
                }

                Array sqlArray = resultSet.getArray("facility_features");
                if (sqlArray != null) {
                    String[] featuresArray = (String[]) sqlArray.getArray();
                    List<FacilityFeatures> featuresList = Arrays.stream(featuresArray)
                            .map(FacilityFeatures::valueOf)
                            .collect(Collectors.toList());
                    hotel.setFacilityFeatures(featuresList);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return hotel;
    }

    public static boolean add(Hotel hotel) {
        String query = "INSERT INTO hotel (name, address, email, phone_number, star, boarding_house_type, facility_features) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Hotel findHotel = getHotelByEmail(hotel.getEmail());

        if (findHotel != null && findHotel.getEmail().equals(hotel.getEmail())){
            GUIHelper.showMessage("This hotel with this email address used before");
            return false;
        }

        try (Connection connection = DatabaseConfig.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, hotel.getName());
            preparedStatement.setString(2, hotel.getAddress());
            preparedStatement.setString(3, hotel.getEmail());
            preparedStatement.setString(4, hotel.getPhoneNumber());
            preparedStatement.setString(5, hotel.getStar());

            preparedStatement.setObject(6, hotel.getBoardingHouseType(), java.sql.Types.OTHER);

            List<FacilityFeatures> featuresList = hotel.getFacilityFeatures();
            if (featuresList != null && !featuresList.isEmpty()) {
                String[] featuresArray = featuresList.stream()
                        .map(Enum::name)
                        .toArray(String[]::new);
                Array sqlArray = connection.createArrayOf("facility_features_enum", featuresArray);
                preparedStatement.setArray(7, sqlArray);
            } else {
                preparedStatement.setArray(7, null);
            }

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean update(Hotel hotel) {
        String query = "UPDATE hotel SET name = ?, address = ?, email = ?, phone_number = ?, star = ?, " +
                "boarding_house_type = ?, facility_features = ? WHERE id = ?";
        try (Connection connection = DatabaseConfig.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, hotel.getName());
            preparedStatement.setString(2, hotel.getAddress());
            preparedStatement.setString(3, hotel.getEmail());
            preparedStatement.setString(4, hotel.getPhoneNumber());
            preparedStatement.setString(5, hotel.getStar());

            preparedStatement.setObject(6, hotel.getBoardingHouseType(), java.sql.Types.OTHER);

            List<FacilityFeatures> featuresList = hotel.getFacilityFeatures();
            if (featuresList != null && !featuresList.isEmpty()) {
                String[] featuresArray = featuresList.stream()
                        .map(Enum::name)
                        .toArray(String[]::new);
                Array sqlArray = connection.createArrayOf("facility_features_enum", featuresArray);
                preparedStatement.setArray(7, sqlArray);
            } else {
                preparedStatement.setArray(7, null);
            }

            preparedStatement.setInt(8, hotel.getId());
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("SQL Error Code: " + e.getErrorCode());
            System.out.println("SQL State: " + e.getSQLState());
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<Hotel> findHotelByCityName(String cityName){
        String query = "SELECT * FROM hotel WHERE address ILIKE ?";
        ArrayList<Hotel> hotels = new ArrayList<>();
        Hotel hotel;
        try(Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + cityName + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hotel = new Hotel();
                hotel.setId(resultSet.getInt("id"));
                hotel.setName(resultSet.getString("name"));
                hotel.setAddress(resultSet.getString("address"));
                hotel.setEmail(resultSet.getString("email"));
                hotel.setPhoneNumber(resultSet.getString("phone_number"));
                hotel.setStar(resultSet.getString("star"));
                String boardingTypeStr = resultSet.getString("boarding_house_type");
                if (boardingTypeStr != null){
                    hotel.setBoardingHouseType(BoardingHouseType.valueOf(boardingTypeStr));
                }

                Array sqlArray = resultSet.getArray("facility_features");
                if (sqlArray != null) {
                    String[] featuresArray = (String[]) sqlArray.getArray();
                    List<FacilityFeatures> featuresList = Arrays.stream(featuresArray)
                            .map(FacilityFeatures::valueOf)
                            .collect(Collectors.toList());
                    hotel.setFacilityFeatures(featuresList);
                }

                hotels.add(hotel);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return hotels;
    }

    public static ArrayList<Hotel> findHotelByHotelName(String name){
        String query = "SELECT * FROM hotel WHERE name ILIKE ?";
        ArrayList<Hotel> hotels = new ArrayList<>();
        Hotel hotel;
        try (Connection connection = DatabaseConfig.connect()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + name + "%");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                hotel = new Hotel();
                hotel.setId(resultSet.getInt("id"));
                hotel.setName(resultSet.getString("name"));
                hotel.setAddress(resultSet.getString("address"));
                hotel.setEmail(resultSet.getString("email"));
                hotel.setPhoneNumber(resultSet.getString("phone_number"));
                hotel.setStar(resultSet.getString("star"));
                String boardingTypeStr = resultSet.getString("boarding_house_type");
                if (boardingTypeStr != null){
                    hotel.setBoardingHouseType(BoardingHouseType.valueOf(boardingTypeStr));
                }

                Array sqlArray = resultSet.getArray("facility_features");
                if (sqlArray != null) {
                    String[] featuresArray = (String[]) sqlArray.getArray();
                    List<FacilityFeatures> featuresList = Arrays.stream(featuresArray)
                            .map(FacilityFeatures::valueOf)
                            .collect(Collectors.toList());
                    hotel.setFacilityFeatures(featuresList);
                }

                hotels.add(hotel);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return hotels;

    }

    public static boolean deleteById(int id) {
        String query = "DELETE FROM hotel WHERE id = ?";
        try (Connection connection = DatabaseConfig.connect()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);

            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
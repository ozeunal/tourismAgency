package main.ui;

import main.helper.Constants;
import main.helper.GUIHelper;
import main.model.Reservation;
import main.model.ResultDetails;
import main.model.hotel.FacilityFeatures;
import main.model.hotel.Hotel;
import main.model.room.RoomDetails;
import main.service.HotelService;
import main.service.ReservationService;
import main.service.RoomService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class StaffGUI extends JFrame{
    private JPanel wrapper;
    private JPanel pnl_top;
    private JTabbedPane tab_hotel;
    private JTable tbl_hotel_list;
    private JButton btn_logout;
    private JTextField fld_hotel_name;
    private JButton btn_search_hotel;
    private JButton btn_add_hotel;
    private JButton addNewRoomButton;
    private JTable tbl_room_list;
    protected JTextField fld_rs_checkin_date;
    protected JTextField fld_rs_checkout_date;
    protected JTextField fld_rs_city_name;
    private JButton btn_rs_search_button;
    private JTable tbl_rs_result_list;
    private JButton btn_make_rs;
    private JComboBox cmb_searching_hotel_name;
    private JComboBox cmb_searching_city_name;
    private JTable tbl_reservation_list;
    private JTextField fld_reservation_id;
    private JButton btn_delete_reservation;
    private DefaultTableModel mdl_hotel_list;
    private Object[] row_hotel_list;
    private DefaultTableModel mdl_room_list;
    private Object[] row_room_list;
    private DefaultTableModel mdl_result_list;
    private Object[] row_result_list;
    private String selectedRowRoomId;
    private DefaultTableModel mdl_reservation_list;
    private Object[] row_reservation_list;

    public StaffGUI(){
        initializeGUI();

        setHotelTable();
        setRoomTable();
        setResultTable();
        setReservationTable();
        loadHotelModel(HotelService.listAll());
        loadRoomModel(RoomService.listAllDetails());
        loadReservationModel(ReservationService.listAll());

        initializeEvents();
    }

    private void initializeGUI() {
        GUIHelper.setLookAndFeel();
        add(wrapper);
        setSize(1500, 500);
        GUIHelper.centerFrame(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.WINDOW_TITLE_STAFF);
        setVisible(true);
        populateHotelsAndCities();
        fld_rs_checkin_date.setText("dd/mm/yyyy");
        fld_rs_checkin_date.setForeground(Color.GRAY);
        fld_rs_checkout_date.setText("dd/mm/yyyy");
        fld_rs_checkout_date.setForeground(Color.GRAY);
    }

    private void setHotelTable(){
        String[] columnHotelList = {"Id", "Name", "Address", "Email", "Phone Number", "Star", "Boarding House Type", "Facility Features"};
        mdl_hotel_list = GUIHelper.createCustomTableModel(columnHotelList, 0);
        row_hotel_list = new Object[columnHotelList.length];

        tbl_hotel_list.setModel(mdl_hotel_list);
        tbl_hotel_list.getTableHeader().setReorderingAllowed(false);
        tbl_hotel_list.getColumnModel().getColumn(0).setMaxWidth(40);
        tbl_hotel_list.getColumnModel().getColumn(5).setMaxWidth(40);
        tbl_hotel_list.getColumnModel().getColumn(4).setMaxWidth(150);
    }

    private void setRoomTable() {
        String[] columnRoomList = {
                "Id", "Hotel Id", "Room Type", "Bed Count", "Square Meters",
                "Stock", "Adult Price", "Child Price", "Features"
        };
        mdl_room_list = GUIHelper.createCustomTableModel(columnRoomList, 0);
        row_room_list = new Object[columnRoomList.length];

        tbl_room_list.setModel(mdl_room_list);
        tbl_room_list.getTableHeader().setReorderingAllowed(false);
        tbl_room_list.getColumnModel().getColumn(0).setMaxWidth(40);
        tbl_room_list.getColumnModel().getColumn(1).setMaxWidth(80);
    }

    private void setResultTable(){
        String[] columnResultList = {
                "Room ID", "Room Type", "Bed Count", "Stock",
                "Adult Price", "Child Price", "Hotel Name", "Square Meters"
        };
        mdl_result_list = GUIHelper.createCustomTableModel(columnResultList ,0);
        row_result_list = new Object[columnResultList.length];

        tbl_rs_result_list.setModel(mdl_result_list);
        tbl_rs_result_list.getTableHeader().setReorderingAllowed(false);
    }

    private void setReservationTable(){
        String[] columnReservationList = {
                "Id", "Room Id", "Customer Name", "Customer Surname", "Customer Identity Number",
                "Check-In Date", "Check-Out Date", "Total Price"
        };
        mdl_reservation_list = GUIHelper.createCustomTableModel(columnReservationList, 0);
        row_reservation_list = new Object[columnReservationList.length];

        tbl_reservation_list.setModel(mdl_reservation_list);
        tbl_reservation_list.getTableHeader().setReorderingAllowed(false);
    }


    private void initializeEvents(){
        btn_logout.addActionListener(e -> {
            dispose();
        });
        btn_search_hotel.addActionListener(e -> {
            String name = fld_hotel_name.getText();
            ArrayList<Hotel> search = HotelService.findHotelByHotelName(name);
            loadHotelModel(search);
            System.out.println("Search results size: " + search.size());
        });
        btn_add_hotel.addActionListener(e -> {
            AddHotelGUI addHotelGUI = new AddHotelGUI(this);
        });
        addNewRoomButton.addActionListener(e -> {
            AddRoomGUI addRoomGUI = new AddRoomGUI(this);
        });
        btn_rs_search_button.addActionListener(e -> {
            if (GUIHelper.isFieldEmpty(fld_rs_checkin_date) || GUIHelper.isFieldEmpty(fld_rs_checkout_date)
            || fld_rs_checkin_date.getText().equals("dd/mm/yyyy") || fld_rs_checkout_date.getText().equals("dd/mm/yyyy")){
                GUIHelper.showMessage("Please fill CheckIn and CheckOut dates");
            }
            else{
                String checkInString  = fld_rs_checkin_date.getText();
                String checkOutString  = fld_rs_checkout_date.getText();
                String hotelName = Objects.requireNonNull(cmb_searching_hotel_name.getSelectedItem()).toString();
                String cityName = Objects.requireNonNull(cmb_searching_city_name.getSelectedItem()).toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate checkInDate = LocalDate.parse(checkInString, formatter);
                LocalDate checkOutDate = LocalDate.parse(checkOutString, formatter);
                ArrayList<ResultDetails> roomDetailsList = ReservationService.searchRooms(cityName, hotelName, checkInDate, checkOutDate);

                loadResultModel(roomDetailsList);
            }
        });
        fld_rs_checkin_date.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fld_rs_checkin_date.getText().equals("dd/mm/yyyy")){
                    fld_rs_checkin_date.setText("");
                    fld_rs_checkin_date.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (fld_rs_checkin_date.getText().isEmpty()){
                    fld_rs_checkin_date.setText("dd/mm/yyyy");
                    fld_rs_checkin_date.setForeground(Color.GRAY);
                }
            }
        });
        fld_rs_checkout_date.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (fld_rs_checkout_date.getText().equals("dd/mm/yyyy")){
                    fld_rs_checkout_date.setText("");
                    fld_rs_checkout_date.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (fld_rs_checkout_date.getText().isEmpty()){
                    fld_rs_checkout_date.setText("dd/mm/yyyy");
                    fld_rs_checkout_date.setForeground(Color.GRAY);
                }
            }
        });
        tbl_rs_result_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                selectedRowRoomId = tbl_rs_result_list.getValueAt(tbl_rs_result_list.getSelectedRow(), 0).toString();
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        });
        cmb_searching_city_name.addActionListener(e -> {
            String selectedCity = (String) cmb_searching_city_name.getSelectedItem();
            ArrayList<Hotel> hotelsInCity = HotelService.findHotelByCityName(selectedCity);
            cmb_searching_hotel_name.removeAllItems();
            for (Hotel hotel : hotelsInCity){
                cmb_searching_hotel_name.addItem(hotel.getName());
            }

        });
        btn_make_rs.addActionListener(e -> {
            MakeReservationGUI makeReservationGUI = new MakeReservationGUI(selectedRowRoomId, fld_rs_checkin_date.getText(), fld_rs_checkout_date.getText(), this);
        });
        tbl_reservation_list.getSelectionModel().addListSelectionListener(e -> {
            try{
                String selectedRowReservationId = tbl_reservation_list.getValueAt(tbl_reservation_list.getSelectedRow(), 0).toString();
                fld_reservation_id.setText(selectedRowReservationId);
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        });
        btn_delete_reservation.addActionListener(e -> {
            if (GUIHelper.isFieldEmpty(fld_reservation_id)){
                GUIHelper.showMessage(Constants.MSG_FILL);
            }
            else{
                if (GUIHelper.confirm(Constants.MSG_SURE)){
                    if (ReservationService.deleteById(Integer.parseInt(fld_reservation_id.getText()))){
                        GUIHelper.showMessage(Constants.MSG_DONE);
                        loadReservationModel(ReservationService.listAll());
                    }
                    else{
                        GUIHelper.showMessage(Constants.MSG_ERROR);
                    }
                }
            }
        });
    }

    protected void loadHotelModel(ArrayList<Hotel> hotels) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_hotel_list.getModel();
        clearModel.setRowCount(0);
        hotels.forEach(this::addHotelToModel);
    }

    protected void loadRoomModel(ArrayList<RoomDetails> roomDetailsList) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_room_list.getModel();
        clearModel.setRowCount(0);
        roomDetailsList.forEach(this::addRoomToModel);
    }

    protected void loadResultModel(ArrayList<ResultDetails> resultDetailsList) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_rs_result_list.getModel();
        clearModel.setRowCount(0);
        resultDetailsList.forEach(this::addResultToModel);
    }

    protected void loadReservationModel(ArrayList<Reservation> reservations){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_reservation_list.getModel();
        clearModel.setRowCount(0);
        reservations.forEach(this::addReservationToModel);
    }

    private void addHotelToModel(Hotel hotel) {
        DefaultTableModel model = (DefaultTableModel) tbl_hotel_list.getModel();
        Object[] row = {
                hotel.getId(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getEmail(),
                hotel.getPhoneNumber(),
                hotel.getStar(),
                formatEnum(hotel.getBoardingHouseType()),
                formatFeatures(hotel.getFacilityFeatures())
        };
        model.addRow(row);
    }

    private void addRoomToModel(RoomDetails roomDetails) {
        DefaultTableModel model = (DefaultTableModel) tbl_room_list.getModel();
        Object[] row = {
                roomDetails.getRoomId(),
                roomDetails.getHotelId(),
                roomDetails.getRoomType(),
                roomDetails.getBedCount(),
                roomDetails.getSquareMeters(),
                roomDetails.getStock(),
                roomDetails.getAdultPrice(),
                roomDetails.getChildPrice(),
                String.join(", ", roomDetails.getRoomFeatures()) // Listeyi virgülle birleştir
        };
        model.addRow(row);
    }

    private void addResultToModel(ResultDetails resultDetails) {
        DefaultTableModel model = (DefaultTableModel) tbl_rs_result_list.getModel();
        Object[] row = {
                resultDetails.getRoomId(),
                resultDetails.getRoomType(),
                resultDetails.getBedCount(),
                resultDetails.getStock(),
                resultDetails.getAdultPrice(),
                resultDetails.getChildPrice(),
                resultDetails.getHotelName(),
                resultDetails.getSquareMeters()
        };
        model.addRow(row);
    }

    private void addReservationToModel(Reservation reservation){
        DefaultTableModel model = (DefaultTableModel) tbl_reservation_list.getModel();
        Object[] row = {
                reservation.getId(),
                reservation.getRoomId(),
                reservation.getCustomerName(),
                reservation.getCustomerSurname(),
                reservation.getCustomerIdentityNo(),
                reservation.getCheckinDate(),
                reservation.getCheckoutDate(),
                reservation.getTotalPrice()
        };
        model.addRow(row);
    }

    private void populateHotelsAndCities(){
        cmb_searching_hotel_name.addItem("");
        cmb_searching_city_name.addItem("");
        ArrayList<Hotel> hotels = HotelService.listAll();
        String cityName = null;
        for (Hotel hotel : hotels){
            cmb_searching_hotel_name.addItem(hotel.getName());
            cityName = hotel.getAddress().split(",")[0];
            cmb_searching_city_name.addItem(cityName);
        }
    }


    private String formatEnum(Enum<?> enumValue) {
        String rawName = enumValue.name().toLowerCase().replace("_", " ");
        return Character.toUpperCase(rawName.charAt(0)) + rawName.substring(1);
    }

    private String formatFeatures(List<FacilityFeatures> features) {
        return features.stream()
                .map(this::formatEnum)
                .collect(Collectors.joining(", "));
    }

}

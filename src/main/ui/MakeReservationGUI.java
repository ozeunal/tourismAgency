package main.ui;

import main.helper.Constants;
import main.helper.GUIHelper;
import main.model.Reservation;
import main.model.room.Room;
import main.model.room.RoomDetails;
import main.service.ReservationService;
import main.service.RoomService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class MakeReservationGUI extends JFrame{
    private JPanel panel1;
    private JPanel wrapper;
    private JTextField fld_customer_name;
    private JTextField fld_customer_surname;
    private JTextField fld_customer_id_no;
    private JTextField fld_check_in;
    private JTextField fld_check_out;
    private JTextField fld_number_of_adult;
    private JTextField fld_number_of_child;
    private JTextField fld_total_price;
    private JButton btn_make_reservation;
    private StaffGUI staffGUI;
    private String roomId;
    private int nights;
    private double totalPrice;
    private RoomDetails roomDetails;

    public MakeReservationGUI(String roomId, String checkIn, String checkOut, StaffGUI staffGUI){
        this.staffGUI = staffGUI;
        this.roomId = roomId;
        initializeGUI();
        initializeEvents();
        fld_check_in.setText(checkIn);
        fld_check_out.setText(checkOut);
        fld_number_of_child.setText("0");
        fld_number_of_adult.setText("0");
    }

    public void initializeGUI(){
        GUIHelper.setLookAndFeel();
        add(wrapper);
        setSize(400, 400);
        GUIHelper.centerFrame(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.WINDOW_TITLE_STAFF);
        setVisible(true);
    }

    public void initializeEvents(){
        btn_make_reservation.addActionListener(e -> {
            String customerName = fld_customer_name.getText();
            String customerSurname = fld_customer_surname.getText();
            String customerIdentityNumber = fld_customer_id_no.getText();
            String checkInString = fld_check_in.getText();
            String checkOutString = fld_check_out.getText();
            double numberOfAdult = Double.parseDouble(fld_number_of_adult.getText());
            double numberOfChildren = Double.parseDouble(fld_number_of_child.getText());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate checkInDate = LocalDate.parse(checkInString, formatter);
            LocalDate checkOutDate = LocalDate.parse(checkOutString, formatter);

            nights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            roomDetails = RoomService.getRoomDetailsById(Integer.parseInt(roomId));
            totalPrice = ReservationService.calculatePrice(roomDetails, numberOfAdult, numberOfChildren, nights);


            Reservation reservation = new Reservation();
            reservation.setRoomId(Integer.parseInt(roomId));
            reservation.setCustomerName(customerName);
            reservation.setCustomerSurname(customerSurname);
            reservation.setCustomerIdentityNo(customerIdentityNumber);
            reservation.setCheckinDate(java.sql.Date.valueOf(checkInDate));
            reservation.setCheckoutDate(java.sql.Date.valueOf(checkOutDate));
            reservation.setTotalPrice(totalPrice);

            if (GUIHelper.isFieldEmpty(fld_customer_name) || GUIHelper.isFieldEmpty(fld_customer_surname)
            || GUIHelper.isFieldEmpty(fld_customer_id_no) || GUIHelper.isFieldEmpty(fld_number_of_adult)){
                GUIHelper.showMessage(Constants.MSG_FILL);
            }
            else{
                if (GUIHelper.confirm(Constants.MSG_SURE)){
                    if (ReservationService.add(reservation)){
                        GUIHelper.showMessage(Constants.MSG_DONE);
                        ReservationService.decreaseRoomStock(reservation.getRoomId());
                        staffGUI.loadReservationModel(ReservationService.listAll());
                        dispose();
                    }
                    else{
                        GUIHelper.showMessage(Constants.MSG_ERROR);
                    }
                }
            }
        });
        fld_number_of_adult.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotalPrice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotalPrice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTotalPrice();
            }
        });
        fld_number_of_child.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTotalPrice();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTotalPrice();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTotalPrice();
            }
        });
    }

    private void updateTotalPrice(){
        try {
            String checkInString = fld_check_in.getText();
            String checkOutString = fld_check_out.getText();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate checkInDate = LocalDate.parse(checkInString, formatter);
            LocalDate checkOutDate = LocalDate.parse(checkOutString, formatter);

            nights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
            roomDetails = RoomService.getRoomDetailsById(Integer.parseInt(roomId));

            double numberOfAdults = Double.parseDouble(fld_number_of_adult.getText());
            double numberOfChildren = Double.parseDouble(fld_number_of_child.getText());

            double totalPrice = ReservationService.calculatePrice(roomDetails, numberOfAdults, numberOfChildren, nights);
            fld_total_price.setText(String.format("%.2f", totalPrice));
        }catch (NumberFormatException e){
            fld_total_price.setText("");
        }
    }

}

package main.ui;

import main.helper.Constants;
import main.helper.GUIHelper;
import main.model.hotel.BoardingHouseType;
import main.model.hotel.FacilityFeatures;
import main.model.hotel.Hotel;
import main.service.HotelService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AddHotelGUI extends JFrame{
    private JPanel wrapper;
    private JTextField fld_hotel_name;
    private JTextField fld_address;
    private JTextField fld_email;
    private JTextField fld_phone_number;
    private JTextField fld_star;
    private JComboBox cmb_boarding_type;
    private JCheckBox chkbox_parking;
    private JCheckBox chkbox_wifi;
    private JCheckBox chkbox_pool;
    private JCheckBox chkbox_fittness;
    private JCheckBox chkbox_SPA;
    private JCheckBox chkbox_concierge;
    private JCheckBox chkbox_roomservice;
    private JButton btn_add;
    private JButton btn_cancel;
    private StaffGUI staffGUI;

    public AddHotelGUI(StaffGUI staffGUI){
        this.staffGUI = staffGUI;
        initializeGUI();
        initializeEvents();
    }

    private void initializeGUI() {
        add(wrapper);
        setSize(500, 400);
        GUIHelper.centerFrame(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.WINDOW_TITLE_STAFF);
        setVisible(true);

        for (BoardingHouseType type : BoardingHouseType.values()){
            cmb_boarding_type.addItem(formatEnumName(type));
        }
    }

    private void initializeEvents(){
        btn_cancel.addActionListener(e -> {
            dispose();
        });

        btn_add.addActionListener(e -> {
            int selectedCheckBox = 0;
            Hotel hotel = new Hotel();
            hotel.setName(fld_hotel_name.getText());
            hotel.setAddress(fld_address.getText());
            hotel.setEmail(fld_email.getText());
            hotel.setPhoneNumber(fld_phone_number.getText());
            hotel.setStar(fld_star.getText());

            String selectedFormattedName = cmb_boarding_type.getSelectedItem().toString();
            BoardingHouseType selectedType = getEnumFromFormattedName(selectedFormattedName);
            hotel.setBoardingHouseType(selectedType);

            List<FacilityFeatures> selectedFeatures = new ArrayList<>();
            if (chkbox_parking.isSelected()) {
                selectedFeatures.add(FacilityFeatures.PARKING);
                selectedCheckBox++;
            }
            if (chkbox_wifi.isSelected()) {
                selectedFeatures.add(FacilityFeatures.WIFI);
                selectedCheckBox++;
            }
            if (chkbox_pool.isSelected()) {
                selectedFeatures.add(FacilityFeatures.SWIMMING_POOL);
                selectedCheckBox++;
            }
            if (chkbox_fittness.isSelected()) {
                selectedFeatures.add(FacilityFeatures.FITNESS_CENTER);
                selectedCheckBox++;
            }
            if (chkbox_SPA.isSelected()) {
                selectedFeatures.add(FacilityFeatures.SPA);
                selectedCheckBox++;
            }
            if (chkbox_concierge.isSelected()) {
                selectedFeatures.add(FacilityFeatures.HOTEL_CONCIERGE);
                selectedCheckBox++;
            }
            if (chkbox_roomservice.isSelected()) {
                selectedFeatures.add(FacilityFeatures.ROOM_SERVICE);
                selectedCheckBox++;
            }

            hotel.setFacilityFeatures(selectedFeatures);

            if (fld_hotel_name.getText() == null || fld_address.getText() == null || fld_email.getText() == null
                    || fld_star.getText() == null || fld_phone_number.getText() == null || selectedCheckBox == 0){
                GUIHelper.showMessage(Constants.MSG_FILL);
            }
            else{
                if (GUIHelper.confirm(Constants.MSG_SURE)){
                    if (HotelService.add(hotel)){
                        GUIHelper.showMessage(Constants.MSG_DONE);
                    }
                    else{
                        GUIHelper.showMessage(Constants.MSG_ERROR);
                    }
                }

            }
            fld_hotel_name.setText(null);
            fld_address.setText(null);
            fld_email.setText(null);
            fld_star.setText(null);
            fld_phone_number.setText(null);
            staffGUI.loadHotelModel(HotelService.listAll());
        });

    }

    private String formatEnumName(Enum<?> enumValue) {
        String rawName = enumValue.name().toLowerCase().replace("_", " ");
        return Character.toUpperCase(rawName.charAt(0)) + rawName.substring(1);
    }

    private BoardingHouseType getEnumFromFormattedName(String formattedName) {
        for (BoardingHouseType type : BoardingHouseType.values()) {
            String formatted = formatEnumName(type);
            if (formatted.equals(formattedName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid BoardingHouseType: " + formattedName);
    }
}

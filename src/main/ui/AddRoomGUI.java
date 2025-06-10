package main.ui;

import main.helper.Constants;
import main.helper.GUIHelper;
import main.model.room.Room;
import main.model.room.RoomFeatures;
import main.model.room.RoomFeaturesEnum;
import main.model.room.RoomPrice;
import main.service.RoomFeaturesService;
import main.service.RoomPriceService;
import main.service.RoomService;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AddRoomGUI extends JFrame{
    private JPanel panel1;
    private JPanel wrapper;
    private JTextField fld_hotel_id;
    private JTextField fld_bed_count;
    private JTextField fld_sqr_meters;
    private JTextField fld_stock;
    private JButton addButton;
    private JButton cancelButton;
    private JComboBox cmb_room_type;
    private JComboBox cmb_seasons;
    private JTextField fld_adult_price;
    private JTextField fld_child_price;
    private JButton setPriceForSelectedButton;
    private JTextField fld_room_id;
    private JCheckBox chck_tv;
    private JCheckBox chck_minibar;
    private JCheckBox chck_game_console;
    private JCheckBox chck_safe;
    private JCheckBox chck_projector;
    private StaffGUI staffGUI;

    public AddRoomGUI(StaffGUI staffGUI){
        this.staffGUI = staffGUI;
        initializeGUI();
        initializeEvents();

    }

    private void initializeGUI() {
        GUIHelper.setLookAndFeel();
        add(wrapper);
        setSize(600, 500);
        GUIHelper.centerFrame(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.WINDOW_TITLE_STAFF);
        setVisible(true);
    }

    private void initializeEvents() {
        cancelButton.addActionListener(e -> {
            dispose();
        });
        addButton.addActionListener(e -> {
            Room room = new Room();
            RoomFeatures roomFeatures = new RoomFeatures();


            if (fld_hotel_id.getText().isEmpty() || fld_bed_count.getText().isEmpty()
                    || fld_sqr_meters.getText().isEmpty() || fld_stock.getText().isEmpty()){
                GUIHelper.showMessage(Constants.MSG_FILL);
            }
            else{
                room.setHotelId(Integer.parseInt(fld_hotel_id.getText()));
                room.setRoomType(cmb_room_type.getSelectedItem().toString());
                room.setBedCount(Integer.parseInt(fld_bed_count.getText()));
                room.setSquareMeters(Integer.parseInt(fld_sqr_meters.getText()));
                room.setStock(Integer.parseInt(fld_stock.getText()));

                List<RoomFeaturesEnum> selectedRoomFeatures = new ArrayList<>();

                if (chck_tv.isSelected()){
                    selectedRoomFeatures.add(RoomFeaturesEnum.TELEVISION);
                }
                if (chck_minibar.isSelected()){
                    selectedRoomFeatures.add(RoomFeaturesEnum.MINIBAR);
                }
                if (chck_game_console.isSelected()){
                    selectedRoomFeatures.add(RoomFeaturesEnum.GAME_CONSOLE);
                }
                if (chck_safe.isSelected()){
                    selectedRoomFeatures.add(RoomFeaturesEnum.SAFE);
                }
                if (chck_projector.isSelected()){
                    selectedRoomFeatures.add(RoomFeaturesEnum.PROJECTOR);
                }

                if (GUIHelper.confirm(Constants.MSG_SURE)){
                    if (RoomService.add(room)){
                        GUIHelper.showMessage(Constants.MSG_DONE);
                        roomFeatures.setRoomFeatures(selectedRoomFeatures);
                        roomFeatures.setRoomId(room.getId());
                        RoomFeaturesService.add(roomFeatures);
                    }
                    else{
                        GUIHelper.showMessage(Constants.MSG_ERROR);
                    }
                }
            }

            fld_hotel_id.setText(null);
            fld_bed_count.setText(null);
            fld_sqr_meters.setText(null);
            fld_stock.setText(null);

            staffGUI.loadRoomModel(RoomService.listAllDetails());
        });

        setPriceForSelectedButton.addActionListener(e -> {
            if (fld_adult_price.getText().isEmpty() || fld_child_price.getText().isEmpty()){
                GUIHelper.showMessage(Constants.MSG_FILL);
            }
            else{
                String seasonName = cmb_seasons.getSelectedItem().toString();
                int seasonId = 1;
                if (seasonName.equals("Winter")){
                    seasonId = 2;
                }

                RoomPrice roomPrice = new RoomPrice();
                roomPrice.setRoomId(Integer.parseInt(fld_room_id.getText()));
                roomPrice.setSeasonId(seasonId);
                roomPrice.setAdultPrice(Double.parseDouble(fld_adult_price.getText()));
                roomPrice.setChildPrice(Double.parseDouble(fld_child_price.getText()));

                if (GUIHelper.confirm(Constants.MSG_DONE)){
                    if (RoomPriceService.add(roomPrice, Integer.parseInt(fld_room_id.getText()), seasonName)){
                        GUIHelper.showMessage(Constants.MSG_DONE);
                    }
                    else{
                        GUIHelper.showMessage(Constants.MSG_ERROR);
                    }
                }
                fld_adult_price.setText(null);
                fld_child_price.setText(null);

            }
        });
    }
}

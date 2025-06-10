package main.ui;

import main.helper.Constants;
import main.helper.GUIHelper;
import main.model.User;
import main.service.UserService;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AdminGUI extends JFrame {
    private JPanel wrapper;
    private JLabel lbl_welcome;
    private JButton btn_logout;
    private JTable tbl_user_list;
    private JPanel pnl_user_list;
    private JPanel pnl_filter_role;
    private JComboBox cmb_search_user_role;
    private JButton btn_listbyrole_user;
    private JTextField fld_name;
    private JTextField fld_surname;
    private JTextField fld_username;
    private JPasswordField fld_password;
    private JButton btn_add_user;
    private JTextField fld_delete_id;
    private JButton btn_delete;
    private JComboBox cmb_role;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;

    public AdminGUI() {
        GUIHelper.setLookAndFeel();
        initializeGUI();

        String[] columnUserList = {"Id", "Name", "Surname", "User Name", "Role"};
        mdl_user_list = GUIHelper.createCustomTableModel(columnUserList, 0);
        row_user_list = new Object[columnUserList.length];

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        loadUserModel(Constants.ROLES_ALL);
        initializeEvents();

    }

    private void loadUserModel(String roleFilter){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);

        List<User> users;

        if (roleFilter == null || roleFilter.equals("All Roles")){
            users = UserService.listAll();
        }
        else {
            users = UserService.getUsersByRole(roleFilter);
        }

        for (User user : users){
            int i = 0;
            row_user_list[i++] = user.getId();
            row_user_list[i++] = user.getName();
            row_user_list[i++] = user.getSurname();
            row_user_list[i++] = user.getUserName();
            row_user_list[i++] = user.getRole();
            mdl_user_list.addRow(row_user_list);
        }
    }

    private void initializeGUI() {
        add(wrapper);
        setSize(1000, 500);
        GUIHelper.centerFrame(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.WINDOW_TITLE_ADMIN);
        populateRoles();
        setVisible(true);
    }

    private void initializeEvents() {
        btn_logout.addActionListener(e -> dispose());

        btn_add_user.addActionListener(e -> {
            if (GUIHelper.isFieldEmpty(fld_name) || GUIHelper.isFieldEmpty(fld_surname) ||GUIHelper.isFieldEmpty(fld_username) ||
                GUIHelper.isFieldEmpty(fld_password)){
                GUIHelper.showMessage(Constants.MSG_FILL);
            }
            else{
                String name = fld_name.getText();
                String surname = fld_surname.getText();
                String userName = fld_username.getText();
                char[] password = fld_password.getPassword();
                String passwordString = new String(password);
                String role = cmb_role.getSelectedItem().toString();
                java.util.Arrays.fill(password, '\0');
                if (UserService.add(name, surname, userName, passwordString, role)){
                    GUIHelper.showMessage(Constants.MSG_DONE);
                    loadUserModel(Constants.ROLES_ALL);
                    fld_name.setText(null);
                    fld_surname.setText(null);
                    fld_username.setText(null);
                    fld_password.setText(null);
                }
            }
        });

        btn_listbyrole_user.addActionListener(e -> {
            String role = cmb_search_user_role.getSelectedItem().toString();
            loadUserModel(role);
        });

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try{
                String selectedRowUserId = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                fld_delete_id.setText(selectedRowUserId);
            }catch (Exception exception){
                System.out.println(exception.getMessage());
            }
        });

        btn_delete.addActionListener(e -> {
            if (GUIHelper.isFieldEmpty(fld_delete_id)){
                GUIHelper.showMessage(Constants.MSG_FILL);
            }
            else{
                if (GUIHelper.confirm(Constants.MSG_SURE)){
                    int userId = Integer.parseInt(fld_delete_id.getText());
                    if (UserService.deleteById(userId)){
                        GUIHelper.showMessage(Constants.MSG_DONE);
                        loadUserModel(Constants.ROLES_ALL);
                        fld_delete_id.setText(null);
                    }
                }
                else{
                    GUIHelper.showMessage(Constants.MSG_ERROR);
                }
            }
        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            System.out.println("Event triggered: " + e.getType());
            if (e.getType() == TableModelEvent.UPDATE){
                int id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String surname = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String userName = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String role = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();

                if (UserService.update(id, name, surname, userName, role)){
                    GUIHelper.showMessage(Constants.MSG_DONE);
                }
                loadUserModel(Constants.ROLES_ALL);
            }
        });

    }

    private void populateRoles() {
        cmb_search_user_role.addItem(Constants.ROLES_ALL);
        List<String> roles = UserService.getUserRoles();
        for (String role : roles) {
            cmb_role.addItem(role);
            cmb_search_user_role.addItem(role);
        }
    }
}

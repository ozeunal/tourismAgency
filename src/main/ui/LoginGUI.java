package main.ui;

import main.helper.Constants;
import main.helper.GUIHelper;
import main.model.User;
import main.service.UserService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class LoginGUI extends JFrame{
    private JPanel panel1;
    private JTextField fld_user_name;
    private JPasswordField fld_password;
    private JButton loginButton;
    private JPanel wrapper;

    public LoginGUI(){
        initializeGUI();
        initializeEvents();
    }

    public void initializeGUI(){
        add(wrapper);
        setSize(400, 400);
        GUIHelper.centerFrame(this);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Constants.WINDOW_TITLE_LOGIN);
        setVisible(true);
    }

    public void initializeEvents(){
        loginButton.addActionListener(e -> {
            if (GUIHelper.isFieldEmpty(fld_user_name) ||GUIHelper.isFieldEmpty(fld_password)){
                GUIHelper.showMessage(Constants.MSG_FILL);
            }
            else{
                User user = UserService.getUserByUserName(fld_user_name.getText());
                char[] inputPassword = fld_password.getPassword();

                if (user == null) {
                    GUIHelper.showMessage("Username or Password is invalid");
                } else if (!Arrays.equals(user.getPassword().toCharArray(), inputPassword)) {
                    GUIHelper.showMessage("Username or Password is invalid");
                } else {
                    Arrays.fill(inputPassword, '\0');
                    if (user.getRole().equals(Constants.ROLES_ADMIN)) {
                        fld_user_name.setText("");
                        fld_password.setText("");
                        AdminGUI adminGUI = new AdminGUI();
                    }
                    if (user.getRole().equals(Constants.ROLES_STAFF)) {
                        fld_user_name.setText("");
                        fld_password.setText("");
                        StaffGUI staffGUI = new StaffGUI();
                    }
                }
                Arrays.fill(inputPassword, '\0');
            }
        });
    }

    public static void main(String[] args) {
        GUIHelper.setLookAndFeel();
        LoginGUI loginGUI = new LoginGUI();
    }
}

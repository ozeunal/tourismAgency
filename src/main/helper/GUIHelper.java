package main.helper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GUIHelper {

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            throw new RuntimeException("Failed to set Look and Feel", e);
        }
    }

    public static void centerFrame(Window frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation(
                (screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2
        );
    }

    public static DefaultTableModel createCustomTableModel(String[] columns, int nonEditableColumnIndex) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != nonEditableColumnIndex;
            }
        };
    }

    public static boolean isFieldEmpty(JTextField jTextField){
        return jTextField.getText().trim().isEmpty();
    }

    public static void showMessage(String s){
        String message;
        String title = switch (s) {
            case "fill" -> {
                message = "Please fill in all fields";
                yield "Error";
            }
            case "done" -> {
                message = "Transaction successful";
                yield "Result";
            }
            case "error" -> {
                message = "An error occurred";
                yield "Error";
            }
            default -> {
                message = s;
                yield "";
            }
        };

        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm(String str){
        String msg;
        switch (str){
            case "sure":
                msg = "Are you sure ?";
                break;
            default:
                msg = str;
                break;
        }
        return JOptionPane.showConfirmDialog(null, msg, "Last Decision?", JOptionPane.YES_NO_OPTION) == 0;
    }

}

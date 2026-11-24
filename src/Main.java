package studentregistrationsystem;

import java.awt.Font;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {

        // Smoother text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Make dialogs and tooltips use the same font as the rest of the app
        Font body = Theme.font(Font.PLAIN, 13);
        Font bold = Theme.font(Font.BOLD, 13);

        UIManager.put("OptionPane.messageFont", body);
        UIManager.put("OptionPane.buttonFont", bold);
        UIManager.put("ToolTip.font", Theme.font(Font.PLAIN, 12));

        SwingUtilities.invokeLater(() -> {

            StudentRegistrationForm application =
                    new StudentRegistrationForm();

            application.setVisible(true);
        });
    }
}
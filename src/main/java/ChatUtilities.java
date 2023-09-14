import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ChatUtilities {
    public static String getActualTime() {
        LocalTime time = LocalTime.now();
        return time.format(DateTimeFormatter.ofPattern("h:mm:ss a"));
    }

    // Cambiar el diseño del ScrollBar
    static class CustomScrollBarUI extends BasicScrollBarUI {
        private Color scrollBarColor = Styles.offOrange;

        @Override
        protected void configureScrollBarColors() {
            thumbColor = scrollBarColor;
            thumbDarkShadowColor = scrollBarColor.darker();
            thumbHighlightColor = scrollBarColor.brighter();
            thumbLightShadowColor = scrollBarColor;
            trackColor = Styles.boneWhite;
            trackHighlightColor = Color.WHITE;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            Dimension zeroDim = new Dimension(0, 0);
            button.setPreferredSize(zeroDim);
            button.setMinimumSize(zeroDim);
            button.setMaximumSize(zeroDim);
            return button;
        }
    }

    public static String formatMessage(String history, String message, String author, boolean rightAlign){
        String align = rightAlign ? "right" : "left";
        String out_message = history + // Mensajes anteriores
                "<div style='font-family:\"Product Sans\",Roboto, Helvetica;" +
                " font-size: 15px; text-align: " + align +"; margin-left: 15px'>" + // Alineación a la derecha
                "<p style='color: #a4a6ad; font-size: 13px;'><i>" +
                author +
                "</i></p>" +
                message + // Texto enviado
                "<span style='color: #a4a6ad; font-size: 10px; padding-left: 10px;'><i><br>" +
                " a las " + ChatUtilities.getActualTime() + // Hora
                "</i></span>" +
                "</div>" +
                "<div style='color: #fffff2; font-size: 4px'>" +
                new String(new char[167]).replace("\0", "-") + // Espacios
                "</div>";

        out_message = out_message.replace("<html>", "");
        out_message = out_message.replace("</html>", "");

        return "<html>" + out_message + "</html>";

    }

}

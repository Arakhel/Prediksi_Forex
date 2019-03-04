import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class Prediksi {
    private JButton button1;
    private JTextArea iterasiTextArea;
    private JButton pilihButton;
    private JButton pembelajaranButton;
    private JTextPane jTextPane2;
    private JTextPane jTextPane3;
    private JTextPane jTextPane4;
    private JTextPane jTextPane5;
    private JTextPane jTextPane1;
    private JTextArea prediksiHinggaTextArea;
    private JTextPane jTextPane6;
    private JTextArea hariKeDepanTextArea;
    private JButton prediksiButton;
    private JButton visualisasiButton;
    private JTextPane jTextPane7;

    public Prediksi() {
        pembelajaranButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iterasi = Integer.parseInt(jTextPane1.getText());
                int populasi = Integer.parseInt(jTextPane2.getText());
                int jumlahgen = Integer.parseInt(jTextPane3.getText());
                double pc = Double.parseDouble(jTextPane4.getText());
                double pm = Double.parseDouble(jTextPane5.getText());
                double alfa = 0.3;
                final int type = Integer.parseInt(jTextPane7.getText());
                DecimalFormat numberFormat = new DecimalFormat("#0.00000000");
                DecimalFormat numberFormat2 = new DecimalFormat("#0.00000000000000");

                Double[] matauang = null;
                try{
                    matauang = new Double[Data.InputData(type).length];
                }
            }
        });
    }
}

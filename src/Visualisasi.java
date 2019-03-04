import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class Visualisasi extends JFrame {
    public Visualisasi(int x) throws FileNotFoundException, IOException, ParseException {
        super("Indeks Harga");
        JPanel chartPanel = createChartPanel(x);
        add(chartPanel, BorderLayout.CENTER);
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private JPanel createChartPanel(int x) throws FileNotFoundException, IOException, ParseException {
        String chartTitle;
        String yAxisLabel;
        chartTitle = "Indeks harga EUR/USD";
        yAxisLabel = "Indeks harga USD tiap : EUR";

        String xAxisLabel = "Tanggal";
        XYDataset dataset = createDataset(x);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, xAxisLabel, yAxisLabel, dataset, true, true, false);

        return new ChartPanel(chart);
    }

    private XYDataset createDataset(int x) throws FileNotFoundException, IOException, ParseException {
        String hasil;
        String data;
        hasil = "EURUSDpredict.csv";

        TimeSeries series1 = new TimeSeries("Data");
        TimeSeries series2 = new TimeSeries("Prediksi");

        BufferedReader tgl = new BufferedReader(new FileReader(hasil));
        String splitBy = ",";
        String line = tgl.readLine();
        String value1;
        Double value2, value3;

        List<String> datax = new ArrayList<>();
        List<Double> datay = new ArrayList<>();
        List<Double> dataz = new ArrayList<>();
        while ((line = tgl.readLine()) != null) {
            String[] a = line.split(splitBy);
            value1 = String.valueOf(a[0]);
            value2 = Double.valueOf(a[1]);
            value3 = Double.valueOf(a[2]);
            datax.add(value1);
            datay.add(value2);
            dataz.add(value3);
        }
        tgl.close();

        String[] tanggal = null;
        Double[] uangpredict = null;
        Double[] uangdata = null;
        tanggal = datax.toArray(new String[0]);
        uangpredict = datay.toArray(new Double[0]);
        uangdata = datax.toArray(new Double[0]);

        int n = tanggal.length;
        Date[] date = new Date[n];
        Double[] d = new Double[n];
        Double[] m = new Double[n];
        Double[] y = new Double[n];
        SimpleDateFormat format = new SimpleDateFormat("yyy.MM.dd");
        SimpleDateFormat formatd = new SimpleDateFormat("dd,MM,yyyy");
        for (int i = 0; i < n; i++) {
            date[i] = format.parse(tanggal[i]);
        }

        for (int i = 0; i < n; i++) {
            if (uangdata[i] != 0) {
                series1.add(new Day(date[i]), uangdata[i]);
            }
            if (uangpredict[i] != 0) {
                series2.add(new Day(date[i]), uangpredict[i]);
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);

        return dataset;
    }
}

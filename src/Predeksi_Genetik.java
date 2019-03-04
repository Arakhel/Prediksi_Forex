import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Predeksi_Genetik {
    public static void WriteBest(Double[] Best, int jumlahgen, double fitness, int type) {
        try {
            String simpan;
            simpan = "EURUSDbest.csv";

            Double fitnes[] = null;
            BufferedReader cekbest = new BufferedReader(new FileReader(simpan));
            String splitBy = ",";
            String line = cekbest.readLine();
            Double value = 0.0;
            while ((line = cekbest.readLine()) != null) {
                String[] a = line.split(splitBy);
                value = Double.valueOf(a[1]);
            }
            cekbest.close();

            if (value < fitness) {
                FileWriter writer = new FileWriter(simpan, true);
                writer.append('\n');
                writer.append(Double.toString(jumlahgen));
                writer.append(',');
                writer.append(Double.toString(fitness));
                for (int i = 0; i < jumlahgen; i++) {
                    writer.append(',');
                    writer.append(Double.toString(Best[i]));
                }
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Double[] Kromget(int type) throws FileNotFoundException, IOException {
        String simpan;
        simpan = "EURUSDbest.csv";

        BufferedReader best = new BufferedReader(new FileReader(simpan));
        Double KromosomBest[] = null;
        String splitBy = ",";
        String tmp;
        String line = "";
        Double value;

        List<Double> jenis = new ArrayList<>();
        while ((tmp = best.readLine()) != null) {
            line = tmp;
        }
        String[] a = line.split(splitBy);
        for (int i = 2; i < a.length; i++) {
            value = Double.valueOf(a[i]);
            jenis.add(value);
        }

        best.close();
        KromosomBest = jenis.toArray(new Double[0]);
        return KromosomBest;
    }

    public static void Prediksi(int type, Double[] KromosomBest, Double[] matauang, int range) throws IOException, ParseException {
        String hasil;
        String data;
        hasil = "EURUSDpredict.csv";
        data = "EURUSD2016.csv";

        BufferedReader tgl = new BufferedReader(new FileReader(data));
        String tanggal[] = null;
        String splitBy = ",";
        String line = tgl.readLine();
        String value;

        List<String> kolom = new ArrayList<>();
        while ((line = tgl.readLine()) != null) {
            String[] a = line.split(splitBy);
            value = String.valueOf(a[0]);
            kolom.add(value);
        }
        tgl.close();
        tanggal = kolom.toArray(new String[0]);

        int n = matauang.length + range;
        int pgen = KromosomBest.length;
        double[] prediksi = new double[n];
        double[] different = new double[n];
        double[] error = new double[n];
        double SE = 0;
        double E = 0;
        double total = 0;
        double MSE;
        double MAPD;
        Date[] date = new Date[n];
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");

        for (int i = 0; i < pgen; i++) {
            prediksi[i] = 0;
            different[i] = 0;
            error[i] = 0;
            date[i] = format.parse(tanggal[i]);
        }

        for (int i = pgen; i < matauang.length; i++) {
            prediksi[i] = KromosomBest[0];
            for (int j = 1; j < pgen; j++) {
                prediksi[i] += KromosomBest[j] * matauang[i - pgen + j];
            }
            different[i] = (matauang[i] - prediksi[i]) * (matauang[i] - prediksi[i]);
            error[i] = Math.abs(matauang[i] - prediksi[i]);
            SE += different[i];
            E += error[i];
            total += matauang[i];
            date[i] = format.parse(tanggal[i]);
        }

        for (int i = matauang.length; i < n; i++) {
            prediksi[i] = KromosomBest[0];
            for (int j = 1; j < pgen; j++) {
                prediksi[i] += KromosomBest[j] * prediksi[i - pgen + j];
            }
            different[i] = 0;

            Date x = format.parse(tanggal[matauang.length - 1]);
            Calendar c = Calendar.getInstance();
            c.setTime(x);
            c.add(Calendar.DATE, i - matauang.length);
            date[i] = c.getTime();
        }

        int k = n - range - pgen + 1;
        MSE = SE / k;
        MAPD = E / total;
        double dummy = 0;
        DecimalFormat numberFormat = new DecimalFormat("#0.0000000000000000");
        DecimalFormat numberFormat2 = new DecimalFormat("#0.0000");

        FileWriter writer = new FileWriter(hasil);
        writer.append("MAPD = "+numberFormat2.format(MAPD*100)+" %,   MSE = "+numberFormat.format(MSE)+"\n");

        for (int i = 0; i < matauang.length; i++) {
            writer.append(format.format(date[i]));
            writer.append(',');
            writer.append(Double.toString(prediksi[i]));
            writer.append(',');
            writer.append(Double.toString(matauang[i]));
            writer.append('\n');
        }

        for (int i = matauang.length; i < n; i++) {
            writer.append(format.format(date[i]));
            writer.append(',');
            writer.append(Double.toString(prediksi[i]));
            writer.append(',');
            writer.append(Double.toString(matauang[i]));
            writer.append('\n');
        }

        writer.flush();
        writer.close();
    }

    public static void Refresh() throws FileNotFoundException, IOException {
        FileWriter writer1 = new FileWriter("bestdefault.csv");
        writer1.append("jumlahgen,fitness,kromosom");
        writer1.flush();
        writer1.close();

        FileWriter writer2 = new FileWriter("predict.csv");
        writer2.append("");
        writer2.flush();
        writer2.close();

        FileWriter writer3 = new FileWriter("datadefault.csv");
        writer3.append("");
        writer3.flush();
        writer3.close();
    }
}

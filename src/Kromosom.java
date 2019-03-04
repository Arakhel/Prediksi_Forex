import java.awt.*;
import java.util.*;
import java.util.List;

public class Kromosom {
    private static ArrayDeque<Object> generasibaru;
    private double[] krom;

    public Kromosom(int genjumlah) {
        krom = new double[genjumlah];
        for (int i = 0; i < genjumlah; i++) {
            krom[i] = (double) (Math.random());
        }
    }

    public static double[] Fitness(List<Kromosom> individu, Double[] matauang, int genjumlah, int totalpopulasi) {
        int n = matauang.length - genjumlah + 1;
        double[] prediksi = new double[n];
        double[] different = new double[n];
        double[] SE = new double[totalpopulasi];
        double[] MSE = new double[totalpopulasi];
        double[] genfitness = new double[totalpopulasi];

        for (int p = 0; p < totalpopulasi; p++) {
            for (int i = 0; i < matauang.length - genjumlah + 1; i++) {
                different[i] = 0;
                prediksi[i] = individu.get(p).krom[0];

                for (int j = 1; j < genjumlah; j++) {
                    prediksi[i] += individu.get(p).krom[j] * matauang[i + j - 1];
                }
                different[i] = Math.pow((matauang[i + genjumlah - 1] - prediksi[i]), 2);
                SE[p] += different[i];
            }
            MSE[p] = SE[p] / n;
            genfitness[p] = 1 / MSE[p];
        }
        return genfitness;
    }

    public static int[] Seleksiparent(double[] fitness, int banyakparent) {
        int[] parent = new int[banyakparent];
        double[] fitnessprob = new double[fitness.length];
        double[] fitnesskumul = new double[fitness.length + 1];

        //menghitung nilai totatfitnes, probabilitastotal dan kumulatif fitness
        double totalfit = 0;
        for (int i = 0; i < fitness.length; i++) {
            totalfit += fitness[i];
        }

        for (int i = 0; i < fitness.length; i++) {
            fitnessprob[i] = fitness[i] / totalfit;
        }

        fitnesskumul[0] = 0;
        for (int i = 1; i < fitness.length + 1; i++) {
            fitnesskumul[i] = fitnessprob[i - 1] + fitnesskumul[i - 1];
        }

        //nilai random untuk seleksi parent roda roulette kumulatif
        for (int i = 0; i < banyakparent; i++) {
            parent[i] = 0;
            double random = Math.random();
            for (int j = 0; j < fitness.length; j++) {
                if (random >= fitnesskumul[j] && random <= fitnesskumul[j + 1]) {
                    parent[i] = j;
                }
            }
        }

        //menghilangkan nilai duplicate menggunakan fungsi hash
        Set<Integer> nilai = new HashSet<>();
        int[] indeks = new int[0];

        for (int next : parent) {
            if (!nilai.contains(next)) {
                indeks = Arrays.copyOf(indeks, indeks.length + 1);
                indeks[indeks.length - 1] = next;
                nilai.add(next);
            }
        }
        return indeks;
    }

    public static List<Kromosom> Crossover(List<Kromosom> individu, int[] indeks, double alfa, int jumlahgen) {

        int m = indeks.length;
        int banyakoff;
        if (m % 2 == 1) {
            banyakoff = m - 1;
        } else {
            banyakoff = m;
        }

        List<Kromosom> induk = new ArrayList<>();
        for (int i = 0; i < banyakoff; i++) {
            int value = indeks[i];
            induk.add(individu.get(value));
        }
        Kromosom[] crossover = induk.toArray(new Kromosom[0]);

        //proses crossover whole
        for (int i = 0; i < banyakoff; i += 2) {
            for (int n = 0; n < jumlahgen; n++) {
                crossover[i].krom[n] = (alfa * crossover[i].krom[n]) + ((1 - alfa) * crossover[i + 1].krom[n]);
            }
            for (int n = 0; n < jumlahgen; n++) {
                crossover[i + 1].krom[n] = (alfa * crossover[i + 1].krom[n]) + ((1 - alfa) * crossover[i].krom[n]);
            }
        }

        List<Kromosom> offspring = new ArrayList<>();
        for (int i = 0; i < banyakoff; i++) {
            offspring.add(crossover[i]);
        }

        return offspring;
    }

    public static List<Kromosom> Mutasi(List<Kromosom> individu, List<Kromosom> offspring, double pm, int populasi, int jumlahgen) {

        List<Kromosom> genereasibaru = new ArrayList<>();

        for (int i = 0; i < individu.size(); i++) {
            generasibaru.add(individu.get(i));
        }
        for (int i = 0; i < offspring.size(); i++) {
            generasibaru.add(offspring.get(i));
        }
        Kromosom[] popbaru = generasibaru.toArray(new Kromosom[0]);

        int totalpop = popbaru.length;
        int totalgen = totalpop * jumlahgen;
        int jumlahmutasi = (int) (pm * totalgen);

        //membuat bilangan unique random, supaya titik random mutasi pasti berbeda
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < totalgen; i++) {
            list.add(new Integer(i));
        }
        Collections.shuffle(list);

        for (int i = 0; i < jumlahmutasi; i++) {
            int pop = list.get(i) / jumlahgen;
            int genke = list.get(i) % jumlahgen;
            double beta = Math.random() + 0.1;
            popbaru[pop].krom[genke] = beta * popbaru[pop].krom[genke];
        }
        List<Kromosom> populasibaru = new ArrayList<>();
        populasibaru.addAll(Arrays.asList(popbaru));

        return populasibaru;
    }

    public static List<Kromosom> Elitism(List<Kromosom> Generasibaru, double[] fitnessnew, int populasi) {

        Kromosom[] elitism = Generasibaru.toArray(new Kromosom[0]);

        //sorting populasi berdasarkan fitness tertinggi
        for (int i = 0; i < fitnessnew.length - 1; i++) {
            for (int j = 1; j < fitnessnew.length; j++) {
                if (fitnessnew[j - 1] < fitnessnew[j]) {
                    double swap = fitnessnew[j - 1];
                    Kromosom value = elitism[j - 1];

                    fitnessnew[j - 1] = fitnessnew[j];
                    elitism[j - 1] = elitism[j];

                    fitnessnew[j] = swap;
                    elitism[j] = value;
                }
            }
        }

        //hanya menyimpan kromosom dengan ukuran sesuai populasi awal
        List<Kromosom> survivor = new ArrayList<>();
        for (int i = 0; i < populasi; i++) {
            survivor.add(elitism[i]);
        }
        return survivor;
    }

    public static Double[] BestKrom(List<Kromosom> individu, int jumlahgen) {
        // mengambil kromosom-kromosom dalam individu terbaik pada proses learn
        Double[] Best = new Double[jumlahgen];
        double prediksi = 0;
        for (int j = 0; j < jumlahgen; j++) {
            Best[j] = individu.get(0).krom[j];
        }

        return Best;
    }

    public static double MSE(Double[] Best,Double[] matauang){
        //menghitung nilai MSE
        int n = matauang.length-Best.length+1;
        double[] prediksi = new double[n];
        double[] different = new double[n];
        double E = 0;
        double MSE;

        for (int i=0;i<n;i++)
        { prediksi[i]=Best[0];
            different[i]=0;

            for (int j=1;j<Best.length;j++)
            {prediksi[i]+=Best[j]*matauang[i+j-1];
            }
            different[i] = (matauang[i+Best.length-1]-prediksi[i])*(matauang[i+Best.length-1]-prediksi[i]);
            E += different[i];

        }
        MSE=E/n;
        return MSE;
    }
}

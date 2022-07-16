import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;
public class Genome {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BEG = "\u001B[3";
    public static final String ANSI_END = "m";
    private double speed_evolution;
    private double light_need; //0-not need, 10-max need
    double[] dna;
    private Genome() {
        dna = new double[10];
    }

    public static Genome createGenome() {
        return new Genome();
    }

    public void random() {
        speed_evolution = Math.random() / 2 + 0.25; //0..1 -> 0.25..0.75; 0..1 / 2 -> 0..0.5 + 0.25 -> 0.25..0.75
        light_need = Math.random() * 10 ;/// 2; //  0..5// 0..10
        for (int i = 0; i < dna.length; ++i){
            dna[i] = Math.random();
        }
    }

    public void draw() {
        Integer v = (int)(light_need * 8 / 10);
        System.out.print(ANSI_BEG + v.toString() + ANSI_END + "*" + ANSI_RESET);
    }

    private Double[] randomSum(double needSum, int n){
        Double[] doubles = new Double[n];
        Double sum = needSum / n;//approximately minimum
        for(int i = 0; i < doubles.length; ++i){
            doubles[i] = Math.random();
            sum += doubles[i];
        }
        sum -= needSum / n;
        Double k = needSum / sum;
        for(int i = 0; i < doubles.length; ++i){
            doubles[i] *= k;
        }
        return doubles;
    }
    private Double fix(double old, double _new, double speed) {
        return (1 - speed)*old + _new*speed;
    }
    public Genome cross(Stream<Genome> genomeStream) {
        double speedK = Math.random() / 5 + 0.7;// 0.7..0.9
        double lightK = Math.random() / 10 + 0.8;// 0.8..0.9
        Genome[] genArr = genomeStream.toArray(Genome[]::new);
        int cnt = genArr.length;
        Iterator<Double> randDoublesSpeed = Arrays.stream(randomSum(1 - speedK, cnt)).iterator();
        Iterator<Double> randDoublesK = Arrays.stream(randomSum(1 - lightK, cnt)).iterator();
        Iterator<Double> randDoublesSpeed_ = Arrays.stream(genArr).map((Genome j) -> j.speed_evolution).iterator();
        Iterator<Double> randDoublesK_ = Arrays.stream(genArr).map((Genome j) -> j.light_need).iterator();
        double sumSpeed = speedK * speed_evolution;
        double sumLight = lightK * light_need;
        while (randDoublesSpeed.hasNext()){
            sumSpeed += randDoublesSpeed.next() * speed_evolution * randDoublesSpeed_.next();
            sumLight += randDoublesK.next() * speed_evolution * randDoublesK_.next();
        }
        Genome newGenome = createGenome();
        newGenome.speed_evolution = fix(speed_evolution, sumSpeed, speed_evolution);
        newGenome.light_need = fix(light_need, sumLight, speed_evolution);
        crossDna(genArr, newGenome, cnt);
        return newGenome;
    }

    private void crossDna(Genome[] genomeStream, Genome newGenome, int cnt) {
        Double[] randDnaK = new Double[dna.length];
        for (int i = 0; i < randDnaK.length; ++i) {
            randDnaK[i] = Math.random();
        }
        for(int i = 0; i < randDnaK.length; ++i){
            int finalI = i;
            Iterator<Double> s1 = Arrays.stream(randomSum(1 - randDnaK[i], cnt)).iterator();
            Iterator<Double> s2 = Arrays.stream(genomeStream).map((Genome j) -> j.dna[finalI]).iterator();
            double sum = randDnaK[i] * dna[i];
            while (s1.hasNext()){
                sum += s1.next() * s2.next();
            }
            newGenome.dna[i] = fix(dna[i], sum, speed_evolution);
        }
    }
}

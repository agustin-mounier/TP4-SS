/**
 * Created by amounier on 4/21/17.
 */
public class Oscillator {

    public static final double dT = 0.001;
    public static final double tf = 5;
    public static final double g = 100;
    public static final double m = 70;


    public static void runVerlet() {
        Particle particle = new Particle(1, 0, -(g / (2 * m)), 0, 1, m);
        OscillatorForce oF = new OscillatorForce(Math.pow(10, 4), g, particle);
        Verlet verlet = new Verlet(oF, dT);

        double t = 0;
        while (t < tf) {
            System.out.print(t + "\t");
            System.out.println(verlet.moveParticle());
            t += dT;
        }
    }

    public static void runBeeman() {
        Particle particle = new Particle(1, 0, -(g / (2 * m)), 0, 1, m);
        OscillatorForce oF = new OscillatorForce(Math.pow(10, 4), g, particle);
        Beeman beeman = new Beeman(oF, dT);

        double t = 0;
        while (t < tf) {
            System.out.print(t + "\t");
            System.out.println(beeman.moveParticle());
            t += dT;
        }
    }


    public static void main(String[] args) {
        runBeeman();
    }
}

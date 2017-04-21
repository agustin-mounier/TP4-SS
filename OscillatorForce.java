/**
 * Created by amounier on 4/21/17.
 */
public class OscillatorForce {

    private double K;
    private double G;
    private Particle particle;

    public OscillatorForce(double k, double g, Particle p) {
        this.K = k;
        this.G = g;
        this.particle = p;
    }

    public double getXForce() {
        return -(K * particle.x) - (G * particle.velX);
    }

    public double getYForce() {
        return -(K * particle.y) - (G * particle.velY);
    }

    public Particle getParticle() {
        return particle;
    }
}

package forces;

import models.Particle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sebastian on 4/23/17.
 */
public class GravityForce implements Force {

    private final double G = 6.693E-11; // (m^3) / (Kg * s^2)
    private Particle particle;
    public List<Particle> otherParticles;

    public GravityForce(Particle particle, Particle ... otherParticles) {
        this.particle = particle;
        this.otherParticles = new ArrayList<>();
        for (Particle p : otherParticles) {
            this.otherParticles.add(p);
        }
    }

    public GravityForce(Particle sun) {
        this.otherParticles = new ArrayList<>();
        this.particle = sun;
    }

    @Override
    public double getXForce() {
        double fx = 0;
        for (Particle p : otherParticles) {
            double dx = Particle.getDx(this.particle, p);
            fx += specificForce(dx, this.particle, p);
        }
        return fx;
    }

    @Override
    public double getYForce() {
        double fy = 0;
        for (Particle p : otherParticles) {
            double dy = Particle.getDy(this.particle, p);
            fy += specificForce(dy, this.particle, p);
        }
        return fy;
    }

    @Override
    public Particle getParticle() {
        return particle;
    }

    // Private functions
    private double specificForce(double delta, Particle p1, Particle p2) {
        return (-Math.signum(delta)) * ((this.G * p1.mass * p2.mass) / Math.pow(delta, 2));
    }

}

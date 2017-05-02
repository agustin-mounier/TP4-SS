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

    double getForce(Particle p, Particle other) {
        return G * p.mass * other.mass / Math.pow(Particle.getDistance(p, other), 2);
    }

    public double getXForce() {

        double ret = 0;
        for (Particle particle : otherParticles) {
            ret -= getForce(this.particle, particle) * (this.particle.x - particle.x) / Particle.getDistance(this.particle, particle);
        }
        return ret;
    }

    public double getYForce() {
        double ret = 0;
        for (Particle particle : otherParticles) {
            ret -= getForce(this.particle, particle) * (this.particle.y - particle.y) / Particle.getDistance(this.particle, particle);
        }
        return ret;
    }

    public Particle getParticle() {
        return this.particle;
    }

}

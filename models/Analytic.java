package models;

import models.Particle;
import models.ParticleState;

/**
 * Created by amounier on 4/21/17.
 */
public class Analytic {

    private double G;
    private double K;
    private double mass;

    public Analytic(double g, double k, Particle particle) {
        G = g;
        K = k;
        mass = particle.mass;
    }

    public ParticleState moveParticle(double t) {
        double x = Math.pow(Math.E, -(G / (2 * mass)) * t) * Math.cos(Math.sqrt((K / mass) - (G * G) / (4 * mass * mass)) * t);
        return new ParticleState(x, 0, 0, 0);
    }
}

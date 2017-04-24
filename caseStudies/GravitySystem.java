package caseStudies;

import algorithms.Verlet;
import forces.GravityForce;
import models.Particle;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by sebastian on 4/23/17.
 */
public class GravitySystem {

    // Earth

    private final double E_mass = 5.972E24;
    private final double E_radius = 6371.0;

    private final double E_Xo = 1.391734353396533E8;
    private final double E_Yo = -0.571059040560652E8;
    private final double E_VXo = 10.801963811159256;
    private final double E_VYo = 27.565215006898345;

    private Particle earth;

    // Mars
    private final double M_mass = 6.4185E23;
    private final double M_radius = 3389.9;

    private final double M_Xo = 0.831483493435295E8;
    private final double M_Yo = -1.914579540822006E8;
    private final double M_VXo = 23.637912321314047;
    private final double M_VYo = 11.429021426712032;

    private Particle mars;

    // Sun
    private final double S_mass = 1.988E30;
    private final double S_radius = 695700.0;

    private Particle sun;

    // System
    public static final double dt = 0.005;
    public static final double t = 100;

    public GravitySystem() {
        this.earth = new Particle(E_Xo, E_Yo, E_VXo, E_VYo, E_radius, E_mass);
        this.mars = new Particle(M_Xo, M_Yo, M_VXo, M_VYo, M_radius, M_mass);
        this.sun = new Particle(0.0, 0.0, 0.0, 0.0, S_radius, S_mass);
    }

    public void start() {
        GravityForce sunForce = new GravityForce(sun);
        GravityForce earthForce = new GravityForce(earth, sun, mars);
        GravityForce marsForce = new GravityForce(mars, earth, sun);

        // Verlet
        Verlet sunVerlet = new Verlet(sunForce, dt);
        Verlet earthVerlet = new Verlet(earthForce, dt);
        Verlet marsVerlet = new Verlet(marsForce, dt);

        double auxT = 0;

        try{
            PrintWriter writer = new PrintWriter("System.xyz", "UTF-8");
            while (auxT < t) {
                writer.println(1);
                writer.println(auxT);
                sunVerlet.moveParticle();
                writer.println(earthVerlet.moveParticle().toString(true));
                marsVerlet.moveParticle();
                auxT += dt;
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }

}

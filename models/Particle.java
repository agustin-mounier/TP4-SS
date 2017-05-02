package models;

import sun.nio.cs.ext.MacHebrew;

import java.rmi.MarshalException;

/**
 * Created by amounier on 4/21/17.
 */
public class Particle {

    public double x;
    public double y;
    public double radius;

    public double mass;
    public double velX = 0;
    public double velY = 0;


    public Particle(double x, double y, double velX, double velY, double radius, double mass) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.radius = radius;
        this.mass = mass;
    }

    // Static functions

    public static double getAngle(Particle p1, Particle p2) {
        return Math.atan2( (p1.y - p2.y), (p1.x - p1.x) );
    }

    /**
     *
     * @param p1 Particle 1
     * @param p2 Partcile 2
     * @return the distance over x of both particles
     *
     */
    public static double getDx (Particle p1, Particle p2) {
        return (p1.x - p2.x);
    }

    /**
     *
     * @param p1 Particle 1
     * @param p2 Partcile 2
     * @return the distance over y of both particles
     *
     */
    public static double getDy (Particle p1, Particle p2) {
        return (p1.y - p2.y);
    }

    /**
     *
     * @param p1
     * @param p2
     * @return the normal distance between both particles.
     */
    public static double getDistance(Particle p1, Particle p2) {
        return Math.sqrt(Math.pow(getDx(p1, p2), 2) + Math.pow(getDy(p1, p2), 2));
    }

}

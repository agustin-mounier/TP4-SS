package models;

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
     * @return the distance over x of both particles, measured in metres
     *
     */
    public static double getDx (Particle p1, Particle p2) {
        return (p2.x - p1.x)*1000;
    }

    /**
     *
     * @param p1 Particle 1
     * @param p2 Partcile 2
     * @return the distance over y of both particles, measured in metres
     *
     */
    public static double getDy (Particle p1, Particle p2) {
        return (p2.y - p1.y)*1000;
    }
}

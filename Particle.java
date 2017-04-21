/**
 * Created by amounier on 4/21/17.
 */
public class Particle {

    double x;
    double y;
    double radius;

    double mass;
    double velX = 0;
    double velY = 0;


    public Particle(double x, double y, double velX, double velY, double radius, double mass) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.radius = radius;
        this.mass = mass;
    }
}

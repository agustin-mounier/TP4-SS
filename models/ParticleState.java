package models;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by amounier on 4/21/17.
 */
public class ParticleState {

    double x;
    double y;

    double radius;

    double velX;
    double velY;

    public ParticleState(double x, double y, double velX, double velY) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
    }

    public ParticleState(Particle p) {
        this.x = p.x;
        this.y = p.y;
        this.radius = p.radius;
        this.velX = p.velX;
        this.velY = p.velY;
    }

    public String toString(boolean radius) {
        NumberFormat formatter = new DecimalFormat("#0.00");

        if (radius)
            return (new BigDecimal(x)).toPlainString() + "\t" + (new BigDecimal(y)).toPlainString() + "\t" + (new BigDecimal(this.radius)).toPlainString();
        else return toString();
    }

    @Override
    public String toString() {
        return x + "\t" + y + "\t" + velX + "\t" + velY;
    }
}

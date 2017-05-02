package models;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amounier on 4/21/17.
 */
public class ParticleState {

    List<String> aux = new ArrayList<>();

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
        aux.add("sun");
        aux.add("earth");
        aux.add("mars");
        aux.add("ship");
    }

    public String toString(String name) {
        int mult = name.equals("sun") ? 10 : (name.equals("ship") ? 10000000 : 1000);
        return aux.indexOf(name) + "\t" + x + "\t" + y + "\t" + this.radius * mult;
    }

    @Override
    public String toString() {
        return x + "\t" + y + "\t" + velX + "\t" + velY;
    }
}

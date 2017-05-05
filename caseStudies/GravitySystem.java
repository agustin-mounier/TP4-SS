package caseStudies;

import algorithms.Verlet;
import forces.GravityForce;
import models.Particle;
import models.ParticleState;
import utils.StartingPoint;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by sebastian on 4/23/17.
 */
public class GravitySystem {

    // Earth

    /**
     * mass         kg
     * radius       m
     * position     m
     * vel          m/s
     */


    private final double E_mass = 5.972E24;
    private final double E_radius = 6371000.0;
    private boolean shouldGenerateFile;

    private double E_Xo = 1.391734353396533E11;
    private double E_Yo = -0.571059040560652E11;
    private double E_VXo = 10801.963811159256;
    private double E_VYo = 27565.215006898345;

    private Particle earth;

    // Mars
    private final double M_mass = 6.4185E23;
    private final double M_radius = 3389900.0;

    private double M_Xo = 0.831483493435295E11;
    private double M_Yo = -1.914579540822006E11;
    private double M_VXo = 23637.912321314047;
    private double M_VYo = 11429.021426712032;

    private Particle mars;

    // Sun
    private final double S_mass = 1.988E30;
    private final double S_radius = 695700000.0;

    private double S_Xo = 0;
    private double S_Yo = 0;
    private double S_VXo = 0;
    private double S_VYo = 0;

    private Particle sun;

    // Space Ship
    private final double ship_mass = 2E5;
    private final double ship_radius = 50.0;
    private double ship_vo = 0.0; // m/s
    private final double ship_height = 1500E3; // m
    private double ship_launchAngle = 0; // degrees
    private static double groundSpeed = 7120;

    private Particle ship;

    // System
    public static final double DAY = 24*3600.0;
    public static final double WEEK = DAY * 7;
    public static final double MONTH = DAY * 31;
    public static final double YEAR = DAY * 365;

    public static final double dt = 100;
    public static final double t = 1 * YEAR;
    private boolean hasCrashed = false;
    private int crashWith = -1; // 0 -> sun, 1 -> earth

    public GravitySystem(double angle, int day, double vel, boolean generateFile) {
        this.ship_vo = vel;
        this.shouldGenerateFile = generateFile;
        if(day != 0) {
            Map<String, List<String>> startingPoints = StartingPoint.getStartingPoints(day);
            for (String s : startingPoints.keySet()) {
                List<String> values = startingPoints.get(s);
                double x = Double.parseDouble(values.get(0));
                double y = Double.parseDouble(values.get(1));
                double vx = Double.parseDouble(values.get(2));
                double vy = Double.parseDouble(values.get(3));

                switch (s) {
                    case "Sun:":
                        S_Xo = x;
                        S_Yo = y;
                        S_VXo = vx;
                        S_VYo = vy;
                        break;
                    case "Earth:":
                        E_Xo = x;
                        E_Yo = y;
                        E_VXo = vx;
                        E_VYo = vy;
                        break;
                    case "Mars:":
                        M_Xo = x;
                        M_Yo = y;
                        M_VXo = vx;
                        M_VYo = vy;
                        break;
                    default:
                        break;
                }
            }
        }
        this.earth = new Particle(E_Xo, E_Yo, E_VXo, E_VYo, E_radius, E_mass);
        this.mars = new Particle(M_Xo, M_Yo, M_VXo, M_VYo, M_radius, M_mass);
        this.sun = new Particle(S_Xo, S_Yo, S_VXo, S_VYo, S_radius, S_mass);
        this.ship_launchAngle = angle;

        this.ship = setShip();
    }

    public void runUntil(double day) {
        GravityForce sunForce = new GravityForce(sun, earth, mars, ship);
        GravityForce earthForce = new GravityForce(earth, sun, mars, ship);
        GravityForce marsForce = new GravityForce(mars, earth, sun, ship);

        GravityForce shipForce = new GravityForce(ship, earth, sun, mars);

        // Verlet
        Verlet sunVerlet = new Verlet(sunForce, dt);
        Verlet earthVerlet = new Verlet(earthForce, dt);
        Verlet marsVerlet = new Verlet(marsForce, dt);

        Verlet shipVerlet = new Verlet(shipForce, dt);

        double auxT = 0;
        int counter = 0;

        try {
            PrintWriter writer = new PrintWriter("System-withShip4.xyz", "UTF-8");
            while (auxT < t && !hasCrashed(auxT) && auxT < day*DAY) {

                if ((int)(counter % 100) == 0) {
                    //printPositions(auxT);

                    writer.println(4);
                    writer.println(auxT);
                    writer.println(sunVerlet.moveParticle().toString("sun"));
                    writer.println(earthVerlet.moveParticle().toString("earth"));
                    writer.println(marsVerlet.moveParticle().toString("mars"));

                    writer.println(shipVerlet.moveParticle().toString("ship"));

                } else {
                    sunVerlet.moveParticle();
                    earthVerlet.moveParticle();
                    marsVerlet.moveParticle();
                    shipVerlet.moveParticle();
                }
                auxT += dt;
                counter ++;
            }
            writer.close();
        } catch (IOException e) {

        }
        System.out.println("Velocidad Final: " + String.format(Locale.FRENCH, "%.3E", Math.sqrt(Math.pow((ship.velX - mars.velX),2) + Math.pow((ship.velY - mars.velY), 2))));
        System.out.println("Distancia de Marte: " + String.format(Locale.FRENCH, "%.3E", Particle.getDistance(ship, mars) - mars.radius - ship.radius) );
        System.out.println("Tiempo de Viaje: " + String.format(Locale.FRENCH, "%.3f", auxT/DAY) + " dias");
    }

    public Map<Double, List<String>> start() {
        boolean arrived = false;
        Double nearestToMars = null;
        Double arrivalTime = null;
        double finalVel = 0;

        GravityForce sunForce = new GravityForce(sun, earth, mars, ship);
        GravityForce earthForce = new GravityForce(earth, sun, mars, ship);
        GravityForce marsForce = new GravityForce(mars, earth, sun, ship);

        GravityForce shipForce = new GravityForce(ship, earth, sun, mars);

        // Verlet
        Verlet sunVerlet = new Verlet(sunForce, dt);
        Verlet earthVerlet = new Verlet(earthForce, dt);
        Verlet marsVerlet = new Verlet(marsForce, dt);

        Verlet shipVerlet = new Verlet(shipForce, dt);

        double auxT = 0;
        int counter = 0;

        System.out.print(String.format(Locale.FRENCH, "%.1f", ship_launchAngle) + "\t");

        Map<Double, List<String>> snapsByDt = new HashMap<>();
        while (auxT < t && !hasCrashed(auxT)) {


            if((int)(counter%5) == 0) {
                List<String> snaps = new ArrayList<>();
                snaps.add(sunVerlet.moveParticle().toString("sun"));
                snaps.add(earthVerlet.moveParticle().toString("earth"));
                snaps.add(marsVerlet.moveParticle().toString("mars"));

                snaps.add(shipVerlet.moveParticle().toString("ship"));
                snapsByDt.put(auxT, snaps);
            } else {
                sunVerlet.moveParticle();
                earthVerlet.moveParticle();
                marsVerlet.moveParticle();

                shipVerlet.moveParticle();
            }

            if (inMars()) {
                arrived = true;
                double distance = Particle.getDistance(ship, mars);
                if (nearestToMars == null ) {
                    arrivalTime = auxT;
                    nearestToMars = distance;
                    finalVel = getShipVel();
                } else {
                    nearestToMars = distance < nearestToMars ? distance : nearestToMars;
                    arrivalTime = auxT;
                    finalVel = getShipVel();
                }
            }
            counter++;
            auxT += dt;
        }
        if (arrived) {
            System.out.println(String.format(Locale.FRENCH, "%.3E", arrivalTime/DAY)
                    + "\t" + String.format(Locale.FRENCH, "%.3E",nearestToMars)
                    + "\t" + String.format(Locale.FRENCH, "%.3E",finalVel));
            return snapsByDt;
        }
        if (!hasCrashed) System.out.println("-\t-");
        return null;
    }

    public void findShortestDistance() {
        GravityForce sunForce = new GravityForce(sun, earth, mars);
        GravityForce earthForce = new GravityForce(earth, sun, mars);
        GravityForce marsForce = new GravityForce(mars, earth, sun);

        // Verlet
        Verlet sunVerlet = new Verlet(sunForce, dt);
        Verlet earthVerlet = new Verlet(earthForce, dt);
        Verlet marsVerlet = new Verlet(marsForce, dt);


        double auxT = 0;
        int counter = 0;
        double distance = Particle.getDistance(earth, mars);
        double tOfLessDistance = 0;

        try {
            PrintWriter writer = new PrintWriter("System-short-distance.xyz", "UTF-8");
            Map<Double, List<String>> snapsByDt = new LinkedHashMap<>();
            while (auxT < 3*t) {

                if ((int)(counter % dt) == 0) {
                    //printPositions(auxT);
                    List<String> snaps = new ArrayList<>();
                    snaps.add(sunVerlet.moveParticle().toString("sun"));
                    snaps.add(earthVerlet.moveParticle().toString("earth"));
                    snaps.add(marsVerlet.moveParticle().toString("mars"));
                    snapsByDt.put(auxT, snaps);

                } else {
                    sunVerlet.moveParticle();
                    earthVerlet.moveParticle();
                    marsVerlet.moveParticle();
                }
                double newDistance = Particle.getDistance(earth, mars);
                if (newDistance < distance) {
                    distance = newDistance;
                    tOfLessDistance = auxT;
                }

                if ((int)(auxT % (5*DAY)) == 0) {
                    printPositions(auxT);
                }

                auxT += dt;
                counter ++;
            }

            System.out.println(tOfLessDistance/DAY);
            System.out.println(distance);

            for (Double tt : snapsByDt.keySet()) {
                if (tt > tOfLessDistance) break;
                writer.println(snapsByDt.get(tt).size());
                writer.println(tt);
                for (String s : snapsByDt.get(tt)) {
                    writer.println(s);
                }
            }


            writer.close();
        } catch (IOException e) {

        }
    }

    public List<Double> findDistances(double finalDay) {
        if (finalDay == 0) {
            finalDay = t;
        }
        GravityForce sunForce = new GravityForce(sun, earth, mars, ship);
        GravityForce earthForce = new GravityForce(earth, sun, mars, ship);
        GravityForce marsForce = new GravityForce(mars, earth, sun, ship);

        GravityForce shipForce = new GravityForce(ship, mars, earth, sun);

        // Verlet
        Verlet sunVerlet = new Verlet(sunForce, dt);
        Verlet earthVerlet = new Verlet(earthForce, dt);
        Verlet marsVerlet = new Verlet(marsForce, dt);
        Verlet shipVerlet = new Verlet(shipForce, dt);

        double auxT = 0;
        double counter = 0;
        double originalDistance = distanceToMars();

        double minDistance = Particle.getDistance(earth, mars);
        double dtOfMin = 0;

        try {
            PrintWriter writer = new PrintWriter("System-withShip.xyz", "UTF-8");

            while (auxT < t && !hasCrashed(auxT) && auxT < DAY*finalDay) {

                ParticleState sunState = sunVerlet.moveParticle();
                ParticleState earthState = earthVerlet.moveParticle();
                ParticleState marsState = marsVerlet.moveParticle();
                ParticleState shipState = shipVerlet.moveParticle();

                double newDistance = distanceToMars();
                if (newDistance < minDistance) {
                    minDistance = newDistance;
                    dtOfMin = auxT;
                }

                if (shouldGenerateFile && counter % 100 == 0) {
                    writer.println(4);
                    writer.println(auxT);
                    writer.println(sunState.toString("sun"));
                    writer.println(earthState.toString("earth"));
                    writer.println(marsState.toString("mars"));
                    writer.println(shipState.toString("ship"));
                }

                counter ++;
                auxT += dt;
            }
            writer.close();
        } catch (IOException e){

        }

        //System.out.println(counter);

        double velF = Math.sqrt(Math.pow((ship.velX - mars.velX),2) + Math.pow((ship.velY - mars.velY), 2));

        List <Double> answer = new ArrayList<>();

        answer.add(minDistance);
        answer.add(originalDistance);
        answer.add(ship_launchAngle);
        answer.add(velF);
        answer.add(dtOfMin/DAY);

        if(minDistance == originalDistance) return null;
        if(hasCrashed) answer.add((double)crashWith);

        return answer;
    }

    // Private functions

    private Particle setShip() {

        double dx = earth.x - sun.x;
        double dy = earth.y - sun.y;

        double angle = Math.atan2(dy, dx);
        double variation = Math.toRadians(ship_launchAngle);

        double shipRad = 50;

        double x = earth.x + ((earth.radius + shipRad + ship_height) * Math.cos(angle));
        double y = earth.y + ((earth.radius + shipRad + ship_height) * Math.sin(angle));

        double Vx = earth.velX + (ship_vo + groundSpeed) * Math.cos(variation + angle);
        double Vy = earth.velY + (ship_vo + groundSpeed) * Math.sin(variation + angle);

        return new Particle(x, y, Vx, Vy, ship_radius, ship_mass);
    }

    private boolean inMars() {
        return Particle.getDistance(ship, mars) < (ship.radius + mars.radius + 1000000000);
    }

    private double distanceToMars() {
        return Particle.getDistance(ship, mars) - mars.radius;
    }

    private boolean hasCrashed(double time) {
        if (time > 3*dt && Particle.getDistance(ship, sun) < (ship.radius + sun.radius + ship_height) ) {
            //System.out.println("sun\t-");
            hasCrashed = true;
            crashWith = 0; // sun
            return true;
        } else if (time > 3*dt && Particle.getDistance(ship, earth) < (ship.radius + earth.radius)) {
            //System.out.println("earth\t-");
            hasCrashed = true;
            crashWith = 1; // earth
            return true;
        }
        return false;
    }

    private void printPositions(double auxT) {
        System.out.println("***\t" + (int)(auxT/DAY) + "\t***");
        System.out.println("Sun:\t" + sun.x + "\t" + sun.y + "\t" + sun.velX + "\t" + sun.velY);
        System.out.println("Earth:\t" + earth.x + "\t" + earth.y + "\t" + earth.velX + "\t" + earth.velY);
        System.out.println("Mars:\t" + mars.x + "\t" + mars.y + "\t" + mars.velX + "\t" + mars.velY);
    }

    private double getShipVel() {
        return Math.sqrt(Math.pow(ship.velX,2) + Math.pow(ship.velY,2));
    }
}

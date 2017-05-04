import caseStudies.GravitySystem;
import forces.GravityForce;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by sebastian on 4/23/17.
 */
public class Runner {

    public static void main(String[] args) {
        /*
        startWithSpecifConditions();
        GravitySystem gs = new GravitySystem(0, 0, 3E3);
        gs.runUntil(1.0);
        GravitySystem gs = new GravitySystem(100.0, 620, 3E3);
        gs.runUntil(1.0);
        */
        for (int day = 550; day <= 700; day+=10) {
            System.out.println("Calculating on day " + day);
            //System.out.println("** " + String.format(Locale.FRENCH, "%.1f", i) + " **");
            // <Angle, <Dt, <Snapshots>>>
            Map<Double, Map<Double, List<String>>> arrivedSnaps = new HashMap<>();

            Map<Double, List<Double>> answers = new HashMap<>();

            for (double i = 0; i < 181; i+=45) {
                //System.out.println("launch on day " + day + " at " + i + " degrees");
                GravitySystem gs = new GravitySystem(i, day, 8E3);
                List<Double> rta = gs.findDistances(365);
                if (rta != null) answers.put(i, rta);
                //gs.findShortestDistance();
                //if (snapsByDt != null) arrivedSnaps.put(i, snapsByDt);
            }


            double minAngle = -1;
            for (Double angle : answers.keySet() ) {
                double minDistance = 1e11;
                if (answers.get(angle).get(0) < minDistance) {
                    minAngle = angle;
                    minDistance = answers.get(angle).get(0);
                }
            }
            if (minAngle!= -1) {
                printData(day, answers.get(minAngle));
            } else {
                System.out.println("Ninguno llegó");
            }

            System.out.println();
            /*
            for (Double d : arrivedSnaps.keySet()) {
                System.out.println(day + " ** " + d);
                printToFile(d, arrivedSnaps.get(d), day, 10E3);
            }
            */
        }


    }

    private static void printData(int day, List<Double> answers) {
        System.out.println("Launch on day " + day + " at " + answers.get(1) + " degrees");
        System.out.println("Velocidad Final: " + String.format(Locale.FRENCH, "%.3E", answers.get(2)));
        System.out.println("Distancia de Marte: " + String.format(Locale.FRENCH, "%.3E", answers.get(0) ));
        System.out.println("Tiempo de Viaje: " + String.format(Locale.FRENCH, "%.3f", answers.get(3)) + " dias");
    }

    private static void printToFile(Double angle, Map<Double, List<String>> snapsByDt, int day, double vel) {
        try {
            PrintWriter writer = new PrintWriter("System-withShip-"+vel+"-"+day+"-"+angle+".xyz", "UTF-8");
            for (Double dt : snapsByDt.keySet()) {
                writer.println(snapsByDt.get(dt).size());
                writer.println(dt);
                for (String s : snapsByDt.get(dt)) {
                    writer.println(s);
                }
            }
            writer.close();
        } catch (IOException e){

        }

    }

    private static void startWithSpecifConditions() {
        try {
            String cadena;
            FileReader f = new FileReader("utils/finalVelocityCalculator.txt");
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                double vel = 0.0, angle = 0.0, distance = 0.0;
                int day = 0;
                List<String> line = new ArrayList<>(Arrays.asList(cadena.split(" ")));
                for (String v : line) {
                    switch (line.indexOf(v)) {
                        case 0: // vel
                            vel = Double.parseDouble(v);
                            break;
                        case 1: // day
                            day = Integer.parseInt(v);
                            break;
                        case 2: // angle
                            distance = Double.parseDouble(v);
                            break;
                        default:
                            break;
                    }
                }
                GravitySystem gs = new GravitySystem(angle, day, vel);
                gs.runUntil(distance);
            }
            b.close();
        } catch (IOException e) {

        }
    }

}

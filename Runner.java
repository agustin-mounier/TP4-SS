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

    private final static double LAUNCH_ANGLE = 90; // 90 -- tangencial
    private final static int LAUNCH_DAY = 0;
    private final static double LAUNCH_VEL = 3E3;
    private final static int LAST_DAY = 90;


    public static void main(String[] args) {
        //runAll(0, 1000, 1, 90, 90, 365, 10E3);
        calculateSpecific(LAUNCH_ANGLE, LAUNCH_DAY, LAUNCH_VEL, LAST_DAY, true);
    }

    /**
     *
     * @param startingDay
     * @param endingDate
     * @param startingAngle Minimun is -90°
     * @param endingAngle Maximun is 90° (tangencial)
     */
    private static void runAll(int startingDay, int endingDate, int dayStep, double startingAngle, double endingAngle, int wholePeriod, double shipVel) {
        for (int day = startingDay; day <= endingDate; day+=dayStep) {
            System.out.println("Calculando en el día " + day);

            Map<Double, List<Double>> answers = new HashMap<>();

            for (double i = startingAngle; i <= endingAngle; i+=20) {
                List<Double> rta = calculateSpecific(i, day, shipVel, wholePeriod, false);
                if (rta != null) answers.put(i, rta);
            }
            printMin(day, answers);
        }
    }

    private static List<Double> calculateSpecific(double angle, int day, double shipVel, int until, boolean print) {
        GravitySystem gs = new GravitySystem(angle, day, shipVel, true);
        List<Double> rta = gs.findDistances(until);
        if (print) {
            Map<Double, List<Double>> auxiliar = new HashMap<>();
            auxiliar.put(angle,rta);
            printMin(day, auxiliar);
        }

        return rta;
    }

    private static void printMin(int day, Map<Double, List<Double>> answers) {
        double minAngle = -1;
        for (Double angle : answers.keySet() ) {
            if(answers.get(angle) == null) continue;
            double minDistance = -1;

            if (minDistance == -1) {
                minAngle = angle;
                minDistance = answers.get(angle).get(0);
            } else if (answers.get(angle).get(0) < minDistance) {
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
    }

    private static void printData(int day, List<Double> answers) {
        System.out.println("Lanzado el día " + day + " at " + answers.get(2) + " degrees");
        System.out.println("Velocidad Final: " + String.format(Locale.FRENCH, "%.3E", answers.get(3)));
        System.out.print("Distancia de Marte: " + String.format(Locale.FRENCH, "%.3E", answers.get(0)) + " (Distancia inicial: " + String.format(Locale.FRENCH, "%.3E", answers.get(1)) + ")");
        if (answers.size() == 6) {
            System.out.print(" - después chocó con " + (answers.get(5) == 0.0 ? "Sol" : "Tierra"));
        }
        System.out.println();
        System.out.println("Tiempo de Viaje: " + String.format(Locale.FRENCH, "%.3f", answers.get(4)) + " dias");
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
                GravitySystem gs = new GravitySystem(angle, day, vel, false);
                gs.runUntil(distance);
            }
            b.close();
        } catch (IOException e) {

        }
    }

}

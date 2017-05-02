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

        for (double i = 120; i <= 120; i+=1) {
            System.out.println("** " + String.format(Locale.FRENCH, "%.1f", i) + " **");
            // <Angle, <Dt, <Snapshots>>>
            Map<Double, Map<Double, List<String>>> arrivedSnaps = new HashMap<>();
            //System.out.println("launch on " + day);
            for (int day = 640; day < 641; day+=5) {
                GravitySystem gs = new GravitySystem(i, day, 3E3);
                gs.runUntil(6.1);
                //Map<Double, List<String>> snapsByDt = gs.start();
                //if (snapsByDt != null) arrivedSnaps.put(i, snapsByDt);
            }
            /*
            for (Double d : arrivedSnaps.keySet()) {
                System.out.println(day + " ** " + d);
                printToFile(d, arrivedSnaps.get(d), day, 10E3);
            }
            */
        }

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

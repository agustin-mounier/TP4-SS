package utils;

import sun.misc.IOUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by sebastian on 4/28/17.
 */
public class StartingPoint {

    public static Map<String, List<String>> getStartingPoints(int day) {
        Map<String, List<String>> rta = new HashMap<>();
        int counter = 0;
        int flag = 0;
        try {
            String cadena;
            FileReader f = new FileReader("positions.txt");
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null && flag < 4) {
                if (counter == (4*day) ) {
                    if (flag > 0) {
                        List<String> line = new ArrayList<String>(Arrays.asList(cadena.split("\t")));
                        rta.put(line.remove(0), line);
                    }
                    counter --;
                    flag++;
                }
                counter ++;
            }
            b.close();
        } catch (IOException e) {

        }
        return rta;
    }

}

import com.co.back.flexes.FleXES;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class FleXESUnitTest {

    @Test
    void mergeTest() throws IOException {

        XLog l1 = FleXES.loadXES("pdc2021_000000.xes");
        XLog l2 = FleXES.loadXES("pdc2021_000001.xes");

        NavigableMap<String, XLog> logMap = new TreeMap<>();
        logMap.put("pdc2021_000000.xes", l1);
        logMap.put("pdc2021_000001.xes", l2);

        XLog merged = FleXES.merge(logMap);

        for ( XTrace t : l1) {
            System.out.println(t.hashCode());
        }

        System.out.println("+++");

        for ( XTrace t : l2) {
            System.out.println(t.hashCode());
        }

        System.out.println("===");

        for ( XTrace t : merged) {
            System.out.println(t.hashCode() + ": " + t.getAttributes().get("sublog"));
        }
    }

    @Test
    void mergeAndSerialize() throws IOException {

        String prefix = "round 5 treeSeed ";
        String suffix = ".ptml.tree-logRound13-noiseRound";

        for (int model = 1; model <= 10; model++) {

            NavigableMap<String, XLog> logMap = new TreeMap<>();

            for ( int i = 0; i <= 13; i++ ) {
                String filename = prefix + model + suffix + i + ".xes";
                XLog l = FleXES.loadXES(filename);
                logMap.put(filename, l);
            }

            OutputStream os = Files.newOutputStream(Paths.get(prefix + model + suffix + "ALL.xes"));
            FleXES.mergeAndSerialize(logMap, os, false);
        }
    }

    @Test
    void mergeAndSerialize2() throws IOException {

        String prefix = "/home/tuck/code/process-similarity-metrics/logs/round 5 treeSeed ";
        String suffix = ".ptml.tree-logRound13-noiseRound";

        NavigableMap<String, XLog> logMap = new TreeMap<>();

        for (int model = 1; model <= 10; model++) {

            System.out.println("loading original log" + model + " of 10");

                String filename = prefix + model + suffix + "ALL.xes";
                XLog l = FleXES.loadXES(filename);
                logMap.put(filename, l);
        }

        OutputStream os = Files.newOutputStream(Paths.get(prefix + "ALL" + suffix + "ALL.xes"));
        FleXES.mergeAndSerialize(logMap, os, true);
    }

}

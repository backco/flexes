import com.co.back.flexes.FleXES;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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

}

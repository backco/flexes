package com.co.back.flexes;

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
    void loadMXML() throws IOException {

        XLog log = FleXES.loadEventLog("cb2.5k.mxml");

        log.forEach( t -> {
            System.out.println(t.getAttributes().get("concept:name"));
            t.forEach(e -> System.out.println("   " + e.getAttributes().get("concept:name") + "-" + e.getAttributes().get("lifecycle:transition")));
        });
    }

    @Test
    void mergeTest() throws IOException {

        XLog l1 = FleXES.loadEventLog("pdc2021_000000.xes");
        XLog l2 = FleXES.loadEventLog("pdc2021_000001.xes");

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
                XLog l = FleXES.loadEventLog(filename);
                logMap.put(filename, l);
            }

            OutputStream os = Files.newOutputStream(Paths.get(prefix + model + suffix + "ALL.xes"));
            FleXES.mergeAndSerialize(logMap, os, false, null, null);
        }
    }

    @Test
    void mergeAndSerialize2() throws IOException {

        String prefix = "/home/tuck/code/process-similarity-metrics/logs/round 5 treeSeed ";
        String suffix = ".ptml.tree-logRound";

        NavigableMap<String, XLog> logMap = new TreeMap<>();

        for (int model = 1; model <= 10; model++) {

            System.out.println("loading original log " + model + " of 10");

                String filename = prefix + model + suffix + "ALL.xes";
                XLog l = FleXES.loadEventLog(filename);
                logMap.put(filename, l);
        }

        OutputStream os = Files.newOutputStream(Paths.get(prefix + "ALL" + suffix + "ALL.xes"));
        FleXES.mergeAndSerialize(logMap, os, true, null, null);
    }

    @Test
    void mergeAndSerialize3() throws IOException    {

        String[] files = {
        "pdc2021_000000.xes",
        "pdc2021_000001.xes",
        "pdc2021_000010.xes",
        "pdc2021_000011.xes",
        "pdc2021_000100.xes",
        "pdc2021_000101.xes",
        "pdc2021_000110.xes",
        "pdc2021_000111.xes",
        "pdc2021_001000.xes",
        "pdc2021_001001.xes",
        "pdc2021_001010.xes",
        "pdc2021_001011.xes",
        "pdc2021_001100.xes",
        "pdc2021_001101.xes",
        "pdc2021_001110.xes",
        "pdc2021_001111.xes",
        "pdc2021_010000.xes",
        "pdc2021_010001.xes",
        "pdc2021_010010.xes",
        "pdc2021_010011.xes",
        "pdc2021_010100.xes",
        "pdc2021_010101.xes",
        "pdc2021_010110.xes",
        "pdc2021_010111.xes",
        "pdc2021_011000.xes",
        "pdc2021_011001.xes",
        "pdc2021_011010.xes",
        "pdc2021_011011.xes",
        "pdc2021_011100.xes",
        "pdc2021_011101.xes",
        "pdc2021_011110.xes",
        "pdc2021_011111.xes",
        "pdc2021_020000.xes",
        "pdc2021_020001.xes",
        "pdc2021_020010.xes",
        "pdc2021_020011.xes",
        "pdc2021_020100.xes",
        "pdc2021_020101.xes",
        "pdc2021_020110.xes",
        "pdc2021_020111.xes",
        "pdc2021_021000.xes",
        "pdc2021_021001.xes",
        "pdc2021_021010.xes",
        "pdc2021_021011.xes",
        "pdc2021_021100.xes",
        "pdc2021_021101.xes",
        "pdc2021_021110.xes",
        "pdc2021_021111.xes",
        "pdc2021_100000.xes",
        "pdc2021_100001.xes",
        "pdc2021_100010.xes",
        "pdc2021_100011.xes",
        "pdc2021_100100.xes",
        "pdc2021_100101.xes",
        "pdc2021_100110.xes",
        "pdc2021_100111.xes",
        "pdc2021_101000.xes",
        "pdc2021_101001.xes",
        "pdc2021_101010.xes",
        "pdc2021_101011.xes",
        "pdc2021_101100.xes",
        "pdc2021_101101.xes",
        "pdc2021_101110.xes",
        "pdc2021_101111.xes",
        "pdc2021_110000.xes",
        "pdc2021_110001.xes",
        "pdc2021_110010.xes",
        "pdc2021_110011.xes",
        "pdc2021_110100.xes",
        "pdc2021_110101.xes",
        "pdc2021_110110.xes",
        "pdc2021_110111.xes",
        "pdc2021_111000.xes",
        "pdc2021_111001.xes",
        "pdc2021_111010.xes",
        "pdc2021_111011.xes",
        "pdc2021_111100.xes",
        "pdc2021_111101.xes",
        "pdc2021_111110.xes",
        "pdc2021_111111.xes",
        "pdc2021_120000.xes",
        "pdc2021_120001.xes",
        "pdc2021_120010.xes",
        "pdc2021_120011.xes",
        "pdc2021_120100.xes",
        "pdc2021_120101.xes",
        "pdc2021_120110.xes",
        "pdc2021_120111.xes",
        "pdc2021_121000.xes",
        "pdc2021_121001.xes",
        "pdc2021_121010.xes",
        "pdc2021_121011.xes",
        "pdc2021_121100.xes",
        "pdc2021_121101.xes",
        "pdc2021_121110.xes",
        "pdc2021_121111.xes"};

        NavigableMap<String, XLog> logMap = new TreeMap<>();

        String prefix = "/home/tuck/code/process-similarity-metrics/logs/";

        for (String f : files) {
            String filename = prefix + f;
            XLog l = FleXES.loadEventLog(filename);
            logMap.put(filename, l);
        }

        OutputStream os = Files.newOutputStream(Paths.get(prefix + "pdc2021ALL.xes"));
        FleXES.mergeAndSerialize(logMap, os, false, null, null);
    }

    @Test
    void liveobjectTest() throws IOException, InterruptedException {

        System.out.print("loading log...");

        load();

        System.out.print("done");

        Thread.sleep(60000);
    }

    private void load() throws IOException {

        XLog log = FleXES.loadEventLog("/home/tuck/code/process-similarity-metrics/logs/round_5_treeSeed_ALL.ptml.tree-logRound13-noiseRoundALL.xes");
        log.clear();
        System.gc();
    }
}
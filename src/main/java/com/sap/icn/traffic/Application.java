package com.sap.icn.traffic;

import com.sap.icn.traffic.calculation.Calculator;
import com.sap.icn.traffic.calculation.DateTimeUtil;
import com.sap.icn.traffic.calculation.TaxiPointUtil;
import com.sap.traffic.foundation.traffic_lib.ITaxiMonitor;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class Application {
    public static void main(String[] args) {
        boolean isLocal = false;

        final String master = isLocal ? "local[4]" : "spark://10.128.184.199:7077";
        final String csv = isLocal ? "Z:/RCS_SP1/RAW_DATA_MORE/2016_03/TAXI/TAXI_20160301.csv" : "/pi_nj_57/RCS_SP1/RAW_DATA_MORE/2016_03/TAXI/TAXI_20160301.csv";
        final String appName = "SpeedCalculator";

        Calculator calculator = new Calculator();

        SparkConf conf = new SparkConf()
                .set("spark.executor.memory", "4G")
                .set("spark.submit.deployMode", "cluster")
                .setMaster("spark://10.128.184.199:7077")
                .setJars(new String[]{"C:\\Users\\i321761\\Desktop\\git\\github.wdf.sap.corp\\i321761\\hadoop-sample\\target\\hadoopsample-1.0-SNAPSHOT.jar"});

        JavaSparkContext sc = new JavaSparkContext(master, appName, conf);
//        JavaRDD<String> rdd = sc.textFile(csv, 2);
        JavaRDD<String> rdd = sc.parallelize(Arrays.asList("abc", "def"));
        long start = System.currentTimeMillis();
        System.out.println("Count Start ....");

        // Convert csv string to taxi point structure and remove invalid records
        JavaRDD<ITaxiMonitor.TaxiPoint> taxiPointRDD = rdd.map(line -> TaxiPointUtil.parseTaxiPoint(line))
                .filter(point -> point != null && !point.receiveTime.isEmpty() && point.receiveTime.contains(" 08:"));

        JavaPairRDD<Long, List<ITaxiMonitor.TaxiPoint>> slotsIn5 = taxiPointRDD
                .keyBy(point -> (DateTimeUtil.parseToMillSecond(point.receiveTime, "UTC+8") / 300000) * 300000)
                .combineByKey(
                        v -> {
                            List<ITaxiMonitor.TaxiPoint> points = new ArrayList();
                            points.add(v);
                            return points;
                        },
                        (c, v) -> {
                            c.add(v);
                            return c;
                        },
                        (c1, c2) -> {
                            c1.addAll(c2);
                            return c1;
                        }
                )
                .sortByKey();
        slotsIn5.map(slot -> calculator.execute(slot._2(), slot._1(), slot._1()))
                .collect().forEach(speedResult -> {
                    speedResult.getTimedEdgeSpeeds().forEach(timedEdgeSpeeds -> {
                        long t = DateTimeUtil.parseToMillSecond(timedEdgeSpeeds.timestamp, "UTC+0");
                        timedEdgeSpeeds.edgeSpeeds.forEach(speed -> System.out.println(" * EDGE_SPEED: " + TaxiPointUtil.formatEdgeSpeed(t, speed, ",")));
                    });
                });

        slotsIn5.take(10)
                .forEach(slot -> System.out.println("slot: " + slot._1() + ", " + DateTimeUtil.formatToUTC(slot._1()) + ", count: " + slot._2().size()));
//                .foreach(slot -> System.out.println("slot: " + DateTimeUtil.formatToUTC(slot._1()) + ", count" + slot._2().size()));

        sc.stop();
    }
}

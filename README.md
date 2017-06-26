# hadoop-sample

## How to run it?
1. Copy `hadoopsample-1.0-SNAPSHOT.jar` to master node
2. Copy `traffic-foundation-libs-1.1.00.jar` to all workers's node and add it to class path,
   a simple way is copy it to `$SPARK_HOME/jars/`
2. execute command:
   `spark-submit --master spark://10.128.184.199:7077 --class com.sap.icn.traffic.App hadoopsample-1.0-SNAPSHOT.jar`
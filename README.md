# hadoop-sample

## How to run it?
1. Copy `hadoopsample-1.0-SNAPSHOT.jar` to master node
2. Copy `traffic-foundation-libs-1.1.00.jar` to all workers's node and add it to class path,
   a simple way is copy it to `$SPARK_HOME/jars/`
2. execute command:
   `spark-submit --master spark://10.128.184.199:7077 --class com.sap.icn.traffic.Application hadoopsample-1.0-SNAPSHOT.jar`
   
-----   
# Install Spark on windows (x64)

### Basic information
* Spark 2.1.0
* Hadoop 2.7.1 (only use winutils.exe)
* Scala 2.12.1
* JDK 1.8 x64
* Python 2.7 x64 (for pyspark)

## 1. Install JDK 1.8
----
> JDK installation is very easy and you can find lots of tutorials in google, so we ignore it.


## 2. Install Scala
----
* Download and install Scala from offical website  
  [http://www.scala-lang.org/download/](http://www.scala-lang.org/download/)


* Add Scala environment variables
> - Add `SCALA_HOME` point to your installation folder path
> - Add `%SCALA_HOME%\bin` to `Path` env 

* Test Scala
>>  Open windows shell (cmd or powershell) and execute "`scala -version`", some information like below should be printed:  
>>>`Scala code runner version 2.12.1 -- Copyright 2002-2016, LAMP/EPFL and Lightbend, Inc.`


## 3. Install Spark
----
* Download Spark zip package from offical website:  
[http://spark.apache.org/downloads.html](http://spark.apache.org/downloads.html)  
 Unzip it to your system.

* Add Spark environment variables
> - Add `SPARK_HOME` point to your installation folder path
> - Add `%SPARK_HOME%\bin` to `Path` env

*  Test Spark
>>  Open windows shell (cmd or powershell) and execute "`scala -version`", some information like below should be printed:  
``` yml
Welcome to
   SPARK   version 2.1.0
Using Scala version 2.11.8, Java HotSpot(TM) 64-Bit Server VM, 1.8.0_60
Branch
Compiled by user jenkins on 2016-12-16T02:04:48Z
Revision
Url
Type --help for more information.  
```

## 4. Install Hadoop tools (winutils.exe)
----
> We can download winutils from github:  
[https://github.com/steveloughran/winutils](https://github.com/steveloughran/winutils)
> Here we use 2.7.1 version, so copy `winutils.exe` to system, and add it in `Path` env.

## 5. Run Spark
----
Before first spark running, we need modify permission of folder `"c:\tmp\hive"` by below command:  
> `winutils.exe chmod 777 \tmp\hive`  

Run Spark by below command:  
> `spark-shell`

## 6. Run pyspark
----
pyspark is a Python binding for Spark API, it is already included in Spark installation package, you can check it in folder: `"%SPARK_HOME%\python"`
* Run pyspark  
Execute command `"pyspark"` in shell.  

* Use `IPYTHON` in `pyspark` shell  
If `IPYTHON` is not installed, install it via `pip`:  
`pip install ipython --upgrade [--proxy myproxy.com:8080]`  
Add env key `PYSPARK_DRIVER_PYTHON`, set its value to `ipython`.

* First `pyspark` example
``` python
In [2]: textFile = sc.textFile(r'C:\Users\i321761\Desktop\socket\csv\data.csv')

In [3]: textFile.count()
Out[3]: 414092

In [4]: ft = textFile.filter(lambda line: '32.033' in line)

In [5]: ft.count()
Out[5]: 167903
```

* Run `pyspark` in Python shell or IDE
There is no isolate `pyspark` in PyPI, we cannot install it via `pip`, there are 2 solutions:
> 1. Copy `"%SPARK_HOME%\python\spark"` folder to Python's site-packages folder, e.g. `"C:\Python27\Lib\site-packages\"`
> 2. Add `PYTHONPATH` env, and set its value to `%SPARK_HOME%\python\spark;%PYTHONPATH%`

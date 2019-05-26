# Apache Hadoop 2.7.7 Installation

   * [Apache Hadoop 2.7.7 Installation](#apache-hadoop-277-installation)
      * [1. Install Required Packages](#1-install-required-packages)
      * [2. Configure Java Environment](#2-configure-java-environment)
      * [3. Modify Hadoop Configuration](#3-modify-hadoop-configuration)
      * [4. Check SSH Configuration](#4-check-ssh-configuration)
      * [5. Configure Hadoop Environment](#5-configure-hadoop-environment)
      * [6. Initialize HDFS](#6-initialize-hdfs)
      * [7. Test HDFS](#7-test-hdfs)
      * [7. Shutdown HDFS](#7-shutdown-hdfs)
      * [References:](#references)

## 1. Install Required Packages

```bash
$ sudo apt install ssh rsync
```

## 2. Configure Java Environment

Find and modify the file `etc/hadoop/hadoop-env.sh` as following:

```bash
#export JAVA_HOME=${JAVA_HOME}
export JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")
```

Try the following command:

```bash
bin/hadoop
```

This will display the usage documentation for the hadoop script.

## 3. Modify Hadoop Configuration

Modify the file `etc/hadoop/core-site.xml` as following:

```xml
<configuration>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
```

Modify the file `etc/hadoop/hdfs-site.xml` as following:

```xml
<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
</configuration>
```

## 4. Check SSH Configuration

Now check that you can ssh to the localhost without a passphrase:

```bash
$ ssh localhost
```

If you cannot ssh to localhost without a passphrase, execute the following commands:

```bash
$ ssh-keygen -t rsa -P '' -f ~/.ssh/id_rsa
$ cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
$ chmod 0600 ~/.ssh/authorized_keys
```

## 5. Configure Hadoop Environment

Add the following lines at the end of  `~/.bashrc` file.

```bash
export HADOOP_HOME=/usr/local/hadoop # <-- Modify this line depends on hadoop installation
export HADOOP_MAPRED_HOME=$HADOOP_HOME 
export HADOOP_COMMON_HOME=$HADOOP_HOME 
export HADOOP_HDFS_HOME=$HADOOP_HOME 
export YARN_HOME=$HADOOP_HOME 
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native 
export PATH=$PATH:$HADOOP_HOME/sbin:$HADOOP_HOME/bin 
export HADOOP_INSTALL=$HADOOP_HOME 
```

Now apply all the changes into the current terminal.

```bash
$ source ~/.bashrc
```

## 6. Initialize HDFS

Format the filesystem:

```bash
$ hdfs namenode -format # or $ hadoop namenode -format
```

Start NameNode daemon and DataNode daemon:

```bash
$ start-dfs.sh # or $ start-all.sh
```

Browse the web interface for the NameNode; by default it is available at: http://localhost:50070/

## 7. Test HDFS

Make the HDFS directories required to execute MapReduce jobs:

```bash
$ hdfs dfs -mkdir /user
$ hdfs dfs -mkdir /user/$USER
```

Copy the input files into the distributed file system:

```bash
$ hdfs dfs -put etc/hadoop input
```

Run some of the examples provided:

```bash
$ hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.9.1.jar grep input output 'dfs[a-z.]+'
```

Examine the output files: Copy the output files from the distributed file system to the local file system and examine them:

```bash
$ hdfs dfs -get output output
```

## 7. Shutdown HDFS

When you’re done, stop the daemons with:

```bash
$ stop-dfs.sh # or $ stop-all.sh
```

## References:

https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/SingleCluster.html
https://www.tutorialspoint.com/hadoop/hadoop_enviornment_setup.htm
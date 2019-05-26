/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HdfsReader extends Configured implements Tool {
    
    public static final String FS_PARAM_NAME = "fs.defaultFS";
    
    public int run(String[] args) throws Exception {
        
        if (args.length < 2) {
            System.err.println("HdfsReader [hdfs input path] [local output path]");
            return 1;
        }
        
        Path inputPath = new Path(args[0]);
        String localOutputPath = args[1];
        Configuration conf = getConf();
        System.out.println("configured filesystem = " + conf.get(FS_PARAM_NAME));
        
        FileSystem fs = FileSystem.get(conf);
        InputStream is = fs.open(inputPath);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
    
        BufferedWriter os = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(localOutputPath)));

        for (int i=0; i<100; i++){
            os.write(br.readLine()+"\n");
        }
        
        os.flush();
        os.close();
        return 0;
    }
    
    public static void main( String[] args ) throws Exception {
        int returnCode = ToolRunner.run(new HdfsReader(), args);
        System.exit(returnCode);
    }
}


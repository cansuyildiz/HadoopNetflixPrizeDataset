/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 
 *
 * @author cansu OKEY!
 */
public class ModAndCountPerMovie {
    
    public static class IntArrayWritable extends ArrayWritable { 
        public IntArrayWritable() { 
            super(IntWritable.class); 
        }
        
        @Override
        public String toString() {
            String tmp = "";
            for (Writable s: this.get()){
                tmp += s.toString() + ",";
            }
            tmp = tmp.substring(0, tmp.length() - 1);
            return tmp;
        }
 
    } 
    
    public static class ModAndCountPerMovieMapper extends Mapper<LongWritable, Text, LongWritable, IntWritable> {
                
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            context.write(new LongWritable(Long.parseLong(parts[0])), new IntWritable(Integer.parseInt(parts[2])));
        }
    }

    public static class ModAndCountPerMovieReducer extends Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {
        
        @Override
        protected void reduce(LongWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int[] arr = new int[5];
            for (IntWritable value : values) {
                arr[value.get()-1] ++;
            }
            
            int max = 0;
            int maxIndex = 0;
            for (int i=0; i< arr.length; i++ ){
                if(arr[i] > max){
                    max = arr[i];
                    maxIndex = i;
                }
            }
           
            
            context.write(key, new IntWritable(maxIndex+1));
        }
    }

    
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Std");
        job.setJarByClass(ModAndCountPerMovie.class);
        job.setMapperClass(ModAndCountPerMovieMapper.class);
        //job.setCombinerClass(AverageStarPerMovieReducer.class);
        job.setReducerClass(ModAndCountPerMovieReducer.class);
        
        // mapper
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        // reducer
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

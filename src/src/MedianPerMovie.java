/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author cansu    OKEY!
 */
public class MedianPerMovie {

    
    public static class MedianPerMovieMapper extends Mapper<LongWritable, Text, LongWritable, IntWritable> {
                
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            context.write(new LongWritable(Long.parseLong(parts[0])), new IntWritable(Integer.parseInt(parts[2])));
        }
    }

    public static class MedianPerMovieReducer extends Reducer<LongWritable, IntWritable, LongWritable, IntWritable> {
        
        @Override
        protected void reduce(LongWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            for (IntWritable value : values) {
                arr.add(value.get());
            }
            Collections.sort(arr);
 
            context.write(key, new IntWritable(arr.get(arr.size()/2)));
        }
    }

    
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "median per movie");
        job.setJarByClass(MedianPerMovie.class);
        job.setMapperClass(MedianPerMovieMapper.class);
        //job.setCombinerClass(AverageStarPerMovieReducer.class);
        job.setReducerClass(MedianPerMovieReducer.class);
        
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

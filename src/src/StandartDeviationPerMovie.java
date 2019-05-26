
import java.io.IOException;
import java.util.ArrayList;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cansu OKEY!
 */
public class StandartDeviationPerMovie {
    
    public static class StandartDeviationPerMovieMapper extends Mapper<LongWritable, Text, LongWritable, IntWritable> {
        
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            context.write(new LongWritable(Long.parseLong(parts[0])), new IntWritable(Integer.parseInt(parts[2])));
        }

    }

    public static class StandartDeviationPerMovieReducer extends Reducer<LongWritable, IntWritable, LongWritable, FloatWritable> {
        
        @Override
        protected void reduce(LongWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            ArrayList<Integer> arr = new ArrayList<Integer>();
            int sum = 0;
            int N = 0;
            float std = 0.0f;
            
            // save values
            for (IntWritable value : values) {
                arr.add(value.get());
            } 
            
            for (Integer value : arr) {
                sum += value;
                N++;
            }
            float avg = ((float)sum) / ((float)N);
            
            for (Integer value : arr) {
                float diff = value - avg;
                std += diff*diff;       
            }
            
            std = std / ((float)(N-1));
            context.write(key, new FloatWritable(std));
            
        }
    }

    
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Std");
        job.setJarByClass(StandartDeviationPerMovie.class);
        job.setMapperClass(StandartDeviationPerMovieMapper.class);
        //job.setCombinerClass(AverageStarPerMovieReducer.class);
        job.setReducerClass(StandartDeviationPerMovieReducer.class);
        
        // mapper
        job.setMapOutputKeyClass(LongWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        // reducer
        job.setOutputKeyClass(LongWritable.class);
        job.setOutputValueClass(FloatWritable.class);

        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
    
}

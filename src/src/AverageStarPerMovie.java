/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * @author cansu
 */
public class AverageStarPerMovie {
    
    public static class AverageStarPerMovieMapper extends Mapper<Object, Text, LongWritable, IntWritable> {
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            context.write(new LongWritable(Long.parseLong(parts[0])), new IntWritable(Integer.parseInt(parts[2])));
        }
    }

    public static class AverageStarPerMovieReducer extends Reducer<LongWritable, IntWritable, LongWritable, FloatWritable> {
        @Override
        protected void reduce(LongWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            int count = 0;
            for (IntWritable value : values) {
                sum += value.get();
                count++;
            }
            float result = ((float) sum) / count;
            context.write(key, new FloatWritable(result));
        }
    }

    
    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Average Star Per Movie");
        job.setJarByClass(AverageStarPerMovie.class);
        job.setMapperClass(AverageStarPerMovieMapper.class);
        //job.setCombinerClass(AverageStarPerMovieReducer.class);
        job.setReducerClass(AverageStarPerMovieReducer.class);
        
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
 
 



/*
Job job = new Job();
        job.setJarByClass(AverageStarPerMovieJobs.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(AverageStarPerMovieMapper.class);
        job.setReducerClass(AverageStarPerMovieReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FloatWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);


film,user,rate,date
1,1488844,3,2005-09-06
1,822109,5,2005-05-13
1,885013,4,2005-10-19
1,30878,4,2005-12-26
1,823519,3,2004-05-03
1,893988,3,2005-11-17
 
  if (key.get() > 0) {
   try {
     CSVParser parser = new CSVParser();
     String[] lines = parser.parseLine(value.toString());
 
     SimpleDateFormat formatter = 
     new SimpleDateFormat("EEEEE, MMMMM dd, yyyy HH:mm:ss Z");
     Date dt = formatter.parse(lines[3]);
     formatter.applyPattern("dd-MM-yyyy");
 
     String dtstr = formatter.format(dt);
     context.write(new Text(dtstr), new FloatWritable(1));
   } catch (ParseException e) {}
  }
 }
}*/
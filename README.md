# Big Data Analysis on Netflix Prize Dataset with Hadoop

   * [Big Data Analysis on Netflix Prize Dataset with Hadoop](#big-data-analysis-on-netflix-prize-dataset-with-hadoop)
      * [Introduction](#introduction)
      * [How to Use](#how-to-use)
      * [References](#references)

## Introduction

The Apache Hadoop software library is a framework that allows for the distributed processing of large data sets across clusters of computers using simple programming models. [1]

The goal of this project is implementing statistical functions on big data using Apache Hadoop which is distributed open source platform. 

Netflix Prize dataset has been chosen as a dataset. Follow the [link](https://www.kaggle.com/netflix-inc/netflix-prize-data/downloads/netflix-prize-data.zip/1) to download the dataset.

__Statistical functions:__

- `Histogram:` View rate of each movie.

- `Mean:` Average star of each movie.

- `Median:` Median star of each movie.

- `Mod and Count:` Mod and count of star of each movie.

- `Standart Deviation:` Std of star distribution over movies.

## How to Use

1. Download Netflix Prize dataset from the following the [link](https://www.kaggle.com/netflix-inc/netflix-prize-data/downloads/netflix-prize-data.zip/1).

2. Install Java Development Kit 8:

    ```bash
    $ sudo apt install openjdk-8-jdk
    ```

3. Install Apache Hadoop 2.7.7 following the tutorial: 

4. Clone the repository:

    ```bash
    $ git clone https://github.com/cansuyildiz/HadoopNetflixPrizeDataset
    ```

3. Run jar files with parameters like below:

   ```bash
   $ cd executable/
   $ java -jar AverageStarPerMovie.jar <hdfs_input> <hdfs_output>
   ```

6. Examples:

   ```bash
   $ java -jar AverageStarPerMovie.jar /user/netflix /user/netflix_output/
   $ java -jar HistogramOfMovies.jar /user/netflix /user/netflix_output/
   $ java -jar MedianPerMovie.jar /user/netflix /user/netflix_output/
   $ java -jar ModAndCountPerMovie.jar /user/netflix /user/netflix_output/
   $ java -jar StandartDeviationPerMovie.jar /user/netflix /user/netflix_output/
   ```


## References

[1] https://hadoop.apache.org/

 
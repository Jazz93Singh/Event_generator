package org.carleton.generator.sensors;


import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.carleton.generator.event.Event;
import org.carleton.generator.event.EventGenerator;

import java.util.Properties;


public class sensor2 {

    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public static void main(String[] args)  throws Exception {


        Constants constants = null;


        // ip of server where data needs to be sent
        String KafkaIP = "localhost";

        //setting kafka properties {setting this properties after env will led to error, it must be difined in begining}
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", KafkaIP+":9092");
        properties.setProperty("zookeeper.connect", KafkaIP+":2181");
        properties.setProperty("group.id", "test");


        //setting the envrionment variable as StreamExecutionEnvironment
        StreamExecutionEnvironment envrionment = StreamExecutionEnvironment.getExecutionEnvironment();

        envrionment.setParallelism(1);



        DataStream<Event> stream1  = envrionment
                .addSource(new EventGenerator(constants.sensor2_data_rate,constants.sensor2_run_time_sec,1,2,constants.sensor2_lowerbound,
                        constants.sensor2_UpperBound ))
                .name("stream 2")
                .setParallelism(1);

        System.out.println("");


        //Sending the stream to timekeeper
        stream1.map(new RichMapFunction<Event, String>() {
            @Override
            public String map(Event event) throws Exception {
                String tuple = event.toString();
                return tuple + "\n";
            }
        }).writeToSocket(constants.timekeeper_ip, 8002, new SimpleStringSchema() );



        // Sending the stream to mobile phone
        DataStreamSink<String> total_tuples = stream1.map(new RichMapFunction<Event, String>() {

            IntCounter Sensor2_tuple_count;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                this.Sensor2_tuple_count = getRuntimeContext().getIntCounter("total_tuples");
            }

            @Override
            public String map(Event event) throws Exception {
                String tuple = event.toString();
                Sensor2_tuple_count.add(1);

                System.lineSeparator();
                System.out.print(ANSI_PURPLE + " \r  sensor 2 count =" + Sensor2_tuple_count);
                System.out.flush();

//              System.out.println(ANSI_BLUE + tuple);

                return tuple + "\n";
            }
        }).writeToSocket(constants.mobile_ip, 7002, new SimpleStringSchema() );
//






        //start the execution
        JobExecutionResult executionResult = envrionment.execute();

        Integer number_of_tuples = (Integer) executionResult.getAllAccumulatorResults().get("total_tuples");
        int input_rate = number_of_tuples/constants.sensor1_run_time_sec;

        System.out.println("\n");
        System.out.println(ANSI_BLUE + "  Expected Input rate of sensor 2    = " + constants.sensor1_data_rate + " tuples/second");
        System.out.println(ANSI_RED + "  Actual Input rate of sensor 2      = " + input_rate + " tuples/second");
        System.out.println(ANSI_PURPLE + "  Total # of tuples sent by sensor 2 = " + number_of_tuples );





    }// main



} //class




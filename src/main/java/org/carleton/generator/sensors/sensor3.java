package org.carleton.generator.sensors;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.carleton.generator.event.Event;
import org.carleton.generator.event.EventGenerator;

import java.util.Properties;


public class sensor3 {

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
                .addSource(new EventGenerator(constants.sensor3_data_rate,constants.sensor3_run_time_sec,1,3,10, 20 ))
                .name("stream 1")
                .setParallelism(1);



        stream1.map(new RichMapFunction<Event, String>() {
            @Override
            public String map(Event event) throws Exception {
                String tuple = event.toString();
                System.out.println(ANSI_CYAN + tuple);
                return tuple + "\n";
            }
        }).writeToSocket(constants.mobile_ip, 7003, new SimpleStringSchema() );


        stream1.map(new RichMapFunction<Event, String>() {
            @Override
            public String map(Event event) throws Exception {
                String tuple = event.toString();
                return tuple + "\n";
            }
        }).writeToSocket(constants.timekeeper_ip, 8003, new SimpleStringSchema() );


        //start the execution
        envrionment.execute(" Started sending patient data ");


    }// main



} //class




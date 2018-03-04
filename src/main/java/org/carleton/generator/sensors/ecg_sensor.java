package org.carleton.generator.sensors;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.carleton.generator.event.stream_event;
import org.carleton.generator.generators.EventGenerator_ecg;
import org.carleton.generator.variables.Constants;


public class ecg_sensor {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";


    public static void main(String[] args)  throws Exception {

        Constants constants = null;

        //setting the envrionment variable as StreamExecutionEnvironment
        StreamExecutionEnvironment envrionment = StreamExecutionEnvironment.getExecutionEnvironment();

        envrionment.setParallelism(1);


        DataStream<stream_event> stream1  = envrionment
                    .addSource(new EventGenerator_ecg(constants.ecg_data_rate,constants.ecg_run_time_sec ,1,1))
                    .name("stream 1")
                    .setParallelism(1);



        //Sending the stream to timekeeper
        stream1.map(new MapFunction<stream_event, String>() {
            @Override
            public String map(stream_event event) throws Exception {
                String tuple = event.toString();
                System.out.println(tuple);
                return tuple + "\n";
            }
        }).writeToSocket(constants.timekeeper_ip, 8001, new SimpleStringSchema());



        // Sending the stream to mobile phone
        DataStreamSink<String> total_tuples = stream1.map(new RichMapFunction<stream_event, String>() {

            IntCounter Sensor1_tuple_count;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                this.Sensor1_tuple_count = getRuntimeContext().getIntCounter("total_tuples");
            }


            @Override
            public String map(stream_event event) throws Exception {
                String tuple = event.toString();
                Sensor1_tuple_count.add(1);

                System.out.print(ANSI_PURPLE + "\r  ECG Sensor count =" + Sensor1_tuple_count);
                 System.out.flush();

//              System.out.println(ANSI_BLUE + tuple);

                return tuple + "\n";
            }
        }).writeToSocket(constants.mobile_ip, 7001, new SimpleStringSchema() );


        JobExecutionResult executionResult = envrionment.execute();


       Integer number_of_tuples = (Integer) executionResult.getAllAccumulatorResults().get("total_tuples");
       int input_rate = number_of_tuples/constants.ecg_run_time_sec;




       System.out.println("\n");
       System.out.println(ANSI_BLUE + "  Expected Input rate of ECG sensor       = " + constants.ecg_data_rate + " tuples/second");
       System.out.println(ANSI_RED + "   Actual Input rate of ECG sensor         = " + input_rate + " tuples/second");
       System.out.println(ANSI_PURPLE + "  Total # of tuples sent by ECG sensor  = " + number_of_tuples );





    }// main


} //class




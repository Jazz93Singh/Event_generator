package org.carleton.generator.sensors;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.carleton.generator.event.stream_event;
import org.carleton.generator.generators.EventGenerator_resp;
import org.carleton.generator.variables.Constants;


public class respiration_sensor {

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
        

        //setting the envrionment variable as StreamExecutionEnvironment
        StreamExecutionEnvironment envrionment = StreamExecutionEnvironment.getExecutionEnvironment();

        envrionment.setParallelism(1);


        DataStream<stream_event> respiration_stream  = envrionment
                .addSource(new EventGenerator_resp(constants.resp_data_rate,constants.resp_run_time_sec,1,3))
                .name("respiration rate")
                .setParallelism(1);



        //Sending the stream to timekeeper
        respiration_stream.map(new RichMapFunction<stream_event, String>() {
            @Override
            public String map(stream_event event) throws Exception {
                String tuple = event.toString();
                return tuple + "\n";
            }
        }).writeToSocket(constants.timekeeper_ip, 8003, new SimpleStringSchema() );




        // Sending the stream to mobile phone
        DataStreamSink<String> total_tuples = respiration_stream.map(new RichMapFunction<stream_event, String>() {

            IntCounter Sensor3_tuple_count;

            @Override
            public void open(Configuration parameters) throws Exception {
                super.open(parameters);
                this.Sensor3_tuple_count = getRuntimeContext().getIntCounter("total_tuples");
            }

            @Override
            public String map(stream_event event) throws Exception {
                String tuple = event.toString();
                Sensor3_tuple_count.add(1);

                System.out.print(ANSI_PURPLE + "                                                                                             \r  sensor 3 count =" + Sensor3_tuple_count);
                System.out.flush();


//              System.out.println(ANSI_BLUE + tuple);

                return tuple + "\n";
            }
        }).writeToSocket(constants.mobile_ip, 7003, new SimpleStringSchema() );



        JobExecutionResult executionResult = envrionment.execute();

        Integer number_of_tuples = (Integer) executionResult.getAllAccumulatorResults().get("total_tuples");
        int input_rate = number_of_tuples/constants.resp_run_time_sec;

        System.out.println("\n");
        System.out.println(ANSI_BLUE + "  Expected Input rate of sensor 3      = " + constants.resp_data_rate + " tuples/second");
        System.out.println(ANSI_RED + "  Actual Input rate of sensor 3         = " + input_rate + " tuples/second");
        System.out.println(ANSI_PURPLE + "  Total # of tuples sent by sensor 3 = " + number_of_tuples );






    }// main



} //class




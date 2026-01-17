package com.pm.analyticsservice.kafka;


import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import patient.events.PatientEvents;

public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

    //byte array as when we converted data to byte array in kafka producer
    @KafkaListener(topics = "patient" , groupId = "analytics-service")
    public void consumeEvent(byte[]event){

        try {
            PatientEvents patientEvent = PatientEvents.parseFrom(event);
            // perform any business related analytics here
          log.info("Received patient event : [ PatientId={} + PatientName={} + PatientEmail={}",
                  patientEvent.getPatientId(),
                  patientEvent.getName(),
                  patientEvent.getEmail());
        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}",e.getMessage());
        }

    }
}

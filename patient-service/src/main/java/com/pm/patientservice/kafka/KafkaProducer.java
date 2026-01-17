package com.pm.patientservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    //crate a key value pair that we send data to
    private final KafkaTemplate<String , byte[]> kafkaTemplate;


    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


}

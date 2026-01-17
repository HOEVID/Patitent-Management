package com.pm.patientservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


//doing same this as creating grpc request but notm anually
@Service
public class BillingServiceGrpcClient {
    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);
    // holds client and make synchronous calls to grpc server {it waits for response when we use blocking stub}
    private final BillingServiceGrpc.BillingServiceBlockingStub blockingStub;


    // localhost:9001/BillingService/CreatePatientAccount
    // aws.grpc.12121/BillingService/CreatePatientAccount
    public BillingServiceGrpcClient(

            @Value("${billing.service.address:localhost}") String serverAddress,
            @Value("${billing.service.grpc.port:9001}") int serverPort
    ) {

        log.info("Connecting to billing Service GRPC at {}:{}", serverAddress, serverPort);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, serverPort).usePlaintext().build();
        blockingStub = BillingServiceGrpc.newBlockingStub(channel);
    }

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder().setPatientId(patientId)
                .setName(name).setEmail(email).build();

        BillingResponse response = blockingStub.createBillingAccount(request);
        log.info("Received response from billing service by GRPC {}",response);

        return response;

    }
}

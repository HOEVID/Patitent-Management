package com.pm.stack;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.rds.*;
import software.amazon.awscdk.services.route53.CfnHealthCheck;

public class LocalStack extends Stack {

    //creating vpc
    private final Vpc vpc;

    DatabaseInstance authServiceDb ;

    DatabaseInstance patientServiceDb ;

    CfnHealthCheck authDbHealthCheck ;

    CfnHealthCheck patientDbHealthCheck;
    public LocalStack(final App scope, String id, final StackProps props ){

        super(scope,id,props);
        this.vpc = createVpc();

        this.authServiceDb =createDatabase("AuthServiceDB","auth-service-db");
        this.patientServiceDb=createDatabase("PatientServiceDB","patient-service-db");
        this.authDbHealthCheck= createDbHealthCheck(authServiceDb,"AuthServiceDBHealthCheck");
        this.patientDbHealthCheck = createDbHealthCheck(patientServiceDb,"PatientServiceDBHealthCheck");
    }

    private Vpc createVpc(){

        return Vpc.Builder
        .create(this,"PatientManagementVPC")
        .vpcName("PatientManagementVPC")
        .maxAzs(2).build();
    }
    private DatabaseInstance createDatabase(String id, String dbName) {
        return DatabaseInstance.Builder.create(this, id)
                .engine(DatabaseInstanceEngine.postgres(
                        PostgresInstanceEngineProps.builder()
                                .version(PostgresEngineVersion.VER_17_2)
                                .build()))
                .vpc(vpc)
                .instanceType(InstanceType.of(
                        InstanceClass.BURSTABLE2,
                        InstanceSize.MICRO))
                .allocatedStorage(20)
                .credentials(Credentials.fromGeneratedSecret("admin_user"))
                .databaseName(dbName)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }
        private CfnHealthCheck createDbHealthCheck(DatabaseInstance db ,String id){

        return CfnHealthCheck.Builder.create(this,id)
                .healthCheckConfig(CfnHealthCheck.HealthCheckConfigProperty.builder()
                        .type("TCP")
                        .port(Token.asNumber(db.getDbInstanceEndpointPort()))
                        .ipAddress(db.getDbInstanceEndpointAddress())
                        .requestInterval(30)
                        .failureThreshold(3)
                        .build())
                .build();

}
    public static void main(final String[] args){

App app = new App(AppProps.builder().outdir("./cdk.out").build());
StackProps props=  StackProps.builder().synthesizer
        (new BootstraplessSynthesizer())
                .build();

new LocalStack(app, "localstack",props);
app.synth();
System.out.println("App synthesizing in progress..");



    }
}

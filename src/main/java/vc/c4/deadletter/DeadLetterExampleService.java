package vc.c4.deadletter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Enabling @Scheduled
public class DeadLetterExampleService {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(DeadLetterExampleService.class, args);
  }

}

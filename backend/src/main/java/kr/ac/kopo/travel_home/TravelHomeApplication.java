package kr.ac.kopo.travel_home;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class TravelHomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelHomeApplication.class, args);
	}

}

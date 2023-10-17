package wanted.preonboarding;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
public class PreOnboardingApplication {

	public static void main(String[] args) {
		SpringApplication.run(PreOnboardingApplication.class, args);
	}

	@PostConstruct
	public void setup() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}

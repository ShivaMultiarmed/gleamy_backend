package mikhail.shell.gleamy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;

@SpringBootApplication//(exclude = JmsAutoConfiguration.class)
public class GleamyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GleamyApplication.class, args);
	}

}

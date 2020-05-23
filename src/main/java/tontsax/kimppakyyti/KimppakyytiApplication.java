package tontsax.kimppakyyti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
public class KimppakyytiApplication {
	
	@GetMapping("*")
	@ResponseBody
	public String hello() {
		return "Hello Kimppakyytisovellus!";
	}

	public static void main(String[] args) {
		SpringApplication.run(KimppakyytiApplication.class, args);
	}

}

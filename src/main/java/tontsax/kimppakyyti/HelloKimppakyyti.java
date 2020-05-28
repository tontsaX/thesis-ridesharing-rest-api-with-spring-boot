package tontsax.kimppakyyti;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloKimppakyyti {

	@GetMapping("/")
	@ResponseBody
	public String hello() {
		return "Hello Kimppakyytisovellus!";
	}
}

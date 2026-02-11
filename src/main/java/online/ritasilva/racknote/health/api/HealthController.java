package online.ritasilva.racknote.health.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

@RestController
public class HealthController {

    @GetMapping(value= "/racknote/alive", produces = MediaType.TEXT_PLAIN_VALUE)
    public String alive() {
        return "racknote alive";
    }
}

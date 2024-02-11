package es.ucm.guess.control;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "error"; // Asegúrate de tener una vista llamada "error" para mostrar los errores.
    }
}


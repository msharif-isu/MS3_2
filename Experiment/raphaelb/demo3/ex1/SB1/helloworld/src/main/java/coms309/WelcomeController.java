package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "My name is Raphael and this is a welcome page to start with the hello world";
    }

    @GetMapping("/{input}")
    public String welcome(@PathVariable String input) {
        if (input.contentEquals("rock")) {
            return "paper you lose";
        }
        else if (input.contentEquals("paper")) {
            return "scissors you lose";
        }
        else if (input.contentEquals("scissors")) {
            return "rock you lose";
        }
        else {
            return "you don't know how to play the game";
        }


    }
}

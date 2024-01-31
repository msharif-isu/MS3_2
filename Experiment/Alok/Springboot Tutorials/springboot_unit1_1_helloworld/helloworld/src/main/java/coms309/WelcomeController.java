package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "This is my project now";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return String.format("This is %s's project now", name);
    }
}

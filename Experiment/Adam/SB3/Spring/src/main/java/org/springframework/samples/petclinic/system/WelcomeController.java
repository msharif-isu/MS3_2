package org.springframework.samples.petclinic.system;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome</br> Go to localhost:8080/owner/create to create dummy data </br>" + "\n"
                + "Go to localhost:8080/owners to look at all the owners";
    }
}

package com.project.trivia.PelicanQuestions;

import com.project.trivia.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PelicanQuestionController {

    @Autowired
    PelicanQuestionRepository pelicanQuestionRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @GetMapping(path = "/pelican")
    List<PelicanQuestion> getAllPelicans() {
        return pelicanQuestionRepository.findAll();
    }

    @GetMapping(path = "/pelican/{id}")
    PelicanQuestion getQuestionById(@PathVariable int id){
        return pelicanQuestionRepository.findById(id);
    }

    @PostMapping(path = "/puestion")
    String createQuestion(@RequestBody PelicanQuestion question){
        if (question == null)
            return failure;
        pelicanQuestionRepository.save(question);
        return success;
    }


    @PutMapping("/puestion/{id}")
    public PelicanQuestion updateUser(@PathVariable int id, @RequestBody PelicanQuestion request){
        PelicanQuestion user = pelicanQuestionRepository.findById(id);
        if(user == null)
            return null;
        pelicanQuestionRepository.save(request);
        return pelicanQuestionRepository.findById(id);
    }

    @DeleteMapping(path = "/pelete/{id}")
    public String deleteUser(@PathVariable int id){
        pelicanQuestionRepository.deleteById(id);
        return success;
    }

    @GetMapping(path = "/getPuestions")
    public int[] threeRandom(){
        ArrayList<Integer> e = new ArrayList<>();

        for (PelicanQuestion pelican: getAllPelicans()) {
            e.add(pelican.getId());
        }
        Random rand = new Random();
        Set<Integer> pickedIndices = new HashSet<>();
        int[] pickedQuestions = new int[3];

        for (int i = 0; i < 3; i++) {
            int randomIndex = rand.nextInt(e.size());
            while (pickedIndices.contains(randomIndex)) {
                randomIndex = rand.nextInt(randomIndex);
            }
            pickedIndices.add(randomIndex);
            pickedQuestions[i] = e.get(randomIndex);
        }

        return pickedQuestions;
    }

    @PutMapping(path = "/correct/{id}/{loggedInUser}")
    public PelicanQuestion placement(@PathVariable int id, @PathVariable String loggedInUser){
        PelicanQuestion anwseredQuestion = pelicanQuestionRepository.findById(id);
        if(Objects.equals(pelicanQuestionRepository.findById(id).getFirst(), "")){
            pelicanQuestionRepository.findById(id).setFirst(loggedInUser);
        }else if (Objects.equals(pelicanQuestionRepository.findById(id).getSecond(), "")){
            pelicanQuestionRepository.findById(id).setSecond(loggedInUser);
        }else if (Objects.equals(pelicanQuestionRepository.findById(id).getThird(), "")){
            pelicanQuestionRepository.findById(id).setThird(loggedInUser);
        }

        pelicanQuestionRepository.save(anwseredQuestion);

        return pelicanQuestionRepository.findById(id);
    }

    @PutMapping(path = "/reset")
    public PelicanQuestion resetPlacement(@PathVariable int id){
        PelicanQuestion anwseredQuestion = pelicanQuestionRepository.findById(id);
        pelicanQuestionRepository.findById(id).setFirst("");
        pelicanQuestionRepository.findById(id).setSecond("");
        pelicanQuestionRepository.findById(id).setThird("");

        pelicanQuestionRepository.save(anwseredQuestion);

        return pelicanQuestionRepository.findById(id);
    }



}

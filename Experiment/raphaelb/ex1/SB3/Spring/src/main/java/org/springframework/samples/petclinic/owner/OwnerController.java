/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.owner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @Modified By Tanmay Ghosh
 * @Modified By Vivek Bengre
 */
@RestController
class OwnerController {

    @Autowired
    OwnerRepository ownersRepository;

    private final Logger logger = LoggerFactory.getLogger(OwnerController.class);

     // function just to create dummy data
    @RequestMapping(method = RequestMethod.GET, path = "/trivia/populate")
    public String createDummyData() {
        Owners o1 = new Owners(1, "History", "Who was the first US President?", "George Washington");
        Owners o2 = new Owners(2, "Philosophy", "Who was the first Philosopher?", "Thales of Miletus");
        Owners o3 = new Owners(3, "Art", "Who painted the Mona Lisa?", "Leonardo DaVinci");
        Owners o4 = new Owners(4, "Music", "What instrument has a first and a second in the orchestra?", "Violin");
        Owners o5 = new Owners (5, "Mythology", "Who was the hero in the Ulster Cycle?", "Cu Chulainn");
        ownersRepository.save(o1);
        ownersRepository.save(o2);
        ownersRepository.save(o3);
        ownersRepository.save(o4);
        ownersRepository.save(o5);
        return "Successfully created dummy data";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{category}")
    public List<Owners> getAllOwners() {
        logger.info("Entered into Controller Layer");
        List<Owners> results = ownersRepository.findAll();
        logger.info("Number of Records Fetched:" + results.size());
        return results;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/trivia/{ownerId}")
    public Optional<Owners> findOwnerById(@PathVariable("ownerId") int id) {
        logger.info("Entered into Controller Layer");
        Optional<Owners> results = ownersRepository.findById(id);
        return results;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/trivia/{ownerId}/q")
    public String getQuestion(@PathVariable("ownerId") int id) {
        Owners o7 = ownersRepository.getReferenceById(id);

        String s = "You got the category " + o7.getCategories();
        String r = s + " and the question was: " + o7.getQuestions();
        return r;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/trivia/{ownerId}/a")
    public String getAnswer(@PathVariable("ownerId") int id) {

        Owners o7 = ownersRepository.getReferenceById(id);
        String s = "You got the category " + o7.getCategories();
        String r = s + " and the answer was: " + o7.getAnswers();
        return r;
    }



}

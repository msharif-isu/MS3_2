package com.project.trivia.Queryboard;

import com.project.trivia.Questions.Question;

import java.util.Collections;
import java.util.List;

public class Query {

    public List<Question> Randomize(List<Question> questionList) {
        Collections.shuffle(questionList);
        return questionList;
    }


}
package com.project.trivia.Queryboard;

import com.project.trivia.Questions.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Query {

    public List<Question> Randomize(List<Question> questionList) {
        Collections.shuffle(questionList);
        return questionList;
    }


    public static List<Question> limitList(List<Question> questionList, int limit) {
        Collections.shuffle(questionList);
        List<Question> limitedList = new ArrayList<>();
        for(int i=0; i<limit; i++) {
            limitedList.add(questionList.get(i));
        }

        return limitedList;
    }

    public static List<Question> joinList(List<Question> questionList1, List<Question> questionList2) {
        Collections.shuffle(questionList1);
        Collections.shuffle(questionList2);
        List<Question> combinedList = new ArrayList<>();
        combinedList.addAll(questionList1);
        combinedList.addAll(questionList2);

        return combinedList;
    }

    public static List<Question> topicFilter(List<Question> questionList1, String topic) {
        questionList1.removeIf(n -> (!n.getQuestionType().equals(topic)));

        return questionList1;
    }

}
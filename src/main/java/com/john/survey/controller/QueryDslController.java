package com.john.survey.controller;

import com.john.survey.dao.NpsQuestionRepository;
import com.john.survey.entities.NpsQuestion;
import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/query")
public class QueryDslController {

    @Autowired
    private NpsQuestionRepository npsQuestionRepository;

    @RequestMapping(value="/nps-questions", method = RequestMethod.GET)
    public Page<NpsQuestion> index(
            @QuerydslPredicate(root = NpsQuestion.class) Predicate predicate,
            @PageableDefault(sort = { "answeredAt" }) Pageable pageable) {

        return npsQuestionRepository.findAll(predicate, pageable);
    }
}

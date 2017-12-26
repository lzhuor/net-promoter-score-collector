package com.john.survey.controller;

import com.john.survey.dao.NpsQuestionRepository;
import com.john.survey.entities.NpsQuestion;
import com.querydsl.core.types.Predicate;
import io.swagger.annotations.ApiOperation;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/nps-questions")
public class NpsController {

    @Autowired
    private NpsQuestionRepository npsQuestionRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Page<NpsQuestion> index(
            @QuerydslPredicate(root = NpsQuestion.class) Predicate predicate,
            @PageableDefault(sort = { "answeredAt" }) Pageable pageable) {

        return npsQuestionRepository.findAll(predicate, pageable);
    }

    @ApiOperation(value = "Get all NPS questions")
    @RequestMapping(value = "/last", params = "customerReference", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NpsQuestion> getLastUnAnsweredNpsQuestion(@RequestParam(value = "customerReference", required = false) String customerReference) {

        NpsQuestion latestQuestion = npsQuestionRepository.findTopByCustomerReferenceOrderByAnsweredAtDesc(customerReference);

        if (latestQuestion == null) {
            throw new NullPointerException("customerReference is not associated with any questions");
        } else {
            return new ResponseEntity<>(latestQuestion, HttpStatus.OK);
        }
    }

    @ApiOperation(value = "Create new NPS question")
    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NpsQuestion> createNpsQuestion(@RequestBody NpsQuestion npsQuestion) {

        String customerReference = npsQuestion.getCustomerReference();
        return new ResponseEntity<>(
                npsQuestionRepository.insert(new NpsQuestion(customerReference)),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Find qualified customers for new NPS question", notes = "Returns a list of qualified customer references")
    @RequestMapping(value="/participants/qualified", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public @ResponseBody Set<String> findCustomersAnsweredBeforeCertainDays(
            @RequestParam(value="daysAfterLatest", defaultValue = "30") int daysAfterLatest) {

        Date daysAgo = new DateTime(new Date()).minusDays(daysAfterLatest).toDate();
        return ListAdapter
                .adapt(npsQuestionRepository.findCustomersAnsweredBeforeDate(daysAgo))
                .groupBy(NpsQuestion::getCustomerReference)
                .keySet()
                .toSet();
    }

    @ApiOperation(value = "Get NPS question by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NpsQuestion> getNpsResponseById(@PathVariable String id) {

        NpsQuestion NpsQuestion = npsQuestionRepository.findOne(id);

        if (NpsQuestion == null) { throw new NullPointerException("Not Exist"); }

        return new ResponseEntity<>(NpsQuestion, HttpStatus.OK);
    }

    @ApiOperation(value = "Update the answer of NPS question")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NpsQuestion> answerNpsQuestion(
            @PathVariable String id,
            @RequestBody NpsQuestion data) {

        NpsQuestion NpsQuestion = npsQuestionRepository.findOne(id);

        if (NpsQuestion.isAnswered()) return new ResponseEntity<>(NpsQuestion, HttpStatus.CONFLICT);

        NpsQuestion.setAnswer(data.getAnswer());
        NpsQuestion.setAnswered(true);

        return new ResponseEntity<>(npsQuestionRepository.save(NpsQuestion), HttpStatus.OK);
    }
}
package li.zhuoran.survey.controller;

import li.zhuoran.survey.entities.NpsQuestion;
import li.zhuoran.survey.exception.SurveyException;
import li.zhuoran.survey.services.NpsFinderService;
import li.zhuoran.survey.services.NpsMutatorService;
import li.zhuoran.survey.services.SlackService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@EnableAsync
@RequestMapping("/api/v1/nps-questions")
public class NpsController {

    @Inject
    private NpsFinderService npsFinderService;

    @Inject
    private NpsMutatorService npsMutatorService;

    @Inject
    private SlackService slackService;

    @ApiOperation(value = "Get the latest NPS question given a customerReference")
    @RequestMapping(value = "/latest", params = "customerReference", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NpsQuestion> getLastUnAnsweredNpsQuestion(
            @RequestParam(value = "customerReference") String customerReference) throws SurveyException {

        return new ResponseEntity<>(
                npsFinderService.findLastUnansweredByCustomerReference(customerReference),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Update the answer of NPS score")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NpsQuestion> answerNpsQuestion(
            @PathVariable String id,
            @RequestBody NpsQuestion answeredQuestion) throws SurveyException {

        NpsQuestion answered = npsMutatorService.answerNpsScore(id, answeredQuestion.getAnswer());

        return new ResponseEntity<>(
                answered,
                HttpStatus.OK);
    }

    @ApiOperation(value = "Update the answer of additional questions")
    @RequestMapping(value = "/{id}/additions", method = RequestMethod.PUT, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NpsQuestion> answerAdditions(
            @PathVariable String id,
            @RequestBody NpsQuestion answeredQuestion) {

        NpsQuestion answered = npsMutatorService.answerAdditions(
                id,
                answeredQuestion.getComment(),
                answeredQuestion.isContactable());

        slackService.asyncNotifyChannel(answered);

        return new ResponseEntity<>(answered, HttpStatus.OK);
    }
}
package li.zhuoran.survey.controller;

import li.zhuoran.survey.entities.EmailNotification;
import li.zhuoran.survey.entities.NpsQuestion;
import li.zhuoran.survey.exception.QuestionNotFoundException;
import li.zhuoran.survey.services.EmailService;
import li.zhuoran.survey.services.NpsCreatorService;
import li.zhuoran.survey.services.NpsFinderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/internal/v1/nps-questions")
public class InternalController {

    @Inject
    private NpsCreatorService npsCreatorService;

    @Inject
    private NpsFinderService npsFinderService;

    @Inject
    private EmailService emailService;

    @ApiOperation(value = "Batch creation of NPS survey")
    @ApiParam
    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NpsQuestion>> initializeSurvey(
            @ApiParam(value = "Limit of new NPS survey created", required = true)
            @RequestParam Optional<Integer> limit) {

        List<NpsQuestion> addedQuestions = limit.isPresent()
                ? npsCreatorService.createNpsQuestions(limit.get())
                : npsCreatorService.createNpsQuestions();

        return new ResponseEntity<>(
                addedQuestions,
                HttpStatus.OK);
    }

    @ApiOperation(value = "Batch send NPS survey email")
    @RequestMapping(value = "/emails", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<EmailNotification> sendNpsSurveyEmails() {

        return new ResponseEntity<>(
                emailService.sendNpsEmails(),
                HttpStatus.OK);
    }

    @ApiOperation(value = "Get NPS question by nps question id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<NpsQuestion> getNpsResponseById(
            @PathVariable String id) throws QuestionNotFoundException {

        return new ResponseEntity<>(
                npsFinderService.findById(id),
                HttpStatus.OK);
    }
}

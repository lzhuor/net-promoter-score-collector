package li.zhuoran.survey.services;

import li.zhuoran.survey.dao.NpsQuestionRepository;
import li.zhuoran.survey.entities.NpsQuestion;
import li.zhuoran.survey.utils.DateGenerator;
import org.apache.log4j.Logger;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static java.util.Objects.isNull;

@Service
public class NpsCreatorService {
    @Autowired
    private NpsQuestionRepository npsQuestionRepository;

    @Inject
    private NpsFinderService npsFinderService;

    @Inject
    private NpsCreatorService npsCreatorService;

    @Inject
    private AppInternalService appInternalService;

    private Logger logger = Logger.getLogger(getClass());

    public List<NpsQuestion> createNpsQuestions() {

        MutableList<NpsQuestion> newQuestions = gatherQuestionsToCreate();

        return npsQuestionRepository.insert(newQuestions);
    }

    public List<NpsQuestion> createNpsQuestions(int limit) {

        MutableList<NpsQuestion> limitedNewQuestions = gatherQuestionsToCreate().take(limit);

        return npsQuestionRepository.insert(limitedNewQuestions);
    }

    private MutableList<NpsQuestion> gatherQuestionsToCreate() {

        MutableList<NpsQuestion> maybeQuestions = appInternalService
                .getActiveCustomersStartedInvestingDaysAgo(90)
                .collect(NpsQuestion::new);

        MutableList<NpsQuestion> followUps = npsCreatorService.followUpExistingQuestions(maybeQuestions);
        MutableList<NpsQuestion> initials = npsCreatorService.initNewQuestions(maybeQuestions);

        return followUps
                .withAll(initials)
                .sortThisBy(each -> each.getCustomer().getReference());
    }

    private MutableList<NpsQuestion> initNewQuestions(MutableList<NpsQuestion> questions) {

        MutableSet<String> initialisedCustomers = npsFinderService.findAllCustomers();

        return questions
                .select(each -> !initialisedCustomers.anySatisfy(ref -> ref.equalsIgnoreCase(each.getCustomer().getReference())));
    }

    private MutableList<NpsQuestion> followUpExistingQuestions(MutableList<NpsQuestion> questions) {

        Date aYearAgo = DateGenerator.yearsAgo(1);

        return questions
                .select(each -> isEligibleForFollowUp(each.getCustomer().getReference(), aYearAgo));
    }

    private boolean isEligibleForFollowUp(String customerReference, Date date) {

        boolean eligible = false;

        NpsQuestion question = npsQuestionRepository.findTopByCustomerReferenceOrderByCreatedAtDesc(customerReference);

        if (!isNull(question)) {
            eligible = question.isAnswered() ?
                    isAnsweredBeforeDate(question, date)
                    : isCreatedBeforeDate(question, date);
        }

        return eligible;
    }

    private boolean isCreatedBeforeDate(NpsQuestion question, Date date) {

        Date createdAt = question.getCreatedAt();

        return createdAt.before(date);
    }

    private boolean isAnsweredBeforeDate(NpsQuestion question, Date date) {

        Date answeredAt = question.getAnsweredAt();

        if (isNull(answeredAt)) {
            logger.error(format("Answered %s , but 'answeredAt' is null", question));
        }

        return answeredAt.before(date);
    }
}

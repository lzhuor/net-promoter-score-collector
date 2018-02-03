package li.zhuoran.survey.services;

import li.zhuoran.survey.entities.Customer;
import li.zhuoran.survey.entities.EmailNotification;
import li.zhuoran.survey.entities.NpsQuestion;
import org.eclipse.collections.api.RichIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class EmailService {

    @Inject
    private NpsFinderService npsFinderService;

    @Inject
    private NpsMutatorService npsMutatorService;

    @Inject
    private AppInternalService appInternalService;

    @Value("${appServer.emailBatchSize:10}")
    private int EMAIL_BATCH_SIZE;

    public EmailNotification sendNpsEmails() {
        MutableList<NpsQuestion> topUnsentQuestionsOfEachCustomer = npsFinderService.findUnsentLatestNpsQuestionOfEachCustomer();
        RichIterable<RichIterable<NpsQuestion>> chunks = topUnsentQuestionsOfEachCustomer.chunk(EMAIL_BATCH_SIZE);

        MutableList<Customer> sentCustomers = new FastList<>();

        chunks.tap(each -> sentCustomers.withAll(sendEmailAndSetSent(each.toList())));

        EmailNotification emailNotification = new EmailNotification("NPS_SURVEY", sentCustomers);

        emailNotification.setTotalCount(topUnsentQuestionsOfEachCustomer.size());
        emailNotification.setSentCount(sentCustomers.size());

        return emailNotification;
    }

    private MutableList<Customer> sendEmailAndSetSent(MutableList<NpsQuestion> questions) {

        MutableList<Customer> customers = questions.collect(NpsQuestion::getCustomer);
        MutableList<String> referencesSent = appInternalService.sendNotification(
                new EmailNotification("NPS_SURVEY", customers));

        return questions
                .select(each -> referencesSent.contains(each.getCustomer().getReference()))
                .tap(each -> each.setEmailSent(true))
                .tap(npsMutatorService::updateNpsQuestion)
                .collect(NpsQuestion::getCustomer);
    }
}

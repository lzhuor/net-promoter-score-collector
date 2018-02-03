package li.zhuoran.survey.services;

import li.zhuoran.survey.dao.NpsQuestionRepository;
import li.zhuoran.survey.entities.NpsQuestion;
import li.zhuoran.survey.exception.QuestionNotFoundException;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NpsFinderService {
    @Autowired
    private NpsQuestionRepository npsQuestionRepository;

    public NpsQuestion findLastUnansweredByCustomerReference(String customerReference) throws QuestionNotFoundException {
        NpsQuestion latestQuestion = npsQuestionRepository.findTopByCustomerReferenceOrderByCreatedAtDesc(customerReference);

        if (latestQuestion == null) {
            throw new QuestionNotFoundException("customerReference is not associated with any questions");
        } else {
            return latestQuestion;
        }
    }

    public NpsQuestion findById(String npsQuestionId) throws QuestionNotFoundException {
        NpsQuestion npsQuestion = npsQuestionRepository.findOne(npsQuestionId);

        if (npsQuestion == null) {
            throw new QuestionNotFoundException("not exist");
        }

        return npsQuestion;
    }

    public MutableList<NpsQuestion> findUnsentLatestNpsQuestionOfEachCustomer() {
        MutableList<String> customerReferences = ListAdapter.adapt(npsQuestionRepository
                .findAll())
                .collect(each -> each.getCustomer().getReference())
                .distinct();

        return customerReferences
                .collect(ref -> npsQuestionRepository.findTopByCustomerReferenceOrderByCreatedAtDesc(ref))
                .select(NpsQuestion::isEmailUnsent);
    }

    public MutableSet<String> findAllCustomers() {

        return ListAdapter
                .adapt(npsQuestionRepository.findAll())
                .groupBy(each -> each.getCustomer().getReference())
                .keySet()
                .toSet();
    }
}

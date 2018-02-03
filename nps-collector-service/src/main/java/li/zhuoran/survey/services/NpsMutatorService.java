package li.zhuoran.survey.services;

import li.zhuoran.survey.dao.NpsQuestionRepository;
import li.zhuoran.survey.entities.NpsQuestion;
import li.zhuoran.survey.exception.QuestionAnsweredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NpsMutatorService {
    @Autowired
    NpsQuestionRepository npsQuestionRepository;

    public NpsQuestion answerNpsScore(String npsQuestionId, int answer) throws QuestionAnsweredException {
        NpsQuestion npsQuestion = npsQuestionRepository.findOne(npsQuestionId);

        if (npsQuestion.isAnswered()) throw new QuestionAnsweredException("Question has been answered");

        npsQuestion.setAnswer(answer);
        npsQuestion.setAnswered(true);
        npsQuestion.setAnsweredAt(new Date());

        return npsQuestionRepository.save(npsQuestion);
    }

    public NpsQuestion answerAdditions(String npsQuestionId, String comment, boolean contactable) {
        NpsQuestion npsQuestion = npsQuestionRepository.findOne(npsQuestionId);

        npsQuestion.setComment(comment);
        npsQuestion.setContactable(contactable);

        return npsQuestionRepository.save(npsQuestion);
    }

    public NpsQuestion updateNpsQuestion(NpsQuestion npsQuestion) {
        return npsQuestionRepository.save(npsQuestion);
    }
}

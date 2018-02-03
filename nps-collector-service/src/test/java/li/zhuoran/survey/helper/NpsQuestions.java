package li.zhuoran.survey.helper;

import li.zhuoran.survey.entities.Customer;
import li.zhuoran.survey.entities.NpsQuestion;
import li.zhuoran.survey.utils.DateGenerator;

public class NpsQuestions {
    public static NpsQuestion answeredDaysAgo(String customerReference, int daysAgo) {

        NpsQuestion question = answeredDaysAgoWithScoreOnly(customerReference, daysAgo);

        question.setComment("aComment");
        question.setContactable(false);
        question.setEmailSent(true);

        return question;
    }

    public static NpsQuestion answeredDaysAgoWithScoreOnly(String customerReference, int daysAgo) {

        String firstName = customerReference.concat(" name");
        Customer customer = new Customer(customerReference, firstName);

        NpsQuestion question = new NpsQuestion(customer);

        question.setEmailSent(true);
        question.setCreatedAt(DateGenerator.daysAgo(daysAgo + 1));
        question.setAnswer(10);
        question.setAnswered(true);
        question.setAnsweredAt(DateGenerator.daysAgo(daysAgo));

        return question;
    }

    public static NpsQuestion unansweredCreatedDaysAgo(String customerReference, int daysAgo) {

        String firstName = customerReference.concat(" name");
        Customer customer = new Customer(customerReference, firstName);

        NpsQuestion question = new NpsQuestion(customer);

        question.setCreatedAt(DateGenerator.daysAgo(daysAgo));

        return question;
    }
}

package li.zhuoran.survey.dao;

import li.zhuoran.survey.entities.NpsQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * MongoRepository Inheritance of NpsQuestion Collection
 * @see <a href="https://docs.spring.io/spring-data/mongodb/docs/1.2.0.RELEASE/reference/html/mongo.repositories.html" />
 * @author John Zhuoran Li
 */
public interface NpsQuestionRepository extends MongoRepository<NpsQuestion, String>, QueryDslPredicateExecutor<NpsQuestion> {
    NpsQuestion findTopByCustomerReferenceOrderByCreatedAtDesc(String name);
}
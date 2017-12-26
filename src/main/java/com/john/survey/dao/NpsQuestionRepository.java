package com.john.survey.dao;

import com.john.survey.entities.NpsQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.Date;
import java.util.List;

/**
 * MongoRepository Inheritance of NpsQuestion Collection
 * @see <a href="https://docs.spring.io/spring-data/mongodb/docs/1.2.0.RELEASE/reference/html/mongo.repositories.html" />
 * @author John Zhuoran Li
 */
public interface NpsQuestionRepository extends MongoRepository<NpsQuestion, String>, QueryDslPredicateExecutor<NpsQuestion> {
    @Query(value="{ 'updatedAt': {'$lte': ?0 }, 'isAnswered': true }")
    List<NpsQuestion> findCustomersAnsweredBeforeDate(Date beforeDate);

    NpsQuestion findTopByCustomerReferenceOrderByAnsweredAtDesc(String name);
}
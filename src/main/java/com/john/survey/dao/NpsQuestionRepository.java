package com.john.survey.dao;

import com.john.survey.entities.NpsQuestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface NpsQuestionRepository extends MongoRepository<NpsQuestion, String> {
    List<NpsQuestion> findByCustomerReference(String customerReference);

    @Query(value="{ 'updatedAt': {'$lte': ?0 }, 'isAnswered': true }")
    List<NpsQuestion> findCustomersAnsweredBeforeDate(Date beforeDate);
}
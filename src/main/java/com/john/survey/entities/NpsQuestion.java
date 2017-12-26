package com.john.survey.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

@Document(collection = "nps_question")
public class NpsQuestion {

    @Id
    private String id;

    @Min(1)
    @Max(10)
    private Integer answer;

    @Indexed
    private String customerReference;

    private boolean isAnswered;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date answeredAt;

    public NpsQuestion() {

    }

    public NpsQuestion(String customerReference) {
        this.customerReference = customerReference;
        this.isAnswered = false;
        this.createdAt = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAnswer() {
        return answer;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public String getCustomerReference() {
        return customerReference;
    }

    public void setCustomerReference(String customerReference) {
        this.customerReference = customerReference;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        this.isAnswered = answered;
        this.answeredAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Date answeredAt) {
        this.answeredAt = answeredAt;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%s]", id);
    }
}

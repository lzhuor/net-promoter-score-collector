package li.zhuoran.survey.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Date;

import static java.util.Objects.isNull;

@Document(collection = "nps_question")
public class NpsQuestion {
    @Id
    private String id;

    @Min(0)
    @Max(10)
    private Integer answer;

    private boolean answered;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date createdAt;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date answeredAt;

    private String comment;

    private Customer customer;

    private boolean contactable;

    private boolean emailSent;

    public NpsQuestion() {

    }

    public NpsQuestion(Customer customer) {
        this.customer = customer;
        this.answered = false;
        this.createdAt = new Date();
        this.contactable = false;
        this.emailSent = false;
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

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isContactable() {
        return contactable;
    }

    public void setContactable(boolean contactable) {
        this.contactable = contactable;
    }

    public boolean isEmailSent() {
        return emailSent;
    }

    public boolean isEmailUnsent() { return !emailSent; }

    public void setEmailSent(boolean emailSent) {
        this.emailSent = emailSent;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public MutableMap toSlackMessage() {

        MutableMap message = UnifiedMap.newMap();

        message.put("color", getPriorityColor());
        message.put("fallback", toString());
        message.put("author_name", "New NPS survey submitted");
        message.put("footer", "Slack API");
        message.put("ts", new Date().getTime()/1000);
        message.put("fields", FastList.newListWith(
                UnifiedMap.newWithKeysValues(
                        "title", "<Reference> Score",
                        "value", String.format("<%s> %s", customer.getReference(), answer)),
                UnifiedMap.newWithKeysValues(
                        "title", "Comment",
                        "value", comment),
                UnifiedMap.newWithKeysValues(
                        "title", "Contactable",
                        "value", contactable ? "True" : "False"))
        );

        return message;
    }

    private String getPriorityColor() {
        String DANGER = "#dc3545";
        String OK = "#ffc107";
        String VERY_GOOD = "#28a745";

        String color = "";

        if (!isNull(answer)) {
            if (answer < 7) {
                color = DANGER;
            } else if (answer < 9) {
                color = OK;
            } else {
                color = VERY_GOOD;
            }
        }

        return color;
    }

    @Override
    public String toString() {
        return "NpsQuestion{" +
                "id='" + id + '\'' +
                ", answer=" + answer +
                ", answered=" + answered +
                ", createdAt=" + createdAt +
                ", answeredAt=" + answeredAt +
                ", comment='" + comment + '\'' +
                ", customer=" + customer +
                ", contactable=" + contactable +
                ", emailSent=" + emailSent +
                '}';
    }
}

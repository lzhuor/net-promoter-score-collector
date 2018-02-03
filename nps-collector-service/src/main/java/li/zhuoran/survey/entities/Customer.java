package li.zhuoran.survey.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

public class Customer {
    @Id
    @JsonProperty("_id")
    private String id;

    @Indexed
    private String reference;

    private CustomerInfo info;

    public Customer() {

    }

    public Customer(String reference, String firstName) {
        this.reference = reference;
        this.info = new CustomerInfo();
        this.info.setFirstName(firstName);
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public CustomerInfo getInfo() {
        return info;
    }

    public void setInfo(CustomerInfo info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", reference='" + reference + '\'' +
                ", info=" + info +
                '}';
    }
}

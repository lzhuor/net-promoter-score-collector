package li.zhuoran.survey.entities;

import java.util.List;

public class EmailNotification {
    private String notificationType;

    private List<Customer> customers;

    private int totalCount;

    private int sentCount;

    public EmailNotification() {

    }

    public EmailNotification(String notificationType, List<Customer> customers) {
        this.notificationType = notificationType;
        this.customers = customers;
        this.totalCount = customers.size();
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getSentCount() {
        return sentCount;
    }

    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    @Override
    public String toString() {
        return "EmailNotification{" +
                "notificationType='" + notificationType + '\'' +
                ", customers=" + customers +
                ", totalCount=" + totalCount +
                ", sentCount=" + sentCount +
                '}';
    }
}

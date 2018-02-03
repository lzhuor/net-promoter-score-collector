package li.zhuoran.survey.services;

import li.zhuoran.survey.entities.Customer;
import li.zhuoran.survey.entities.EmailNotification;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;

@Service
public class AppInternalService {

    private final RestTemplate restTemplate;

    @Value("${appServer.baseUrl}")
    private String BASE_URL;

    public AppInternalService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public MutableList<Customer> getActiveCustomersStartedInvestingDaysAgo(int daysAgo) {
        URI targetUrl = UriComponentsBuilder.fromUriString(BASE_URL)
                .path("/customers/query")
                .queryParam("isActive", true)
                .queryParam("startedInvestingDaysAgo", daysAgo)
                .queryParam("select", "user,info.lastName,info.firstName,reference")
                .build()
                .encode()
                .toUri();

        return ListAdapter.adapt(Arrays.asList(
                this.restTemplate.getForObject(targetUrl, Customer[].class)));
    }

    public MutableList<String> sendNotification(EmailNotification npsNotification) {
        MutableList<String> referencesSent = FastList.newList();

        try {
            URI targetUrl = UriComponentsBuilder.fromUriString(BASE_URL)
                    .path("/cron/email/reminders")
                    .build()
                    .encode()
                    .toUri();

            String[] response = this.restTemplate.postForObject(targetUrl, new HttpEntity<>(npsNotification), String[].class);

            referencesSent = ListAdapter.adapt(Arrays.asList(response));
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return referencesSent;
    }
}

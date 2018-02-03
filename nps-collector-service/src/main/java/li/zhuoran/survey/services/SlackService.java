package li.zhuoran.survey.services;

import li.zhuoran.survey.entities.NpsQuestion;
import org.apache.log4j.Logger;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;

@Service
public class SlackService {

    @Value("${slack.npsWebHookUrl}")
    private String npsWebHookUrl;

    private final RestTemplate restTemplate;

    private final Logger logger;

    public SlackService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        this.logger = Logger.getLogger(getClass());
    }

    @Async
    public void asyncNotifyChannel(NpsQuestion question) {
        try {
            this.restTemplate.postForLocation(npsWebHookUrl, buildMessage(question));
        } catch (RestClientException e) {
            logger.error(format("Failed to send %s to Slack channel, caused by %s", question, e.getMessage()));
        }
    }

    private MutableMap buildMessage(NpsQuestion question) {
        return UnifiedMap.newWithKeysValues(
                "attachments", FastList.newListWith(question.toSlackMessage()),
                "icon_emoji", ":wave:");
    }
}

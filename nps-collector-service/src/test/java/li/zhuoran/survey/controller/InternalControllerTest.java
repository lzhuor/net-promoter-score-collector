package li.zhuoran.survey.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import li.zhuoran.survey.App;
import li.zhuoran.survey.dao.NpsQuestionRepository;
import li.zhuoran.survey.entities.Customer;
import li.zhuoran.survey.entities.EmailNotification;
import li.zhuoran.survey.entities.NpsQuestion;
import li.zhuoran.survey.services.AppInternalService;
import li.zhuoran.survey.utils.DateGenerator;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static li.zhuoran.survey.helper.NpsQuestions.answeredDaysAgo;
import static li.zhuoran.survey.helper.NpsQuestions.unansweredCreatedDaysAgo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Every.everyItem;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class InternalControllerTest {
    private ObjectMapper mapper;

    private MediaType contentType;

    private MockMvc mockMvc;

    @MockBean
    private AppInternalService appInternalService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private NpsQuestionRepository npsQuestionRepository;

    private final String JOHN = "john";
    private final String LISA = "lisa";
    private final String ALICE = "alice";
    private final String BOB = "bob";

    @Before
    public void setup() {
        this.mapper = new ObjectMapper();
        this.contentType = new MediaType(
                MediaType.APPLICATION_JSON.getType(),
                MediaType.APPLICATION_JSON.getSubtype(),
                Charset.forName("utf8"));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.npsQuestionRepository.deleteAll();

        // Eligible for followup
        this.npsQuestionRepository.insert(
                answeredDaysAgo(JOHN, DateGenerator.DAYS_PER_YEAR * 2 + 1));
        this.npsQuestionRepository.insert(
                answeredDaysAgo(JOHN, DateGenerator.DAYS_PER_YEAR + 1));

        // Eligible for followup
        this.npsQuestionRepository.insert(
                unansweredCreatedDaysAgo(ALICE, DateGenerator.DAYS_PER_YEAR * 3 + 1));
        this.npsQuestionRepository.insert(
                answeredDaysAgo(ALICE, DateGenerator.DAYS_PER_YEAR * 2 + 1));
        this.npsQuestionRepository.insert(
                unansweredCreatedDaysAgo(ALICE, DateGenerator.DAYS_PER_YEAR + 1));

        // Not eligible for followup
        this.npsQuestionRepository.insert(
                unansweredCreatedDaysAgo(BOB, DateGenerator.DAYS_PER_YEAR * 2 + 1));
        this.npsQuestionRepository.insert(
                answeredDaysAgo(BOB, DateGenerator.DAYS_PER_YEAR + 1));
        this.npsQuestionRepository.insert(
                answeredDaysAgo(BOB, 100));

        // Not eligible for followup
        this.npsQuestionRepository.insert(
                unansweredCreatedDaysAgo(LISA, DateGenerator.DAYS_PER_YEAR * 2 + 1));
        this.npsQuestionRepository.insert(
                answeredDaysAgo(LISA, DateGenerator.DAYS_PER_YEAR + 1));
        this.npsQuestionRepository.insert(
                unansweredCreatedDaysAgo(LISA, 100));
    }

    @Test
    public void shouldCreateQualifiedNpsQuestionGivenListOfCustomerReferences() throws Exception {
        given(this.appInternalService.getActiveCustomersStartedInvestingDaysAgo(90)).willReturn(FastList.newListWith(
                new Customer(ALICE, ALICE),
                new Customer(BOB, BOB),
                new Customer(JOHN, JOHN),
                new Customer(LISA, LISA)
        ));

        mockMvc.perform(post("/internal/v1/nps-questions")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$")
                        .value(hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customer.reference")
                        .value(ALICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customer.reference")
                        .value(JOHN))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].emailSent", everyItem(is(false))));
    }

    @Test
    public void shouldCreateLimitedQualifiedNpsQuestionGivenListOfCustomerReferences() throws Exception {
        given(this.appInternalService.getActiveCustomersStartedInvestingDaysAgo(90)).willReturn(FastList.newListWith(
                new Customer(ALICE, ALICE),
                new Customer(BOB, BOB),
                new Customer(JOHN, JOHN),
                new Customer(LISA, LISA)
        ));

        mockMvc.perform(post("/internal/v1/nps-questions?limit=1")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customer.reference")
                        .value(ALICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].emailSent", everyItem(is(false))));
    }

    @Test
    public void shouldSendNpsSurveyEmails() throws Exception {
        given(this.appInternalService.sendNotification(Matchers.any())).willAnswer((Answer<MutableList<String>>)invocation -> {
            Object[] args = invocation.getArguments();
            EmailNotification emailNotification = (EmailNotification) args[0];
            return ListAdapter.adapt(emailNotification.getCustomers()).collect(Customer::getReference);
        });

        mockMvc.perform(post("/internal/v1/nps-questions/emails")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customers")
                        .value(hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customers[0].reference")
                        .value(ALICE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customers[1].reference")
                        .value(LISA));
    }

    @Test
    public void shouldGetNpsQuestion() throws Exception {
        NpsQuestion question = npsQuestionRepository.save(answeredDaysAgo("alice", 1));

        mockMvc.perform(get("/internal/v1/nps-questions/" + question.getId())
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().string(mapper.writeValueAsString(question)));
    }

    @Test
    public void shouldGetNpsQuestionReturnNotFound() throws Exception {
        mockMvc.perform(get("/internal/v1/nps-questions/" + "nonExistId")
                .contentType(contentType))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("not exist"));
    }
}

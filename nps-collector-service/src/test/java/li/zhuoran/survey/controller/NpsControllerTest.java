package li.zhuoran.survey.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import li.zhuoran.survey.App;
import li.zhuoran.survey.dao.NpsQuestionRepository;
import li.zhuoran.survey.entities.NpsQuestion;
import li.zhuoran.survey.helper.NpsQuestions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class NpsControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private NpsQuestionRepository npsQuestionRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.npsQuestionRepository.deleteAll();
    }

    @Test
    public void shouldAnswerNpsScore() throws Exception {
        NpsQuestion question = npsQuestionRepository.insert(
                NpsQuestions.unansweredCreatedDaysAgo("alice", 1));
        NpsQuestion answer = npsQuestionRepository.insert(
                NpsQuestions.answeredDaysAgoWithScoreOnly("alice", 0));

        mockMvc.perform(put("/api/v1/nps-questions/" + question.getId())
                .content(mapper.writeValueAsString(answer))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.answer")
                        .value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.contactable")
                        .value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.reference")
                        .value("alice"));
    }

    @Test
    public void shouldAnswerNpsAdditionalQuestions() throws Exception {
        NpsQuestion question = npsQuestionRepository.insert(
                NpsQuestions.answeredDaysAgo("alice", 1));
        NpsQuestion answer = npsQuestionRepository.insert(
                NpsQuestions.answeredDaysAgo("alice", 0));

        mockMvc.perform(put("/api/v1/nps-questions/" + question.getId() + "/additions")
                .content(mapper.writeValueAsString(answer))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.answer")
                        .value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.comment")
                        .value("aComment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.contactable")
                        .value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customer.reference")
                        .value("alice"));
    }

    @Test
    public void shouldReturnPreconditionFailedError() throws Exception {
        NpsQuestion question = npsQuestionRepository
                .save(NpsQuestions.answeredDaysAgo("aCustomerReference", 1));

        String npsQuestionId = question.getId();
        mockMvc.perform(put("/api/v1/nps-questions/" + npsQuestionId)
                .content(mapper.writeValueAsString(question))
                .contentType(contentType))
                .andExpect(status().isPreconditionFailed());
    }

    @Test
    public void shouldReturnTheLastNpsQuestion() throws Exception {
        npsQuestionRepository
                .save(NpsQuestions.unansweredCreatedDaysAgo("aCustomerReference", 10));
        NpsQuestion secondNpsQuestion = npsQuestionRepository
                .save(NpsQuestions.unansweredCreatedDaysAgo("aCustomerReference", 5));

        mockMvc.perform(get("/api/v1/nps-questions/latest?customerReference=" + "aCustomerReference")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(secondNpsQuestion.getId()));
    }

    @Test
    public void shouldReturnErrorIfGivenCustomerReferenceIsNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/nps-questions/latest?customerReference=" + "nonExistCustomerReference")
                .contentType(contentType))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value("customerReference is not associated with any questions"));
    }
}
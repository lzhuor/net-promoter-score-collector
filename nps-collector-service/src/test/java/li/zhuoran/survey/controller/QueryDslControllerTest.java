package li.zhuoran.survey.controller;

import li.zhuoran.survey.App;
import li.zhuoran.survey.dao.NpsQuestionRepository;
import li.zhuoran.survey.entities.Customer;
import li.zhuoran.survey.entities.NpsQuestion;
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

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class QueryDslControllerTest {

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

        Customer customerOne = new Customer("aCustomerReference", "name");
        Customer customerTwo = new Customer("anotherCustomerReference", "name");

        NpsQuestion npsQuestion1 = npsQuestionRepository.save(new NpsQuestion(customerOne));
        npsQuestion1.setAnswer(1);
        npsQuestionRepository.save(npsQuestion1);

        NpsQuestion npsQuestion2 = npsQuestionRepository.save(new NpsQuestion(customerTwo));
        npsQuestion2.setAnswer(10);
        npsQuestionRepository.save(npsQuestion2);
    }

    @Test
    public void shouldQueryDslReturnPageableResponseGivenPredicateNull() throws Exception {
        mockMvc.perform(get("/internal/v1/query/nps-questions")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)));
    }

    @Test
    public void shouldReturnNpsQuestionWithAnswerEqualsTen() throws Exception {
        mockMvc.perform(get("/internal/v1/query/nps-questions?answer=10")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].answer").value(10));
    }

    @Test
    public void shouldReturnNpsQuestionWithCustomerReference() throws Exception {
        mockMvc.perform(get("/internal/v1/query/nps-questions?customerReference=aCustomerReference")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].customer.reference").value("aCustomerReference"));
    }
}
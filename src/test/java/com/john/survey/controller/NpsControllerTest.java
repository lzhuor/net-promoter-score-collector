package com.john.survey.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.john.survey.App;
import com.john.survey.dao.NpsQuestionRepository;
import com.john.survey.entities.NpsQuestion;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void shouldCreateANpsQuestionWithGivenCustomerReference() throws Exception {
        String customerReference = "aCustomerReference";
        mockMvc.perform(post("/api/v1/nps-questions")
                .content(mapper.writeValueAsString(new NpsQuestion(customerReference)))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerReference").value(customerReference));
    }

    @Test
    public void shouldUpdateNpsQuestionWithGivenCustomerReferenceAndScore() throws Exception {
        mockMvc.perform(post("/api/v1/nps-questions")
                .content(mapper.writeValueAsString(new NpsQuestion("aCustomerReference")))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", hasSize(2)));
    }

    // TODO: Add more tests
}
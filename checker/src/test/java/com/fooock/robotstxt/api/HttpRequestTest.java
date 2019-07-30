package com.fooock.robotstxt.api;

import com.fooock.robotstxt.api.controller.ApiController;
import com.fooock.robotstxt.api.model.AllowData;
import com.fooock.robotstxt.api.service.ApiService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ApiController.class)
public class HttpRequestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApiService apiService;
    private Gson gson = new Gson();

    @Test
    public void testPostRequestWithoutAgent() throws Exception {
        AllowData data = new AllowData();
        data.setUrl("http://example.com/test/user");
        data.setAgent("");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/allowed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(data))
                .header("user-agent", "");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testPostRequestWithRequestAgent() throws Exception {
        AllowData data = new AllowData();
        data.setUrl("http://example.com/test/user");
        data.setAgent("");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/allowed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(data))
                .header("user-agent", "AwesomeBot");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    public void testPostRequestWithDataAgent() throws Exception {
        AllowData data = new AllowData();
        data.setUrl("http://example.com/test/user");
        data.setAgent("AwesomeBot");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/allowed")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(data))
                .header("user-agent", "");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    public void testGetRequestWithoutAgent() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/allowed")
                .param("url", "http://example.com/test/user")
                .header("user-agent", "");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testGetRequestWithoutAgentAndUrl() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/allowed")
                .param("url", "")
                .header("user-agent", "");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    public void testGetRequestWithRequestAgent() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/allowed")
                .param("url", "http://example.com/test/user")
                .header("user-agent", "AwesomeBot");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    public void testGetRequestWithParamAgent() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/allowed")
                .param("url", "http://example.com/test/user")
                .param("agent", "AwesomeBot");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(200));
    }
}

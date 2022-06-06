package bykuharev.taskforalfa.exchangeratesingif.controller;

import bykuharev.taskforalfa.exchangeratesingif.service.ServiceImpl.ExchangeRatesServiceImpl;
import bykuharev.taskforalfa.exchangeratesingif.service.ServiceImpl.GifServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@WebMvcTest(AppController.class)
public class AppControllerTest {

    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.zero}")
    private String whatTag;
    @Value("${giphy.error}")
    private String errorTag;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeRatesServiceImpl exchangeRatesService;
    @MockBean
    private GifServiceImpl gifService;

    @Test
    public void whenReturnListOfCharCodes() throws Exception {
        List<String> responseList = new ArrayList<>();
        responseList.add("TEST");
        Mockito.when(exchangeRatesService.getCodes())
                .thenReturn(responseList);
        mockMvc.perform(get("/gg/getcodes")
                        .content(mapper.writeValueAsString(responseList))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0]").value("TEST"));
    }

    @Test
    public void whenListIsNull() throws Exception {
        Mockito.when(exchangeRatesService.getCodes())
                .thenReturn(null);
        mockMvc.perform(get("/gg/getcodes")
                        .content(mapper.writeValueAsString(null))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void whenReturnBrokeGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.brokeTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(exchangeRatesService.getKeyFromTag(anyString()))
                .thenReturn(-1);
        Mockito.when(gifService.getGif(this.brokeTag))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/gg/getgif/TESTCODE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.brokeTag));
    }

    @Test
    public void whenReturnErrorGifMinus101() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.errorTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(exchangeRatesService.getKeyFromTag(anyString()))
                .thenReturn(-101);
        Mockito.when(gifService.getGif(this.errorTag))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/gg/getgif/TESTCODE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.errorTag));
    }

    @Test
    public void whenReturnErrorGifAnyOtherKey() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("compareResult", this.errorTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(exchangeRatesService.getKeyFromTag(anyString()))
                .thenReturn(5);
        Mockito.when(gifService.getGif(this.errorTag))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/gg/getgif/TESTCODE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.compareResult").value(this.errorTag));
    }
}

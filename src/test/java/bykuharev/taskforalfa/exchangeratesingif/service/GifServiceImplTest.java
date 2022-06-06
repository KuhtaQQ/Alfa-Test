package bykuharev.taskforalfa.exchangeratesingif.service;

import bykuharev.taskforalfa.exchangeratesingif.client.GiphyGifFeignClient;
import bykuharev.taskforalfa.exchangeratesingif.service.ServiceImpl.GifServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("bykukharev.taskforalfa")
public class GifServiceImplTest {

    @Autowired
    private GifServiceImpl gifService;
    @MockBean
    private GiphyGifFeignClient gifFeignClient;

    @Test
    public void whenChangesPositive(){
        ResponseEntity<Map> testEntity = new ResponseEntity<>(new HashMap(), HttpStatus.OK);
        Mockito.when(gifFeignClient.getRandomGif(anyString(), anyString()))
                .thenReturn(testEntity);
        ResponseEntity<Map> result = gifService.getGif("control_test_word");
        assertEquals("control_test_word", result.getBody().get("compareResult"));

    }
}

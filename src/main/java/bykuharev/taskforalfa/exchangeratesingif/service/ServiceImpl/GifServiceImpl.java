package bykuharev.taskforalfa.exchangeratesingif.service.ServiceImpl;

import bykuharev.taskforalfa.exchangeratesingif.client.GifClient;
import bykuharev.taskforalfa.exchangeratesingif.service.GifService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class GifServiceImpl implements GifService {

    private GifClient gifClient;
    @Value("${giphy.api.key}")
    private String apiKey;

    @Autowired
    public GifServiceImpl(GifClient gifClient){
        this.gifClient = gifClient;

    }
    @Override
    public ResponseEntity<Map> getGif(String tag) {
        ResponseEntity<Map> result = gifClient.getRandomGif(this.apiKey, tag);

        return result;
    }


}

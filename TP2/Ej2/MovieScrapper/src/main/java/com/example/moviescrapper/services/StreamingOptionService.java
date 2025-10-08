package com.example.moviescrapper.services;

import com.example.moviescrapper.StreamingAvailability;
import com.example.moviescrapper.entities.StreamingOption;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class StreamingOptionService {
    public List<StreamingOption> getStreaming(String id, String country) throws IOException, InterruptedException {
        return StreamingAvailability.getStreaming(id,country);
    }
}

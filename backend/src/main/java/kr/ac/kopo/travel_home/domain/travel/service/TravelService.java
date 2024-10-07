package kr.ac.kopo.travel_home.domain.travel.service;

import kr.ac.kopo.travel_home.domain.travel.domain.dto.TravelRealTime;
import kr.ac.kopo.travel_home.domain.travel.mapper.TravelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelService {
    private final TravelMapper travelMapper;

    public List<TravelRealTime> getTravelRealTime() {
        return travelMapper.getTravelRealTime();
    }
}

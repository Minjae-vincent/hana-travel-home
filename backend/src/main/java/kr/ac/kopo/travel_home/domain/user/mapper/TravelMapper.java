package kr.ac.kopo.travel_home.domain.user.mapper;

import kr.ac.kopo.travel_home.domain.user.domain.dto.TravelRealTime;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelMapper {
    List<TravelRealTime> getTravelRealTime();
}

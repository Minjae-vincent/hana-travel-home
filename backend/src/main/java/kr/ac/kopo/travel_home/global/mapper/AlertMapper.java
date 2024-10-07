package kr.ac.kopo.travel_home.global.mapper;

import kr.ac.kopo.travel_home.domain.user.domain.dto.TargetAmountAlert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertMapper {
    List<TargetAmountAlert> getTargetAmountAlert();
    String getMemberEmail(@Param("memberId") String memberId);
}

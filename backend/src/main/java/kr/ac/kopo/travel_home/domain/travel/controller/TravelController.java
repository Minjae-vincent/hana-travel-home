package kr.ac.kopo.travel_home.domain.travel.controller;

import kr.ac.kopo.travel_home.domain.travel.service.TravelService;
import kr.ac.kopo.travel_home.domain.user.service.UserService;
import kr.ac.kopo.travel_home.global.payload.APIResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/travel")
@RequiredArgsConstructor
@Slf4j
public class TravelController {
    private final TravelService travelService;
    private final UserService userService;
    @GetMapping("/real-time")
    public APIResponse<?> getTravelRealTime() {
        return APIResponse.onSuccess(travelService.getTravelRealTime());
    }

    @GetMapping("/history/{userId}")
    public APIResponse<?> getTravelHistory(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getTravelHistory(userId));
    }

    @GetMapping("/recommendation/{userId}")
    public APIResponse<?> getTravelRecommendCustom(@PathVariable("userId") String userId) {
        return APIResponse.onSuccess(userService.getTravelRecommendCustom(userId));
    }

    @GetMapping("/recommendation")
    public APIResponse<?> getTravelRecommendNow() {
        return APIResponse.onSuccess(userService.getTravelRecommendNow());
    }
}

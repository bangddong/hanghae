package com.hanghae.architecture.interfaces.api;

import com.hanghae.architecture.application.schedule.ScheduleService;
import com.hanghae.architecture.interfaces.dto.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/schedule")
@RestController
@RequiredArgsConstructor
public class ScheduleApiController {

    private static final Logger log = LoggerFactory.getLogger(ScheduleApiController.class);

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> schedule() {
        return ResponseEntity.ok(scheduleService.getSchedule());
    }

}

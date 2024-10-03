package com.hanghae.architecture.application.schedule;

import com.hanghae.architecture.domain.schedule.ScheduleRepository;
import com.hanghae.architecture.interfaces.dto.ScheduleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<ScheduleResponse> getSchedule() {
        return scheduleRepository.getScheduleList()
                .stream()
                .map(ScheduleResponse::fromEntity)
                .toList();
    }

}

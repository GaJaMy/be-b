package com.liveclass.be_b.service.cancellation;

import com.liveclass.be_b.repository.cancellation.CancellationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CancellationService {
    private final CancellationRepository cancellationRepository;


}

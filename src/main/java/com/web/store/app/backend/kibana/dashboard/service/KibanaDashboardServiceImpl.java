package com.web.store.app.backend.kibana.dashboard.service;

import com.web.store.app.backend.kibana.dashboard.repository.KibanaDashboardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class KibanaDashboardServiceImpl implements KibanaDashboardService {

    private final KibanaDashboardRepository kibanaDashboardRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public KibanaDashboardServiceImpl(RestTemplateBuilder builder, KibanaDashboardRepository kibanaDashboardRepository1) {
        this.restTemplate = builder.build();
        this.kibanaDashboardRepository = kibanaDashboardRepository1;
    }

    @Override
    public Optional<String> getDashboard(String name) {
        String body = null;
        var kibanaDashboardUrl = kibanaDashboardRepository.findKibanaDashboardByNameIgnoreCase(name);
        if(kibanaDashboardUrl.isPresent()){
            var response = restTemplate.getForEntity("http://localhost:5601/app/dashboards#/view/" +
                    kibanaDashboardUrl.get().getKibanaUrl(),String.class);
            body = response.getBody();
            System.out.println(body);
        }
        System.out.println(kibanaDashboardUrl);
        return Optional.ofNullable(body);
    }

}

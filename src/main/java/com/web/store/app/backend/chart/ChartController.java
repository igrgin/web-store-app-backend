package com.web.store.app.backend.chart;

import com.web.store.app.backend.chart.service.ChartService;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chart/api")
@RequiredArgsConstructor
@Slf4j
public class ChartController {

    private final ChartService chartService;
    @GetMapping("/private/{brand}")
    private ResponseEntity<Map<String, Integer>> getTopProductsByBrand(@PathVariable final String brand,
                                                                       @RequestParam(name = "col", defaultValue = "5")
                                                                       final int columnNumber) {

        return Optional.of(chartService.getTopProductsByBrand(brand,columnNumber))
                .map(transactions -> ResponseEntity.status(HttpStatus.OK)
                        .body(transactions))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .build());

    }
}

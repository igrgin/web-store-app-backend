package com.web.store.app.backend.chart.service;

import java.util.Map;

public interface ChartService {
    Map<String, Integer> getTopProductsByBrand(String brand, Integer columnCount);
}

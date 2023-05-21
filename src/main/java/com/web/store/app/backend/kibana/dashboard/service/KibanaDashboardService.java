package com.web.store.app.backend.kibana.dashboard.service;

import java.util.Optional;

public interface KibanaDashboardService {

   Optional<String> getDashboard(String name);

}

package com.web.store.app.backend.kibana.dashboard.repository;

import com.web.store.app.backend.kibana.dashboard.entity.KibanaDashboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KibanaDashboardRepository extends JpaRepository<KibanaDashboard, Integer> {

    Optional<KibanaDashboard> findKibanaDashboardByNameIgnoreCase(String url);
}

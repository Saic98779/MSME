package com.metaverse.msme.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DuplicateProbabilityIndexInitializer {

    private final JdbcTemplate jdbcTemplate;
    private volatile boolean trigramEnabled;

    @Bean
    ApplicationRunner duplicateProbabilityIndexes() {
        return args -> {
            createBTreeIndex("""
                    CREATE INDEX IF NOT EXISTS idx_msme_duplicate_district_normalized
                    ON msme_unit_details (
                        LOWER(TRIM(BOTH FROM regexp_replace(COALESCE(CAST(extracteddistrict AS TEXT), ''), '\\s*\\([^)]*\\)', '', 'g')))
                    )
                    """);

            tryEnablePgTrgm();

            if (trigramEnabled) {
                createGinTrigramIndex("idx_msme_duplicate_unitname_trgm", "unitname");
                createGinTrigramIndex("idx_msme_duplicate_ownername_trgm", "unitholderorownername");
                createGinTrigramIndex("idx_msme_duplicate_mandal_trgm", "extractedmandal");
                createGinTrigramIndex("idx_msme_duplicate_village_trgm", "extractedvillage");
            }
        };
    }

    public boolean isTrigramEnabled() {
        return trigramEnabled;
    }

    private void tryEnablePgTrgm() {
        trigramEnabled = isPgTrgmInstalled();
        if (trigramEnabled) {
            return;
        }

        try {
            jdbcTemplate.execute("CREATE EXTENSION IF NOT EXISTS pg_trgm");
            trigramEnabled = isPgTrgmInstalled();
        } catch (DataAccessException exception) {
            log.warn("Could not enable pg_trgm extension; trigram indexes for duplicate matching were skipped");
            trigramEnabled = false;
        }
    }

    private boolean isPgTrgmInstalled() {
        try {
            Boolean installed = jdbcTemplate.queryForObject(
                    "SELECT EXISTS (SELECT 1 FROM pg_extension WHERE extname = 'pg_trgm')",
                    Boolean.class
            );
            return Boolean.TRUE.equals(installed);
        } catch (DataAccessException exception) {
            return false;
        }
    }

    private void createBTreeIndex(String sql) {
        try {
            jdbcTemplate.execute(sql);
        } catch (DataAccessException exception) {
            log.warn("Could not create duplicate-probability btree index", exception);
        }
    }

    private void createGinTrigramIndex(String indexName, String columnName) {
        try {
            jdbcTemplate.execute("""
                    CREATE INDEX IF NOT EXISTS %s
                    ON msme_unit_details
                    USING gin (LOWER(COALESCE(CAST(%s AS TEXT), '')) gin_trgm_ops)
                    """.formatted(indexName, columnName));
        } catch (DataAccessException exception) {
            log.warn("Could not create trigram index {}", indexName);
        }
    }
}

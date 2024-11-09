package com.jobizzz.ctrledu.config;

import com.jobizzz.ctrledu.dto.ThreadContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> resolvedDataSources = new HashMap<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return ThreadContext.getThreadContext();
    }

    @Override
    public void afterPropertiesSet(){
        super.setTargetDataSources(resolvedDataSources);
        super.afterPropertiesSet();
    }

    public void addDataSource(String tenantId, DataSource dataSource){
        resolvedDataSources.put(tenantId,dataSource);
        super.setTargetDataSources(resolvedDataSources);
        super.afterPropertiesSet();
    }
}

// Copyright 2017 Yahoo Inc.
// Licensed under the terms of the Apache license. Please see LICENSE.md file distributed with this work for terms.
package com.yahoo.bard.webservice.application.metadataViews;

import com.yahoo.bard.webservice.data.metric.LogicalMetric;
import com.yahoo.bard.webservice.table.LogicalTable;
import com.yahoo.bard.webservice.table.LogicalTableDictionary;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.container.ContainerRequestContext;

public class LogicalMetricViewFunction implements MetadataViewFunction<LogicalMetric> {

    protected final LogicalTableDictionary logicalTableDictionary;
    protected final MetadataViewFunction<LogicalTable> tableMetadataViewFunction;

    public LogicalMetricViewFunction(LogicalTableDictionary logicalTableDictionary, MetadataViewFunction<LogicalTable> tableViewBuilder) {
        this.logicalTableDictionary = logicalTableDictionary;
        this.tableMetadataViewFunction = tableViewBuilder;
    }

    @Override
    public Object apply(
            final ContainerRequestContext containerRequestContext, final LogicalMetric logicalMetric
    ) {
        Map<String, Object> resultRow = new LinkedHashMap<>();
        resultRow.put("category", logicalMetric.getCategory());
        resultRow.put("name", logicalMetric.getName());
        resultRow.put("longName", logicalMetric.getLongName());
        resultRow.put("description", logicalMetric.getDescription());

        List<Object> tableViews = logicalTableDictionary.findByLogicalMetric(logicalMetric).stream()
                .map(table-> tableMetadataViewFunction.apply(containerRequestContext, table))
                .collect(Collectors.toList());

        resultRow.put("tables", tableViews);
        return resultRow;
    }
}

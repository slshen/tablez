package com.github.slshen.tablez.table;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TableService {

	private final Map<String, Table> tables = new ConcurrentHashMap<>();
	private final Table tablesTable;
	
	public TableService() {
		tablesTable = getTable("tables");
	}

	public Table getTable(String name) {
		return tables.computeIfAbsent(name, $ -> {
			Table table = new Table(name);
			//tablesTable
			return table;
		});
	}

}

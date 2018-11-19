package com.github.slshen.tablez.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.vavr.collection.Map;

public class TableKey extends ArrayList<String> {
	private static final long serialVersionUID = 1L;

	public TableKey(int size) {
		super(size);
	}

	public TableKey(Collection<? extends String> c) {
		super(c);
	}

	public TableKey(String[] values) {
		super(Arrays.asList(values));
	}

	public static TableKey create(Map<String, Integer> columnMap, List<String> identityColumns, TableRow row) {
		TableKey key = new TableKey(identityColumns.size());
		for (String column : identityColumns) {
			key.add(row.getCell(columnMap.get(column).get()).value);
		}
		return key;
	}

	public static Map<TableKey, String> index(Map<TableKey, String> identityMap, Map<String, Integer> columnMap,
			List<String> identityColumns, TableRow previousRow, TableRow currentRow) {
		TableKey key = create(columnMap, identityColumns, currentRow);
		if (previousRow != null) {
			TableKey previousKey = create(columnMap, identityColumns, previousRow);
			if (!key.equals(previousKey)) {
				identityMap = identityMap.remove(previousKey);
			}
		}
		return identityMap.put(key, currentRow.getId());
	}

}

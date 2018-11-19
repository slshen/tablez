package com.github.slshen.tablez.table;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;

class TableState {
	Map<String, TableRow> rowMap = HashMap.empty();
	Map<String, Integer> columnMap = HashMap.empty();
	Map<TableKey, String> identityMap = HashMap.empty();

}

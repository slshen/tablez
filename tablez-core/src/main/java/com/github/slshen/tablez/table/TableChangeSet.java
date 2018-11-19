package com.github.slshen.tablez.table;

import io.vavr.control.Option;

public class TableChangeSet {

	private final Table table;
	private final TableState tableState;
	private final TableState localState = new TableState();

	public TableChangeSet(Table table) {
		this.table = table;
		this.tableState = table.getState();
	}
	
	public TableState getLocalState() {
		return localState;
	}

	public TableRow addRow() {
		TableRow row = new TableRow(this, table.createRowId());
		localState.rowMap = localState.rowMap.put(row.getId(), row);
		return row;
	}

	public TableRow updateRow(TableRow row) {
		return localState.rowMap.computeIfAbsent(row.getId(), $ -> new TableRow(this, row))
				.apply((localRow, rowMap) -> {
					localState.rowMap = rowMap;
					return localRow;
				});
	}

	public int getColumnIndex(String column) {
		int index = tableState.columnMap.getOrElse(column, -1);
		if (index == -1) {
			index = tableState.columnMap.size() + localState.columnMap.size();
			localState.columnMap = localState.columnMap.put(column, index);
		}
		return index;
	}

	public Table getTable() {
		return table;
	}

	public TableRow getRow(String[] identity) {
		Option<String> id = tableState.identityMap.get(new TableKey(identity));
		if (id.isEmpty()) {
			TableRow row = addRow();
			int i = 0;
			for (String column : table.getIdentityColumns()) {
				row.update(column, TableCell.of(identity[i++]));
			}
			return row;
		}
		TableRow row = localState.rowMap.getOrElse(id.get(), null);
		if (row != null) {
			return row;
		}
		Option<TableRow> tableRow = tableState.rowMap.get(id.get());
		row = new TableRow(this, tableRow.get());
		localState.rowMap = localState.rowMap.put(row.getId(), row);
		return row;
	}

}

package com.github.slshen.tablez.table;

import java.util.Iterator;

import io.vavr.collection.Map;
import io.vavr.collection.Vector;

public class TableRow implements Iterable<TableCell> {

	private TableRow baseRow;
	private TableChangeSet changeSet;
	private final String id;
	private Vector<TableCell> cells = Vector.empty();

	public TableRow(TableChangeSet changeSet, String id) {
		this.changeSet = changeSet;
		this.id = id;
	}
	
	public TableRow(TableChangeSet changeSet, TableRow row) {
		this.baseRow = row;
		this.changeSet = changeSet;
		this.id = row.getId();
		this.cells = row.cells;
	}
	
	void commit() {
		changeSet = null;
		baseRow = null;
	}

	public String getId() {
		return id;
	}
	
	public TableCell getCell(int index) {
		return cells.get(index);
	}
	
	public TableRow reshape(Map<String,Integer> oldColumnMap, Map<String,Integer> columnMap) {
		TableRow row = changeSet != null ? this : new TableRow(null, this);
		
		return row;
	}

	public TableRow update(String column, TableCell cell) {
		TableRow row = changeSet.updateRow(this);
		int index = changeSet.getColumnIndex(column);
		row.cells = row.cells.padTo(index + 1, null).update(index, cell);
		return row;
	}

	public Iterator<TableCell> iterator() {
		return cells.iterator();
	}

	public int length() {
		return cells.size();
	}
	
	public TableRow getBaseRow() {
		return baseRow;
	}

	public void render(Object obj) {
		// TODO Auto-generated method stub
		
	}

}

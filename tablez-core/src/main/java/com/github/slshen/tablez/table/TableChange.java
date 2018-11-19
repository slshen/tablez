package com.github.slshen.tablez.table;

public class TableChange {
	public static final TableChange RESET = null;
	private TableRow previousRow;
	private TableRow currentRow;
	private boolean remove;

	public TableChange() {
	}

	public TableRow getPreviousRow() {
		return previousRow;
	}

	public TableChange withPreviousRow(TableRow previousRow) {
		this.previousRow = previousRow;
		return this;
	}

	public TableRow getCurrentRow() {
		return currentRow;
	}

	public TableChange withCurrentRow(TableRow currentRow) {
		this.currentRow = currentRow;
		return this;
	}

	public boolean isRemove() {
		return remove;
	}

	public TableChange withRemove(boolean remove) {
		this.remove = remove;
		return this;
	}

	public boolean isRefresh() {
		// TODO Auto-generated method stub
		return false;
	}

}

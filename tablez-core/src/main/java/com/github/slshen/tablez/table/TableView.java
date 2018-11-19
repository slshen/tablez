package com.github.slshen.tablez.table;

import java.util.ArrayList;
import java.util.List;

public class TableView {
	private final List<TableChange> changes = new ArrayList<>();
	private final int version;

	public TableView(int version) {
		this.version = version;
	}

	public List<TableChange> getChanges() {
		return changes;
	}

	public int getVersion() {
		return version;
	}

}

package com.github.slshen.tablez.table;

public class TableCell {
	public final String value, meta;
	
	public static TableCell of(String value) {
		return new TableCell(value, "");
	}
	
	public TableCell(String value, String meta) {
		this.value = value;
		this.meta = meta;
	}

}

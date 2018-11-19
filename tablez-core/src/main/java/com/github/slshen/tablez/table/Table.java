package com.github.slshen.tablez.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;

import com.github.slshen.tablez.event.AbstractEventSource;
import com.github.slshen.tablez.util.CompactId;
import com.github.slshen.tablez.util.HeldLock;

import io.vavr.Tuple2;
import io.vavr.collection.Vector;
import io.vavr.control.Option;

public class Table extends AbstractEventSource<Table> {
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock(false);
	private final Lock commitLock = new ReentrantLock();
	private final String name;
	private volatile Vector<TableChange> changes = Vector.empty();
	private volatile TableState state = new TableState();

	private volatile int version;
	private final ThreadLocal<TableChangeSet> changeSet = ThreadLocal.withInitial(() -> new TableChangeSet(this));
	private final AtomicLong rowIdCounter = new AtomicLong();

	private List<String> identityColumns = Collections.singletonList("id");
	private BiConsumer<Object, TableRow> rowRenderer = DefaultRenderer.INSTANCE;

	public Table(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Table withIdentityColumns(String... columns) {
		identityColumns = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(columns)));
		return this;
	}

	public List<String> getIdentityColumns() {
		return identityColumns;
	}

	public TableRow getRow(String... identity) {
		return changeSet.get().getRow(identity);

	}

	public TableView getView(int sinceVersion) {
		try (HeldLock $state = HeldLock.lock(rwLock.readLock())) {

		}
		return null;
	}

	public TableRow addRow() {
		return changeSet.get().addRow();
	}

	public void commit() {
		TableState state;

		try (HeldLock $commit = HeldLock.lockInterruptibly(commitLock)) {
			state = this.state;
			TableChangeSet cs = changeSet.get();
			changeSet.remove();
			if (!cs.getLocalState().columnMap.isEmpty()) {
				// merge local columns into column map
				for (Tuple2<String, Integer> entry : cs.getLocalState().columnMap) {
					String column = entry._1();
					if (!state.columnMap.containsKey(column)) {
						state.columnMap = state.columnMap.put(column, state.columnMap.size());
					}
				}
			}
			changes = this.changes;
			// if the columns have changed, then reshape the existing rows
			if (state.columnMap != this.state.columnMap) {
				for (TableRow row : this.state.rowMap.values()) {
					TableRow currentRow = row.reshape(this.state.columnMap, state.columnMap);
					if (currentRow != row) {
						state.rowMap = state.rowMap.put(row.getId(), currentRow);
						changes = addChange(changes, state.rowMap.size(),
								new TableChange().withCurrentRow(currentRow).withPreviousRow(row));
					}
				}
			}
			for (TableRow localRow : cs.getLocalState().rowMap.values()) {
				// if the columns are not the same, then reshape
				if (state.columnMap != cs.getLocalState().columnMap) {
					localRow.reshape(this.state.columnMap, cs.getLocalState().columnMap);
				}
				Option<TableRow> previousRow = state.rowMap.get(localRow.getId());
				TableRow newRow;
				if (previousRow.isDefined()) {
					newRow = merge(localRow.getBaseRow(), previousRow.get(), localRow);
					localRow.commit();
				} else {
					newRow = localRow;
				}
				newRow.commit();
				state.rowMap = state.rowMap.put(localRow.getId(), newRow);
				changes = addChange(changes, state.rowMap.size(),
						new TableChange().withCurrentRow(newRow).withPreviousRow(previousRow.getOrNull()));
				state.identityMap = TableKey.index(state.identityMap, state.columnMap, identityColumns,
						previousRow.getOrNull(), newRow);
			}
		}
		try (HeldLock $state = HeldLock.lock(rwLock.writeLock())) {
			this.state = state;
			this.changes = changes;
			version = version + 1;
		}
		post(this);
	}

	private TableRow merge(TableRow baseRow, TableRow previousRow, TableRow newRow) {
		return newRow;
	}

	private Vector<TableChange> addChange(Vector<TableChange> changes, int limit, TableChange change) {
		if (!changes.isEmpty() && changes.get() == TableChange.RESET) {
			return changes;
		}
		if (changes.size() > limit) {
			return Vector.of(TableChange.RESET);
		}
		return changes.append(change);
	}

	public String createRowId() {
		return CompactId.nextId(rowIdCounter);
	}

	public TableState getState() {
		try (HeldLock $tate = HeldLock.lock(rwLock.readLock())) {
			return state;
		}
	}

}

package com.github.slshen.tablez.problems;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.slshen.tablez.table.Table;
import com.github.slshen.tablez.table.TableCell;
import com.github.slshen.tablez.table.TableRow;
import com.github.slshen.tablez.table.TableService;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

@Component
public class ProblemService {
	
	@Autowired
	private EventBus eventBus;
	@Autowired
	private TableService tables;
	private Table problemsTable;
	
	@PostConstruct
	public void init() {
		eventBus.register(this);
		problemsTable = tables.getTable("problems");
	}
	
	@Subscribe
	public void onProblem(Problem problem) {
		TableRow row = problemsTable.addRow();
		row.update("sourceType", TableCell.of(problem.getSourceType()));
		row.update("source", TableCell.of(problem.getSource()));
		problemsTable.commit();
	}

}

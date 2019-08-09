package model;

import java.util.ArrayList;
import java.util.List;

public class ExcelRow {

	protected List<String> cells;

	public ExcelRow() {
		cells = new ArrayList<>();
	}

	public ExcelRow(String value) {
		cells = new ArrayList<>();
		cells.add(value);
	}

	public ExcelRow(List<String> cells) {
		if (cells == null || cells.isEmpty())
			throw new RuntimeException();

		this.cells = cells;
	}

	public List<String> getRow() {
		return cells;
	}

}

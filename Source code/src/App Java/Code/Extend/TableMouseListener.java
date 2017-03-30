package App.HKM.Extend;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

/**
 * A mouse listener for a JTable component.
 * @author www.codejava.neet
 *
 */
public class TableMouseListener extends MouseAdapter {
	
	private JTable table;
	
	public TableMouseListener(JTable table) {
		this.table = table;
	}
	
	@Override
	public void mousePressed(MouseEvent event) {
		// selects the row at which point the mouse is clicked
		Point point = event.getPoint();
		int currentRow = table.rowAtPoint(point);
		table.setRowSelectionInterval(currentRow, currentRow);
	}
}
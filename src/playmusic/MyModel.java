/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playmusic;

import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Ethan
 */
class MyModel extends DefaultTableModel {

    public MyModel() {
    }

//    @Override
//    public int getRowCount() {
//        return getRowCount();
//    }

//    @Override
//    public int getColumnCount() {
//        return getColumnCount();
//    }

//    @Override
//    public String getColumnName(int columnIndex) {
//        return getColumnName(columnIndex);
//    }

//    @Override
//    public Class<?> getColumnClass(int columnIndex) {
//        return getColumnClass(columnIndex);
//    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
//
//    @Override
//    public Object getValueAt(int rowIndex, int columnIndex) {
//        return getValueAt(rowIndex, columnIndex);
//    }
//
//    @Override
//    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
//        setValueAt(aValue, rowIndex, columnIndex);
//    }
//
//    @Override
//    public void addTableModelListener(TableModelListener l) {
//        addTableModelListener(l);
//    }
//
//    @Override
//    public void removeTableModelListener(TableModelListener l) {
//        removeTableModelListener(l);
//    }

}

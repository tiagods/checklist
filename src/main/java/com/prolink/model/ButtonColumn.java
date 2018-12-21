package com.prolink.model;

import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor{
        /**
	 * 
	 */
	private static final long serialVersionUID = 8156177192636698815L;
		JTable table;
        JButton renderButton;
        JButton editButton;
        String text;
        public ButtonColumn(JTable table, int column)
        {
            super();
            this.table = table;
            renderButton = new JButton();
            editButton = new JButton();
            editButton.setFocusPainted( false );
            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(column).setCellRenderer( this );
            columnModel.getColumn(column).setCellEditor( this );
        }
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (hasFocus){
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }
            else if (isSelected){
                renderButton.setForeground(table.getSelectionForeground());
                renderButton.setBackground(table.getSelectionBackground());
            }
            String text =  (value == null) ? "" : value.toString();
            renderButton.setText(text);
            if("Intervenção Manual".equals(text) ){
            	renderButton.setEnabled(true);
            }
            else
            	renderButton.setEnabled(false);
            return renderButton;
        }
        @Override
        public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column)
        {
            text = (value == null) ? "" : value.toString();
            editButton.setText( text );
            if("Intervenção Manual".equals(text) ){
            	editButton.setEnabled(true);
            }
            else
            	editButton.setEnabled(false);
            return editButton;
        }
        @Override
        public Object getCellEditorValue()
        {
            return text;
        }

        public JButton getButton(){
        	return this.editButton;
        }
    }

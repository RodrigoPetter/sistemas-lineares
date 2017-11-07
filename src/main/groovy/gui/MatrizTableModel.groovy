package gui

import beans.SistemaMatriz
import helper.VariableNames

import javax.swing.table.AbstractTableModel

class MatrizTableModel extends AbstractTableModel{

    private SistemaMatriz dados
    private String[] colunas

    MatrizTableModel(SistemaMatriz dados) {
        this.dados = dados
        colunas = new String[getRowCount()+1]

        for(int i = 0; i < this.getRowCount(); i++){ //o for aqui é pelas linhas
            colunas[i] = VariableNames.values()[i]
        }

        //adiciona a coluna do resultado
        colunas[getRowCount()] = "="
    }

    @Override
    String getColumnName(int column) {
        return colunas[column]
    }

    @Override
    int getRowCount() {
        return this.dados.functions.size()
    }

    @Override
    int getColumnCount() {
        return colunas.size()
    }

    @Override
    Object getValueAt(int rowIndex, int columnIndex) {
        return dados.getValueAt(rowIndex, columnIndex)
    }

    @Override
    boolean isCellEditable(int rowIndex, int columnIndex) {
        return true
    }

    @Override
    void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        dados.setValueAt(Integer.parseInt(aValue), rowIndex, columnIndex)
    }
}

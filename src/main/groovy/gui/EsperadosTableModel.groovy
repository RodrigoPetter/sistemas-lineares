package gui

import beans.ResultadosEsperados
import helper.VariableNames

import javax.swing.table.AbstractTableModel

class EsperadosTableModel extends AbstractTableModel{

    private MatrizTableModel matrizTableModel
    private ResultadosEsperados dados
    private String[] colunas

    EsperadosTableModel(ResultadosEsperados dados, MatrizTableModel matrizTableModel) {
        this.dados = dados
        this.matrizTableModel = matrizTableModel
        int size = dados.getResultadosEsperados().size()
        colunas = new String[size]

        for(int i = 0; i < size; i++){
            colunas[i] = VariableNames.values()[i]
        }

    }

    @Override
    String getColumnName(int column) {
        return colunas[column]
    }

    @Override
    int getRowCount() {
        return 1
    }

    @Override
    int getColumnCount() {
        return colunas.size()
    }

    @Override
    Object getValueAt(int rowIndex, int columnIndex) {
        return dados.resultadosEsperados.get(columnIndex).value
    }

    @Override
    boolean isCellEditable(int rowIndex, int columnIndex) {
        return true
    }

    @Override
    void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        dados.resultadosEsperados.get(columnIndex).value = Float.parseFloat(aValue)
        matrizTableModel.getDados().recalcularResultados(dados)
        matrizTableModel.fireTableDataChanged()
    }
}

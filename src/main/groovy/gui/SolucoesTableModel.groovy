package gui

import beans.SistemaMatriz
import helper.VariableNames

import javax.swing.table.AbstractTableModel

class SolucoesTableModel extends AbstractTableModel{

    private List<List<BigDecimal>> dados
    private String[] colunas

    SolucoesTableModel(List<List<BigDecimal>> dados) {
        this.dados = dados
        colunas = new String[dados.get(0).size()+1]

        colunas[0] = "Interação"

        for(int i = 1; i <= dados.get(0).size(); i++){ //o for aqui é pelas linhas
            colunas[i] = VariableNames.values()[i-1]
        }
    }

    @Override
    String getColumnName(int column) {
        return colunas[column]
    }

    @Override
    int getRowCount() {
        return this.dados.size()
    }

    @Override
    int getColumnCount() {
        return colunas.size()
    }

    @Override
    Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex==0){
            return dados.size()-rowIndex
        }else{
            columnIndex--
            //inverte a ordem
            return dados.reverse().get(rowIndex).get(columnIndex)
        }
    }

}

package gui

import beans.ResultadosEsperados
import helper.VariableNames

import javax.swing.table.AbstractTableModel

class ErrosTableModel extends AbstractTableModel {

    private ResultadosEsperados resultadosEsperados
    private List<List<BigDecimal>> solucoes
    private String[] colunas

    ErrosTableModel(List<List<BigDecimal>> solucoes, ResultadosEsperados resultadosEsperados) {

        this.resultadosEsperados = resultadosEsperados
        this.solucoes = solucoes

        int qtdVariaveis = solucoes.get(0).size()

        colunas = new String[qtdVariaveis + 1]
        colunas[0] = ""

        for (int i = 1; i <= qtdVariaveis; i++) {
            colunas[i] = VariableNames.values()[i - 1]
        }
    }

    @Override
    String getColumnName(int column) {
        return colunas[column]
    }

    @Override
    int getRowCount() {
        return 3
    }

    @Override
    int getColumnCount() {
        return colunas.size()
    }

    @Override
    Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            switch (rowIndex) {
                case 0:
                    return "Variação:"
                    break
                case 1:
                    return "Erro absoluto:"
                    break
                case 2:
                    return "Erro relativo:"
                    break
            }
        } else {

            def indexVariavel = columnIndex-1

            switch (rowIndex) {
                case 0:
                    return variacao(indexVariavel)
                    break
                case 1:
                    return "Erro absoluto:"
                    break
                case 2:
                    return "Erro relativo:"
                    break
            }
        }
    }

    private BigDecimal variacao(int indexVariavel){
        if(solucoes.size() == 1) {
            return solucoes.get(0).get(indexVariavel)
        }

        int rowIndex = solucoes.size()-1

        return solucoes.get(rowIndex).get(indexVariavel).minus(solucoes.get(rowIndex-1).get(indexVariavel)).abs()
    }

    private BigDecimal erroAbsoluto(){

    }

    private BigDecimal erroRelativo(){

    }

}
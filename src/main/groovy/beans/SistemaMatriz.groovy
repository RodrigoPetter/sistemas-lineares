package beans

import groovy.transform.Canonical

@Canonical
class SistemaMatriz {

    List<Function> functions = new ArrayList<Function>()

    SistemaMatriz(Integer quantidadeVariaveis) {

        //geração de uma matriz quadrada. Número de funções igual ao número de variáveis
        for (int i = 0; i < quantidadeVariaveis; i++) {
            functions.add(new Function(quantidadeVariaveis))
        }
    }

    def getValueAt(int linha, int coluna){
        return functions.get(linha).variables.get(coluna).value
    }

}

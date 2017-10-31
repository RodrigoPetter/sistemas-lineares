package helper

import beans.SistemaMatriz

class MatrizHelper {

    SistemaMatriz getDiagonalPrincipal(SistemaMatriz entrada) {
        def totalLinhas = entrada.functions.size()

        SistemaMatriz DiagonalPrincipal = new SistemaMatriz(totalLinhas)

        for (int i = 0; i < totalLinhas; i++) {
            DiagonalPrincipal.functions.get(i).variables.get(i).value = entrada.functions.get(i).variables.get(i).value
        }

        return DiagonalPrincipal
    }

    SistemaMatriz newMatrizWithRandomValues(Integer quantidadeVariaveis){
        Random r = new Random()

        SistemaMatriz matriz = new SistemaMatriz(quantidadeVariaveis)

        matriz.functions.each { linhas -> linhas.variables.each {
            it.value = r.nextInt(100-1)+1
        }}

        return matriz
    }
}

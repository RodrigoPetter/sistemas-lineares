package helper

import beans.Function
import beans.SistemaMatriz
import validations.MatrizValidations

class MatrizHelper {

    SistemaMatriz getDiagonalPrincipal(SistemaMatriz entrada) {
        def totalLinhas = entrada.functions.size()

        SistemaMatriz DiagonalPrincipal = new SistemaMatriz(totalLinhas)

        for (int i = 0; i < totalLinhas; i++) {
            DiagonalPrincipal.functions.get(i).variables.get(i).value = entrada.getValueAt(i, i)
        }

        return DiagonalPrincipal
    }

    SistemaMatriz getTriangulares(SistemaMatriz entrada) {
        def totalLinhas = entrada.functions.size()

        SistemaMatriz matrizTriangular = new SistemaMatriz(totalLinhas)

        for (int i = 0; i < totalLinhas; i++) {
            for (int k = 0; k < totalLinhas; k++) {
                if (i != k) {
                    matrizTriangular.functions.get(i)
                            .variables.get(k).value = entrada.getValueAt(i, k)
                }
            }
        }

        return matrizTriangular
    }

    SistemaMatriz pivotamento(SistemaMatriz entrada) {
        int tamanho = entrada.functions.size()
        Function[] funcOrder = new Function[tamanho]

        for (int linha = 0; linha < tamanho ; linha++) {
            int maxPivo = maxPivo(entrada.functions.get(linha))
            if(funcOrder[maxPivo] == null) {
                funcOrder[maxPivo] = entrada.functions.get(linha)
            }else{
                return null
            }
        }

        println funcOrder

        entrada.functions.clear()
        for (int linha = 0; linha < tamanho ; linha++) {
            entrada.functions.add(funcOrder[linha])
        }

        return entrada
    }

    Integer maxPivo(Function funcao){
        int maxColuna = 0
        int maxValue = 0
        for (int coluna = 0; coluna < funcao.variables.size() ; coluna++) {
            if(funcao.variables.get(coluna).value.abs() > maxValue){
                maxValue = funcao.variables.get(coluna).value.abs()
                maxColuna = coluna
            }
        }
        return maxColuna
    }

    SistemaMatriz getNewMatrizWithRandomValues(Integer quantidadeVariaveis) {
        Random r = new Random()

        SistemaMatriz matriz = new SistemaMatriz(quantidadeVariaveis)

        matriz.functions.each { linhas ->
            linhas.variables.each {
                it.value = r.nextInt(100 - 1) + 1
            }
            linhas.result = r.nextInt(100 - 1) + 1
        }

        return matriz
    }

    SistemaMatriz getInvalidMatriz() {

        SistemaMatriz matriz = new SistemaMatriz(3)

        matriz.functions.get(0).variables.get(0).value = 10
        matriz.functions.get(0).variables.get(1).value = 10
        matriz.functions.get(0).variables.get(2).value = 10
        matriz.functions.get(0).result = 1

        matriz.functions.get(1).variables.get(0).value = 10
        matriz.functions.get(1).variables.get(1).value = 10
        matriz.functions.get(1).variables.get(2).value = 10
        matriz.functions.get(1).result = 2

        matriz.functions.get(2).variables.get(0).value = 10
        matriz.functions.get(2).variables.get(1).value = 10
        matriz.functions.get(2).variables.get(2).value = 10
        matriz.functions.get(2).result = 3

        return matriz
    }

    SistemaMatriz getValidMatriz() {

        SistemaMatriz matriz = new SistemaMatriz(3)

        matriz.functions.get(0).variables.get(0).value = 10
        matriz.functions.get(0).variables.get(1).value = 2
        matriz.functions.get(0).variables.get(2).value = 4
        matriz.functions.get(0).result = 26

        matriz.functions.get(2).variables.get(0).value = 3
        matriz.functions.get(2).variables.get(1).value = -9
        matriz.functions.get(2).variables.get(2).value = 3
        matriz.functions.get(2).result = -6

        matriz.functions.get(1).variables.get(0).value = 4
        matriz.functions.get(1).variables.get(1).value = 3
        matriz.functions.get(1).variables.get(2).value = -12
        matriz.functions.get(1).result = -26

        return matriz
    }
}

package validations

import beans.SistemaMatriz
import helper.MatrizHelper

class MatrizValidations {

    private MatrizHelper matrizHelper = new MatrizHelper()

    boolean isQuadrada(SistemaMatriz matriz) {
        return matriz.functions.size() == matriz.functions.get(0).variables.size()
    }

    boolean isDiagonalmenteDominante(SistemaMatriz matriz) {
        def totalLinhas = matriz.functions.size()
        def principal = matrizHelper.getDiagonalPrincipal(matriz)
        def triangular = matrizHelper.getTriangulares(matriz)

        for (int i = 0; i < totalLinhas; i++) {
            if(principal.functions.get(i).somaModular() <= triangular.functions.get(i).somaModular()){
                return false
            }
        }

        return true
    }

}

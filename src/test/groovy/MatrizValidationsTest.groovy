import beans.SistemaMatriz
import helper.MatrizHelper
import spock.lang.Specification
import validations.MatrizValidations

class MatrizValidationsTest extends Specification {

    private MatrizValidations matrizValidations = new MatrizValidations()
    private MatrizHelper matrizHelper = new MatrizHelper()

    private SistemaMatriz matriz

    def "valida se matriz é quadrada"() {
        setup:
        matriz = new SistemaMatriz(5)

        expect:
        matrizValidations.isQuadrada(matriz)
    }

    def "valida se é diagonalmente dominante"() {
        when:
        matriz = matrizHelper.getValidMatriz()

        then:
        matrizValidations.isDiagonalmenteDominante(matriz)

        when:
        matriz = matrizHelper.getInvalidMatriz()

        then:
        !matrizValidations.isDiagonalmenteDominante(matriz)
    }
}

import beans.SistemaMatriz
import spock.lang.Specification
import validations.MatrizValidations

class MatrizValidationsTest extends Specification {

    private MatrizValidations matrizValidations
    private SistemaMatriz matriz

    def setup() {
        matrizValidations = new MatrizValidations()
        matriz = new SistemaMatriz(5)
    }

    def "valida se matriz é quadrada"() {
        expect:
        matrizValidations.isQuadrada(matriz) == true
    }

    def "valida se as linhas são linearmente independentes"() {
        setup:
        //alimentar a matriz

        expect:
        matrizValidations.isLinearmenteIndependente(matriz) == true
    }

    def "valida se a diagonal principal possui os maiores valores absolutos"() {
        setup:
        //alimentar a matriz

        expect:
        matrizValidations.isDiagonalPrincipalAbsoluta(matriz) == true
    }

    private void AlimentarMatriz(){

    }
}

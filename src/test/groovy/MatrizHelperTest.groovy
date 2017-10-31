import beans.SistemaMatriz
import helper.MatrizHelper
import spock.lang.Specification

class MatrizHelperTest extends Specification {

    private MatrizHelper matrizHelper = new MatrizHelper()

    def "deve gerar uma matriz com valores aleatórios"() {
        setup:
        def matriz = matrizHelper.newMatrizWithRandomValues(5)

        //when:

        //then:
        expect:
        matriz.functions.every { it.variables.every{ it.value >= 1 && it.value <= 100}}
    }

    def "deve retornar apenas a diagonal principal"() {
        setup:
        def matriz = matrizHelper.newMatrizWithRandomValues(5)

        when:
        matriz = matrizHelper.getDiagonalPrincipal(matriz)

        then:
        //expect:
        matriz.getFunctions().get(0).variables.get(0).value != null
        matriz.getFunctions().get(1).variables.get(1).value != null
        matriz.getFunctions().get(2).variables.get(2).value != null
        matriz.getFunctions().get(3).variables.get(3).value != null
        matriz.getFunctions().get(4).variables.get(4).value != null

        matriz.getFunctions().get(0).variables.get(1).value == null
        matriz.getFunctions().get(0).variables.get(2).value == null
        matriz.getFunctions().get(0).variables.get(3).value == null
        matriz.getFunctions().get(0).variables.get(4).value == null

    }



}

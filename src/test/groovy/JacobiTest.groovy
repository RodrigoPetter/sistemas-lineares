import helper.MatrizHelper
import jacobi.Jacobi
import spock.lang.Specification

import java.math.RoundingMode

class JacobiTest extends Specification {

    private MatrizHelper matrizHelper = new MatrizHelper()

    def "A solução inicial deve ter o mesmo tamanho que a quantidade de variáveis da matriz"() {
        setup:
        def matriz = matrizHelper.getNewMatrizWithRandomValues(5)
        Jacobi jacobi = new Jacobi(matriz)

        //when:

        //then:
        expect:
        jacobi.solucoes.size() == 1
        jacobi.solucoes.get(0).size() == 5

        and: "Todas as soluções iniciais devem ser igual a 0"
        jacobi.solucoes.get(0).every { it == 0 }
    }

    def "Deve retornar a interação 1 com os valores corretos"() {
        setup:
        def matriz = matrizHelper.getValidMatriz()
        Jacobi jacobi = new Jacobi(matriz)

        when:
        def interecao1 = jacobi.getNextSolucao(10, RoundingMode.HALF_UP)

        then:
        interecao1.get(0) == 2.6
        interecao1.get(1) == 0.6666666667
        interecao1.get(2) == 2.1666666667

        and: "Deve retornar a interação 2 com os valores corretos"
        def interecao2 = jacobi.getNextSolucao(10, RoundingMode.HALF_UP)
        interecao2.get(0) == 1.6000000000
        interecao2.get(1) == 2.2555555556
        interecao2.get(2) == 3.2000000000
    }

}

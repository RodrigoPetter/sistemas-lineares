import helper.MatrizHelper
import sidel.Seidel
import spock.lang.Specification

import java.math.RoundingMode

class SeidelTest extends Specification {

    private MatrizHelper matrizHelper = new MatrizHelper()

    def "A solução inicial deve ter o mesmo tamanho que a quantidade de variáveis da matriz"() {
        setup:
        def matriz = matrizHelper.getNewMatrizWithRandomValues(5)
        Seidel seidel = new Seidel(matriz)

        //when:

        //then:
        expect:
        seidel.solucoes.size() == 1
        seidel.solucoes.get(0).size() == 5

        and: "Todas as soluções iniciais devem ser igual a 0"
        seidel.solucoes.get(0).every { it == 0 }
    }

    def "Deve retornar a interação 1 com os valores corretos"() {
        setup:
        def matriz = matrizHelper.getValidMatriz()
        Seidel seidel = new Seidel(matriz)

        when:
        def interecao1 = seidel.getNextSolucao(10, RoundingMode.HALF_UP)

        then:
        interecao1.get(0) == 2.6
        interecao1.get(1) == 1.5333333333
        interecao1.get(2) == 3.4166666667

        and: "Deve retornar a interação 2 com os valores corretos"
        def interecao2 = seidel.getNextSolucao(10, RoundingMode.HALF_UP)
        interecao2.get(0) == 0.9266666667
        interecao2.get(1) == 2.1144444445
        interecao2.get(2) == 3.0041666667
    }

}

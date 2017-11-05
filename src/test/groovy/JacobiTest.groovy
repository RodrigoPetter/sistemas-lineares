import helper.MatrizHelper
import jacobi.Jacobi
import spock.lang.Specification

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
        def interecao1 = jacobi.getNextSolucao()

        then:
        interecao1.get(0) == 2.6
        interecao1.get(1) == 0.6666666667
        interecao1.get(2) == 2.1666666667
    }

}

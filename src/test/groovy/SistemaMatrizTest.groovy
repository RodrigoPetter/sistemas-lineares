import beans.SistemaMatriz
import spock.lang.Specification

class SistemaMatrizTest extends Specification {

    def "o construtor deve criar uma matriz quadrada"() {
        setup:
        def matriz = new SistemaMatriz(5)

        //when:

        //then:
        expect:
        matriz.getFunctions().size() == 5
        matriz.getFunctions().get(0).variables.size() == 5
    }

}

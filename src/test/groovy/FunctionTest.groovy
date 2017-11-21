import beans.ResultadosEsperados
import beans.SistemaMatriz
import beans.Variable
import helper.MatrizHelper
import spock.lang.Specification

class FunctionTest extends Specification {

    private MatrizHelper matrizHelper = new MatrizHelper()


    def "Deve calcular o resultado da função corretamente"() {
        setup:
        def matriz = matrizHelper.getValidMatriz()
        ResultadosEsperados resultadosEsperados = new ResultadosEsperados(3)

        resultadosEsperados.resultadosEsperados.get(0).value = 1
        resultadosEsperados.resultadosEsperados.get(1).value = 2
        resultadosEsperados.resultadosEsperados.get(2).value = 3

        matriz.setValueAt(0,0,3)
        matriz.setValueAt(0,1,3)
        matriz.setValueAt(0,2,3)

        when:
        matriz.functions.get(0).calcularResultado(resultadosEsperados)
        matriz.functions.get(1).calcularResultado(resultadosEsperados)
        matriz.functions.get(2).calcularResultado(resultadosEsperados)

        then:
        //expect:
        matriz.getValueAt(0,3) == 26
        matriz.getValueAt(1,3) == -6
        matriz.getValueAt(2,3) == -26
    }

}

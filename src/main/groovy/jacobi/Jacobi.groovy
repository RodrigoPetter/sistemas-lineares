package jacobi

import beans.SistemaMatriz

import java.math.RoundingMode

class Jacobi {

    private SistemaMatriz sistemaMatriz
    List<List<BigDecimal>> solucoes = new ArrayList<>()

    Jacobi(SistemaMatriz sistemaMatriz) {
        this.sistemaMatriz = sistemaMatriz

        List<BigDecimal> solucaoInicial = new ArrayList<>()

        sistemaMatriz.functions.each {
            solucaoInicial.add(0)
        }

        solucoes.add(solucaoInicial)
    }

    List<BigDecimal> getNextSolucao(Integer precisao, RoundingMode roundingMode) {

        List<BigDecimal> novaSolucao = new ArrayList<>()                                  //1
        Integer ultimaInteracaoIndex = solucoes.size() - 1                                //1

        sistemaMatriz.functions.eachWithIndex { funcao, indexF ->                         //N

            BigDecimal calc = new BigDecimal(funcao.result)                               //->1
            calc = calc.setScale(precisao, roundingMode)                                  //->1

            funcao.variables.eachWithIndex { variavel, indexV ->                          //->N
                if (indexF != indexV) {                                                   //-->1
                    def multiplicador = solucoes.get(ultimaInteracaoIndex).get(indexV)    //-->1
                    calc = calc.add(multiplicador.multiply((-variavel.value)))            //-->1
                }
            }

            calc = calc.divide(sistemaMatriz.getValueAt(indexF, indexF), precisao, roundingMode) //->1

            novaSolucao.add(calc)                                                                //->1
        }

        solucoes.add(novaSolucao)                                                               //1
        return novaSolucao                                                                      //1
    }
}

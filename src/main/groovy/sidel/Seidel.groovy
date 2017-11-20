package sidel

import beans.SistemaMatriz

import java.math.RoundingMode

class Seidel {

    private SistemaMatriz sistemaMatriz
    List<List<BigDecimal>> solucoes = new ArrayList<>()

    Seidel(SistemaMatriz sistemaMatriz) {
        this.sistemaMatriz = sistemaMatriz

        List<BigDecimal> solucaoInicial = new ArrayList<>()

        sistemaMatriz.functions.each {
            solucaoInicial.add(0)
        }

        solucoes.add(solucaoInicial)
    }

    List<BigDecimal> getNextSolucao(Integer precisao, RoundingMode roundingMode) {

        List<BigDecimal> novaSolucao = new ArrayList<>()
        Integer ultimaInteracaoIndex = solucoes.size() - 1

        sistemaMatriz.functions.eachWithIndex { funcao, indexF ->

            BigDecimal calc = new BigDecimal(funcao.result)
            calc = calc.setScale(precisao, roundingMode)

            funcao.variables.eachWithIndex { variavel, indexV ->
                if (indexV > indexF) {
                    calc = calc.add(solucoes.get(ultimaInteracaoIndex).get(indexV).multiply((-variavel.value)))
                }
                if (indexV < indexF) {
                    calc = calc.add(novaSolucao.get(indexV).multiply((-variavel.value)))
                }
            }

            calc = calc.divide(sistemaMatriz.getValueAt(indexF, indexF), precisao, roundingMode)

            novaSolucao.add(calc)
        }

        solucoes.add(novaSolucao)
        return novaSolucao
    }
}

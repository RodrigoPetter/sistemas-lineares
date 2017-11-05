package jacobi

import beans.SistemaMatriz

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

    List<BigDecimal> getNextSolucao() {
        List<BigDecimal> novaSolucao = new ArrayList<>()

        Integer ultimaInteracaoIndex = solucoes.size() - 1

        sistemaMatriz.functions.eachWithIndex { funcao, indexF ->

            def soma = funcao.result

            funcao.variables.eachWithIndex { variavel, indexV ->
                if (indexF != indexV) {
                    soma += (-variavel.value) * solucoes.get(ultimaInteracaoIndex).get(indexF)
                }
            }

            def resultado = soma / sistemaMatriz.getValueAt(indexF, indexF)

            novaSolucao.add(resultado)
        }

        solucoes.add(novaSolucao)
        return novaSolucao
    }
}

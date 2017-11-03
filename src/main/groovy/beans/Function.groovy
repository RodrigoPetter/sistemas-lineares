package beans

import groovy.transform.Canonical

@Canonical
class Function {

    List<Variable> variables = new ArrayList<Variable>()
    def result = 0

    Function(Integer quantidadeVariaveis) {

        for (int i = 0; i < quantidadeVariaveis; i++) {
            variables.add(new Variable())
        }

    }

    def somaModular(){

        def soma = 0

        variables.each {
            soma += Math.abs(it.value)
        }

        return soma
    }

}

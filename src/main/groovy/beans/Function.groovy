package beans

import groovy.transform.Canonical

@Canonical
class Function {

    List<Variable> variables = new ArrayList<Variable>()

    Function(Integer quantidadeVariaveis) {

        for (int i = 0; i < quantidadeVariaveis; i++) {
            variables.add(new Variable())
        }

    }

}

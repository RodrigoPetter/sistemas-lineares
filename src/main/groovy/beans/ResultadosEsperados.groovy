package beans

class ResultadosEsperados {

    List<Variable> resultadosEsperados = new ArrayList<>()

    ResultadosEsperados(Integer quantidadeVariaveis) {
        for (int i = 0; i < quantidadeVariaveis; i++) {
            resultadosEsperados.add(new Variable())
        }
    }
}

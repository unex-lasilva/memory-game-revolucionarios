//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {
    exibirMenssagemBoasVidas()
     exibirMenuPrincipal()

}

fun exibirMenssagemBoasVidas(){
    println("========================================")
    println("Bem-vindo ao Manga Rosa Memory Game!")
    println("========================================")
}
fun exibirMenuPrincipal(){
    while (true) {
        println("Menu principal")
        println("1 -> Começar")
        println("2 -> Regras")
        println("3 -> Pontuação")
        println("4 -> Sair ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> iniciaJogo()
            2 -> exibirRegras()
            3 -> exibirPontuacao()
            4 ->{
                println("saindo do jogo... ate mais :(")
                break

            }

            else -> println("opção invalida!! tente novamente digitando uma opção valida")
        }
    }
}
fun iniciaJogo(){
    println("Agora é com vocês sam e Arthur...")
}

fun exibirRegras() {
    println("========================================")
    println("            REGRAS DE PONTUAÇÃO             ")
    println("========================================")
    println(
        "\n Se  encontrar um par de cartas com fundo amarelo, fatura 1 ponto \n" +
                " Se  encontrar um par de cartas com o fundo da sua cor, fatura 5 pontos \n" +
                " Se você encontrar um par de cartas com o fundo da cor de seu adversário e errar, perde 2 pontos. Porém, se acertar, ganha apenas 1 ponto.  \n" +
                " Se você ficar sem pontos não se preocupe não ficara negativo seus pontos continuará nulo ate conserguir marcar algum \n" +
                "Se você encontrar uma carta com o fundo preto e errar o seu par, sinto muito perde o jogo, mesmo que tenha a pontuação superior à da(o) outra(o) participante. Mas se acertar, ganha o jogo. :) "
    )
    println("Aperte ENTER para voltar para o menu principal: ")
    readln()

}

fun exibirPontuacao(){
    println("É contigo meu mano tony...")
}

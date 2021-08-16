# Bootcamp: Inter - Android Developer
### github-portfolio
Atividade para concluir o bootcamp Inter - Android Developer
Objetivo da atividade é criar um App Android para apresentar seu portfólio de projetos do GitHub de maneira elegante e simplificada. Nesse contexto, passamos por todo o processo de desenvolvimento usando o Kotlin, uma das linguagens de programação de maior ascensão nos últimos anos. Por fim, somos desafiados a entregar nosso próprio projeto, incorporando nossas próprias evoluções e melhorias!

Como melhoria ao aplicativo adicionei uma melhor visualização do perfil do usuário do qual o repositorio está sendo mostrado. Melhoria na exibição da mensagem de erro.
A partir do perfil é possível abrir o perfil do twitter do usuário, ou mandar um email, caso o usuário tenha configurado isso nas configurações do seu perfil.
Foi adicionada uma tela de empty state para que a tela não fique em branco ao abrir o App.

As Tecnologias usadas foram:

* Koin para a injeção de dependência.
* Retrofit para acessar a Api do Github
* Glide para carregar as imagens
* Coroutines para tarefas assincronas
* Lifecycle para trabalhar com LiveData e ViewModels

**Abaixo uma pré-visualização do comportamento do App.**

![preview](https://user-images.githubusercontent.com/637273/129627120-ecbb8f57-ea76-485c-9755-4f35ec10f975.gif)
# Plataforma de Review de Jogos

![checkpoint](https://github.com/apabsp/Checkpoint/assets/63313892/83c8a207-6c29-4c70-9a48-e50f3d017cfb)


Esse projeto foi desenvolvido como parte da disciplina de Requisitos, projeto de software e validação. 

O **CheckPoint** tem como objetivo fornecer uma plataforma para que os usuários possam compartilhar, salvar, descobrir e escrever avaliações de jogos.

**Seu guia completo para avaliar e descobrir jogos!**

## O que é?

* Avalie e comente jogos
* Organize sua biblioteca
* Descubra novos jogos
* Conecte-se com a comunidade

## Links:
Lo-fi: https://www.figma.com/design/or1078JrxwtuCJtsCdBJL8/Untitled?node-id=0-1&t=RDqwF0ybdT7fNjob-1

Descrição Onipresente: https://docs.google.com/document/d/15g4gLoQL2DzEMKV60VfiVbXVEGK9qFGaOY5B0pKeNuQ/edit?usp=sharing

Mapa da Jornada do Usuário: https://www.figma.com/board/c6SUCNawoxS44CE2699skJ/Sem-t%C3%ADtulo?node-id=0-1&p=f&t=TYIoZyMhIaEtnPdh-0

Apresentação: https://www.canva.com/design/DAG2Uw5oaCs/OpcpvDQ7PuL4eKPtbChGAQ/edit

## Como rodar

### O backend precisa de duas variáveis de ambiente:
  - RESEND_API_KEY;
  - JWT_SECRET;

***RESEND_API_KEY*** precisa ser a chave de api da plataforma [resend](https://resend.com/), nessa plataforma, voce conseguirá pegar uma chave de api.
Essa etapa é necessária pois o sistema precisa de confirmação de email do usuario para poder logar.

***JWT_SECRET*** pode ser uma string qualquer, mas é recomendado um hash de 32 bits, podendo ser feit com o comando:
```bash
openssl rand -hex 32
```

### O frontend precisa apenas de uma variável de ambiente:
  - VITE_API_URL

***VITE_API_URL*** é a url de acesso ao backend.

### Você também pode rodar o projeto simplesmente executando:
```bash
docker compose up
```

Mas precisa inserir manualmente as variáveis de ambiente do backend (RESEND_API_KEY e JWT_SECRET)

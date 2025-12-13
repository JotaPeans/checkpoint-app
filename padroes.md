## Template Method
- **AutenticacaoTemplate:** `AutenticacaoTemplate()`, `gerarJwt()`, `validarSenha()`, `verificarToken()`
- **AutenticacaoImpl:** `AutenticacaoImpl()`, `gerarJwt()`, `verificarToken()`, `validarSenha()`

## Strategy:
- **EmailStrategy:** `sendEmail()`
- **ResendStrategy:** `sendEmail()`

## Iterator:
- **ListaControlador:** `getListasPublicasPaginadas()`, `getListasPublicasComIterator()`
- **ListaServico:** `ListasPublicasIterator()`, `hasNext()`, `next()`, `nextPage()`, `hasNextPage()`, `getTotalPages()`, `getTotalItems()`, `goToPage()`

## Observer:
- **CheckpointApplication:** `userServico()`
- **UserServico:** `adicionarObservador()`, `removerObservador()`, `solicitarSeguir()`, `onSolicitacaoParaSeguir()`, `toggleSeguir()`
- **EmailFollowObserver:** `EmailFollowObserver()`, `onSolicitacaoParaSeguir()`
- **FollowEventObserver:** `onSolicitacaoParaSeguir()`

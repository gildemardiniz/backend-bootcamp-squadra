## DESAFIO FINAL DO BOOTCAMP SQUADRA
### ENDPOINTS:
1. UF
   - POST - Adiciona uma nova UF ao banco de dados.
   - GET - Realiza as busca de todas as UF's, como tambem por paramentros de codigoUF, sigla, nome e status.
   - PUT - Edita as informações de Uf.
3. MUNICIPIO
   - POST - Adiciona um novo municipio ao banco de dados.
   - GET - Realiza as busca de todos os municipios, como tambem por paramentros de codigoMunicipio, codigoUF, nome e status.
   - PUT - Efita as informações de municipio.
5. BAIRRO
   - POST - Adiciona um novo bairro ao banco de dados.
   - GET - Realiza as busca de todos os bairros, como tambem por paramentros de codigoBairro, codigoMunicipio, codigoUF, nome e status.
   - PUT - Edita as informações de bairro.
7. ENDEREÇO
   - POST - Adiciona um novo endereco ao banco de dados.
   - GET - Realiza as busca de todos os endereções, como tambem por paramentros de codigoEndereço, codigoBairro, nomeRua, numero, complemento, cep, codigoPessoa.
   - PUT - Edita as informações de endereço.
   - DELETE - Deleta do banco o endereço.
9. PESSOA
    - POST - Adiciona uma nova pessoa ao banco de dados, bem como, uma lista de endereços.
    - GET - Realiza as busca de todos os endereções, como tambem por paramentros de codigoPessoa, codigoBairro, nome, login, status.
    - PUT - Edita as informações de pessoas bem como adiciona, edita e apaga endereços.
    - DELETE - Modifica o status da pessoa para inativo.

#### OBSERVAÇÕES:
- Todas as respostas positivas estão com status 200 como solicitado no desafio.
- Todos os erros tem resposta em um JSON com mensagem em portugues e status com o codigo do erro.

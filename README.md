# SolAppICM

#### Link para download da app

[SolApp apk](https://drive.google.com/drive/folders/1azsGA7TSu2nWZj0gYyPiBjDrippF-7BG?usp=sharing)

## Funcionalidade implementada

A SolApp é uma aplicação para dispositivos Android que através da API do IPMA apresenta dados sobre o tempo de várias regiões. O utilizador escolhe um local como 'Aveiro' e de seguida são apresentadas as previsões para os próximos 5 dias. Cada previsão é composta por temperatura máxima, temperatura mínima, probabilidade de chover.

![alt text](https://github.com/joaosilva9/SolAppICM/blob/master/app.png)

## Arquitetura
![alt text](https://github.com/joaosilva9/SolAppICM/blob/master/architecture.png)

### Activity/Fragment:
- MainActivity

### ViewModel:
- MViewModel (ligação do repositório às atividades)

### Repository:
- Repository (componente que liga à base de dados e ao webService e escolhe quando usar qual e quando fazer novas transferências do repositório remoto)

### Model:
- MyDatabase (definição da base de dados)
- WeatherDao (interface que define todos os métodos de interação com a base de dados)
- CityDao (interface que define todos os métodos de interação com a base de dados)
- CityEntry (objeto associado a tabela da db)
- DistrictInfo (objeto usado para obter informações para o objeto CityEntry)
- Weather (objeto associado a tabela da db)
- WeatherInfo (objeto usado para obter informações para o objeto Weather)
- WeatherTypeEntry (objeto associado a tabela da db)
- TypeDataWeather (objeto usado para obter informações para o objeto WeatherTypeEntry)

### Remote Data Source:
- ApiService (definição dos caminhos da API)

Para consumo da API foi utilizado GSON e Retrofit, tendo sido utilizada a cache do retrofit para guardar alguns dados. Para manipulação de tarefas que devem ser executadas fora da MainThread foi utilizado Executor.

## Limitações

Não consegui integrar a componente Model no projeto.


## Cuidados particulares

Convém ter a internet ligada pelo menos uma vez para obetenção dos dados à api. Offline apenas mostra informação da cache do Retrofit.
Tem um Target Mínimo de 21 e foi testado até ao 27.


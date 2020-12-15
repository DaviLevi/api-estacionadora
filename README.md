# api-estacionadora
Api básica desenvolvida a escopo didatico para fins de controle diario de um estacionamento

# API Domain - Diagrama entidade-relacionamento
![Diagrama ER](../main/docs/er_diagram_estacionadora.png)

# Endpoints

VeiculoEstacionados :

- Estacionar um carro : POST - localhost:8080/veiculos-estacionados
- Obter o status do veiculo estacionado : GET - localhost:8080/veiculos-estacionados/:placa
- Listar os carros estacionados até o momento : GET - localhost:8080/veiculos-estacionados
- Resgatar um carro estacionado anteriormente : PUT localhost:8080/veiculos-estacionados/:placa/resgate

Fluxo de Caixa :

- Obter o fluxo de caixa/faturamento entre um intervalo de dias : GET - localhost:8080/fluxo-de-caixa?dataInicial=ddMMyyyy&dataFinal=ddMMyyyy   





# Request Payloads 

- VeiculoEstacionado :
```json
{
	"marca" : "Citroen",
	"placa" : "HHL7499",
	"modelo" : "C3",
	"dataEntrada" : "12/12/2020 10:30"
}
```



# Response Payloads 
- Veiculo estacionado  :
```json
{
  "placa": "HHL7499",
  "modelo": "C3",
  "marca": "Citroen",
  "dataEntrada": "2020-12-12T10:30:00",
  "valorPorHora": 3.0,
  "valorPrimeiraHora": 5.0
}
```

- Veiculo resgatado  :
```json
{
  "placa": "HHL7499",
  "modelo": "C3",
  "marca": "Citroen",
  "dataResgate": "13/12/2020 16:02",
  "valorTotal": 92.0
}
```

- Veiculo buscado  :
```json
{
  "placa": "HHL7488",
  "modelo": "C3",
  "marca": "Citroen",
  "dataEntrada": "12/12/2020 10:30",
  "dataSaida": "12/12/2020 20:02",
  "custoTotal": 32.0
}
```

- Veiculos estacionados  :
```json
[
  {
    "placa": "HHL7499",
    "modelo": "C3",
    "marca": "Citroen",
    "dataEntrada": "2020-12-12T10:30:00",
    "valorPorHora": 3.0,
    "valorPrimeiraHora": 5.0
  }
]
```
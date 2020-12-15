package br.com.estacionadora.api.veiculosEstacionados;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.estacionadora.domain.model.Fatura;
import br.com.estacionadora.domain.repository.FaturaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VeiculosEstacionadosResourceTests {

    @Autowired
    private WebApplicationContext context;

    @Test
    public void seEuEstacionarUmVeiculoNovo_EntaoARespostaSeraCreated() throws Exception {

    	String placa = "HHL7499";
    	
        final String body = "{\n"
        		+ "	\"marca\" : \"Citroen\",\n"
        		+ "	\"placa\" : \"" + placa + "\",\n"
        		+ "	\"modelo\" : \"C3\",\n"
        		+ "	\"dataEntrada\" : \"12/12/2020 10:30\"\n"
        		+ "}";

        MockMvcBuilders
            .webAppContextSetup(this.context)
            .build()
            .perform(request(HttpMethod.POST, "/veiculos-estacionados").contentType(MediaType.APPLICATION_JSON).content(body))
            .andExpect(status().isCreated());
    }
    
    @Test
    public void seEuEstacionarOMesmoVeiculoDuasVezes_EntaoASegundaRespostaSeraBadRequest() throws Exception {

    	String placa = "HHL7488";
    	
        final String bodyVeiculo1 = "{\n"
        		+ "	\"marca\" : \"Citroen\",\n"
        		+ "	\"placa\" : \"" + placa + "\",\n"
        		+ "	\"modelo\" : \"C3\",\n"
        		+ "	\"dataEntrada\" : \"12/12/2020 10:30\"\n"
        		+ "}";
        
        final String bodyVeiculo2 = "{\n"
        		+ "	\"marca\" : \"Citroen\",\n"
        		+ "	\"placa\" : \"" + placa + "\",\n"
        		+ "	\"modelo\" : \"C3\",\n"
        		+ "	\"dataEntrada\" : \"12/12/2020 10:30\"\n"
        		+ "}";

        MockMvcBuilders
            .webAppContextSetup(this.context)
            .build()
            .perform(request(HttpMethod.POST, "/veiculos-estacionados").contentType(MediaType.APPLICATION_JSON).content(bodyVeiculo1))
            .andExpect(status().isCreated());
        
        MockMvcBuilders
	        .webAppContextSetup(this.context)
	        .build()
	        .perform(request(HttpMethod.POST, "/veiculos-estacionados").contentType(MediaType.APPLICATION_JSON).content(bodyVeiculo2))
	        .andExpect(status().isBadRequest());
    }
    
    
    @Test
    public void seEuResgatarOMesmoVeiculoDuasVezes_EntaoASegundaRespostaSeraNotFound() throws Exception {

    	String placa = "HHL7500";
    	FaturaRepository faturas = this.context.getBean(FaturaRepository.class);
    	
        final String bodyVeiculo1 = "{\n"
        		+ "	\"marca\" : \"Citroen\",\n"
        		+ "	\"placa\" : \"" + placa + "\",\n"
        		+ "	\"modelo\" : \"C3\",\n"
        		+ "	\"dataEntrada\" : \"12/12/2020 10:30\"\n"
        		+ "}";
        
        MockMvcBuilders
            .webAppContextSetup(this.context)
            .build()
            .perform(request(HttpMethod.POST, "/veiculos-estacionados").contentType(MediaType.APPLICATION_JSON).content(bodyVeiculo1))
            .andExpect(status().isCreated());
        
        MockMvcBuilders
	        .webAppContextSetup(this.context)
	        .build()
	        .perform(request(HttpMethod.PUT, "/veiculos-estacionados/HHL7500/resgate").contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk());
        
        MockMvcBuilders
	        .webAppContextSetup(this.context)
	        .build()
	        .perform(request(HttpMethod.PUT, "/veiculos-estacionados/HHL7500/resgate").contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound());
        
        faturas.deleteAll();
    }
    
    @Test
    public void seEuResgatarUmVeiculo_EntaoSeraGeradaUmaFaturaComValorCorrespondente() throws Exception {

    	String placa = "HHL7600";
    	FaturaRepository faturas = this.context.getBean(FaturaRepository.class);
    	
        final String bodyVeiculo1 = "{\n"
        		+ "	\"marca\" : \"Citroen\",\n"
        		+ "	\"placa\" : \"" + placa + "\",\n"
        		+ "	\"modelo\" : \"C3\",\n"
        		+ "	\"dataEntrada\" : \"12/12/2020 10:30\"\n"
        		+ "}";
        
        BigDecimal custoPorHoraEstacionada = BigDecimal.valueOf(3.0);
    	BigDecimal custoPrimeiraHoraEstacionada = BigDecimal.valueOf(5.0);
        LocalDateTime dataAtual = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		Duration tempoEstacionado = Duration.between(LocalDateTime.parse("12/12/2020 10:30", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), dataAtual);
		long horasEstacionadas = (long)(Math.ceil(tempoEstacionado.toMinutes()/60.0));
		Function<Long, BigDecimal> calculaValor = hrs -> custoPorHoraEstacionada.multiply(BigDecimal.valueOf(hrs-1)).add(custoPrimeiraHoraEstacionada);
		BigDecimal valorAPagar = calculaValor.apply(horasEstacionadas);
        
        MockMvcBuilders
            .webAppContextSetup(this.context)
            .build()
            .perform(request(HttpMethod.POST, "/veiculos-estacionados").contentType(MediaType.APPLICATION_JSON).content(bodyVeiculo1))
            .andExpect(status().isCreated());
        
        MockMvcBuilders
	        .webAppContextSetup(this.context)
	        .build()
	        .perform(request(HttpMethod.PUT, "/veiculos-estacionados/HHL7600/resgate").contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk());
        
//        .andReturn()
//        .getResponse()
//        .getContentAsString()
        
        long contagemFaturas = faturas.count();
        
        assert contagemFaturas == 1;
        
        Fatura fatura = faturas.findById(2L).get();
        BigDecimal valorFatura = fatura.getValorPago();
        
        assert valorFatura.compareTo(valorAPagar) == 0;
        
        faturas.deleteById(2L);
    }
    
    
    @Test
    public void seEuResgatarUmVeiculoEstacionado_EntaoARespostaSeraOk() throws Exception {

    	String placa = "HHL7700";
    	
        final String bodyVeiculo1 = "{\n"
        		+ "	\"marca\" : \"Citroen\",\n"
        		+ "	\"placa\" : \"" + placa + "\",\n"
        		+ "	\"modelo\" : \"C3\",\n"
        		+ "	\"dataEntrada\" : \"12/12/2020 10:30\"\n"
        		+ "}";

        MockMvcBuilders
            .webAppContextSetup(this.context)
            .build()
            .perform(request(HttpMethod.POST, "/veiculos-estacionados").contentType(MediaType.APPLICATION_JSON).content(bodyVeiculo1))
            .andExpect(status().isCreated());
        
        MockMvcBuilders
	        .webAppContextSetup(this.context)
	        .build()
	        .perform(request(HttpMethod.PUT, "/veiculos-estacionados/HHL7700/resgate").contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk());
        
    }
}
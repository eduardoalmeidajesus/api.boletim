package br.edu.utfpr.td.tsi.trabalho1.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import br.edu.utfpr.td.tsi.trabalho1.endpoint.BoletimFurtoVeiculoEndpoint;
import jakarta.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api")
public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration() {
        register(BoletimFurtoVeiculoEndpoint.class);
        packages("br.edu.utfpr.td.tsi.trabalho1.endpoint");
    }
}
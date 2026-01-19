package br.edu.utfpr.td.tsi.trabalho1.endpoint;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.edu.utfpr.td.tsi.trabalho1.model.BoletimFurtoVeiculo;
import br.edu.utfpr.td.tsi.trabalho1.model.Veiculo;
import br.edu.utfpr.td.tsi.trabalho1.service.BoletimFurtoVeiculoService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Component
@Path("/boletins")
public class BoletimFurtoVeiculoEndpoint {

    @Autowired
    private BoletimFurtoVeiculoService boletimService;

    @GET
    @Path("/status")
    @Produces(MediaType.TEXT_PLAIN)
    public Response status() {
        return Response.ok("API de Boletins de Furto de Veículos ativa").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrar(BoletimFurtoVeiculo boletim) {
        try {
            boletimService.cadastrar(boletim);
            BoletimFurtoVeiculo mascarado = boletimService.mascararPartes(boletim);
            return Response.status(Response.Status.CREATED).entity(mascarado).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"erro\": \"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro interno do servidor\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizar(@PathParam("id") String id, BoletimFurtoVeiculo boletim) {
        try {
            boletimService.atualizar(id, boletim);
            BoletimFurtoVeiculo mascarado = boletimService.mascararPartes(boletim);
            return Response.ok(mascarado).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"erro\": \"" + e.getMessage() + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro interno do servidor\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") String id) {
        try {
            boletimService.remover(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro interno do servidor\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response localizarPorId(@PathParam("id") String id) {
        try {
            BoletimFurtoVeiculo boletim = boletimService.localizarPorId(id);
            if (boletim != null) {
                return Response.ok(boletimService.mascararPartes(boletim)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("{\"erro\": \"Boletim não encontrado\"}").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro interno do servidor\"}").build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarTodos() {
        try {
            List<BoletimFurtoVeiculo> boletins = boletimService.listarTodos()
                    .stream()
                    .map(b -> boletimService.mascararPartes(b))
                    .collect(Collectors.toList());
            return Response.ok(boletins).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro interno do servidor\"}").build();
        }
    }

    @GET
    @Path("/cidade/{cidade}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPorCidade(@PathParam("cidade") String cidade) {
        try {
            List<BoletimFurtoVeiculo> boletins = boletimService.listarPorCidade(cidade)
                    .stream()
                    .map(b -> boletimService.mascararPartes(b))
                    .collect(Collectors.toList());
            return Response.ok(boletins).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro interno do servidor\"}").build();
        }
    }

    @GET
    @Path("/periodo/{periodo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarPorPeriodo(@PathParam("periodo") String periodo) {
        try {
            List<BoletimFurtoVeiculo> boletins = boletimService.listarPorPeriodo(periodo)
                    .stream()
                    .map(b -> boletimService.mascararPartes(b))
                    .collect(Collectors.toList());
            return Response.ok(boletins).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro interno do servidor\"}").build();
        }
    }

    @GET
    @Path("/veiculos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarVeiculos(@QueryParam("placa") String placa,
                                  @QueryParam("cor") String cor,
                                  @QueryParam("tipo") String tipo) {
        try {
            List<Veiculo> veiculos;

            if (placa != null && !placa.trim().isEmpty()) {
                veiculos = boletimService.listarVeiculosPorPlaca(placa);
            } else if (cor != null && !cor.trim().isEmpty()) {
                veiculos = boletimService.listarVeiculosPorCor(cor);
            } else if (tipo != null && !tipo.trim().isEmpty()) {
                veiculos = boletimService.listarVeiculosPorTipo(tipo);
            } else {
                return Response.status(Response.Status.BAD_REQUEST).entity("{\"erro\": \"Deve ser informado placa, cor ou tipo\"}").build();
            }

            return Response.ok(veiculos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro interno do servidor\"}").build();
        }
    }

    @POST
    @Path("/carregar-csv")
    @Produces(MediaType.APPLICATION_JSON)
    public Response carregarCSV() {
        try {
            boletimService.carregarDadosCSV("furtos.csv");
            return Response.ok("{\"mensagem\": \"Dados carregados com sucesso\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"erro\": \"Erro ao carregar CSV\"}").build();
        }
    }
}
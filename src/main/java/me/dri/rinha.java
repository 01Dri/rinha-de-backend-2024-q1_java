package me.dri;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.dri.entities.dto.ExtratoDTO;
import me.dri.entities.dto.TransacaoDTO;
import me.dri.entities.dto.TransacaoResponseDTO;
import me.dri.exceptions.*;
import me.dri.repository.ClientesRepository;
import me.dri.repository.ClientesRepositoryImpl;
import me.dri.repository.TransacaoRepository;
import me.dri.repository.TransacaoRepositoryImpl;
import me.dri.services.ClienteService;
import me.dri.services.ClienteServiceImpl;
import me.dri.services.ExtratoService;
import me.dri.services.ExtratoServiceImpl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class rinha {
    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
            .create();

    public static void main(String[] args) throws IOException, SQLException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/clientes/", new ClientesHandler());
        server.setExecutor(null);
        System.out.println("Servidor iniciado na porta 8080...");
        server.start();
    }
    static class ClientesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.matches("/clientes/\\d+/extrato")) {
                new ExtratoHandler().handle(exchange);
            } else if (path.matches("/clientes/\\d+/transacoes")) {
                new TransacoesHandler().handle(exchange);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        }
    }

    static class ExtratoHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                String path = exchange.getRequestURI().getPath();
                Pattern pattern = Pattern.compile("/clientes/(\\d+)/extrato");
                Matcher matcher = pattern.matcher(path);
                if (matcher.matches()) {
                    String clienteId = matcher.group(1);
                    try {
                        var response = extratoService(clienteId);

                        String jsonResponse = gson.toJson(response);
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(jsonResponse.getBytes());
                        os.close();

                    } catch (ClienteNaoEncontradoException e) {
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(404, e.getMessage().getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(e.getMessage().getBytes());
                        os.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                        exchange.sendResponseHeaders(500, e.getMessage().getBytes().length);

                    } catch (ExceptionInInitializerError e) {
                        e.printStackTrace();
                    }

                }
            } else {
                exchange.sendResponseHeaders(405, 0); // Método não permitido
            }
        }
    }

    static class TransacoesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("POST")) {
                String path = exchange.getRequestURI().getPath();
                Pattern pattern = Pattern.compile("/clientes/(\\d+)/transacoes");
                Matcher matcher = pattern.matcher(path);
                if (matcher.matches()) {
                    String clienteId = matcher.group(1);
                    String requestBody = new String(exchange.getRequestBody().readAllBytes());

                    ObjectMapper objectMapper = new ObjectMapper();
                    TransacaoDTO transacaoDTO = objectMapper.readValue(requestBody, TransacaoDTO.class);

                    try {
                        System.out.println("Processando transação para o cliente ID: " + clienteId);
                        String jsonResponse = gson.toJson(transacaoService(clienteId, transacaoDTO));
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(jsonResponse.getBytes());
                        os.close();
                    } catch (TipoInvalidoTransacaoException | DescricaoInvalidaTransacaoException |
                             ValorInvalidoTransacaoException | LimiteExcedidoTransacaoException |
                             TipoDeOperacaoInvalidoTransacaoException | LimiteTamanhoDescricaoExcedidoException e) {

                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(422, e.getMessage().getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(e.getMessage().getBytes());
                        os.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        exchange.sendResponseHeaders(500, e.getMessage().getBytes().length);


                    } catch (ClienteNaoEncontradoException e) {
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(404, e.getMessage().getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(e.getMessage().getBytes());
                        os.close();
                    }
                }
            }
        }
    }

    private static ExtratoDTO extratoService (String clienteId) throws SQLException {
        ClientesRepository clientesRepository = new ClientesRepositoryImpl();
        TransacaoRepository transacaoRepository = new TransacaoRepositoryImpl();
        ExtratoService extratoService = new ExtratoServiceImpl(clientesRepository, transacaoRepository);
        return extratoService.getExtratoByClienteId(Integer.parseInt(clienteId));
    }

    private static TransacaoResponseDTO transacaoService(String clienteId, TransacaoDTO transacaoDTO) throws SQLException {
        ClientesRepository clientesRepository = new ClientesRepositoryImpl();
        TransacaoRepository transacaoRepository = new TransacaoRepositoryImpl();
        ClienteService clienteService = new ClienteServiceImpl(clientesRepository, transacaoRepository);
        return clienteService.transacao(Integer.parseInt(clienteId), transacaoDTO);

    }

}
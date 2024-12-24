package com.example.grpcbackend;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class GrpcConfig {

    @Bean
    public Server grpcServer(AlertBackendService alertBackendService) throws IOException {
        // Запуск gRPC-сервера на нужном порту и регистрация вашего сервиса
        return ServerBuilder
                .forPort(9090)
                .addService(alertBackendService)
                .build()
                .start();
    }

    @Bean
    public CommandLineRunner blockThreadUntilShutdown(Server server) {
        // Делаем блокировку основного потока, чтобы сервер не завершался
        return args -> {
            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
    }
}
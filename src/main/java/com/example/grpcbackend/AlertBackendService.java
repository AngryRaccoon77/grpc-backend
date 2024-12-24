package com.example.grpcbackend;

import com.example.grpcbackend.AlertBackendGrpc;
import com.example.grpcbackend.AlertRequest;
import com.example.grpcbackend.AlertResponse;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class AlertBackendService extends AlertBackendGrpc.AlertBackendImplBase {

    @Override
    public void processEvent(AlertRequest request, StreamObserver<AlertResponse> responseObserver) {
        // Извлечение данных из запроса
        String deviceType = request.getEvent().getType();
        String data = request.getEvent().getData();
        boolean status = request.getEvent().getStatus();

        boolean isAlert = false;
        String alertType = null;
        String message = null;
        // Логика определения тревоги
        if (!status) {
            isAlert = true;
            alertType = "OFFLINE";
            message = "Device is offline.";
        } else if (deviceType.equals("WATERLEAK") && data.contains("waterleak: true")) {
            isAlert = true;
            alertType = "WATERLEAK";
            message = "Water leak detected!";
        } else if (deviceType.equals("SMOKE") && data.contains("smoke: true")) {
            isAlert = true;
            alertType = "SMOKE";
            message = "Smoke detected!";
        } else if (deviceType.equals("GAS") && data.contains("gas: true")) {
            isAlert = true;
            alertType = "GAS";
            message = "Gas leak detected!";
        }

        // Формирование ответа
        AlertResponse response = AlertResponse.newBuilder()
                .setIsAlert(isAlert)
                .setAlertType(alertType != null ? alertType : "NONE")
                .setMessage(message != null ? message : "No alert detected.")
                .build();

        // Отправка ответа
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

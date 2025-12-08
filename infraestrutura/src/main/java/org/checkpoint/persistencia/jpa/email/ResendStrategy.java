package org.checkpoint.persistencia.jpa.email;

import org.checkpoint.dominio.email.EmailStrategy;
import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResendStrategy implements EmailStrategy {
  @Value("${resend.api.key}")
  private String apiKey;

  @Override
  public void sendEmail(String recipient, String subject, String body) {
    try {
      Map<String, Object> bodyMap = new HashMap<>();
      bodyMap.put("from", "Checkpoint <checkpoint@geddocs.com.br>");
      bodyMap.put("to", List.of(recipient));
      bodyMap.put("subject", subject);
      bodyMap.put("html", body);
      bodyMap.put("reply_to", "checkpoint@geddocs.com.br");

      Gson gson = new Gson();
      String jsonBody = gson.toJson(bodyMap);

      HttpClient client = HttpClient.newHttpClient();

      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create("https://api.resend.com/emails"))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

    } catch (Exception e) {
      System.out.println("EmailResendImpl: Erro ao enviar email - " + e.getMessage());
    }
  }

}

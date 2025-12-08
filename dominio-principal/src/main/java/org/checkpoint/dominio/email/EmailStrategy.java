package org.checkpoint.dominio.email;

public interface EmailStrategy {
  void sendEmail(String recipient, String subject, String body);
}

package com.supermarket.manager.service;

import com.supermarket.manager.model.produit.Stock;
import com.supermarket.manager.model.rh.Absence;

public interface EmailService {

    void sendEmail(String to, String subject, String body);

    void sendHtmlEmail(String to, String subject, String htmlBody);

    void sendAlertStock(Stock stock);

    void sendAlertPeremption(Stock stock);

    void sendNotificationAbsence(Absence absence, String destinataire);

    void sendNotificationValidationAbsence(Absence absence);
}

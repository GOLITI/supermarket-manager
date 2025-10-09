package com.supermarket.manager.service.impl;

import com.supermarket.manager.model.produit.Stock;
import com.supermarket.manager.model.rh.Absence;
import com.supermarket.manager.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.text.NumberFormat;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.email.admin:admin@supermarket.com}")
    private String adminEmail;

    @Override
    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            log.info("Email envoyé avec succès à: {}", to);
        } catch (MailException e) {
            log.error("Erreur lors de l'envoi de l'email à {}: {}", to, e.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
            log.info("Email HTML envoyé avec succès à: {}", to);
        } catch (MessagingException | MailException e) {
            log.error("Erreur lors de l'envoi de l'email HTML à {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendAlertStock(Stock stock) {
        String subject = "ALERTE STOCK - " + stock.getProduit().getNom();

        StringBuilder body = new StringBuilder();
        body.append("ALERTE STOCK CRITIQUE\n\n");
        body.append("Produit: ").append(stock.getProduit().getNom()).append("\n");
        body.append("Code: ").append(stock.getProduit().getCode()).append("\n");
        body.append("Entrepôt: ").append(stock.getEntrepot().getNom()).append("\n\n");
        body.append("Quantité actuelle: ").append(stock.getQuantite()).append("\n");
        body.append("Seuil d'alerte: ").append(stock.getSeuilReapprovisionnement()).append("\n");
        body.append("Quantité recommandée: ").append(stock.getQuantiteRecommandeeCommande()).append("\n\n");
        body.append("Action requise: Réapprovisionnement urgent\n\n");
        body.append("---\nSupermarket Manager - Système d'alerte automatique");

        sendEmail(adminEmail, subject, body.toString());
    }

    @Override
    public void sendAlertPeremption(Stock stock) {
        String subject = "ALERTE PEREMPTION - " + stock.getProduit().getNom();

        StringBuilder body = new StringBuilder();
        body.append("ALERTE PRODUIT PROCHE DE LA PEREMPTION\n\n");
        body.append("Produit: ").append(stock.getProduit().getNom()).append("\n");
        body.append("Code: ").append(stock.getProduit().getCode()).append("\n");
        body.append("Entrepôt: ").append(stock.getEntrepot().getNom()).append("\n");
        body.append("Date de péremption: ").append(stock.getDatePeremption()).append("\n\n");
        body.append("Quantité en stock: ").append(stock.getQuantite()).append("\n\n");
        body.append("Action requise: Vérification et mise en promotion urgente\n\n");
        body.append("---\nSupermarket Manager - Système d'alerte automatique");

        sendEmail(adminEmail, subject, body.toString());
    }

    @Override
    public void sendNotificationAbsence(Absence absence, String destinataire) {
        String subject = "Nouvelle demande d'absence - " + absence.getEmploye().getNom();

        StringBuilder body = new StringBuilder();
        body.append("NOUVELLE DEMANDE D'ABSENCE\n\n");
        body.append("Employé: ").append(absence.getEmploye().getNom()).append(" ").append(absence.getEmploye().getPrenom()).append("\n");
        body.append("Type: ").append(absence.getType().getLibelle()).append("\n");
        body.append("Du: ").append(absence.getDateDebut()).append("\n");
        body.append("Au: ").append(absence.getDateFin()).append("\n");
        body.append("Durée: ").append(java.time.temporal.ChronoUnit.DAYS.between(absence.getDateDebut(), absence.getDateFin()) + 1).append(" jours\n");
        body.append("Motif: ").append(absence.getMotif()).append("\n");
        body.append("Statut: ").append(absence.getStatut().getLibelle()).append("\n\n");
        body.append("---\nSupermarket Manager - Système de gestion RH");

        sendEmail(destinataire, subject, body.toString());
    }

    @Override
    public void sendNotificationValidationAbsence(Absence absence) {
        String employeEmail = absence.getEmploye().getEmail();
        if (employeEmail == null || employeEmail.isEmpty()) {
            log.warn("Impossible d'envoyer la notification: email employé manquant");
            return;
        }

        String subject = absence.getStatut().name().equals("APPROUVEE")
            ? "Absence approuvée"
            : "Absence refusée";

        StringBuilder body = new StringBuilder();
        body.append("Bonjour ").append(absence.getEmploye().getPrenom()).append(",\n\n");
        body.append("Votre demande d'absence a été ").append(absence.getStatut().getLibelle().toLowerCase()).append(".\n\n");
        body.append("Période: Du ").append(absence.getDateDebut()).append(" au ").append(absence.getDateFin()).append("\n");
        body.append("Type: ").append(absence.getType().getLibelle()).append("\n");
        if (absence.getCommentaireValidation() != null) {
            body.append("Commentaire: ").append(absence.getCommentaireValidation()).append("\n");
        }
        body.append("\n---\nSupermarket Manager - Système de gestion RH");

        sendEmail(employeEmail, subject, body.toString());
    }
}

package comcom.supermarket.manager.service.impl;

import comcom.supermarket.manager.model.dto.*;
import comcom.supermarket.manager.model.reporting.StatistiqueFrequentation;
import comcom.supermarket.manager.model.reporting.VenteProduit;
import comcom.supermarket.manager.repository.StatistiqueFrequentationRepository;
import comcom.supermarket.manager.repository.StockRepository;
import comcom.supermarket.manager.repository.VenteProduitRepository;
import comcom.supermarket.manager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    private final VenteProduitRepository venteProduitRepository;
    private final StatistiqueFrequentationRepository frequentationRepository;
    private final StockRepository stockRepository;

    @Override
    public DashboardDTO getDashboard(LocalDate debut, LocalDate fin) {
        log.info("Génération du tableau de bord pour la période du {} au {}", debut, fin);

        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<VenteProduit> ventes = venteProduitRepository.findByDateVenteBetween(debutDateTime, finDateTime);

        return DashboardDTO.builder()
                .periode(buildPeriodeDTO(debut, fin))
                .ventesGlobales(calculerVentesGlobales(ventes, debut, fin))
                .topProduits(getTopProduits(ventes, 10))
                .produitsEnBaisse(getProduitsEnBaisse(debut, fin, 10.0))
                .alertesStock(getAlertesStock())
                .heuresPointe(getHeuresPointe(debut, fin))
                .margesParCategorie(getMargesParCategorie(debut, fin))
                .frequentation(getStatistiquesFrequentation(debut, fin))
                .build();
    }

    @Override
    public List<VenteProduitDTO> getVentesProduits(LocalDate debut, LocalDate fin) {
        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<VenteProduit> ventes = venteProduitRepository.findByDateVenteBetween(debutDateTime, finDateTime);

        Map<Long, List<VenteProduit>> ventesParProduit = ventes.stream()
                .collect(Collectors.groupingBy(v -> v.getProduit().getId()));

        return ventesParProduit.entrySet().stream()
                .map(entry -> {
                    List<VenteProduit> ventesGroupees = entry.getValue();
                    VenteProduit premierVente = ventesGroupees.get(0);

                    Integer quantiteTotale = ventesGroupees.stream()
                            .mapToInt(VenteProduit::getQuantiteVendue)
                            .sum();

                    BigDecimal montantTotal = ventesGroupees.stream()
                            .map(VenteProduit::getMontantVente)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal margeTotal = ventesGroupees.stream()
                            .map(VenteProduit::getMargeBeneficiaire)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal pourcentageMarge = montantTotal.compareTo(BigDecimal.ZERO) > 0
                            ? margeTotal.divide(montantTotal, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;

                    return VenteProduitDTO.builder()
                            .produitId(entry.getKey())
                            .nomProduit(premierVente.getProduit().getNom())
                            .categorie(premierVente.getProduit().getCategorie().getNom())
                            .quantiteVendue(quantiteTotale)
                            .montantVente(montantTotal)
                            .margeBeneficiaire(margeTotal)
                            .pourcentageMarge(pourcentageMarge)
                            .periode(debut + " au " + fin)
                            .build();
                })
                .sorted(Comparator.comparing(VenteProduitDTO::getMontantVente).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<VenteProduitDTO> getProduitsEnBaisse(LocalDate debut, LocalDate fin, Double seuilPourcentage) {
        log.info("Recherche des produits en baisse de plus de {}%", seuilPourcentage);

        LocalDateTime debutActuel = debut.atStartOfDay();
        LocalDateTime finActuel = fin.atTime(23, 59, 59);

        long joursEcart = java.time.temporal.ChronoUnit.DAYS.between(debut, fin);
        LocalDate debutPrecedent = debut.minusDays(joursEcart + 1);
        LocalDate finPrecedent = debut.minusDays(1);
        LocalDateTime debutPrecedentDateTime = debutPrecedent.atStartOfDay();
        LocalDateTime finPrecedentDateTime = finPrecedent.atTime(23, 59, 59);

        List<VenteProduit> ventesActuelles = venteProduitRepository.findByDateVenteBetween(debutActuel, finActuel);
        List<VenteProduit> ventesPrecedentes = venteProduitRepository.findByDateVenteBetween(debutPrecedentDateTime, finPrecedentDateTime);

        Map<Long, Integer> quantitesActuelles = ventesActuelles.stream()
                .collect(Collectors.groupingBy(v -> v.getProduit().getId(),
                        Collectors.summingInt(VenteProduit::getQuantiteVendue)));

        Map<Long, Integer> quantitesPrecedentes = ventesPrecedentes.stream()
                .collect(Collectors.groupingBy(v -> v.getProduit().getId(),
                        Collectors.summingInt(VenteProduit::getQuantiteVendue)));

        List<VenteProduitDTO> produitsEnBaisse = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : quantitesActuelles.entrySet()) {
            Long produitId = entry.getKey();
            Integer quantiteActuelle = entry.getValue();
            Integer quantitePrecedente = quantitesPrecedentes.getOrDefault(produitId, 0);

            if (quantitePrecedente > 0) {
                double evolution = ((quantiteActuelle - quantitePrecedente) * 100.0) / quantitePrecedente;

                if (evolution < -seuilPourcentage) {
                    VenteProduit venteProduit = ventesActuelles.stream()
                            .filter(v -> v.getProduit().getId().equals(produitId))
                            .findFirst()
                            .orElse(null);

                    if (venteProduit != null) {
                        produitsEnBaisse.add(VenteProduitDTO.builder()
                                .produitId(produitId)
                                .nomProduit(venteProduit.getProduit().getNom())
                                .categorie(venteProduit.getProduit().getCategorie().getNom())
                                .quantiteVendue(quantiteActuelle)
                                .evolutionPourcentage(BigDecimal.valueOf(evolution))
                                .periode(debut + " au " + fin)
                                .build());
                    }
                }
            }
        }

        return produitsEnBaisse.stream()
                .sorted(Comparator.comparing(VenteProduitDTO::getEvolutionPourcentage))
                .collect(Collectors.toList());
    }

    @Override
    public List<HeurePointeDTO> getHeuresPointe(LocalDate debut, LocalDate fin) {
        List<StatistiqueFrequentation> stats = frequentationRepository.findHeuresPointe(debut, fin);

        return stats.stream()
                .map(stat -> {
                    double intensite = calculerIntensite(stat.getNombreClients(), stats);

                    return HeurePointeDTO.builder()
                            .jourSemaine(stat.getJourSemaine())
                            .plageHoraire(stat.getHeureDebut() + " - " + stat.getHeureFin())
                            .nombreClients(stat.getNombreClients())
                            .nombreTransactions(stat.getNombreTransactions())
                            .intensite(intensite)
                            .build();
                })
                .sorted(Comparator.comparing(HeurePointeDTO::getIntensite).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<MargeBeneficiaireDTO> getMargesParCategorie(LocalDate debut, LocalDate fin) {
        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<Object[]> resultats = venteProduitRepository.findStatistiquesByCategorie(debutDateTime, finDateTime);

        return resultats.stream()
                .map(row -> {
                    String categorie = (String) row[0];
                    BigDecimal chiffreAffaires = (BigDecimal) row[1];
                    BigDecimal coutTotal = (BigDecimal) row[2];
                    BigDecimal margeBrute = (BigDecimal) row[3];
                    Long nombreProduits = (Long) row[4];

                    BigDecimal pourcentageMarge = chiffreAffaires.compareTo(BigDecimal.ZERO) > 0
                            ? margeBrute.divide(chiffreAffaires, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                            : BigDecimal.ZERO;

                    return MargeBeneficiaireDTO.builder()
                            .categorie(categorie)
                            .chiffreAffaires(chiffreAffaires)
                            .coutTotal(coutTotal)
                            .margeBrute(margeBrute)
                            .pourcentageMarge(pourcentageMarge)
                            .nombreProduits(nombreProduits.intValue())
                            .build();
                })
                .sorted(Comparator.comparing(MargeBeneficiaireDTO::getMargeBrute).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<VenteProduitDTO> getEvolutionVentesProduit(Long produitId, LocalDate debut, LocalDate fin) {
        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<Object[]> evolution = venteProduitRepository.findEvolutionVentesProduit(produitId, debutDateTime, finDateTime);

        return evolution.stream()
                .map(row -> {
                    LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
                    Long quantite = (Long) row[1];
                    BigDecimal montant = (BigDecimal) row[2];

                    return VenteProduitDTO.builder()
                            .produitId(produitId)
                            .quantiteVendue(quantite.intValue())
                            .montantVente(montant)
                            .periode(date.toString())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public FrequentationDTO getStatistiquesFrequentation(LocalDate debut, LocalDate fin) {
        List<StatistiqueFrequentation> stats = frequentationRepository.findByDateStatBetween(debut, fin);

        if (stats.isEmpty()) {
            return FrequentationDTO.builder()
                    .clientsTotal(0)
                    .clientsMoyenParJour(0)
                    .jourPlusFrequente("Aucune donnée")
                    .heurePointeMoyenne(0)
                    .build();
        }

        int clientsTotal = stats.stream().mapToInt(StatistiqueFrequentation::getNombreClients).sum();
        int clientsMoyenParJour = clientsTotal / (int) java.time.temporal.ChronoUnit.DAYS.between(debut, fin.plusDays(1));

        Map<String, Integer> clientsParJour = stats.stream()
                .collect(Collectors.groupingBy(
                        StatistiqueFrequentation::getJourSemaine,
                        Collectors.summingInt(StatistiqueFrequentation::getNombreClients)
                ));

        String jourPlusFrequente = clientsParJour.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Inconnu");

        int heurePointeMoyenne = stats.stream()
                .filter(StatistiqueFrequentation::getEstHeurePointe)
                .mapToInt(s -> s.getHeureDebut().getHour())
                .average()
                .orElse(0);

        return FrequentationDTO.builder()
                .clientsTotal(clientsTotal)
                .clientsMoyenParJour(clientsMoyenParJour)
                .jourPlusFrequente(jourPlusFrequente)
                .heurePointeMoyenne((int) heurePointeMoyenne)
                .build();
    }

    // Méthodes privées utilitaires

    private PeriodeDTO buildPeriodeDTO(LocalDate debut, LocalDate fin) {
        return PeriodeDTO.builder()
                .debut(debut.toString())
                .fin(fin.toString())
                .type(determinerTypePeriode(debut, fin))
                .build();
    }

    private String determinerTypePeriode(LocalDate debut, LocalDate fin) {
        long jours = java.time.temporal.ChronoUnit.DAYS.between(debut, fin);
        if (jours <= 1) return "JOUR";
        if (jours <= 7) return "SEMAINE";
        if (jours <= 31) return "MOIS";
        return "ANNEE";
    }

    private VentesGlobalesDTO calculerVentesGlobales(List<VenteProduit> ventes, LocalDate debut, LocalDate fin) {
        if (ventes.isEmpty()) {
            return VentesGlobalesDTO.builder()
                    .chiffreAffaires(BigDecimal.ZERO)
                    .margeTotale(BigDecimal.ZERO)
                    .nombreVentes(0)
                    .panierMoyen(BigDecimal.ZERO)
                    .evolutionPourcentage(BigDecimal.ZERO)
                    .build();
        }

        BigDecimal chiffreAffaires = ventes.stream()
                .map(VenteProduit::getMontantVente)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal margeTotale = ventes.stream()
                .map(VenteProduit::getMargeBeneficiaire)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int nombreVentes = ventes.size();

        BigDecimal panierMoyen = chiffreAffaires.divide(
                BigDecimal.valueOf(nombreVentes), 2, RoundingMode.HALF_UP);

        BigDecimal evolutionPourcentage = calculerEvolution(debut, fin, chiffreAffaires);

        return VentesGlobalesDTO.builder()
                .chiffreAffaires(chiffreAffaires)
                .margeTotale(margeTotale)
                .nombreVentes(nombreVentes)
                .panierMoyen(panierMoyen)
                .evolutionPourcentage(evolutionPourcentage)
                .build();
    }

    private BigDecimal calculerEvolution(LocalDate debut, LocalDate fin, BigDecimal chiffreAffairesActuel) {
        long joursEcart = java.time.temporal.ChronoUnit.DAYS.between(debut, fin);
        LocalDate debutPrecedent = debut.minusDays(joursEcart + 1);
        LocalDate finPrecedent = debut.minusDays(1);

        List<VenteProduit> ventesPrecedentes = venteProduitRepository.findByDateVenteBetween(
                debutPrecedent.atStartOfDay(), finPrecedent.atTime(23, 59, 59));

        BigDecimal chiffreAffairesPrecedent = ventesPrecedentes.stream()
                .map(VenteProduit::getMontantVente)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (chiffreAffairesPrecedent.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return chiffreAffairesActuel.subtract(chiffreAffairesPrecedent)
                .divide(chiffreAffairesPrecedent, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    private List<VenteProduitDTO> getTopProduits(List<VenteProduit> ventes, int limite) {
        Map<Long, List<VenteProduit>> ventesParProduit = ventes.stream()
                .collect(Collectors.groupingBy(v -> v.getProduit().getId()));

        return ventesParProduit.entrySet().stream()
                .map(entry -> {
                    List<VenteProduit> ventesGroupees = entry.getValue();
                    VenteProduit premierVente = ventesGroupees.get(0);

                    Integer quantiteTotale = ventesGroupees.stream()
                            .mapToInt(VenteProduit::getQuantiteVendue)
                            .sum();

                    BigDecimal montantTotal = ventesGroupees.stream()
                            .map(VenteProduit::getMontantVente)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return VenteProduitDTO.builder()
                            .produitId(entry.getKey())
                            .nomProduit(premierVente.getProduit().getNom())
                            .categorie(premierVente.getProduit().getCategorie().getNom())
                            .quantiteVendue(quantiteTotale)
                            .montantVente(montantTotal)
                            .build();
                })
                .sorted(Comparator.comparing(VenteProduitDTO::getMontantVente).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    private List<StockAlertDTO> getAlertesStock() {
        return stockRepository.findStocksEnDessousDuSeuil().stream()
                .map(stock -> StockAlertDTO.builder()
                        .produitId(stock.getProduit().getId())
                        .nomProduit(stock.getProduit().getNom())
                        .quantiteActuelle(stock.getQuantiteActuelle())
                        .seuilAlerte(stock.getSeuilAlerte())
                        .niveau("CRITIQUE")
                        .build())
                .collect(Collectors.toList());
    }

    private double calculerIntensite(int nombreClients, List<StatistiqueFrequentation> toutesStats) {
        int max = toutesStats.stream()
                .mapToInt(StatistiqueFrequentation::getNombreClients)
                .max()
                .orElse(1);

        return (nombreClients * 100.0) / max;
    }
}


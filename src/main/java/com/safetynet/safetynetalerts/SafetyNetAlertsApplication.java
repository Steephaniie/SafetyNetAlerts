package com.safetynet.safetynetalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Classe principale de l'application SafetyNetAlerts.
 * Cette application gère les alertes de sécurité pour les habitants d'une communauté.
 */
@SpringBootApplication
public class SafetyNetAlertsApplication {

	/**
	 * Point d'entrée principal de l'application.
	 *
	 * @param args arguments de ligne de commande
	 */
	public static void main(String[] args) {
		SpringApplication.run(SafetyNetAlertsApplication.class, args);
	}

}

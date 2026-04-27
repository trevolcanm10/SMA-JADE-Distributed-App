package com.jade.agentes;

// Importaciones de JADE
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

/**
 * Contenedor 1: INFRAESTRUCTURA
 * Este es el Main Container que centraliza el AMS y el DF.
 */
public class MainContainer {

    public static void main(String[] args) {
        // 1. Obtenemos la instancia del Runtime de JADE
        Runtime runtime = Runtime.instance();

        // 2. Creamos el perfil para el Contenedor Principal
        Profile profile = new ProfileImpl();
        
        // Configuramos para que se abra la interfaz gráfica (GUI)
        profile.setParameter(Profile.GUI, "true");

        try {
            // 3. Creamos el contenedor principal (Aquí nacen AMS y DF)
            AgentContainer mainContainer = runtime.createMainContainer(profile);
            
            System.out.println("==============================================");
            System.out.println("Plataforma JADE iniciada correctamente");
            System.out.println("Nombre del Contenedor: " + mainContainer.getContainerName());
            System.out.println("Estado: Activo y esperando agentes remotos...");
            System.out.println("==============================================");

        } catch (Exception e) {
            System.err.println("Error al iniciar el contenedor de infraestructura:");
            e.printStackTrace();
        }
    }
}

















/*package com.jade.agentes;
//Traemos las herramientas de JADE
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

/**
 * MainContainer es la clase principal que inicia el entorno JADE
 * y crea los agentes necesarios para la aplicación de gestión hospitalaria.
 * 
 * Esta clase configura el contenedor principal de JADE y crea los siguientes agentes:
 * - Recepcionista: Encargado de recibir a los pacientes
 * - Triage: Clasifica la urgencia de los pacientes
 * - Asignador: Asigna doctores a los pacientes
 * - Doctor: Atiende a los pacientes
 */
/*public class MainContainer {

    /**
     * Método principal que inicia la aplicación
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    /*public static void main(String[] args) {

        // Obtiene la instancia del runtime de JADE
        Runtime runtime = Runtime.instance();

        // Crea un perfil de configuración para el contenedor principal
        Profile profile = new ProfileImpl();
        // Habilita la interfaz gráfica de JADE para monitorear los agentes
        //profile.setParameter(Profile.GUI, "true");

        // Crea el contenedor principal de JADE con el perfil configurado
        AgentContainer container =
                runtime.createMainContainer(profile);

        try {

            // Crea el agente Recepcionista
            AgentController recepcionista =
                    container.createNewAgent(
                            "Recepcionista", // Nombre del agente
                            "com.jade.agentes.RecepcionistaAgent", // Clase del agente
                            null); // Sin argumentos de inicialización

            // Crea el agente Triage
            AgentController triage =
                    container.createNewAgent(
                            "Triage", // Nombre del agente
                            "com.jade.agentes.TriageAgent", // Clase del agente
                            null); // Sin argumentos de inicialización

            // Crea el agente Asignador de Doctores
            AgentController asignador =
                    container.createNewAgent(
                            "Asignador", // Nombre del agente
                            "com.jade.agentes.DoctorAssignerAgent", // Clase del agente
                            null); // Sin argumentos de inicialización

            // Crea el agente Doctor
            AgentController doctor =
                    container.createNewAgent(
                            "Doctor", // Nombre del agente
                            "com.jade.agentes.DoctorAgent", // Clase del agente
                            null); // Sin argumentos de inicialización

            // Inicia todos los agentes creados
            recepcionista.start();
            triage.start();
            asignador.start();
            doctor.start();

        } catch (Exception e) {
            // Maneja cualquier excepción que ocurra durante la creación de agentes
            e.printStackTrace();
        }

    }
}*/
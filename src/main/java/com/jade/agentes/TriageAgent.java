package com.jade.agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Agente de Triage: Se encarga de clasificar a los pacientes
 * segun su sintoma y asignarles un nivel de prioridad.
 */
public class TriageAgent extends Agent {

    /**
     * Metodo que se ejecuta automaticamente cuando se inicia el agente
     */
    protected void setup() {

        // --- REGISTRO DEL SERVICIO EN EL DF ---
        // Registramos al agente de triaje para que la recepcionista pueda encontrarlo
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("triaje-medico"); // Tipo de servicio que ofrece
        sd.setName("servicio-triaje");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Mostrar mensaje en consola confirmando que el agente se ha iniciado
        System.out.println(getLocalName() + " iniciado.");

        // Agregar un comportamiento que se ejecutara de forma continua (infinitamente)
        addBehaviour(new CyclicBehaviour() {

            /**
             * Accion principal del comportamiento:
             * Espera mensajes y procesa cada paciente que llega
             */
            public void action() {

                // Recibir mensaje entrante con los datos del paciente
                ACLMessage msg = receive();

                // Si llego un mensaje (hay un paciente para procesar)
                if (msg != null) {

                    // Obtener el contenido del mensaje
                    String contenido = msg.getContent();

                    // Separar los datos del paciente (nombre y sintoma)
                    String[] datos = contenido.split(",");

                    // Extraer el nombre del paciente
                    String paciente = datos[0];

                    // Extraer el sintoma del paciente
                    String sintoma = datos[1];

                    // Clasificar al paciente segun su sintoma (obtener nivel de prioridad)
                    String nivel = clasificar(sintoma);

                    // Mostrar en consola el resultado de la clasificacion
                    System.out.println("Paciente: " + paciente + " Nivel: " + nivel);

                    // Crear un nuevo mensaje para enviar al agente asignador
                    ACLMessage nuevo = new ACLMessage(ACLMessage.INFORM);

                    // --- BÚSQUEDA DINÁMICA DEL ASIGNADOR EN EL DF ---
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sdAsignador = new ServiceDescription();
                    sdAsignador.setType("asignacion-medica"); // Busca el servicio del Asignador
                    template.addServices(sdAsignador);
                    
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        if (result.length > 0) {
                            nuevo.addReceiver(result[0].getName());
                        } else {
                            System.out.println("No se encontró ningún Asignador en las Páginas Amarillas.");
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    // Poner en el mensaje los datos del paciente y su nivel de prioridad
                    nuevo.setContent(paciente + "," + nivel);

                    // Enviar el mensaje
                    send(nuevo);

                // Si no hay ningun mensaje pendiente
                } else {

                    // Poner el agente en modo espera hasta que llegue un nuevo mensaje
                    block();

                }

            }

        });

    }

    // --- DESREGISTRO AL FINALIZAR ---
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    /**
     * Metodo que clasifica la prioridad de un paciente segun su sintoma
     * ROJO = Emergencia (mayor prioridad)
     * AMARILLO = Urgencia (prioridad media)
     * VERDE = Normal (menor prioridad)
     */
    private String clasificar(String sintoma) {

        // Caso 1: Dificultad respiratoria = Maxima prioridad
        if (sintoma.equals("dificultad respiratoria")) {

            return "ROJO";

        // Caso 2: Fiebre = Prioridad media
        } else if (sintoma.equals("fiebre")) {

            return "AMARILLO";

        }

        // Cualquier otro sintoma = Prioridad baja
        return "VERDE";
    }

}
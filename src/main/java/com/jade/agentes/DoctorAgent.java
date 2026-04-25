package com.jade.agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

/**
 * Clase DoctorAgent que representa un agente JADE que maneja asignaciones
 * paciente-médico. Este agente recibe mensajes que contienen información
 * de paciente y médico y procesa la asignación.
 */
public class DoctorAgent extends Agent {

    /**
     * Método setup llamado cuando el agente es inicializado.
     * Agrega un comportamiento cíclico para manejar mensajes entrantes.
     */
    protected void setup() {

        // Registro en las Páginas Amarillas
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("atencion-medica"); // Tipo de servicio
        sd.setName("servicio-doctor");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Agrega un comportamiento cíclico para escuchar continuamente mensajes entrantes
        addBehaviour(new CyclicBehaviour() {

            /**
             * Método action que define la lógica principal del comportamiento.
             * Procesa mensajes entrantes que contienen asignaciones paciente-médico.
             */
            public void action() {

                // Recibe un mensaje entrante
                ACLMessage msg = receive();

                // Verifica si se recibió un mensaje
                if (msg != null) {

                    // Recibimos 3 datos: [0]paciente, [1]medico, [2]nivel
                    String[] datos = msg.getContent().split(",");

                    String paciente = datos[0];
                    String medico = datos[1];
                    String nivel = datos[2];

                    // Imprime la información de asignación en la consola
                    System.out.println("      [Asignador] -> " + paciente + " (Prioridad: " + nivel + ") asignado a: " + medico);
                    System.out.println("      Mensaje enviado al Doctor: " + medico);
                    System.out.println("--------------------------------------------------");
                    System.out.println("  ATENCION MEDICA: " + paciente + " | MEDICO: " + medico);
                    System.out.println("--------------------------------------------------");

                } else {
                    // Si no hay mensaje disponible, bloquea hasta que llegue un nuevo mensaje
                    block();

                }

            }

        });

    }

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

}

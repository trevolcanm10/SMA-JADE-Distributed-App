package com.jade.agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Clase DoctorAgent que representa un agente JADE que maneja asignaciones
 * paciente-médico. Este agente recibe mensajes que contienen información
 * de paciente y médico y procesa la asignación.
 */
public class DoctorAgent
        extends Agent {

    /**
     * Método setup llamado cuando el agente es inicializado.
     * Agrega un comportamiento cíclico para manejar mensajes entrantes.
     */
    protected void setup() {

        // Agrega un comportamiento cíclico para escuchar continuamente mensajes entrantes
        addBehaviour(
                new CyclicBehaviour() {

            /**
             * Método action que define la lógica principal del comportamiento.
             * Procesa mensajes entrantes que contienen asignaciones paciente-médico.
             */
            public void action() {

                // Recibe un mensaje entrante
                ACLMessage msg =
                        receive();

                // Verifica si se recibió un mensaje
                if (msg != null) {

                    // Divide el contenido del mensaje por coma para extraer paciente y médico
                    String[] datos =
                            msg.getContent()
                            .split(",");

                    // Obtiene el nombre del paciente del primer elemento
                    String paciente =
                            datos[0];

                    // Obtiene el nombre del médico del segundo elemento
                    String medico =
                            datos[1];

                    // Imprime la información de asignación en la consola
                    System.out.println(
                            "Paciente "
                            + paciente
                            + " será atendido por "
                            + medico);

                } else {
                    // Si no hay mensaje disponible, bloquea hasta que llegue un nuevo mensaje
                    block();

                }

            }

        });

    }

}

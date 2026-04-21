package com.jade.agentes;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Agente encargado de asignar médicos a pacientes según su nivel de urgencia.
 * Este agente recibe mensajes del agente de triaje con información del paciente
 * y su nivel de urgencia, luego asigna el médico correspondiente y envía la
 * información al agente médico.
 */
public class DoctorAssignerAgent
        extends Agent {

    /**
     * Método que se ejecuta cuando el agente se inicia.
     * Configura el comportamiento cíclico para recibir y procesar mensajes.
     */
    protected void setup() {

        // Agrega un comportamiento cíclico para procesar mensajes continuamente
        addBehaviour(
                new CyclicBehaviour() {

            /**
             * Método que define la acción del comportamiento cíclico.
             * Recibe mensajes, procesa la información y asigna médicos.
             */
            public void action() {

                // Recibe un mensaje del agente de triaje
                ACLMessage msg =
                        receive();

                // Verifica si se recibió un mensaje
                if (msg != null) {

                    // Extrae los datos del mensaje y los separa por comas
                    String[] datos =
                            msg.getContent()
                            .split(",");

                    // Obtiene el nombre del paciente (primer elemento)
                    String paciente =
                            datos[0];

                    // Obtiene el nivel de urgencia (segundo elemento)
                    String nivel =
                            datos[1];

                    // Asigna el médico correspondiente según el nivel de urgencia
                    String medico =
                            asignar(nivel);

                    // Muestra en consola el médico asignado
                    System.out.println(
                            "Medico asignado: "
                            + medico);

                    // Crea un nuevo mensaje para enviar al agente médico
                    ACLMessage nuevo =
                            new ACLMessage(
                                    ACLMessage.INFORM);

                    // Establece el receptor del mensaje (agente médico)
                    nuevo.addReceiver(
                            getAID("Doctor"));

                    // Establece el contenido del mensaje con paciente y médico asignado
                    nuevo.setContent(
                            paciente + ","
                            + medico);

                    // Envía el mensaje al agente médico
                    send(nuevo);

                } else {
                    // Si no hay mensajes, bloquea el comportamiento hasta que llegue uno nuevo
                    block();

                }

            }

        });

    }

    /**
     * Método que asigna un médico según el nivel de urgencia del paciente.
     * @param nivel Nivel de urgencia del paciente (ROJO, AMARILLO, o otros)
     * @return Nombre del médico asignado
     */
    private String asignar(
            String nivel) {

        // Si el nivel es ROJO (emergencia), asigna médico de emergencia
        if (nivel.equals("ROJO")) {

            return "Emergencia";

        // Si el nivel es AMARILLO (prioridad media), asigna médico general
        } else if (nivel.equals(
                "AMARILLO")) {

            return "General";

        }
        // Para otros niveles, asigna médico de consulta
        return "Consulta";
    }
}

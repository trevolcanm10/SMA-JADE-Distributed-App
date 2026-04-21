package com.jade.agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * RecepcionistaAgent es el agente encargado de recibir a los pacientes
 * en la entrada del hospital. Su función principal es recopilar la información
 * básica del paciente y enviarla al agente Triage para su clasificación.
 */
public class RecepcionistaAgent extends Agent {

    /**
     * Método setup que se ejecuta cuando el agente es iniciado
     * Este método configura el comportamiento inicial del agente
     */
    protected void setup() {

        // Muestra un mensaje indicando que el agente ha sido iniciado
        System.out.println(
                getLocalName()
                + " iniciado.");

        // Agrega un comportamiento de una sola ejecución (OneShotBehaviour)
        addBehaviour(new OneShotBehaviour() {

            /**
             * Método action que define el comportamiento del agente
             * Este método se ejecuta una sola vez cuando el agente inicia
             */
            public void action() {

                // Datos del paciente (en una aplicación real, estos vendrían de un formulario)
                String paciente = "Juan";
                String sintoma =
                        "dificultad respiratoria";

                // Crea un mensaje ACL para comunicarse con otros agentes
                ACLMessage msg =
                        new ACLMessage(
                                ACLMessage.INFORM);

                // Establece el receptor del mensaje (el agente Triage)
                msg.addReceiver(
                        getAID("Triage"));

                // Establece el contenido del mensaje con los datos del paciente
                msg.setContent(
                        paciente + "," + sintoma);

                // Envía el mensaje al agente Triage
                send(msg);

                // Muestra un mensaje confirmando que los datos fueron enviados
                System.out.println(
                        "Datos enviados al Triage.");

            }
        });
    }
}
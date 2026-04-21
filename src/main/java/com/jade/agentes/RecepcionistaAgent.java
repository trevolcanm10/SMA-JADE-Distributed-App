package com.jade.agentes;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

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
        System.out.println(getLocalName() + " iniciado.");

        // Agrega un comportamiento de una sola ejecución (OneShotBehaviour)
        addBehaviour(new OneShotBehaviour() {

            /**
             * Método action que define el comportamiento del agente
             * Este método se ejecuta una sola vez cuando el agente inicia
             */
            public void action() {

                // Datos del paciente (en una aplicación real, estos vendrían de un formulario)
                String paciente = "Juan";
                String sintoma = "dificultad respiratoria";

                // Crea un mensaje ACL para comunicarse con otros agentes
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

                // --- BÚSQUEDA DINÁMICA DEL TRIAJE EN EL DF ---
                // Sustituye a: msg.addReceiver(getAID("Triage"));
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sdTriaje = new ServiceDescription();
                sdTriaje.setType("triaje-medico"); // Busca el servicio que ofrece TriageAgent
                template.addServices(sdTriaje);
                
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    if (result.length > 0) {
                        // Toma el primer agente de triaje que encuentre en el DF
                        msg.addReceiver(result[0].getName());
                    } else {
                        System.out.println("No se encontró ningún agente de Triaje en las Páginas Amarillas.");
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }

                // Establece el contenido del mensaje con los datos del paciente
                msg.setContent(paciente + "," + sintoma);

                // Envía el mensaje al agente Triage
                send(msg);

                // Muestra un mensaje confirmando que los datos fueron enviados
                System.out.println("Datos enviados al Triage.");

            }
        });
    }
}
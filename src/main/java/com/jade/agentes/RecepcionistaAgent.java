package com.jade.agentes;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class RecepcionistaAgent extends Agent {

    protected void setup() {

        System.out.println(getLocalName() + " iniciado. Esperando a que los demás agentes se registren...");

        // --- Usamos WakerBehaviour y le damos 5000 milisegundos (5 segundos) de espera ---
        addBehaviour(new WakerBehaviour(this, 5000) {

            // En un WakerBehaviour, el método se llama onWake() en lugar de action()
            protected void onWake() {

                String paciente = "Juan";
                String sintoma = "dificultad respiratoria";

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sdTriaje = new ServiceDescription();
                sdTriaje.setType("triaje-medico");
                template.addServices(sdTriaje);
                
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    if (result.length > 0) {
                        msg.addReceiver(result[0].getName());
                        System.out.println("¡Triage encontrado en el DF! Enviando datos...");
                    } else {
                        System.out.println("No se encontró ningún agente de Triaje en las Páginas Amarillas.");
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }

                msg.setContent(paciente + "," + sintoma);
                send(msg);

                System.out.println("Datos enviados al Triage.");
            }
        });
    }
}